package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

public class SyabondamaYuanJuLiWuQiwoLiYongdekiruProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double speed = 0;
		double spread = 0;
		speed = 0.1;
		spread = 0.02;
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.BUBBLE, (entity.getX()), (entity.getY() + entity.getEyeHeight()), (entity.getZ()), 0, (entity.getLookAngle().x * speed + Mth.nextDouble(RandomSource.create(), spread * (-1), spread)),
					(entity.getLookAngle().y * speed + Mth.nextDouble(RandomSource.create(), spread * (-1), spread)), (entity.getLookAngle().z * speed + Mth.nextDouble(RandomSource.create(), spread * (-1), spread)), 1);
	}
}
