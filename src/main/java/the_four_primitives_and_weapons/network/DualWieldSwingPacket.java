package the_four_primitives_and_weapons.network;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public record DualWieldSwingPacket(int entityId, boolean offHand, int duration) {
    private static final Map<ServerPlayer, long[]> LAST_SENT = new WeakHashMap<>();
    public DualWieldSwingPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readBoolean(), buf.readVarInt());
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId); buf.writeBoolean(offHand); buf.writeVarInt(duration);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                the_four_primitives_and_weapons.client.DualWieldSwingAnimation.receive(entityId, offHand, duration)));
        }
        context.setPacketHandled(true);
    }
    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(DualWieldSwingPacket.class,
            DualWieldSwingPacket::encode, DualWieldSwingPacket::new, DualWieldSwingPacket::handle);
    }
    public static void send(ServerPlayer player, InteractionHand hand) {
        long now = player.level().getGameTime();
        long[] last = LAST_SENT.computeIfAbsent(player, ignored -> new long[]{Long.MIN_VALUE, Long.MIN_VALUE});
        int index = hand == InteractionHand.OFF_HAND ? 1 : 0;
        if (last[index] == now) return;
        last[index] = now;
        int duration = 6;
        if (player.hasEffect(MobEffects.DIG_SPEED)) duration -= 1 + player.getEffect(MobEffects.DIG_SPEED).getAmplifier();
        else if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) duration += 2 * (1 + player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier());
        TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
            new DualWieldSwingPacket(player.getId(), index == 1, Math.max(1, Math.min(40, duration))));
    }
}
