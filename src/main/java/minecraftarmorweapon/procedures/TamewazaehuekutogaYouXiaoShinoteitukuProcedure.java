package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModEnchantments;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class TamewazaehuekutogaYouXiaoShinoteitukuProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double Radius = 0;
		double loop = 0;
		double X = 0;
		double Y = 0;
		double Z = 0;
		double dis1 = 0;
		if (entity.getPersistentData().getDouble("tame") > 9) {
			loop = Math.toRadians(entity.getPersistentData().getDouble("yaw"));
			Radius = entity.getPersistentData().getDouble("Radius");
			for (int index0 = 0; index0 < 36; index0++) {
				Radius = entity.getPersistentData().getDouble("Radius");
				for (int index1 = 0; index1 < 15; index1++) {
					X = x + Math.cos(loop) * Radius;
					Y = y + 1;
					Z = z + Math.sin(loop) * Radius;
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.SWEEP_ATTACK, X, Y, Z, 1, 0.1, 0.1, 0.1, 0);
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X, Y, Z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.2 0.2 0.2 0 1");
					{
						final Vec3 _center = new Vec3(X, Y, Z);
						List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
								.collect(Collectors.toList());
						for (Entity entityiterator : _entfound) {
							if (!(entityiterator == entity)) {
								if (entityiterator instanceof Mob) {
									if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
										{
											Entity _ent = entityiterator;
											if (!_ent.level.isClientSide() && _ent.getServer() != null) {
												_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(),
														_ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/kill @s");
											}
										}
										{
											Entity _ent = entityiterator;
											if (!_ent.level.isClientSide() && _ent.getServer() != null) {
												_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(),
														_ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/deta merge entity @s (Health:0)");
											}
										}
									} else {
										entityiterator.hurt(DamageSource.GENERIC, 15);
									}
								}
							}
						}
					}
					Radius = Radius + 0.5;
				}
				loop = loop + Math.toRadians(5);
			}
			entity.getPersistentData().putDouble("Radius", (entity.getPersistentData().getDouble("Radius") + 1));
		}
	}
}
