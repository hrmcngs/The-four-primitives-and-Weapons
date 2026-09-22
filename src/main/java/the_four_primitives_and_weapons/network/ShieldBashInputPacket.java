package the_four_primitives_and_weapons.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.event.ShieldBashHandler;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public record ShieldBashInputPacket(boolean forward) {
    public ShieldBashInputPacket(FriendlyByteBuf buffer) { this(buffer.readBoolean()); }
    public static void encode(ShieldBashInputPacket packet, FriendlyByteBuf buffer) { buffer.writeBoolean(packet.forward); }
    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(ShieldBashInputPacket.class,
                ShieldBashInputPacket::encode, ShieldBashInputPacket::new, ShieldBashInputPacket::handle);
    }
    public static void handle(ShieldBashInputPacket packet, Supplier<NetworkEvent.Context> supplier) {
        var context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) context.enqueueWork(() -> {
            var player = context.getSender();
            if (player != null && player.isAlive() && !player.isSpectator()) {
                ShieldBashHandler.updateForwardInput(player, packet.forward);
            }
        });
        context.setPacketHandled(true);
    }
}
