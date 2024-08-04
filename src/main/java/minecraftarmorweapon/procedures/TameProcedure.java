package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

public class TameProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.COOL_DOWN_1.get()) : false)) {
			entity.getPersistentData().putDouble("minecraft_armor_weapon:tame", (entity.getPersistentData().getDouble("minecraft_armor_weapon:tame") + 0.2));
			entity.getPersistentData().putDouble("minecraft_armor_weapon:radius", 3);
		}
	}
}
