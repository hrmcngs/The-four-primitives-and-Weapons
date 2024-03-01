package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;

public class RpgBookGuiGUIgaBiziraretatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.getPersistentData().getBoolean("minecraft_armor_weapon:QuiverItemsyorisunna") == true) {
			entity.getPersistentData().putBoolean("minecraft_armor_weapon:QuiverItemsyorisunna", false);
		}
	}
}
