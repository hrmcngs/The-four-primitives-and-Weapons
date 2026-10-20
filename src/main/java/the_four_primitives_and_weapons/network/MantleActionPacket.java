package the_four_primitives_and_weapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.init.KnifeExtrasRegistrar;
import the_four_primitives_and_weapons.events.MantleCombatHandler;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MantleActionPacket {
    public enum Action { THRUST, GUARD }
    private final Action action;
    public MantleActionPacket(Action action) { this.action = action; }
    public MantleActionPacket(FriendlyByteBuf buf) { action = buf.readEnum(Action.class); }
    public static void encode(MantleActionPacket msg, FriendlyByteBuf buf) { buf.writeEnum(msg.action); }
    public static ItemStack equipped(LivingEntity entity) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, KnifeExtrasRegistrar.FANTASY_MANTLE.get())
            .map(result -> result.stack()).orElse(ItemStack.EMPTY);
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) MantleCombatHandler.start(ctx.get().getSender(), action);
        });
        ctx.get().setPacketHandled(true);
    }
    @SubscribeEvent public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(MantleActionPacket.class,
            MantleActionPacket::encode, MantleActionPacket::new, (msg, ctx) -> msg.handle(ctx));
    }
}
