package the_four_primitives_and_weapons.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.item.*;

@Mod.EventBusSubscriber(modid=TheFourPrimitivesAndWeaponsMod.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public record TicexPatternMessage(InteractionHand hand,ResourceLocation weapon) {
    public TicexPatternMessage(FriendlyByteBuf buf){this(buf.readEnum(InteractionHand.class),buf.readResourceLocation());}
    public static void encode(TicexPatternMessage m,FriendlyByteBuf buf){buf.writeEnum(m.hand);buf.writeResourceLocation(m.weapon);}
    public static void handle(TicexPatternMessage m,Supplier<NetworkEvent.Context> supplier) {
        var ctx=supplier.get();
        ctx.enqueueWork(()->{
            var player=ctx.getSender();if(player==null)return;
            var stack=player.getItemInHand(m.hand);
            var item=ForgeRegistries.ITEMS.getValue(m.weapon);
            if(stack.getItem() instanceof TicexWeaponPatternItem && item!=null && TicexWeaponCatalog.supported(item)) {
                stack.getOrCreateTag().putString("Weapon",m.weapon.toString());
                player.inventoryMenu.broadcastChanges();
            }
        });ctx.setPacketHandled(true);
    }
    @SubscribeEvent public static void register(FMLCommonSetupEvent event) {
        TheFourPrimitivesAndWeaponsMod.addNetworkMessage(TicexPatternMessage.class,TicexPatternMessage::encode,TicexPatternMessage::new,TicexPatternMessage::handle);
    }
}
