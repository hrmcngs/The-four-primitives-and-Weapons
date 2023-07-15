package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;

public class PillagerArmorbutunoMeiteitukunoibentoProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.setTicksFrozen(0);
	}
}
