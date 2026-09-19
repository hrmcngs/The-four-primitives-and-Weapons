package the_four_primitives_and_weapons.command;

import java.util.Locale;
import java.util.function.UnaryOperator;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.network.AstronomySyncPacket;
import the_four_primitives_and_weapons.world.*;

@Mod.EventBusSubscriber
public final class AstronomyCommand {
    @SubscribeEvent public static void register(RegisterCommandsEvent event) {
        var size = Commands.literal("size")
            .then(Commands.literal("auto").executes(c -> size(c.getSource(), -1F)))
            .then(Commands.argument("multiplier", FloatArgumentType.floatArg(0.25F, 4F))
                .executes(c -> size(c.getSource(), FloatArgumentType.getFloat(c, "multiplier"))));
        var phase = Commands.literal("phase")
            .then(Commands.literal("auto").executes(c -> phase(c.getSource(), -1)))
            .then(Commands.argument("phase", IntegerArgumentType.integer(0, 7))
                .executes(c -> phase(c.getSource(), IntegerArgumentType.getInteger(c, "phase"))));
        var color = Commands.literal("color")
            .then(Commands.literal("auto").executes(c -> color(c.getSource(), -1)));
        for (var tint : AstronomicalEvents.MoonTint.values()) {
            color.then(Commands.literal(tint.name().toLowerCase(Locale.ROOT))
                .executes(c -> color(c.getSource(), tint.ordinal())));
        }
        var meteors = Commands.literal("meteors");
        var vanilla = Commands.literal("vanilla")
            .then(Commands.argument("phase", IntegerArgumentType.integer(0, 7))
                .executes(c -> change(c.getSource(), s -> s.vanillaMoon(IntegerArgumentType.getInteger(c, "phase")))));
        String[] phases = {"full", "waning_gibbous", "last_quarter", "waning_crescent",
            "new", "crescent", "first_quarter", "waxing_gibbous"};
        for (int i = 0; i < phases.length; i++) {
            int value = i;
            vanilla.then(Commands.literal(phases[i]).executes(c -> change(c.getSource(), s -> s.vanillaMoon(value))));
        }
        String[] modes = {"auto", "off", "on"};
        for (int i = 0; i < modes.length; i++) {
            int mode = i - 1;
            meteors.then(Commands.literal(modes[i]).executes(c -> change(c.getSource(),
                s -> s.withMeteors(mode))));
        }
        var eclipse = Commands.literal("eclipse");
        for (String type : new String[]{"solar", "lunar"}) {
            boolean solar = type.equals("solar");
            eclipse.then(Commands.literal(type)
                .then(Commands.literal("auto").executes(c -> eclipse(c.getSource(), solar, -1F)))
                .then(Commands.literal("off").executes(c -> eclipse(c.getSource(), solar, -2F)))
                .then(Commands.literal("on").executes(c -> eclipse(c.getSource(), solar, solar ? 0.5F : 1F)))
                .then(Commands.argument("amount", FloatArgumentType.floatArg(0F, 1F))
                    .executes(c -> eclipse(c.getSource(), solar, FloatArgumentType.getFloat(c, "amount")))));
        }
        event.getDispatcher().register(Commands.literal("astronomy").requires(s -> s.hasPermission(2))
            .then(Commands.literal("moon").then(size).then(phase).then(color).then(vanilla))
            .then(meteors).then(eclipse)
            .then(Commands.literal("effects")
                .then(Commands.literal("on").executes(c -> effects(c.getSource(), true)))
                .then(Commands.literal("off").executes(c -> effects(c.getSource(), false))))
            .then(Commands.literal("reset").executes(c -> change(c.getSource(), s -> AstronomySettings.DEFAULT)))
            .then(Commands.literal("status").executes(c -> {
                c.getSource().sendSuccess(() -> Component.literal(describe(AstronomyData.get(c.getSource().getLevel()).settings())), false);
                return 1;
            })));
    }
    private static int eclipse(CommandSourceStack source, boolean solar, float value) {
        return change(source, s -> solar ? s.withSolar(value) : s.withLunar(value));
    }
    private static int size(CommandSourceStack source, float value) {
        return change(source, s -> s.withSize(value));
    }
    private static int phase(CommandSourceStack source, int value) {
        return change(source, s -> s.withPhase(value));
    }
    private static int color(CommandSourceStack source, int value) {
        return change(source, s -> s.withColor(value));
    }
    private static int effects(CommandSourceStack source, boolean value) {
        return change(source, s -> s.withEffects(value));
    }
    private static int change(CommandSourceStack source, UnaryOperator<AstronomySettings> update) {
        var data = AstronomyData.get(source.getLevel());
        data.update(update.apply(data.settings()));
        for (var player : source.getServer().getPlayerList().getPlayers()) AstronomySyncPacket.send(player);
        source.sendSuccess(() -> Component.literal("オーバーワールドの天文設定を保存しました。" + describe(data.settings())), true);
        return 1;
    }
    private static String eclipseName(float value) {
        return value == -1F ? "自動" : value == -2F ? "無効" : Float.toString(value);
    }
    private static String describe(AstronomySettings s) {
        String color = s.color() < 0 ? "自動" : s.color() == 0 ? "白"
            : AstronomicalEvents.MoonTint.values()[s.color()].displayName;
        return "月サイズ: " + (s.size() < 0 ? "自動" : s.size() + "倍")
            + " / 月相: " + (s.phase() < 0 ? "自動" : Integer.toString(s.phase()))
            + " / 色: " + color + " / 流星: " + (s.meteors() < 0 ? "自動" : s.meteors() == 0 ? "無効" : "流星群")
            + " / 日食: " + eclipseName(s.solar()) + " / 月食: " + eclipseName(s.lunar())
            + " / 色の効果: " + (s.effects() ? "有効" : "無効");
    }
}
