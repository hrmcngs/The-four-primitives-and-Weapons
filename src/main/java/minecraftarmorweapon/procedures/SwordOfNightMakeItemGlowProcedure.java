package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

public class SwordOfNightMakeItemGlowProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		return entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.SWORD_OF_NIGHT_EFFECT.get()) : false;
	}
}
