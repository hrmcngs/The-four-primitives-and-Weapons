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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;
import minecraftarmorweapon.init.MinecraftArmorWeaponModEnchantments;

import minecraftarmorweapon.entity.SkeltonMobEntity;
import minecraftarmorweapon.entity.OtiruyoEntity;
import minecraftarmorweapon.entity.KillotiruEntity;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class OtitaehuekutogaYouXiaoShinoteitukuProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double zRadius3 = 0;
		double zRadius2 = 0;
		double yknockback = 0;
		double zRadius = 0;
		double xRadius2 = 0;
		double xRadius3 = 0;
		double xRadius4 = 0;
		double zRadius4 = 0;
		double dis = 0;
		double loop = 0;
		double X = 0;
		double Z2 = 0;
		double Y = 0;
		double Z3 = 0;
		double Z4 = 0;
		double Z = 0;
		double X2 = 0;
		double X3 = 0;
		double X4 = 0;
		double xknockback = 0;
		double loop2 = 0;
		double loop3 = 0;
		double zknockback = 0;
		double xRadius = 0;
		double loop4 = 0;
		double Y2 = 0;
		double Y3 = 0;
		double Y4 = 0;
		if (entity instanceof KillotiruEntity) {
			{
				final Vec3 _center = new Vec3(x, y, z);
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
						.collect(Collectors.toList());
				for (Entity entityiterator : _entfound) {
					if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true) || entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == false) {
						if (!(entityiterator == entity)) {
							if (!(entityiterator instanceof OtiruyoEntity)) {
								if (!(entityiterator instanceof SkeltonMobEntity)) {
									if (entityiterator instanceof Mob) {
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
										dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
									}
								}
							}
						}
					}
				}
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9;
			zRadius4 = 9;
			for (int index0 = 0; index0 < 72; index0++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true) || entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == false) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
										if (entityiterator instanceof Mob) {
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
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9.1;
			zRadius4 = 9.1;
			for (int index1 = 0; index1 < 72; index1++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true) || entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == false) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
										if (entityiterator instanceof Mob) {
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
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9.2;
			zRadius4 = 9.2;
			for (int index2 = 0; index2 < 72; index2++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WIND_STEP_EFFECT.get()) : false) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/particle dust 0.129 0.780 0.000 1 ~ ~ ~ 0.1 0.1 0.1 1 25");
				}
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true) || entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == false) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
										if (entityiterator instanceof Mob) {
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
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9.3;
			zRadius4 = 3.3;
			for (int index3 = 0; index3 < 72; index3++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WIND_STEP_EFFECT.get()) : false) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/particle dust 0.129 0.780 0.000 1 ~ ~ ~ 0.1 0.1 0.1 1 25");
				}
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true) || entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == false) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
										if (entityiterator instanceof Mob) {
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
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9.4;
			zRadius4 = 9.4;
			for (int index4 = 0; index4 < 72; index4++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WIND_STEP_EFFECT.get()) : false) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/particle dust 0.129 0.780 0.000 1 ~ ~ ~ 0.1 0.1 0.1 1 25");
				}
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true) || entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == false) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
										if (entityiterator instanceof Mob) {
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
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9.5;
			zRadius4 = 9.5;
			for (int index5 = 0; index5 < 72; index5++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WIND_STEP_EFFECT.get()) : false) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/particle dust 0.129 0.780 0.000 1 ~ ~ ~ 0.1 0.1 0.1 1 25");
				}
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true) || entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == false) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
										if (entityiterator instanceof Mob) {
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
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			MinecraftArmorWeaponMod.queueServerWork(2, () -> {
				if (!entity.level.isClientSide())
					entity.discard();
			});
		} else {
			{
				final Vec3 _center = new Vec3(x, y, z);
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
						.collect(Collectors.toList());
				for (Entity entityiterator : _entfound) {
					if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true) || entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == false) {
						if (!(entityiterator == entity)) {
							if (!(entityiterator instanceof OtiruyoEntity)) {
								if (!(entityiterator instanceof SkeltonMobEntity)) {
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
											entityiterator.hurt(DamageSource.GENERIC, 10);
										}
										dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
									}
								}
							}
						}
					}
				}
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9;
			zRadius4 = 9;
			for (int index6 = 0; index6 < 72; index6++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true) || entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == false) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
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
																_ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent),
																"/deta merge entity @s (Health:0)");
													}
												}
											} else {
												entityiterator.hurt(DamageSource.GENERIC, 10);
											}
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9.1;
			zRadius4 = 9.1;
			for (int index7 = 0; index7 < 72; index7++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true) || entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == false) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
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
																_ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent),
																"/deta merge entity @s (Health:0)");
													}
												}
											} else {
												entityiterator.hurt(DamageSource.GENERIC, 10);
											}
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9.2;
			zRadius4 = 9.2;
			for (int index8 = 0; index8 < 72; index8++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WIND_STEP_EFFECT.get()) : false) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/particle dust 0.129 0.780 0.000 1 ~ ~ ~ 0.1 0.1 0.1 1 25");
				}
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true)) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
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
																_ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent),
																"/deta merge entity @s (Health:0)");
													}
												}
											} else {
												entityiterator.hurt(DamageSource.GENERIC, 10);
											}
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9.3;
			zRadius4 = 3.3;
			for (int index9 = 0; index9 < 72; index9++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WIND_STEP_EFFECT.get()) : false) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/particle dust 0.129 0.780 0.000 1 ~ ~ ~ 0.1 0.1 0.1 1 25");
				}
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true)) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
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
																_ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent),
																"/deta merge entity @s (Health:0)");
													}
												}
											} else {
												entityiterator.hurt(DamageSource.GENERIC, 10);
											}
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9.4;
			zRadius4 = 9.4;
			for (int index10 = 0; index10 < 72; index10++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WIND_STEP_EFFECT.get()) : false) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/particle dust 0.129 0.780 0.000 1 ~ ~ ~ 0.1 0.1 0.1 1 25");
				}
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true)) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
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
																_ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent),
																"/deta merge entity @s (Health:0)");
													}
												}
											} else {
												entityiterator.hurt(DamageSource.GENERIC, 10);
											}
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			loop4 = Math.toRadians(entity.getYRot());
			xRadius4 = 9.5;
			zRadius4 = 9.5;
			for (int index11 = 0; index11 < 72; index11++) {
				X4 = x + Math.cos(loop4) * xRadius4;
				Y4 = y + 1;
				Z4 = z + Math.sin(loop4) * zRadius4;
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 1 1 1 1 ~ ~ ~ 0.1 0.1 0.1 0 5");
				if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WIND_STEP_EFFECT.get()) : false) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(X4, Y4, Z4), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"/particle dust 0.129 0.780 0.000 1 ~ ~ ~ 0.1 0.1 0.1 1 25");
				}
				{
					final Vec3 _center = new Vec3(X4, Y4, Z4);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator.getPersistentData().getBoolean("F to the P from Shibuya city") == true)) {
							if (!(entityiterator == entity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (!(entityiterator instanceof SkeltonMobEntity)) {
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
																_ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent),
																"/deta merge entity @s (Health:0)");
													}
												}
											} else {
												entityiterator.hurt(DamageSource.GENERIC, 10);
											}
											dis = Math.abs(xknockback) + Math.abs(yknockback) + Math.abs(zknockback);
										}
									}
								}
							}
						}
					}
				}
				loop4 = loop4 + Math.toRadians(5);
			}
			MinecraftArmorWeaponMod.queueServerWork(2, () -> {
				if (!entity.level.isClientSide())
					entity.discard();
			});
		}
	}
}
