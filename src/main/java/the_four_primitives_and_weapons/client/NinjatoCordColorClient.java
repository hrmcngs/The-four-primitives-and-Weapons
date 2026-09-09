package the_four_primitives_and_weapons.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;
import the_four_primitives_and_weapons.util.NinjatoVault;

@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class NinjatoCordColorClient {
    @SubscribeEvent
    public static void register(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> 0xFF000000 | NinjatoVault.cordColor(stack),
            TheFourPrimitivesAndWeaponsModItems.NINJATO_RECALL_CORD.get());
    }
}
