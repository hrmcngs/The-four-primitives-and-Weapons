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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModEnchantments;

import minecraftarmorweapon.entity.SkeltonMobEntity;
import minecraftarmorweapon.entity.OtiruyoEntity;

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
		double speed = 0;
		double spread = 0;
		entity.getPersistentData().putDouble("minecraft_armor_weapon:r", 1);
		entity.getPersistentData().putDouble("minecraft_armor_weapon:alpha", (entity.getYRot()));
		entity.getPersistentData().putDouble("minecraft_armor_weapon:beta", (entity.getXRot()));
		entity.getPersistentData().putDouble("minecraft_armor_weapon:caunt", 100);
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "playsound minecraft:entity.arrow.hit_player neutral @s ~ ~ ~ 1 1.5");
			}
		}
		for (int index0 = 0; index0 < (int) entity.getPersistentData().getDouble("minecraft_armor_weapon:caunt"); index0++) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
												* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
										((y + 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
										(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
												* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha"))))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:dust 0.7 0.17 1 0.75 ~ ~ ~ 0.1 0.1 0.1 0 1 force");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL,
								new Vec3(
										(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
												* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
										((y + 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
										(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
												* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha"))))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
						"particle minecraft:crit ~ ~ ~ 0 0 0 0.02 1 force");
			if (!(!world.getEntitiesOfClass(LivingEntity.class,
					AABB.ofSize(new Vec3(
							(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
									* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
							((y - 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
							(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
									* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha"))))),
							2, 2, 2),
					e -> true).isEmpty())
					&& !world.getBlockState(new BlockPos(
							x - (entity.getPersistentData().getDouble("minecraft_armor_weapon:r") + 1) * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
									* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha"))),
							(y - 1) - (entity.getPersistentData().getDouble("minecraft_armor_weapon:r") + 1) * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta"))),
							z + (entity.getPersistentData().getDouble("minecraft_armor_weapon:r") + 1) * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
									* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))))
							.canOcclude()
					&& !(entity.getPersistentData().getDouble("minecraft_armor_weapon:r") >= 20)) {
				entity.getPersistentData().putDouble("minecraft_armor_weapon:r", (entity.getPersistentData().getDouble("minecraft_armor_weapon:r") + 0.2));
			} else {
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL,
									new Vec3(
											(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
													* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
											((y + 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
											(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
													* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha"))))),
									Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:crit ~ ~ ~ 0 0 0 0.3 15 force");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL,
									new Vec3(
											(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
													* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
											((y + 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
											(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
													* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha"))))),
									Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"particle minecraft:dust 0.7 0.17 1 1 ~ ~ ~ 0.25 0.25 0.25 1 15 force");
				if (world instanceof ServerLevel _level)
					_level.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL,
									new Vec3(
											(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
													* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
											((y + 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
											(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
													* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha"))))),
									Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
							"playsound minecraft:entity.firework_rocket.blast neutral @a ~ ~ ~ 2 1.2");
				{
					Entity _ent = entity;
					_ent.teleportTo(
							(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
									* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
							((y + 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
							(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
									* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))));
					if (_ent instanceof ServerPlayer _serverPlayer)
						_serverPlayer.connection.teleport(
								(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
										* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
								((y + 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
								(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
										* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
								_ent.getYRot(), _ent.getXRot());
				}
				{
					final Vec3 _center = new Vec3(
							(x - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
									* Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))),
							((y + 1) - entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.sin(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))),
							(z + entity.getPersistentData().getDouble("minecraft_armor_weapon:r") * Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:beta")))
									* Math.cos(Math.toRadians(entity.getPersistentData().getDouble("minecraft_armor_weapon:alpha")))));
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
							.collect(Collectors.toList());
					for (Entity entityiterator : _entfound) {
						if (!(entityiterator == entity)) {
							if (!(entityiterator instanceof SkeltonMobEntity)) {
								if (!(entityiterator instanceof OtiruyoEntity)) {
									if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
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
										}
									} else {
										if (entityiterator instanceof Mob) {
											entityiterator.hurt(DamageSource.GENERIC, 7);
										}
									}
								}
							}
						}
					}
				}
				break;
			}
		}
	}
}
