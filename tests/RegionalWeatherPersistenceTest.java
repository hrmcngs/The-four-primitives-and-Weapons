import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import the_four_primitives_and_weapons.weather.*;
import the_four_primitives_and_weapons.network.RegionalWeatherSyncPacket;

public class RegionalWeatherPersistenceTest {
    public static void main(String[] args) throws Exception {
        var dispatcher = new com.mojang.brigadier.CommandDispatcher<net.minecraft.commands.CommandSourceStack>();
        net.minecraft.server.commands.WeatherCommand.register(dispatcher);
        var vanillaRain = dispatcher.getRoot().getChild("weather").getChild("rain");
        var oldRain = vanillaRain.getCommand();
        var oldTimedRain = vanillaRain.getChild("duration").getCommand();
        the_four_primitives_and_weapons.command.RegionalWeatherCommand.register(dispatcher);
        var weather = dispatcher.getRoot().getChild("weather");
        if (weather.getChild("rain").getCommand() == oldRain
            || weather.getChild("rain").getChild("duration").getCommand() == oldTimedRain)
            throw new AssertionError("Vanilla executors must switch the regional override too");
        for (var kind : WeatherKind.values()) {
            var node = weather.getChild(kind.name().toLowerCase(java.util.Locale.ROOT));
            if (node == null || node.getCommand() == null || node.getChild("duration").getCommand() == null)
                throw new AssertionError("Missing weather command: " + kind);
            var argument = (com.mojang.brigadier.tree.ArgumentCommandNode<?, ?>) node.getChild("duration");
            if (!argument.getType().parse(new com.mojang.brigadier.StringReader("30s")).equals(600))
                throw new AssertionError("Duration units");
        }
        for (String name : new String[] {"thunder", "auto", "status"})
            if (weather.getChild(name).getCommand() == null) throw new AssertionError(name);
        if (dispatcher.getRoot().getChild("climate").getChild("set").getChild("rain").getCommand() == null)
            throw new AssertionError("Legacy command alias");
        System.out.println("Weather command registration, vanilla merging and duration parsing passed.");
        for (var kind : WeatherKind.values()) {
            var data = new RegionalWeatherData(); data.forced = kind;
            if (RegionalWeatherData.load(data.save(new CompoundTag())).forced != kind) throw new AssertionError("Saved weather");
            var buf = new FriendlyByteBuf(Unpooled.buffer());
            try {
                RegionalWeatherSyncPacket.encode(new RegionalWeatherSyncPacket(kind.ordinal()), buf);
                if (new RegionalWeatherSyncPacket(buf).ordinal() != kind.ordinal() || buf.readableBytes() != 0) throw new AssertionError("Sync weather");
            } finally { buf.release(); }
        }
        if (RegionalWeatherData.load(new CompoundTag()).forced != null) throw new AssertionError("Old world default");
        var tag = new CompoundTag(); tag.putString("Weather", "unknown");
        if (RegionalWeatherData.load(tag).forced != null) throw new AssertionError("Invalid mode default");
        System.out.println("Regional weather persistence and packet round trips passed.");
    }
}
