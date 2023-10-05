
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package minecraft_armor_weapon.init;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

import minecraftarmorweapon.client.gui.QuiverinventoryScreen;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MinecraftArmorWeaponModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(MinecraftArmorWeaponModMenus.QUIVERINVENTORY.get(), QuiverinventoryScreen::new);
		});
	}
}
