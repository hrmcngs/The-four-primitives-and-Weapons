package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.network.MinecraftArmorWeaponModVariables;

public class DddgemuNeiniobareiwoBiaoShiProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		return (entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).ddd;
	}
}
