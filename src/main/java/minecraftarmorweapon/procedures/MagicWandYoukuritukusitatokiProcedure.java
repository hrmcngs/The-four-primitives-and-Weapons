package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.particles.ParticleTypes;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class MagicWandYoukuritukusitatokiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double r = 0;
		double alpha = 0;
		double beta = 0;
		entity.getPersistentData().putDouble("minecraft_armor_weapon:r", 1);
		entity.getPersistentData().putDouble("minecraft_armor_weapon:alpha", (entity.getYRot()));
		entity.getPersistentData().putDouble("minecraft_armor_weapon:beta", (entity.getXRot()));
		for (int index0 = 0; index0 < 100; index0++) {
			{
				final Vec3 _center = new Vec3(
						(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
								* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
						((y + 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
						(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
								* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))));
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
						.collect(Collectors.toList());
				for (Entity entityiterator : _entfound) {
					if (!(entityiterator == entity)) {
						entityiterator.hurt(DamageSource.GENERIC, 10);
					}
				}
			}
			world.addParticle(ParticleTypes.END_ROD,
					(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
							* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
					((y + 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
					(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
							* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
					0, 0, 0);
			entity.getPersistentData().putDouble("minecraft_armor_weapon:r", (entity.getPersistentData().getDouble("minecraft_armor_weapon:r") + 0.2));
		}
	}
}
