package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class TissokuehuekutogaYouXiaoShinoteitukuProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TISSOKU.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.TISSOKU.get()).getDuration() : 0,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TISSOKU.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.TISSOKU.get()).getAmplifier() : 0, true, false));
		MinecraftArmorWeaponMod.queueServerWork(5, () -> {
			if (entity.getAirSupply() <= 1) {
				if (entity instanceof LivingEntity _entity)
					_entity.hurt(new DamageSource("\u7A92\u606F ").bypassArmor(), 3);
				entity.setAirSupply((int) (entity.getAirSupply() - 5));
			}
		});
		if (entity.getAirSupply() >= 1) {
			entity.setAirSupply((int) (entity.getAirSupply() - 5));
		}
	}
}
