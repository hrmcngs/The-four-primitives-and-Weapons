
package minecraftarmorweapon.item;

import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;
import net.minecraft.world.item.ShieldItem;

public class AchromaticShieldItem extends ShieldItem {
	public AchromaticShieldItem() {
		super(new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_MAGIC_BOOKS).stacksTo(1).fireResistant().rarity(Rarity.COMMON));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.BLOCK;
	}
}
