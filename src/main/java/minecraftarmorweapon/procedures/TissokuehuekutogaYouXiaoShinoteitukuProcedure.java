package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class TissokuehuekutogaYouXiaoShinoteitukuProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		MinecraftArmorWeaponMod.queueServerWork(5, () -> {
			entity.setAirSupply((int) (entity.getAirSupply() - 1));
		});
	}
}
