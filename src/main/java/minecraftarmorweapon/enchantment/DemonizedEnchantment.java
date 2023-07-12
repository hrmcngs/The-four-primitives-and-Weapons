
package minecraftarmorweapon.enchantment;

import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EquipmentSlot;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

import java.util.List;

public class DemonizedEnchantment extends Enchantment {
	public DemonizedEnchantment(EquipmentSlot... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, slots);
	}

	@Override
	public int getMaxLevel() {
		return 10;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		Item item = stack.getItem();
		return List.of(MinecraftArmorWeaponModItems.NETHERITE_KATANA.get(), Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD, Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE,
				Items.GOLDEN_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE, Items.TRIDENT, Items.BOW, Items.CROSSBOW, MinecraftArmorWeaponModItems.ITEM_WEAPONSWORD.get()).contains(item);
	}

	@Override
	public boolean isTradeable() {
		return false;
	}
}
