package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.network.MinecraftArmorWeaponModVariables;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;
import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;
import minecraftarmorweapon.init.MinecraftArmorWeaponModEntities;
import minecraftarmorweapon.init.MinecraftArmorWeaponModEnchantments;

import minecraftarmorweapon.entity.SkeltonMobEntity;
import minecraftarmorweapon.entity.OtiruyoEntity;
import minecraftarmorweapon.entity.KillotiruEntity;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class LunaYoukuritukusitatokiProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double r = 0;
		double alpha = 0;
		double beta = 0;
		double zPos = 0;
		double yPos = 0;
		double yknockback = 0;
		double xPos = 0;
		double dis = 0;
		double zknockback = 0;
		double rad = 0;
		double rad_now = 0;
		double X = 0;
		double Y = 0;
		double Z = 0;
		double radius = 0;
		double xknockback = 0;
		double a = 0;
		double b = 0;
		double loop = 0;
		double XRadius2 = 0;
		double ZRadius2 = 0;
		double Y_pos = 0;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.LUNA.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.LUNA.get()) {
			if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
				_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.WAZA.get(), 100, 1, true, false));
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 2) {
				if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.KURUTIMENASI.get()) : false)) {
					if (entity instanceof Player _player)
						_player.getCooldowns().addCooldown(MinecraftArmorWeaponModItems.LUNA.get(), 100);
				}
				MinecraftArmorWeaponMod.queueServerWork(2, () -> {
					if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WAZA.get()) : false) {
						if (entity instanceof LivingEntity _entity)
							_entity.swing(InteractionHand.MAIN_HAND, true);
					}
				});
				if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
					_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.KAITEN.get(), 1, 1, true, false));
			}
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 3) {
				if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.KURUTIMENASI.get()) : false)) {
					if (entity instanceof Player _player)
						_player.getCooldowns().addCooldown(MinecraftArmorWeaponModItems.LUNA.get(), 50);
				}
				MinecraftArmorWeaponMod.queueServerWork(2, () -> {
					if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WAZA.get()) : false) {
						if (entity instanceof LivingEntity _entity)
							_entity.swing(InteractionHand.MAIN_HAND, true);
					}
				});
				entity.getPersistentData().putDouble("X", x);
				entity.getPersistentData().putDouble("Z", z);
				entity.getPersistentData().putDouble("Ypos", y);
				entity.getPersistentData().putDouble("yaw", (entity.getYRot()));
				entity.getPersistentData().putDouble("distance", 3);
				if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
					_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.NAGIHARAI.get(), 2, 1, true, false));
			}
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 4) {
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(MinecraftArmorWeaponModItems.LUNA.get(), 60);
				MinecraftArmorWeaponMod.queueServerWork(2, () -> {
					if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WAZA.get()) : false) {
						if (entity instanceof LivingEntity _entity)
							_entity.swing(InteractionHand.MAIN_HAND, true);
					}
				});
				entity.getPersistentData().putBoolean("F to the P from Shibuya city", true);
				if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
					MinecraftArmorWeaponMod.queueServerWork(2, () -> {
						if (world instanceof ServerLevel _level) {
							Entity entityToSpawn = new KillotiruEntity(MinecraftArmorWeaponModEntities.KILLOTIRU.get(), _level);
							entityToSpawn.moveTo(x, (y + 10), z, 0, 0);
							entityToSpawn.setYBodyRot(0);
							entityToSpawn.setYHeadRot(0);
							if (entityToSpawn instanceof Mob _mobToSpawn)
								_mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
							world.addFreshEntity(entityToSpawn);
						}
						MinecraftArmorWeaponMod.queueServerWork(20, () -> {
							entity.getPersistentData().putBoolean("F to the P from Shibuya city", false);
						});
					});
				} else {
					MinecraftArmorWeaponMod.queueServerWork(2, () -> {
						if (world instanceof ServerLevel _level) {
							Entity entityToSpawn = new OtiruyoEntity(MinecraftArmorWeaponModEntities.OTIRUYO.get(), _level);
							entityToSpawn.moveTo(x, (y + 10), z, 0, 0);
							entityToSpawn.setYBodyRot(0);
							entityToSpawn.setYHeadRot(0);
							if (entityToSpawn instanceof Mob _mobToSpawn)
								_mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
							world.addFreshEntity(entityToSpawn);
						}
						MinecraftArmorWeaponMod.queueServerWork(20, () -> {
							entity.getPersistentData().putBoolean("F to the P from Shibuya city", false);
						});
					});
				}
			}
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 5) {
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(MinecraftArmorWeaponModItems.LUNA.get(), 50);
				MinecraftArmorWeaponMod.queueServerWork(2, () -> {
					if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WAZA.get()) : false) {
						if (entity instanceof LivingEntity _entity)
							_entity.swing(InteractionHand.MAIN_HAND, true);
					}
				});
				if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WIND_STEP_EFFECT.get()) : false) {
					{
						final Vec3 _center = new Vec3(x, y, z);
						List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
								.collect(Collectors.toList());
						for (Entity entityiterator : _entfound) {
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
				} else {
					{
						final Vec3 _center = new Vec3(x, y, z);
						List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
								.collect(Collectors.toList());
						for (Entity entityiterator : _entfound) {
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
				entity.getPersistentData().putDouble("X", x);
				entity.getPersistentData().putDouble("Z", z);
				entity.getPersistentData().putDouble("Ypos", y);
				entity.getPersistentData().putDouble("yaw", (entity.getYRot()));
				entity.getPersistentData().putDouble("distance", 3);
				if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
					_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.SYUGEKI.get(), 30, 1, true, false));
			}
			if ((entity.getCapability(MinecraftArmorWeaponModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MinecraftArmorWeaponModVariables.PlayerVariables())).aaa == 6) {
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown(MinecraftArmorWeaponModItems.LUNA.get(), 50);
				MinecraftArmorWeaponMod.queueServerWork(2, () -> {
					if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.WAZA.get()) : false) {
						if (entity instanceof LivingEntity _entity)
							_entity.swing(InteractionHand.MAIN_HAND, true);
					}
				});
				entity.getPersistentData().putDouble("X", x);
				entity.getPersistentData().putDouble("Z", z);
				entity.getPersistentData().putDouble("Ypos", y);
				entity.getPersistentData().putDouble("yaw", (entity.getYRot()));
				entity.getPersistentData().putDouble("distance", 3);
				loop = Math.toRadians(entity.getYRot());
				XRadius2 = 3;
				ZRadius2 = 3;
				Y_pos = y + 2;
				for (int index0 = 0; index0 < 36; index0++) {
					X = x + Math.cos(loop) * XRadius2;
					Y = Y_pos;
					Z = z + Math.sin(loop) * ZRadius2;
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.SWEEP_ATTACK, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.CLOUD, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
					{
						final Vec3 _center = new Vec3(X, Y, Z);
						List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
								.collect(Collectors.toList());
						for (Entity entityiterator : _entfound) {
							if (!(entityiterator == entity)) {
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
											}
										}
									}
								}
							}
						}
					}
					loop = loop + Math.toRadians(5);
					Y_pos = Y_pos - 0.0555555555555556;
				}
				loop = Math.toRadians(entity.getYRot());
				XRadius2 = 4;
				ZRadius2 = 4;
				Y_pos = y + 2;
				for (int index1 = 0; index1 < 36; index1++) {
					X = x + Math.cos(loop) * XRadius2;
					Y = Y_pos;
					Z = z + Math.sin(loop) * ZRadius2;
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.SWEEP_ATTACK, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.CLOUD, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
					{
						final Vec3 _center = new Vec3(X, Y, Z);
						List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
								.collect(Collectors.toList());
						for (Entity entityiterator : _entfound) {
							if (!(entityiterator == entity)) {
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
											}
										}
									}
								}
							}
						}
					}
					loop = loop + Math.toRadians(5);
					Y_pos = Y_pos - 0.0555555555555556;
				}
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.LUNA.get()
						&& (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.LUNA.get()) {
					if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
						_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.ZANNGEKITOKUBETU.get(), 60, 2, true, false));
				} else {
					if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
						_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.ZANNGEKITOKUBETU.get(), 60, 1, true, false));
				}
			}
		}
	}
}
