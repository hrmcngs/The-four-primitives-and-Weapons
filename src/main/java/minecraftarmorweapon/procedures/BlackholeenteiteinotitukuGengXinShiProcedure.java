package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

import minecraftarmorweapon.entity.SkeltonMobEntity;
import minecraftarmorweapon.entity.OtiruyoEntity;
import minecraftarmorweapon.entity.BlackholeEntity;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class BlackholeenteiteinotitukuGengXinShiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double dx = 0;
		double dy = 0;
		double dz = 0;
		MukoukaGuardProcedure.execute(entity);
		{
			final Vec3 _center = new Vec3(x, y, z);
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(100 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
			for (Entity entityiterator : _entfound) {
				if (!(entityiterator == entity)) {
					if (!(entityiterator instanceof OtiruyoEntity)) {
						if (!(entityiterator instanceof SkeltonMobEntity)) {
							if (!(entityiterator instanceof BlackholeEntity)) {
								if (!(entityiterator instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.BLACK_HOLE_EFFECT.get()) : false)) {
									dx = entity.getX() - entityiterator.getX();
									dy = entity.getY() - entityiterator.getY();
									dz = entity.getZ() - entityiterator.getZ();
									for (int index0 = 0; index0 < 20; index0++) {
										entityiterator.setDeltaMovement(new Vec3((dx / 20), (dy / 20), (dz / 20)));
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
