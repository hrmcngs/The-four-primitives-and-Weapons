
package minecraftarmorweapon.item;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;

import minecraftarmorweapon.procedures.LunaenteiteigaaitemuwoZhentutaShiProcedure;

import minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;

public class LunaItem extends SwordItem {
	public LunaItem() {
		super(new Tier() {
			public int getUses() {
				return 0;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 4f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 2;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		}, 3, -1.4f, new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_WEAPON));
	}

	@Override
	public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
		boolean retval = super.onEntitySwing(itemstack, entity);
		LunaenteiteigaaitemuwoZhentutaShiProcedure.execute(entity.level, entity.getX(), entity.getY(), entity.getZ(), entity);
		return retval;
	}
}
