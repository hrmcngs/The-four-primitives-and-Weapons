
package minecraftarmorweapon.item;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;

import minecraftarmorweapon.procedures.MotaserumobugaturudeGongJisaretatokiProcedure;

import minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;

public class MotaseruItem extends SwordItem {
	public MotaseruItem() {
		super(new Tier() {
			public int getUses() {
				return 0;
			}

			public float getSpeed() {
				return 0f;
			}
//
//			public float getAttackDamageBonus() {
//				return -2f;
//			}
//

			public float getAttackDamageBonus() {
				return 0f;
			}

			public int getLevel() {
				return 0;
			}

			public int getEnchantmentValue() {
				return 0;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		}, 3, 96f, new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_YOPKEINAMONO));
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		MotaserumobugaturudeGongJisaretatokiProcedure.execute(entity, sourceentity);
		return retval;
	}
}
