package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;
import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;
import minecraftarmorweapon.init.MinecraftArmorWeaponModEnchantments;

import minecraftarmorweapon.entity.SkeltonMobEntity;
import minecraftarmorweapon.entity.OtiruyoEntity;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class ZanngekitokubetuehuekutogaYouXiaoShinoteitukuProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double loop = 0;
		double XRadius2 = 0;
		double ZRadius2 = 0;
		double X = 0;
		double Y = 0;
		double Z = 0;
		double Y_pos = 0;
		double random = 0;
		double loop1 = 0;
		double Y_pos1 = 0;
		if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.ZANNGEKITOKUBETU.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.ZANNGEKITOKUBETU.get()).getAmplifier() : 0) == 2) {
			loop1 = entity.getPersistentData().getDouble("local1");
			loop = entity.getPersistentData().getDouble("local");
			entity.getPersistentData().putDouble("Xpos", (entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance")));
			entity.getPersistentData().putDouble("Zpos", (entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")));
			XRadius2 = 3;
			ZRadius2 = 3;
			Y_pos = entity.getPersistentData().getDouble("Ypos") + 3;
			Y_pos1 = entity.getPersistentData().getDouble("Ypos") + 3;
			for (int index0 = 0; index0 < 100; index0++) {
				if (world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))).canOcclude()) {
					entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
					Y_pos = entity.getPersistentData().getDouble("Ypos") + 3;
				} else {
					break;
				}
			}
			for (int index1 = 0; index1 < 100; index1++) {
				if (world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))).canOcclude()) {
					entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
					Y_pos = entity.getPersistentData().getDouble("Ypos") + 1;
					break;
				}
				entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
				Y_pos = entity.getPersistentData().getDouble("Ypos") + 1;
			}
			world.levelEvent(2001, new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos")),
					Block.getId((world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))))));
			for (int index2 = 0; index2 < 36; index2++) {
				X = entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance") + Math.cos(loop) * XRadius2;
				Y = Y_pos + 3;
				Z = entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance") + Math.sin(loop) * ZRadius2;
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.SWEEP_ATTACK, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.CLOUD, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
				{
					final Vec3 _center = new Vec3(X, Y, Z);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator == entity)) {
							if (!(entityiterator instanceof OtiruyoEntity)) {
								if (!(entityiterator instanceof SkeltonMobEntity)) {
									if (entityiterator instanceof LivingEntity) {
										if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
											if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TUNDERBOLTEFFRCT.get()) : false) {
												if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get(), 100, 2, true, false));
											}
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
											if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TUNDERBOLTEFFRCT.get()) : false) {
												if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get(), 100, 2, true, false));
											}
											if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.KURIKARAKEN.get()
													|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.KURIKARAKENSWORD.get()
													|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.KURIKARAKENUTIGATANA.get())
													&& (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMobType() == MobType.UNDEAD : false)) {
												entityiterator.hurt(DamageSource.GENERIC, 15);
											}
											entityiterator.hurt(DamageSource.GENERIC, 5);
										}
									}
								}
							}
						}
					}
				}
				loop = loop - Math.toRadians(5);
				Y_pos = Y_pos - 0.0555555555555556;
			}
			for (int index3 = 0; index3 < 36; index3++) {
				X = entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance") + Math.cos(loop1) * XRadius2;
				Y = Y_pos1 - 3;
				Z = entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance") + Math.sin(loop1) * ZRadius2;
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.SWEEP_ATTACK, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.CLOUD, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
				{
					final Vec3 _center = new Vec3(X, Y, Z);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator == entity)) {
							if (!(entityiterator instanceof OtiruyoEntity)) {
								if (!(entityiterator instanceof SkeltonMobEntity)) {
									if (entityiterator instanceof LivingEntity) {
										if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
											if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TUNDERBOLTEFFRCT.get()) : false) {
												if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get(), 100, 2, true, false));
											}
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
											if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TUNDERBOLTEFFRCT.get()) : false) {
												if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get(), 100, 2, true, false));
											}
											if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.KURIKARAKEN.get()
													|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.KURIKARAKENSWORD.get()
													|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.KURIKARAKENUTIGATANA.get())
													&& (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMobType() == MobType.UNDEAD : false)) {
												entityiterator.hurt(DamageSource.GENERIC, 15);
											}
											entityiterator.hurt(DamageSource.GENERIC, 5);
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
			entity.getPersistentData().putDouble("distance", (entity.getPersistentData().getDouble("distance") + 0.8));
		} else {
			loop = entity.getPersistentData().getDouble("local");
			entity.getPersistentData().putDouble("Xpos", (entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance")));
			entity.getPersistentData().putDouble("Zpos", (entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")));
			XRadius2 = 3;
			ZRadius2 = 3;
			random = entity.getPersistentData().getDouble("random");
			Y_pos = entity.getPersistentData().getDouble("Ypos") + 1;
			for (int index4 = 0; index4 < 100; index4++) {
				if (world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))).canOcclude()) {
					entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
					Y_pos = entity.getPersistentData().getDouble("Ypos") + 1;
				} else {
					break;
				}
			}
			for (int index5 = 0; index5 < 100; index5++) {
				if (world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))).canOcclude()) {
					entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
					Y_pos = entity.getPersistentData().getDouble("Ypos") + 1;
					break;
				}
				entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
				Y_pos = entity.getPersistentData().getDouble("Ypos") + 1;
			}
			world.levelEvent(2001, new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos")),
					Block.getId((world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))))));
			for (int index6 = 0; index6 < 36; index6++) {
				X = entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance") + Math.cos(loop) * XRadius2;
				Y = Y_pos + 1;
				Z = entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance") + Math.sin(loop) * ZRadius2;
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.SWEEP_ATTACK, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.CLOUD, X, Y, Z, 3, 0.1, 0.1, 0.1, 0);
				{
					final Vec3 _center = new Vec3(X, Y, Z);
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator == entity)) {
							if (!(entityiterator instanceof OtiruyoEntity)) {
								if (!(entityiterator instanceof SkeltonMobEntity)) {
									if (entityiterator instanceof Mob) {
										if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
											if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TUNDERBOLTEFFRCT.get()) : false) {
												if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get(), 100, 2, true, false));
											}
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
											if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TUNDERBOLTEFFRCT.get()) : false) {
												if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
													_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get(), 100, 2, true, false));
											}
											if (((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.KURIKARAKEN.get()
													|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.KURIKARAKENSWORD.get()
													|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.KURIKARAKENUTIGATANA.get())
													&& (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMobType() == MobType.UNDEAD : false)) {
												entityiterator.hurt(DamageSource.GENERIC, 15);
											}
											entityiterator.hurt(DamageSource.GENERIC, 5);
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
			entity.getPersistentData().putDouble("distance", (entity.getPersistentData().getDouble("distance") + 0.8));
		}
	}
}
