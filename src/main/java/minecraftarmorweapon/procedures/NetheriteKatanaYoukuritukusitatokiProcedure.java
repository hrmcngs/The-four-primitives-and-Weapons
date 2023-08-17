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
				_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.KAITENN.get(), 120, 1, true, false));
			for (int index0 = 0; index0 < 180; index0++) {
				{
					Entity _ent = entity;
					_ent.setYRot((float) (entity.getYRot() + 2));
					_ent.setXRot(0);
					_ent.setYBodyRot(_ent.getYRot());
					_ent.setYHeadRot(_ent.getYRot());
					_ent.yRotO = _ent.getYRot();
					_ent.xRotO = _ent.getXRot();
					if (_ent instanceof LivingEntity _entity) {
						_entity.yBodyRotO = _entity.getYRot();
						_entity.yHeadRotO = _entity.getYRot();
					}
				}
			}
		}
	}
}
