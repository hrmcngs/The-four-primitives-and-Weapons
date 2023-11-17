package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
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
import net.minecraft.network.chat.Component;
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

public class YouwakaranehuekutogaYouXiaoShinoteitukuProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double Radius = 0;
		double zknockback = 0;
		double zPos = 0;
		double yPos = 0;
		double yknockback = 0;
		double loop = 0;
		double rad_now = 0;
		double xPos = 0;
		double xknockback = 0;
		double dis = 0;
		double angle = 0;
		double Numerical_value = 0;
		double yaw = 0;
		yaw = entity.getPersistentData().getDouble("yaw");
		Numerical_value = 1;
		Radius = 1;
		angle = entity.getPersistentData().getDouble("angle");
		entity.getPersistentData().putDouble("Xpos", (entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance")));
		entity.getPersistentData().putDouble("Zpos", (entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")));
		for (int index0 = 0; index0 < 100; index0++) {
			if (world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))).canOcclude()) {
				entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
			} else {
				break;
			}
		}
		for (int index1 = 0; index1 < 100; index1++) {
			if (world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))).canOcclude()) {
				entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
				break;
			}
			entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
		}
		for (int index2 = 0; index2 < 50; index2++) {
			world.destroyBlock(new BlockPos(
					((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance")) - 1)
							- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw)),
					(entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle)),
					(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")) - 1
							+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw))),
					false);
			world.destroyBlock(new BlockPos(
					(entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance") + 1)
							- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw)),
					(entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle)), entity.getPersistentData().getDouble("Z")
							+ Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance") + 1 + Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw))),
					false);
			world.destroyBlock(new BlockPos(
					(entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
							- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw)),
					(entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle)), entity.getPersistentData().getDouble("Z")
							+ Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance") + Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw))),
					false);
			world.destroyBlock(new BlockPos(
					(entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
							- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw)),
					entity.getPersistentData().getDouble("Ypos") - Numerical_value * Math.sin(Math.toRadians(angle)), entity.getPersistentData().getDouble("Z")
							+ Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance") + Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw))),
					false);
			{
				final Vec3 _center = new Vec3(
						((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
								- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
						((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))), (entity.getPersistentData().getDouble("Z")
								+ Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance") + Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw))));
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
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
										if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.WATER_KATANA.get()
												|| (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.BUBBLESHOT_EFFECT.get()) : false)) {
											if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
												_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.TISSOKU.get(), 100, 2, true, false));
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
										if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.WATER_KATANA.get()
												|| (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.BUBBLESHOT_EFFECT.get()) : false)) {
											if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
												_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.TISSOKU.get(), 100, 2, true, false));
										}
										if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.TUNDERBOLTEFFRCT.get()) : false) {
											if (entityiterator instanceof LivingEntity _entity && !_entity.level.isClientSide())
												_entity.addEffect(new MobEffectInstance(MinecraftArmorWeaponModMobEffects.THUNDER_HIT.get(), 100, 2, true, false));
										}
										if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.GOMANORIKEN.get()
												&& (entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMobType() == MobType.UNDEAD : false)) {
											entityiterator.hurt(DamageSource.GENERIC, 10);
										}
										entityiterator.hurt(DamageSource.GENERIC, 10);
									}
								}
							}
						}
					}
				}
			}
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^ ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^0.3 ^ ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^-0.3 ^ ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^0.6 ^ ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^-0.6 ^ ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^0.9 ^ ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^-0.9 ^ ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^ ^0.3 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^ ^0.6 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^ ^0.9 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^ ^-0.3 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^ ^-0.6 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^ ^-0.9 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^ ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^0.3 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^-0.3 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^0.6 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^-0.6 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^0.9 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^-0.9 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^0.3 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^0.6 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^0.9 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^-0.3 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^-0.6 ^ 0.01 0.01 0.01 0 10");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										((entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance"))
												- Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw))),
										((entity.getPersistentData().getDouble("Ypos") + 1) - Numerical_value * Math.sin(Math.toRadians(angle))),
										(entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")
												+ Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw)))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.385 0 0.213 1 ^ ^-0.9 ^ 0.01 0.01 0.01 0 10");
			Numerical_value = Numerical_value + 0.4;
		}
		entity.getPersistentData().putDouble("distance", (entity.getPersistentData().getDouble("distance") + 0.8));
	}
}
