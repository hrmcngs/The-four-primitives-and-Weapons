package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;

public class OnazitakasaArrowposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure {
	public static void execute(double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double r = 0;
		double alpha = 0;
		double beta = 0;
		entity.getPersistentData().putDouble("X", x);
		entity.getPersistentData().putDouble("Z", z);
		entity.getPersistentData().putDouble("Ypos", y);
		entity.getPersistentData().putDouble("yaw", (entity.getYRot()));
		entity.getPersistentData().putDouble("distance", 3);
	}
}
