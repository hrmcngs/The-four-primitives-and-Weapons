package minecraftarmorweapon.init;

import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.CuriosApi;

import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinecraftArmorWeaponModCuriosSlots {
	@SubscribeEvent
	public static void enqueueIMC(final InterModEnqueueEvent event) {
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().size(3).build());
	}
}
