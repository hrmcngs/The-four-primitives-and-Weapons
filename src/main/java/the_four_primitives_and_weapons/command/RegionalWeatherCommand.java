package the_four_primitives_and_weapons.command;

import java.util.Locale;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.network.RegionalWeatherSyncPacket;
import the_four_primitives_and_weapons.weather.*;

@Mod.EventBusSubscriber
public final class RegionalWeatherCommand {
    @SubscribeEvent public static void register(RegisterCommandsEvent event) { register(event.getDispatcher()); }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var weather = Commands.literal("weather").requires(s -> s.hasPermission(2));
        var legacySet = Commands.literal("set");
        for (var kind : WeatherKind.values()) {
            String name = kind.name().toLowerCase(Locale.ROOT);
            weather.then(Commands.literal(name).executes(c -> set(c.getSource(), kind, 12000))
                .then(Commands.argument("duration", TimeArgument.time(1))
                    .executes(c -> set(c.getSource(), kind, IntegerArgumentType.getInteger(c, "duration")))));
            legacySet.then(Commands.literal(name).executes(c -> set(c.getSource(), kind, 12000)));
        }
        // Merge with vanilla's tree, replacing both executors so a previous override
        // cannot prevent /weather clear, rain or thunder (including timed forms).
        weather.then(Commands.literal("thunder").executes(c -> set(c.getSource(), WeatherKind.THUNDERSTORM, 12000))
            .then(Commands.argument("duration", TimeArgument.time(1))
                .executes(c -> set(c.getSource(), WeatherKind.THUNDERSTORM, IntegerArgumentType.getInteger(c, "duration")))));
        weather.then(Commands.literal("auto").executes(c -> set(c.getSource(), null, 12000)))
            .then(Commands.literal("status").executes(c -> status(c.getSource())));
        dispatcher.register(weather);
        dispatcher.register(Commands.literal("climate").requires(s -> s.hasPermission(2))
            .then(legacySet)
            .then(Commands.literal("auto").executes(c -> set(c.getSource(), null, 12000)))
            .then(Commands.literal("status").executes(c -> status(c.getSource()))));
    }
    private static int status(CommandSourceStack source) {
        var kind = RegionalWeather.at(source.getLevel(), BlockPos.containing(source.getPosition()));
        source.sendSuccess(() -> Component.literal("現在地: " + kind.label + (kind.wmoCode < 0 ? "" : " / WMO参考コード " + kind.wmoCode)), false);
        return 1;
    }
    private static int set(CommandSourceStack source, WeatherKind kind, int duration) {
        var level = source.getServer().overworld();
        var data = RegionalWeatherData.get(level);
        data.forced = kind; data.setDirty();
        if (kind != null) {
            boolean wet = kind.precipitation != 0;
            level.setWeatherParameters(wet ? 0 : duration, wet ? duration : 0, wet, kind.thunder);
        }
        for (var player : source.getServer().getPlayerList().getPlayers()) RegionalWeatherSyncPacket.send(player);
        source.sendSuccess(() -> Component.literal("天気の種類: " + (kind == null ? "自動" : kind.label)
            + "。バイオーム・昼夜の許可条件を満たす場所に適用します。"), true);
        return kind == null ? 1 : duration;
    }
}
