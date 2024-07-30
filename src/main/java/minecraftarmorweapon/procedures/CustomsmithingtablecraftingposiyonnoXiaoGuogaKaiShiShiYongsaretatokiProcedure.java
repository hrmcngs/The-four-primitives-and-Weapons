package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;

public class CustomsmithingtablecraftingposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putBoolean("minecraft_armor_weapon:custom_smithing_table_crafting", true);
	}
}
