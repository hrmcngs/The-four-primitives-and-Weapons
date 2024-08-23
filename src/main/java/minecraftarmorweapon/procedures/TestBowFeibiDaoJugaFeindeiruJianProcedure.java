package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.Entity;

public class TestBowFeibiDaoJugaFeindeiruJianProcedure {
	public static void execute(Entity entity, Entity immediatesourceentity) {
		if (entity == null || immediatesourceentity == null)
			return;
		entity.getPersistentData().putDouble("mineraft_armor_weapon:test_bow_pulling", (immediatesourceentity instanceof Projectile _projEnt ? _projEnt.getDeltaMovement().length() : 0));
	}
}
