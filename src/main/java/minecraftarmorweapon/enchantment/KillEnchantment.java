
package minecraftarmorweapon.enchantment;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EquipmentSlot;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

import java.util.List;

public class KillEnchantment extends Enchantment {
	public KillEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, slots);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		Item item = stack.getItem();
		return List.of(Items.IRON_SWORD, Items.WOODEN_SWORD, Items.STONE_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD, Items.TRIDENT, Items.BOW, Items.CROSSBOW, MinecraftArmorWeaponModItems.NETHERITE_KATANA.get(),
				MinecraftArmorWeaponModItems.ITEM_WEAPONSWORD.get()).contains(item);
	}

	@Override
	public boolean isTradeable() {
		return false;
	}
}
