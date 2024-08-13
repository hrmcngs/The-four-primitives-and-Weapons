package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class TestBowYoukuritukusitatokiProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putBoolean("minecraft_armor_weapon:armor_stand_tobasu_kill_off", true);
		if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TAMETERU.get()) : false)) {
			if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
				_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.TAMETERU.get(), 20, 1, true, false));
		} else {
			if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
				_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.TAMETERU.get(),
						(int) ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TAMETERU.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.TAMETERU.get()).getDuration() : 0) + 20), 1, true,
						false));
		}
		if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TAMETERU.get()) : false) {
			entity.getPersistentData().putDouble("minecraft_armor_weapon:test_bow_tameteru", (entity.getPersistentData().getDouble("minecraft_armor_weapon:test_bow_tameteru") + 0.05));
		}
		entity.getPersistentData().putBoolean("minecraft_armor_weapon:test_bow_tameteru_true_or_false", true);
		MinecraftArmorWeaponMod.queueServerWork(2, () -> {
			if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TAMETERU.get()) : false)) {
				entity.getPersistentData().putBoolean("minecraft_armor_weapon:test_bow_tameteru_true_or_false", false);
			}
		});
		MinecraftArmorWeaponMod.queueServerWork(200, () -> {
			entity.getPersistentData().putBoolean("minecraft_armor_weapon:armor_stand_tobasu_kill_off", false);
		});
	}
}
