package the_four_primitives_and_weapons.network;

import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public record MantleDeflectPacket(UUID player, int side) {
    public MantleDeflectPacket(FriendlyByteBuf buf) { this(buf.readUUID(), buf.readBoolean() ? 1 : -1); }
    public static void encode(MantleDeflectPacket msg, FriendlyByteBuf buf) { buf.writeUUID(msg.player); buf.writeBoolean(msg.side >= 0); }
    public static void handle(MantleDeflectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
            ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> the_four_primitives_and_weapons.client.MantleAnimationState.deflect(msg.player, msg.side)));
        ctx.get().setPacketHandled(true);
    }
    @SubscribeEvent public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(MantleDeflectPacket.class,
            MantleDeflectPacket::encode, MantleDeflectPacket::new, MantleDeflectPacket::handle);
    }
}
