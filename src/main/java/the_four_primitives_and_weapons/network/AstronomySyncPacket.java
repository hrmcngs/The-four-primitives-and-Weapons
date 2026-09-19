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
import the_four_primitives_and_weapons.world.AstronomyData;
import the_four_primitives_and_weapons.world.AstronomySettings;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public record AstronomySyncPacket(AstronomySettings settings) {
    public AstronomySyncPacket(FriendlyByteBuf buf) {
        this(new AstronomySettings(buf.readFloat(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean(), buf.readFloat(), buf.readFloat()));
    }
    public static void encode(AstronomySyncPacket msg, FriendlyByteBuf buf) {
        var s = msg.settings;
        buf.writeFloat(s.size()); buf.writeInt(s.phase()); buf.writeInt(s.color());
        buf.writeInt(s.meteors()); buf.writeBoolean(s.effects());
        buf.writeFloat(s.solar()); buf.writeFloat(s.lunar());
    }
    public static void handle(AstronomySyncPacket msg, Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT)
            context.enqueueWork(() -> AstronomyData.clientSettings = msg.settings);
        context.setPacketHandled(true);
    }
    @SubscribeEvent public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(AstronomySyncPacket.class,
            AstronomySyncPacket::encode, AstronomySyncPacket::new, AstronomySyncPacket::handle);
    }
    public static void send(ServerPlayer player) {
        TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.sendTo(
            new AstronomySyncPacket(AstronomyData.get(player.serverLevel()).settings()),
            player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    @Mod.EventBusSubscriber
    public static class Events {
        @SubscribeEvent public static void login(PlayerEvent.PlayerLoggedInEvent e) {
            if (e.getEntity() instanceof ServerPlayer player) send(player);
        }
        @SubscribeEvent public static void dimension(PlayerEvent.PlayerChangedDimensionEvent e) {
            if (e.getEntity() instanceof ServerPlayer player) send(player);
        }
        @SubscribeEvent public static void respawn(PlayerEvent.PlayerRespawnEvent e) {
            if (e.getEntity() instanceof ServerPlayer player) send(player);
        }
    }
}
