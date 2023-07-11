
package minecraftarmorweapon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

import minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;

public class BubbleshotItem extends Item {
	public BubbleshotItem() {
		super(new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_MAGIC_BOOKS).stacksTo(1).rarity(Rarity.EPIC));
	}
}
