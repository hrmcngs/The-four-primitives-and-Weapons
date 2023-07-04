
package net.mcreator.minecraftarmorweapon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

import net.mcreator.minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;

public class BubbleshotItem extends Item {
	public BubbleshotItem() {
		super(new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_MAGIC_BOOK).stacksTo(1).rarity(Rarity.RARE));
	}
}
