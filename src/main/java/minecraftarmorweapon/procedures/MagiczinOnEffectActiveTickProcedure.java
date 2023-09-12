package minecraftarmorweapon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import minecraftarmorweapon.init.MinecraftArmorWeaponModMobEffects;
import minecraftarmorweapon.init.MinecraftArmorWeaponModEntities;

import minecraftarmorweapon.entity.MahouzinnEntityEntity;

public class MagiczinOnEffectActiveTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double r = 0;
		double a = 0;
		double b = 0;
		double dis1 = 0;
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null) {
				_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
						_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "function minecraft_armor_weapon:mahouzinn");
			}
		}
		for (int index0 = 0; index0 < 15; index0++) {
			r = 7;
			a = Mth.nextInt(RandomSource.create(), -180, 180);
			b = Mth.nextInt(RandomSource.create(), -90, 90);
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = new MahouzinnEntityEntity(MinecraftArmorWeaponModEntities.MAHOUZINN_ENTITY.get(), _level);
				entityToSpawn.moveTo(((entity.getX() - r * Math.cos(Math.toRadians(b))) * Math.sin(Math.toRadians(a))), (entity.getY() - r * Math.sin(Math.toRadians(b))),
						((entity.getZ() + r * Math.cos(Math.toRadians(b))) * Math.cos(Math.toRadians(a))), (float) a, (float) b);
				entityToSpawn.setYBodyRot((float) a);
				entityToSpawn.setYHeadRot((float) a);
				entityToSpawn.setDeltaMovement(0, 0, 0);
				if (entityToSpawn instanceof Mob _mobToSpawn)
					_mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
				world.addFreshEntity(entityToSpawn);
			}
			r = r - 1;
			for (int index1 = 0; index1 < 90; index1++) {
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.END_ROD, ((entity.getX() - r * Math.cos(Math.toRadians(b))) * Math.sin(Math.toRadians(a))), (entity.getY() - r * Math.sin(Math.toRadians(b))),
							((entity.getZ() + r * Math.cos(Math.toRadians(b))) * Math.cos(Math.toRadians(a))), 1, 0.1, 0.1, 0.1, 0);
				r = r - 0.1;
			}
		}
		if (entity instanceof LivingEntity _entity && !_entity.level.isClientSide())
			_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 9999, 100, true, false));
		if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(MinecraftArmorWeaponModMobEffects.MAGICZIN_KILL.get()) : false) {
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/kill @s");
				}
			}
			{
				Entity _ent = entity;
				if (!_ent.level.isClientSide() && _ent.getServer() != null) {
					_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level instanceof ServerLevel ? (ServerLevel) _ent.level : null, 4,
							_ent.getName().getString(), _ent.getDisplayName(), _ent.level.getServer(), _ent), "/deta merge entity @s (Health:0)");
				}
			}
		} else {
			if (entity instanceof LivingEntity _entity)
				_entity.hurt(new DamageSource("magic").bypassArmor(), 10);
		}
	}
}
