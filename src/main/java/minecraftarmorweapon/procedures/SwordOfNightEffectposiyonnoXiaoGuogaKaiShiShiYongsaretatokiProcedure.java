package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;

public class SwordOfNightEffectposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure {
	public static void execute(Entity sourceentity) {
		if (sourceentity == null)
			return;
		double zknockback = 0;
		double yknockback = 0;
		double xknockback = 0;
		double dis = 0;
		xknockback = sourceentity.getX() - sourceentity.getX();
		yknockback = sourceentity.getY() - sourceentity.getY();
		zknockback = sourceentity.getZ() - sourceentity.getZ();
		dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
		if (dis != 0) {
			xknockback = (xknockback / dis) * 3;
			yknockback = Math.min((yknockback / dis) * 3, 2);
			zknockback = (zknockback / dis) * 3;
		} else {
			xknockback = 0;
			yknockback = 0;
			zknockback = 0;
		}
		sourceentity.setDeltaMovement(new Vec3(xknockback, yknockback, zknockback));
	}
}
