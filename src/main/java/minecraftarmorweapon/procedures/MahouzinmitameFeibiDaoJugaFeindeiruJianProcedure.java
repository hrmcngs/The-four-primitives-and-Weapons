package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class MahouzinmitameFeibiDaoJugaFeindeiruJianProcedure {
	public static void execute(LevelAccessor world, Entity immediatesourceentity) {
		if (immediatesourceentity == null)
			return;
		if (!immediatesourceentity.isNoGravity()) {
			immediatesourceentity.setNoGravity(true);
			immediatesourceentity.setDeltaMovement(new Vec3((immediatesourceentity.getLookAngle().z * 0.001), (immediatesourceentity.getLookAngle().y * 0.001), (immediatesourceentity.getLookAngle().x * 0.001)));
			MinecraftArmorWeaponMod.queueServerWork(40, () -> {
				if (!immediatesourceentity.level.isClientSide())
					immediatesourceentity.discard();
			});
		}
	}
}
