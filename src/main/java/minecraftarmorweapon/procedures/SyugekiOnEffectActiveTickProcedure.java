package minecraftarmorweapon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Block;
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
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;
import minecraftarmorweapon.init.MinecraftArmorWeaponModEnchantments;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class SyugekiOnEffectActiveTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double Zknockback = 0;
		double Yknockback = 0;
		double Xknockback = 0;
		double dis = 0;
		double dis1 = 0;
		entity.getPersistentData().putDouble("Xpos", (entity.getPersistentData().getDouble("X") + Math.sin(Math.toRadians(entity.getPersistentData().getDouble("yaw") + 180)) * entity.getPersistentData().getDouble("distance")));
		entity.getPersistentData().putDouble("Zpos", (entity.getPersistentData().getDouble("Z") + Math.cos(Math.toRadians(entity.getPersistentData().getDouble("yaw"))) * entity.getPersistentData().getDouble("distance")));
		for (int index0 = 0; index0 < 25; index0++) {
			if (world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))).canOcclude()) {
				entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
			} else {
				break;
			}
		}
		for (int index1 = 0; index1 < 50; index1++) {
			if (world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))).canOcclude()) {
				entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
				break;
			}
			entity.getPersistentData().putDouble("Ypos", (entity.getPersistentData().getDouble("Ypos")));
		}
		world.levelEvent(2001, new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos")),
				Block.getId((world.getBlockState(new BlockPos(entity.getPersistentData().getDouble("Xpos"), entity.getPersistentData().getDouble("Ypos"), entity.getPersistentData().getDouble("Zpos"))))));
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.IRON_KATANA.get()) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands()
						.performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3((entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos")), (entity.getPersistentData().getDouble("Zpos"))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "particle minecraft:item iron_ingot ~ ~1 ~ 0.5 0.5 0.5 .0 10 force @p");
		}
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.WATER_KATANA.get()) {
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands()
						.performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3((entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos")), (entity.getPersistentData().getDouble("Zpos"))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "particle minecraft:dolphin ~ ~1 ~ 0.5 0.5 0.5 .0 10 force @p");
			if (world instanceof ServerLevel _level)
				_level.getServer().getCommands()
						.performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3((entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos")), (entity.getPersistentData().getDouble("Zpos"))),
								Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(), "particle minecraft:underwater ~ ~1 ~ 0.5 0.5 0.5 .0 10 force @p");
		}
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.SWEEP_ATTACK, (entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos") + 1), (entity.getPersistentData().getDouble("Zpos")), 10, 0.1, 0.1, 0.1, 0);
		{
			final Vec3 _center = new Vec3((entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos") + 1), (entity.getPersistentData().getDouble("Zpos")));
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
			for (Entity entityiterator : _entfound) {
				if (!(entityiterator == entity)) {
					if (entityiterator instanceof Mob) {
						if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
							if (entityiterator instanceof Mob) {
								{
									Entity _ent = entityiterator;
									if (!_ent.level.isClientSide() && _ent.getServer() != null) {
										_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null,
												4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/kill @s");
									}
								}
								{
									Entity _ent = entityiterator;
									if (!_ent.level.isClientSide() && _ent.getServer() != null) {
										_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null,
												4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/deta merge entity @s (Health:0)");
									}
								}
							}
						} else {
							entityiterator.hurt(DamageSource.GENERIC, 20);
						}
					}
				}
			}
		}
		{
			final Vec3 _center = new Vec3((entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos") + 1.5), (entity.getPersistentData().getDouble("Zpos")));
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
			for (Entity entityiterator : _entfound) {
				if (!(entityiterator == entity)) {
					if (entityiterator instanceof Mob) {
						if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
							if (entityiterator instanceof Mob) {
								{
									Entity _ent = entityiterator;
									if (!_ent.level.isClientSide() && _ent.getServer() != null) {
										_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null,
												4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/kill @s");
									}
								}
								{
									Entity _ent = entityiterator;
									if (!_ent.level.isClientSide() && _ent.getServer() != null) {
										_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null,
												4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/deta merge entity @s (Health:0)");
									}
								}
							}
						} else {
							entityiterator.hurt(DamageSource.GENERIC, 20);
						}
					}
				}
			}
		}
		{
			final Vec3 _center = new Vec3((entity.getPersistentData().getDouble("Xpos")), (entity.getPersistentData().getDouble("Ypos")), (entity.getPersistentData().getDouble("Zpos")));
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
			for (Entity entityiterator : _entfound) {
				if (!(entityiterator == entity)) {
					if (entityiterator instanceof Mob) {
						if (EnchantmentHelper.getItemEnchantmentLevel(MinecraftArmorWeaponModEnchantments.KILL.get(), (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
							if (entityiterator instanceof Mob) {
								{
									Entity _ent = entityiterator;
									if (!_ent.level.isClientSide() && _ent.getServer() != null) {
										_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null,
												4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/kill @s");
									}
								}
								{
									Entity _ent = entityiterator;
									if (!_ent.level.isClientSide() && _ent.getServer() != null) {
										_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null,
												4, _ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/deta merge entity @s (Health:0)");
									}
								}
							}
						} else {
							entityiterator.hurt(DamageSource.GENERIC, 20);
						}
					}
				}
			}
		}
		entity.getPersistentData().putDouble("distance", (entity.getPersistentData().getDouble("distance") + 0.8));
	}
}
