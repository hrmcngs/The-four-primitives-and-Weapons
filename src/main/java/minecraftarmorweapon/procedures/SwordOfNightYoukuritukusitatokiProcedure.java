package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

import java.util.Comparator;

public class SwordOfNightYoukuritukusitatokiProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double r = 0;
		r = 1;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.SWORD_OF_NIGHT.get()) {
			for (int index0 = 0; index0 < 20; index0++) {
				if (!world.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(new Vec3((entity.getX() + r * entity.getLookAngle().x), (entity.getY() + 1.5 + r * entity.getLookAngle().y), (entity.getZ() + r * entity.getLookAngle().z)), 0.5, 0.5, 0.5),
						e -> true).isEmpty()) {
					if (((Entity) world
							.getEntitiesOfClass(LivingEntity.class,
									AABB.ofSize(new Vec3((entity.getX() + r * entity.getLookAngle().x), (entity.getY() + 1.5 + r * entity.getLookAngle().y), (entity.getZ() + r * entity.getLookAngle().z)), 0.5, 0.5, 0.5), e -> true)
							.stream().sorted(new Object() {
								Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
									return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
								}
							}.compareDistOf((entity.getX() + r * entity.getLookAngle().x), (entity.getY() + 1.5 + r * entity.getLookAngle().y), (entity.getZ() + r * entity.getLookAngle().z))).findFirst().orElse(null)) instanceof LivingEntity _entity
							&& !_entity.level.isClientSide())
						_entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60, 1, true, false));
					break;
				}
				r = r + 1;
			}
		}
	}
}
