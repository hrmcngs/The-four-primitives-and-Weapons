package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;

public class TissokuposiyonXiaoGuogaQieretaShiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.setAirSupply((int) (entity.getAirSupply() + 1));
	}
}
