package the_four_primitives_and_weapons.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.weather.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public record RegionalWeatherSyncPacket(int ordinal) {
    public RegionalWeatherSyncPacket(FriendlyByteBuf buf) { this(buf.readInt()); }
    public static void encode(RegionalWeatherSyncPacket msg, FriendlyByteBuf buf) { buf.writeInt(msg.ordinal); }
    public static void handle(RegionalWeatherSyncPacket msg, Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if (ctx.getDirection() == NetworkDirection.PLAY_TO_CLIENT) ctx.enqueueWork(() -> {
            RegionalWeatherData.clientForced = msg.ordinal >= 0 && msg.ordinal < WeatherKind.values().length
                ? WeatherKind.values()[msg.ordinal] : null;
        });
        ctx.setPacketHandled(true);
    }
    @SubscribeEvent public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(RegionalWeatherSyncPacket.class,
            RegionalWeatherSyncPacket::encode, RegionalWeatherSyncPacket::new, RegionalWeatherSyncPacket::handle);
    }
    public static void send(ServerPlayer player) {
        WeatherKind kind = RegionalWeatherData.get(player.serverLevel()).forced;
        TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.sendTo(new RegionalWeatherSyncPacket(kind == null ? -1 : kind.ordinal()),
            player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    @Mod.EventBusSubscriber public static class Events {
        @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e) {
            if (e.getEntity() instanceof ServerPlayer p) send(p);
        }
        @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent e) {
            if (e.getEntity() instanceof ServerPlayer p) send(p);
        }
        @SubscribeEvent public static void respawn(PlayerEvent.PlayerRespawnEvent e) {
            if (e.getEntity() instanceof ServerPlayer p) send(p);
        }
    }
}
