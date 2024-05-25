
package minecraftarmorweapon.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.entity.LivingEntity;

import minecraftarmorweapon.procedures.KentimobugaturudeGongJisaretatokiProcedure;

import minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;

public class KentiItem extends FishingRodItem {
	public KentiItem() {
		super(new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_YOPKEINAMONO).durability(0));
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		KentimobugaturudeGongJisaretatokiProcedure.execute(entity);
		return retval;
	}
}
