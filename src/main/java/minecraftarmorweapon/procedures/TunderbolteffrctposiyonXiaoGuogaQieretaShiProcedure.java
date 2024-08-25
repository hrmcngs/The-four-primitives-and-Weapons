package minecraftarmorweapon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

public class TunderbolteffrctposiyonXiaoGuogaQieretaShiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).setBaseValue((entity.getPersistentData().getDouble("tunderboltattackdamege")));
		new ItemStack(MinecraftArmorWeaponModItems.KURIKARAKEN.get()).getOrCreateTag().putDouble("CustomModelData", 0);
		new ItemStack(MinecraftArmorWeaponModItems.KURIKARAKENSWORD.get()).getOrCreateTag().putDouble("CustomModelData", 0);
		new ItemStack(MinecraftArmorWeaponModItems.KURIKARAKENUTIGATANA.get()).getOrCreateTag().putDouble("CustomModelData", 0);
	}
}
