package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

public class ThunderHitehuekutogaYouXiaoShinoteitukuProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get()).getDuration() : 0,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get()).getAmplifier() : 0, true, false));
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get()).getDuration() : 0,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get()).getAmplifier() : 0, true, false));
	}
}
