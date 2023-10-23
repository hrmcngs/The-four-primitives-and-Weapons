
/*
*	MCreator note: This file will be REGENERATED on each build.
*/
package minecraftarmorweapon.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.common.BasicItemListing;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MinecraftArmorWeaponModTrades {
	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event) {
		if (event.getType() == MinecraftArmorWeaponModVillagerProfessions.MINECRAFT_ARMOR_AND_WEAPON.get()) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.ARROW, 3), new ItemStack(Items.RABBIT_HIDE, 20), new ItemStack(MinecraftArmorWeaponModItems.QUIVER_ITEM.get()), 10, 5, 0.05f));
		}
	}
}
