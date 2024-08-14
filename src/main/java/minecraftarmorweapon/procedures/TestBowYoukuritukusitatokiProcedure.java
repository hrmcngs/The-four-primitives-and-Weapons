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
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.TAMETERU.get(), 20, 1, true, false));
		entity.getPersistentData().putBoolean("minecraft_armor_weapon:test_bow_tameteru_true_or_false", true);
		MinecraftArmorWeaponMod.queueServerWork(5, () -> {
			if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TAMETERU.get()) : false)) {
				entity.getPersistentData().putBoolean("minecraft_armor_weapon:test_bow_tameteru_true_or_false", false);
			}
		});
		MinecraftArmorWeaponMod.queueServerWork(200, () -> {
			entity.getPersistentData().putBoolean("minecraft_armor_weapon:armor_stand_tobasu_kill_off", false);
		});
	}
}
