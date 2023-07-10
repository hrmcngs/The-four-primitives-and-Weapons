
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
		if (event.getType() == MinecraftArmorWeaponModVillagerProfessions.ARMOR_AND_WEAPON_VILLAGERS_POSITION.get()) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.BOOK, 10), new ItemStack(MinecraftArmorWeaponModItems.COMPRESSED_MAGMA.get(), 64), new ItemStack(MinecraftArmorWeaponModItems.FIREBALL.get()), 10, 5, 0.05f));
		}
	}
}
