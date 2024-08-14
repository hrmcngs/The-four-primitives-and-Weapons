
package minecraftarmorweapon.item;

import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;

public class TestBowArrow0Item extends Item {
	public TestBowArrow0Item() {
		super(new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_WEAPON).stacksTo(1).rarity(Rarity.EPIC));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.BOW;
	}
}
