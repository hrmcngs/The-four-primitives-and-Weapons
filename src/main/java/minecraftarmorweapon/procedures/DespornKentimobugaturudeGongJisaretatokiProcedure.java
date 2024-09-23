package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

public class DespornKentimobugaturudeGongJisaretatokiProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (!(sourceentity instanceof Player)) {
			if (!(sourceentity instanceof ServerPlayer)) {
				if (!entity.level.isClientSide())
					entity.discard();
			}
		}
	}
}
