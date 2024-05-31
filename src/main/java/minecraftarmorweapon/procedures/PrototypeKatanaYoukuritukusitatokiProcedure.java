package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class PrototypeKatanaYoukuritukusitatokiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double r = 0;
		double alpha = 0;
		double beta = 0;
		ZokuseiritokutyuwazaProcedure.execute(world, x, y, z, entity);
	}
}
