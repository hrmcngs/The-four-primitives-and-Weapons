package net.mcreator.minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minecraftarmorweapon.network.MinecraftArmorWeaponModVariables;

public class RkigaYasaretatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			double ggg = (entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null)
					.orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).ggg;
			entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null)
					.ifPresent(capability -> {
						String aaa = "";
						switch ((int) ggg) {
							case 2:
								aaa = "薙ぎ払い";
								break;
							case 3:
								aaa = "突き";
								break;
							case 4:
								aaa = "回転斬り";
								break;
							default:
								aaa = Double.toString(ggg);
								break;
						}
//						while (aaa.length() < ggg) {
//							aaa = aaa + String.format("%d", (int) ggg);
//						}
						capability.aaa = aaa;
						capability.ggg = ggg >= 4 ? 2 : ggg + 1;
						capability.syncPlayerVariables(entity);
					});
		}
	}
}
