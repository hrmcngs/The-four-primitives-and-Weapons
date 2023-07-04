
/*
*	MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.minecraftarmorweapon.init;

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
		if (event.getType() == MinecraftArmorWeaponModVillagerProfessions.ARMOR_AND_WEAPON.get()) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.WRITABLE_BOOK), new ItemStack(MinecraftArmorWeaponModItems.FIRE_IRON_INGOT.get(), 64), new ItemStack(MinecraftArmorWeaponModItems.FIREBALL.get()), 1, 72000, 0.05f));
		}
		if (event.getType() == MinecraftArmorWeaponModVillagerProfessions.ARMOR_AND_WEAPON.get()) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Items.WRITABLE_BOOK), new ItemStack(MinecraftArmorWeaponModItems.WATER_IRON_INGOT.get(), 64), new ItemStack(MinecraftArmorWeaponModItems.BUBBLESHOT.get()), 1, 72000, 0.05f));
		}
	}
}
