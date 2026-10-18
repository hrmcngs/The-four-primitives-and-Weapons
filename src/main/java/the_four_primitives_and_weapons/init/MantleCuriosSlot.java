package the_four_primitives_and_weapons.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MantleCuriosSlot {
    @SubscribeEvent public static void register(InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
            () -> new SlotTypeMessage.Builder("mantle").priority(91).size(1).build());
    }
}
