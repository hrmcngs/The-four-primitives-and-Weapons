
package minecraftarmorweapon.item;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;

public class MagicMcrystalItem extends Item {
	public MagicMcrystalItem() {
		super(new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_MAGIC_BOOKS).stacksTo(64).rarity(Rarity.RARE));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}
}
