package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class YouwakaranposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("local", (Math.toRadians(entity.getYRot()) - Mth.nextDouble(RandomSource.create(), 0, 180)));
		entity.getPersistentData().putDouble("angle", (entity.getXRot()));
	}
}
