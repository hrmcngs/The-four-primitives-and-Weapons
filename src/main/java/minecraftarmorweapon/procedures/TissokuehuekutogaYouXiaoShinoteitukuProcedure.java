package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class TissokuehuekutogaYouXiaoShinoteitukuProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		MinecraftArmorWeaponMod.queueServerWork(5, () -> {
			entity.setAirSupply((int) (entity.getAirSupply()
					- (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TISSOKU.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.TISSOKU.get()).getAmplifier() : 0)));
			if (entity.getAirSupply() == 0) {
				if (entity instanceof LivingEntity _entity)
					_entity.hurt(new DamageSource("\u7A92\u606F ").bypassArmor(), (float) (entity.getAirSupply() * 6 * 3));
			}
		});
	}
}
