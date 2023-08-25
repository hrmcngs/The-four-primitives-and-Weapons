package minecraftarmorweapon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import minecraftarmorweapon.network.MinecraftArmorWeaponModVariables;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

public class NetheriteKatanaYoukuritukusitatokiProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double degree = 0;
		double xRadius = 0;
		double zRadius = 0;
		double x_pos = 0;
		double y_pos = 0;
		double z_pos = 0;
		double r = 0;
		double alpha = 0;
		double beta = 0;
		if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 2) {
			if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
				_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.KAITENN.get(), 2, 1, true, false));
		}
	}
}
