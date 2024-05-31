package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class MyTestIronKatanaYoukuritukusitatokiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double r = 0;
		double alpha = 0;
		double beta = 0;
		double zknockback = 0;
		double yknockback = 0;
		double xknockback = 0;
		double dis = 0;
		MytestkatanawazaProcedure.execute(world, x, y, z, entity);
	}
}
