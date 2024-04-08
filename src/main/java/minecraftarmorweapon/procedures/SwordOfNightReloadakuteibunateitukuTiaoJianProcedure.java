package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;

public class SwordOfNightReloadakuteibunateitukuTiaoJianProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("sword_of_night_shot_number_of_remaining_ammunition_score_2", 0);
	}
}
