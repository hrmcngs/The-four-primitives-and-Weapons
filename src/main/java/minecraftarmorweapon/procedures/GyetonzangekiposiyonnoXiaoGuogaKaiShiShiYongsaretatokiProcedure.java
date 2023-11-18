package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;

public class GyetonzangekiposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double Radius = 0;
		double Ypos = 0;
		double Z1 = 0;
		double loop2 = 0;
		double Z2 = 0;
		double Y = 0;
		double X1 = 0;
		double X2 = 0;
		double loop1 = 0;
		entity.getPersistentData().putDouble("local", Math.toRadians(entity.getYRot()));
		entity.getPersistentData().putDouble("local1", Math.toRadians(entity.getYRot() + 180));
	}
}
