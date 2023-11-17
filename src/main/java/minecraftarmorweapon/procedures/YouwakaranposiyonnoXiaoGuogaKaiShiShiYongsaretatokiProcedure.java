package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;

public class YouwakaranposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("yaw", Math.toRadians(entity.getYRot()));
		entity.getPersistentData().putDouble("angle", (entity.getXRot()));
	}
}
