package the_four_primitives_and_weapons.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.events.DodgeAndBattouHandler;

/** Explicit wheel entries retain normal draw/store fallback without sending mantle actions. */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public record WeaponWheelDefaultActionPacket(boolean sheath) {
    public WeaponWheelDefaultActionPacket(FriendlyByteBuf buf) { this(buf.readBoolean()); }
    public static void encode(WeaponWheelDefaultActionPacket msg, FriendlyByteBuf buf) { buf.writeBoolean(msg.sheath); }
    private static boolean weapon(ItemStack stack) {
        return DodgeAndBattouHandler.isWeapon(stack) && !DodgeAndBattouHandler.isSaya(stack)
            && !stack.getItem().getClass().getSimpleName().contains("Bluepurge");
    }
    public static void handle(WeaponWheelDefaultActionPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = ctx.get().getSender();
            if (player == null || !player.isAlive() || player.isSpectator()) return;
            boolean holdingWeapon = weapon(player.getMainHandItem()) || weapon(player.getOffhandItem());
            if (msg.sheath == holdingWeapon) RMessage.pressAction(player, 0, 0);
        });
        ctx.get().setPacketHandled(true);
    }
    @SubscribeEvent public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(WeaponWheelDefaultActionPacket.class,
            WeaponWheelDefaultActionPacket::encode, WeaponWheelDefaultActionPacket::new, WeaponWheelDefaultActionPacket::handle);
    }
}
