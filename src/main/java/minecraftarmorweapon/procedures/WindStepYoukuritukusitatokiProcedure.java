package minecraftarmorweapon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;
import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

public class WindStepYoukuritukusitatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double pitch = 0;
		double yaw = 0;
		double dis = 0;
		double Radius = 0;
		double Ypos = 0;
		double a = 0;
		double r = 0;
		double b = 0;
		double loop = 0;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.WIND_STEP.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.WIND_STEP.get()) {
			if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
				_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.WIND_STEP_EFFECT.get(), 100, 1, true, false));
		}
	}
}
