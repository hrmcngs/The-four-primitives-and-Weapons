package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;
import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

public class GuardposiyonnoXiaoGuogaKaiShiShiYongsaretatokiProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.REPLICA_SWORD_OF_LIGHT.get()) {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level.getServer(), _ent),
							"summon minecraft:armor_stand ~ ~ ~ {NoGravity:1b,Invisible:1b,Tags:[\"minecraft_armor_weapon_guard_bind\"],Pose:{LeftArm:[0f,90f,-90f],RightArm:[0f,-90f,90f]},DisabledSlots:4144959,HandItems:[{id:\"minecraft:yellow_stained_glass_pane\",Count:1b},{id:\"minecraft:yellow_stained_glass_pane\",Count:1b}]}");
				}
			}
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.LOKI_THE_TRICKSTER.get()) {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level.getServer(), _ent),
							"summon minecraft:armor_stand ~ ~ ~ {NoGravity:1b,Invisible:1b,Tags:[\"minecraft_armor_weapon_guard_bind\"],Pose:{LeftArm:[0f,90f,-90f],RightArm:[0f,-90f,90f]},DisabledSlots:4144959,HandItems:[{id:\"minecraft:minecraft:light_gray_stained_glass_pane\",Count:1b},{id:\"minecraft:light_gray_stained_glass_pane\",Count:1b}]}");
				}
			}
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinecraftArmorWeaponModItems.PROTOTYPE_KATANA.get()) {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(
							new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4, _ent.getName().getString(), _ent.getDisplayName(),
									_ent.level.getServer(), _ent),
							"summon minecraft:armor_stand ~ ~ ~ {NoGravity:1b,Invisible:1b,Tags:[\"minecraft_armor_weapon_guard_bind\"],Pose:{LeftArm:[0f,90f,-90f],RightArm:[0f,-90f,90f]},DisabledSlots:4144959,HandItems:[{id:\"minecraft:minecraft:black_stained_glass_pane\",Count:1b},{id:\"minecraft:black_stained_glass_pane\",Count:1b}]}");
				}
			}
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
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_x_chuzume", (entity.getX()));
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_y_chuzume", (entity.getY()));
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_z_chuzume", (entity.getZ()));
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_resistance_level",
				(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.DAMAGE_RESISTANCE) ? _livEnt.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() : 0));
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_resistance_time",
				(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.DAMAGE_RESISTANCE) ? _livEnt.getEffect(MobEffects.DAMAGE_RESISTANCE).getDuration() : 0));
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_knockback_resistance", ((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE).getBaseValue());
		entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_speed", ((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).getBaseValue());
		((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).setBaseValue((-10));
		((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "playsound minecraft:block.enchantment_table.use player @s ~ ~ ~ 2 2");
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "playsound minecraft:item.armor.equip_gold player @s ~ ~ ~ 1 1");
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "playsound minecraft:item.armor.equip_gold player @s ~ ~ ~ 1 1");
			}
		}
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "particle minecraft:dust 1 1 0.5 0.5 ~ ~1 ~ 0.25 0.25 0.25 1 35");
			}
		}
		{
			Entity _ent = entity;
			_ent.teleportTo((entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_x_chuzume")), (entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_y_chuzume")),
					(entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_z_chuzume")));
			if (_ent instanceof ServerPlayer _serverPlayer)
				_serverPlayer.connection.teleport((entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_x_chuzume")), (entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_y_chuzume")),
						(entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_z_chuzume")), _ent.getYRot(), _ent.getXRot());
		}
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()).getDuration() : 0, 5, true, false));
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,
					entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()).getDuration() : 0, 10, true, false));
		if (entity instanceof LivingEntity _entity)
			_entity.removeEffect(MobEffects.LEVITATION);
		MinecraftArmorWeaponMod.queueServerWork(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()) ? _livEnt.getEffect(MinecraftArmorWeaponModMobEffects.GUARD.get()).getDuration() : 0, () -> {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "kill @e[tag=minecraft_armor_weapon_guard_bind,sort=nearest,limit=1]");
				}
			}
			((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED).setBaseValue((entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_speed")));
			((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE).setBaseValue((entity.getPersistentData().getDouble("minecraft_armor_weapon:muteki_knockback_resistance")));
			if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MobEffects.WEAKNESS) : false) {
				entity.getPersistentData().putBoolean("minecraft_armor_weapon:muteki_weakenss_chuzume_copy", true);
				entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_weakenss_chuzume_copy_level",
						(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.WEAKNESS) ? _livEnt.getEffect(MobEffects.WEAKNESS).getAmplifier() : 0));
				entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_weakenss_chuzume_copy_tick",
						(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.WEAKNESS) ? _livEnt.getEffect(MobEffects.WEAKNESS).getDuration() : 0));
			}
			if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MobEffects.DAMAGE_RESISTANCE) : false) {
				entity.getPersistentData().putBoolean("minecraft_armor_weapon:muteki_minecraft_armor_weapon:muteki_resistance_chuzume_copy_chuzume_copy", true);
				entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_minecraft_armor_weapon:muteki_resistance_chuzume_copy_chuzume_copy_level",
						(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.WEAKNESS) ? _livEnt.getEffect(MobEffects.WEAKNESS).getAmplifier() : 0));
				entity.getPersistentData().putDouble("minecraft_armor_weapon:muteki_minecraft_armor_weapon:muteki_resistance_chuzume_copy_chuzume_copy_tick",
						(entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.WEAKNESS) ? _livEnt.getEffect(MobEffects.WEAKNESS).getDuration() : 0));
			}
		});
	}
}
