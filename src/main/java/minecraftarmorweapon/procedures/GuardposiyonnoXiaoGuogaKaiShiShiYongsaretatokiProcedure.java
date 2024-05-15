package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class GuardposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(
						new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
								_ent.level.getServer(), _ent),
						"summon minecraft:armor_stand ~ ~ ~ {NoGravity:1b,Invisible:1b,Tags:[\"minecraft_armor_weapon_guard_bind\"],Pose:{LeftArm:[0f,90f,-90f],RightArm:[0f,-90f,90f]},DisabledSlots:4144959,HandItems:[{id:\"minecraft:yellow_stained_glass_pane\",Count:1b},{id:\"minecraft:yellow_stained_glass_pane\",Count:1b}]}");
			}
		}
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_knockback_resistance", ((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE).getBaseValue());
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_x_chuzume", (entity.getX()));
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_y_chuzume", (entity.getY()));
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_z_chuzume", (entity.getZ()));
		if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MobEffects.DAMAGE_RESISTANCE) : false) {
			entity.getPersistentData().putBoolean("minecraft_armor_weapon:muteki_resistance_chuzume_copy", true);
			entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_resistance_chuzume_copy_level",
					(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.DAMAGE_RESISTANCE) ? _livEnt.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() : 0));
			entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_resistance_chuzume_copy_tick",
					(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.DAMAGE_RESISTANCE) ? _livEnt.getEffect(MobEffects.DAMAGE_RESISTANCE).getDuration() : 0));
		}
		if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MobEffects.LEVITATION) : false) {
			entity.getPersistentData().putBoolean("minecraft_armor_weapon:muteki_levitation_chuzume_copy", true);
			entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_levitation_chuzume_copy_level",
					(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.LEVITATION) ? _livEnt.getEffect(MobEffects.LEVITATION).getAmplifier() : 0));
			entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_levitation_chuzume_copy_tick",
					(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.LEVITATION) ? _livEnt.getEffect(MobEffects.LEVITATION).getDuration() : 0));
		}
		if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MobEffects.WEAKNESS) : false) {
			entity.getPersistentData().putBoolean("minecraft_armor_weapon:muteki_weakenss_chuzume_copy", true);
			entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_weakenss_chuzume_copy_level",
					(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.WEAKNESS) ? _livEnt.getEffect(MobEffects.WEAKNESS).getAmplifier() : 0));
			entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_weakenss_chuzume_copy_tick",
					(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.WEAKNESS) ? _livEnt.getEffect(MobEffects.WEAKNESS).getDuration() : 0));
		}
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 1");
			}
		}
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()).getDuration() : 0, 4, true, false));
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()).getDuration() : 0, 10, true, false));
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()).getDuration() : 0, 255, true, false));
		MinecraftArmorWeaponMod.queueServerWork(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()).getDuration() : 0, () -> {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "attribute @s minecraft:generic.knockback_resistance base set 0");
				}
			}
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "kill @e[tag=minecraft_armor_weapon_guard_bind,sort=nearest,limit=1]");
				}
			}
			if (entity.getPersistentData().getBoolean("minecraft_armor_weapon:muteki_levitation_chuzume_copy") == true) {
				entity.getPersistentData().putBoolean("minecraft_armor_weapon:muteki_levitation_chuzume_copy", false);
				if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, (int) entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_levitation_chuzume_copy_tick"),
							(int) entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_levitation_chuzume_copy_level"), false, false));
			}
			if (entity.getPersistentData().getBoolean("minecraft_armor_weapon:muteki_weakenss_chuzume_copy") == true) {
				entity.getPersistentData().putBoolean("minecraft_armor_weapon:muteki_weakenss_chuzume_copy", false);
				if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, (int) entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_weakenss_chuzume_copy_tick"),
							(int) entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_weakenss_chuzume_copy_level"), false, false));
			}
			if (entity.getPersistentData().getBoolean("minecraft_armor_weapon:muteki_resistance_chuzume_copy") == true) {
				entity.getPersistentData().putBoolean("minecraft_armor_weapon:muteki_resistance_chuzume_copy", false);
				if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (int) entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_resistance_chuzume_copy_tick"),
							(int) entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_resistance_chuzume_copy_level"), false, false));
			}
		});
	}
}
