package the_four_primitives_and_weapons.procedures;

import the_four_primitives_and_weapons.util.VersionHelper;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModMobEffects;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModEnchantments;

import the_four_primitives_and_weapons.entity.SkeltonMobEntity;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class LunaenteiteigaaitemuwoZhentutaShiProcedure {
    /** Lunaの通常突き・変更した通常技で共通の命中処理。 */
    public static void damageNormalTarget(Entity attacker, ItemStack weapon, Entity target, Vec3 origin) {
        if (target.level().isClientSide || !(target instanceof LivingEntity)
                || target instanceof SkeltonMobEntity) return;
        // Keep the beam's origin: generic damage without a position cannot be shield-blocked.
        var source = new the_four_primitives_and_weapons.damage.ShieldableSkillDamageSource(
                new net.minecraft.world.damagesource.DamageSource(
                    target.damageSources().generic().typeHolder(), attacker, attacker, origin), origin);
        boolean hit = attacker instanceof LivingEntity living
                ? the_four_primitives_and_weapons.skill.AttackHandContext.hurt(living, (LivingEntity) target, source, 1.0F)
                : target.hurt(source, 1.0F);
        if (hit && EnchantmentHelper.getItemEnchantmentLevel(TheFourPrimitivesAndWeaponsModEnchantments.KILL.get(), weapon) != 0) target.kill();
    }

	/** 召喚Luna用。プレイヤー通常技と同じ直線END_RODレーザーを発射する。 */
	public static boolean fireSummonedStraightLaser(ServerLevel level, Entity source, LivingEntity target,
			@javax.annotation.Nullable ServerPlayer viewer, float damage) {
		Vec3 start = source.position().add(0, source.getBbHeight() * 0.55, 0);
		Vec3 end = target.position().add(0, target.getBbHeight() * 0.5, 0);
		Vec3 clipped = the_four_primitives_and_weapons.event.GreatshieldHandler.clipBeam(level, start, end, damage);
		boolean reachesTarget = clipped.distanceToSqr(end) < 1.0e-12;
		end = clipped;
		Vec3 line = end.subtract(start);
		double length = line.length();
		if (length < 0.001) return false;
		Vec3 direction = line.scale(1.0 / length);
		double spacing = the_four_primitives_and_weapons.item.LunaFormula.get().value(
                the_four_primitives_and_weapons.util.LunaBehaviorScript.Setting.LASER_STEP);
		int steps = Math.max(1, (int)Math.ceil(length / spacing));
		for (int i = 0; i <= steps; i++) {
			Vec3 pos = start.add(direction.scale(Math.min(length, i * spacing)));
			if (viewer != null)
				level.sendParticles(viewer, ParticleTypes.END_ROD, true, pos.x, pos.y, pos.z,
						1, 0.03, 0.03, 0.03, 0.0);
			else
				level.sendParticles(ParticleTypes.END_ROD, pos.x, pos.y, pos.z,
						1, 0.03, 0.03, 0.03, 0.0);
		}
		level.playSound(null, source.getX(), source.getY(), source.getZ(),
				net.minecraft.sounds.SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 2.0F, 2.0F);
		level.playSound(null, source.getX(), source.getY(), source.getZ(),
				net.minecraft.sounds.SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 1.2F, 1.15F);
		return reachesTarget;
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof net.minecraft.world.entity.player.Player player) {
			// Legacy callers also use the ordinary dust attack; no uncharged player beam.
			the_four_primitives_and_weapons.skill.MotionExecutor.executeMotion("thrust", player, 0.0F);
			return;
		}
		fireBeam(world, x, y, z, entity);
	}

    public static void fireReadyBeam(net.minecraft.world.entity.player.Player player, float attackGauge) {
        if (player.level().isClientSide || !the_four_primitives_and_weapons.skill.LunaChargeRules.beamEnabled(attackGauge)) return;
        Vec3 origin = the_four_primitives_and_weapons.skill.AttackHandContext.origin(player, player.position());
        fireBeam(player.level(), origin.x, origin.y, origin.z, player);
    }

    private static void fireBeam(LevelAccessor world, double x, double y, double z, Entity entity) {
        double beamY = entity instanceof net.minecraft.world.entity.player.Player player
                ? y + the_four_primitives_and_weapons.util.ThrustHitbox.height(player) : y + 1;
		double r = 0;
		double alpha = 0;
		double beta = 0;
		double dis1 = 0;
		double yknockback = 0;
		double ZRadius2 = 0;
		double dis = 0;
		double zknockback = 0;
		double loop = 0;
		double XRadius2 = 0;
		double X = 0;
		double Y = 0;
		double Z = 0;
		double xknockback = 0;
		double Y_pos = 0;
		if (TheFourPrimitivesAndWeaponsModItems.LUNA.get() == (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem()
				|| TheFourPrimitivesAndWeaponsModItems.LUNA.get() == (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()) {
			if (!(entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(TheFourPrimitivesAndWeaponsModMobEffects.WAZA.get()) : false)
                    || (entity instanceof net.minecraft.world.entity.player.Player player
                        && the_four_primitives_and_weapons.skill.AttackHandContext.active(player))) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					// 再発射間隔は攻撃速度ゲージ側で管理するため、旧15tick固定ロックは廃止。
					_entity.addEffect(new MobEffectInstance(TheFourPrimitivesAndWeaponsModMobEffects.WAZA.get(), 1, 1, true, false));
				r = 1;
				alpha = entity.getYRot();
				beta = entity.getXRot();
                if (world instanceof ServerLevel level) {
                    if (entity instanceof ServerPlayer player) {
                        level.sendParticles(player, ParticleTypes.ENCHANTED_HIT, true, x, beamY, z, 20, 0.5, 0.5, 0.5, 0);
                        level.sendParticles(player, ParticleTypes.END_ROD, true, x, beamY, z, 5, 1, 1, 1, 0);
                    } else {
                        level.sendParticles(ParticleTypes.ENCHANTED_HIT, x, beamY, z, 20, 0.5, 0.5, 0.5, 0);
                        level.sendParticles(ParticleTypes.END_ROD, x, beamY, z, 5, 1, 1, 1, 0);
                    }
                }
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null,
								new BlockPos((int) (x - r * Math.cos(Math.toRadians(beta)) * Math.sin(Math.toRadians(alpha))), (int) ((beamY) - r * Math.sin(Math.toRadians(beta))), (int) (z + r * Math.cos(Math.toRadians(beta)) * Math.cos(Math.toRadians(alpha)))),
								ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.PLAYERS, 2, 2);
					} else {
						_level.playLocalSound((x - r * Math.cos(Math.toRadians(beta)) * Math.sin(Math.toRadians(alpha))), ((beamY) - r * Math.sin(Math.toRadians(beta))), (z + r * Math.cos(Math.toRadians(beta)) * Math.cos(Math.toRadians(alpha))),
								ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.PLAYERS, 2, 2, false);
					}
				}
				if (world instanceof ServerLevel serverLevel) {
					serverLevel.playSound(null, x, y, z,
							net.minecraft.sounds.SoundEvents.BEACON_DEACTIVATE,
							SoundSource.PLAYERS, 1.2F, 1.15F);
				}
				Vec3 beamOrigin = new Vec3(x, beamY, z);
				Vec3 previous = beamOrigin;
                Vec3 beamEnd = beamOrigin.add(entity.getLookAngle().scale(21));
                var beamShields = the_four_primitives_and_weapons.event.GreatshieldHandler.beamShields(entity.level(), new AABB(beamOrigin, beamEnd));
				java.util.Set<Integer> hitTargets = new java.util.HashSet<>();
                // Reuse one broad-phase query for all samples; do not sort entities per particle.
                var beamTargets = world.getEntitiesOfClass(LivingEntity.class, new AABB(beamOrigin, beamEnd).inflate(0.25),
                        target -> target != entity && !(target instanceof SkeltonMobEntity));
				for (int index0 = 0; index0 < 100; index0++) {
					{
						final Vec3 _center = new Vec3((x - r * Math.cos(Math.toRadians(beta)) * Math.sin(Math.toRadians(alpha))), ((beamY) - r * Math.sin(Math.toRadians(beta))),
								(z + r * Math.cos(Math.toRadians(beta)) * Math.cos(Math.toRadians(alpha))));
						Vec3 clipped = the_four_primitives_and_weapons.event.GreatshieldHandler.clipBeam(beamShields, previous, _center, 1.0F);
						if (clipped.distanceToSqr(_center) > 1.0e-12) break;
						previous = _center;
                        AABB sample = new AABB(_center, _center).inflate(0.25);
                        for (LivingEntity target : beamTargets) {
                            if (!hitTargets.contains(target.getId()) && target.getBoundingBox().intersects(sample)) {
                                hitTargets.add(target.getId());
                                damageNormalTarget(entity, entity instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY,
                                        target, beamOrigin);
                            }
                        }
					}
					if (index0 % 2 == 0 && world instanceof ServerLevel _level) {
						double px = x - r * Math.cos(Math.toRadians(beta)) * Math.sin(Math.toRadians(alpha));
						double py = (beamY) - r * Math.sin(Math.toRadians(beta));
						double pz = z + r * Math.cos(Math.toRadians(beta)) * Math.cos(Math.toRadians(alpha));
						if (entity instanceof ServerPlayer serverPlayer)
							_level.sendParticles(serverPlayer, ParticleTypes.END_ROD, true, px, py, pz, 1, 0.03, 0.03, 0.03, 0);
						else
							_level.sendParticles(ParticleTypes.END_ROD, px, py, pz, 1, 0.03, 0.03, 0.03, 0);
					}
					if (world
							.getBlockState(new BlockPos((int) (x - r * Math.cos(Math.toRadians(beta)) * Math.sin(Math.toRadians(alpha))), (int) ((y + 2) - r * Math.sin(Math.toRadians(beta))), (int) (z + r * Math.cos(Math.toRadians(beta)) * Math.cos(Math.toRadians(alpha)))))
							.canOcclude()) {
						break;
					}
					r = r + 0.2;
				}
			}
		}
	}
}
