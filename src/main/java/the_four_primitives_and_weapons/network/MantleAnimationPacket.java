package the_four_primitives_and_weapons.network;

import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkDirection;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public record MantleAnimationPacket(UUID player, MantleActionPacket.Action action, InteractionHand hand, long elapsed, int duration) {
    public MantleAnimationPacket(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readEnum(MantleActionPacket.Action.class), buf.readEnum(InteractionHand.class), buf.readLong(), buf.readVarInt());
    }
    public static void encode(MantleAnimationPacket msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.player); buf.writeEnum(msg.action); buf.writeEnum(msg.hand); buf.writeLong(msg.elapsed); buf.writeVarInt(msg.duration);
    }
    public static void handle(MantleAnimationPacket msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
            ctx.get().setPacketHandled(true); return;
        }
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
            () -> () -> the_four_primitives_and_weapons.client.MantleAnimationState.accept(msg)));
        ctx.get().setPacketHandled(true);
    }
    @SubscribeEvent public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(MantleAnimationPacket.class,
            MantleAnimationPacket::encode, MantleAnimationPacket::new, MantleAnimationPacket::handle);
    }
}
