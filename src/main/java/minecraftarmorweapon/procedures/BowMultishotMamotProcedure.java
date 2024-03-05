package minecraftarmorweapon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class BowMultishotMamotProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level, event.player.getX(), event.player.getY(), event.player.getZ(), event.player);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("minecraft_armor_weapon:multishotbowpower") > 2) {
			for (int index0 = 0; index0 < (int) ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("minecraft_armor_weapon:multishotbownumber")); index0++) {
				if (world instanceof ServerLevel projectileLevel) {
					Projectile _entityToSpawn = new Object() {
						public Projectile getArrow(Level level, float damage, int knockback) {
							AbstractArrow entityToSpawn = new Arrow(EntityType.ARROW, level);
							entityToSpawn.setBaseDamage(damage);
							entityToSpawn.setKnockback(knockback);
							entityToSpawn.setSecondsOnFire(100);
							entityToSpawn.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
							return entityToSpawn;
						}
					}.getArrow(projectileLevel, 5, 1);
					_entityToSpawn.setPos(x, y, z);
					_entityToSpawn.shoot((entity.getLookAngle().x + Math.random() - 0.001), (entity.getLookAngle().y + Math.random() - 0.001), (entity.getLookAngle().z + Math.random() - 0.001), 5, 0);
					projectileLevel.addFreshEntity(_entityToSpawn);
				}
			}
			(entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrCreateTag().putDouble("minecraft_armor_weapon:multishotbowpower", 0);
			(entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getOrCreateTag().putDouble("minecraft_armor_weapon:multishotbownumber", 0);
		}
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("minecraft_armor_weapon:multishotbowpower") > 2) {
			for (int index1 = 0; index1 < (int) ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().getDouble("minecraft_armor_weapon:multishotbownumber")); index1++) {
				if (world instanceof ServerLevel projectileLevel) {
					Projectile _entityToSpawn = new Object() {
						public Projectile getArrow(Level level, float damage, int knockback) {
							AbstractArrow entityToSpawn = new Arrow(EntityType.ARROW, level);
							entityToSpawn.setBaseDamage(damage);
							entityToSpawn.setKnockback(knockback);
							entityToSpawn.setSecondsOnFire(100);
							entityToSpawn.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
							return entityToSpawn;
						}
					}.getArrow(projectileLevel, 5, 1);
					_entityToSpawn.setPos(x, y, z);
					_entityToSpawn.shoot((entity.getLookAngle().x + Math.random() - 0.001), (entity.getLookAngle().y + Math.random() - 0.001), (entity.getLookAngle().z + Math.random() - 0.001), 5, 0);
					projectileLevel.addFreshEntity(_entityToSpawn);
				}
			}
			(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().putDouble("minecraft_armor_weapon:multishotbowpower", 0);
			(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getOrCreateTag().putDouble("minecraft_armor_weapon:multishotbownumber", 0);
		}
	}
}
