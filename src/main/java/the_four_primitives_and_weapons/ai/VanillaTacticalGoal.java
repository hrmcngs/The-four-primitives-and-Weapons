package the_four_primitives_and_weapons.ai;

import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import the_four_primitives_and_weapons.events.PostureCombatHandler;
import the_four_primitives_and_weapons.skill.WeaponTypeRegistry;

/** Owns only combat movement while an existing vanilla target is active. No new aggression targets. */
public final class VanillaTacticalGoal extends Goal {
    private final Mob mob;
    private final String species;
    private LivingEntity lockedTarget;
    private ItemStack lockedWeapon = ItemStack.EMPTY;
    private EnemyAttackRules.Attack attack;
    private Vec3 direction = Vec3.ZERO;
    private Vec3 shotAim = Vec3.ZERO;
    private long strikeAt;
    private long recoverUntil;
    private long lastAttack = -1;
    private int combo;
    private boolean shooting;

    public VanillaTacticalGoal(Mob mob, String species) {
        this.mob = mob;
        this.species = species;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive() || !mob.canAttack(target)) return false;
        if (mob instanceof AbstractSkeleton && mob.level().isDay()
                && mob.level().canSeeSky(mob.blockPosition())
                && mob.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD).isEmpty()) return false;
        var item = mob.getMainHandItem().getItem();
        // Existing crossbow, trident throwing and spellcasting routines retain their own weapon behavior.
        return !(item instanceof CrossbowItem) && !(mob instanceof Drowned && item instanceof TridentItem)
                && (!(item instanceof BowItem) || mob instanceof AbstractSkeleton);
    }
    @Override
    public boolean canContinueToUse() { return canUse(); }
    @Override
    public boolean requiresUpdateEveryTick() { return true; }

    @Override
    public void tick() {
        long now = mob.level().getGameTime();
        LivingEntity target = mob.getTarget();
        if (target == null) return;
        if (PostureCombatHandler.isStaggered(mob)) {
            cancelAttack();
            mob.getNavigation().stop();
            return;
        }
        if (now < recoverUntil) {
            mob.getNavigation().stop();
            return;
        }
        if (lockedTarget != null) {
            mob.getNavigation().stop();
            if (target != lockedTarget || !lockedTarget.isAlive() || !mob.hasLineOfSight(lockedTarget)
                    || !ItemStack.matches(lockedWeapon, mob.getMainHandItem())) {
                cancelAttack();
                recoverUntil = now + 8;
                return;
            }
            Vec3 aim = mob.position().add(direction.scale(4));
            mob.getLookControl().setLookAt(aim.x, mob.getEyeY(), aim.z, 30, 30);
            if (now < strikeAt) {
                if (now % 4 == 0) warn();
                return;
            }
            if (shooting) fireArrow(); else strike();
            recoverUntil = now + (shooting ? 28 : attack.recovery());
            lastAttack = now;
            combo++;
            cancelAttack();
            return;
        }
        if (lastAttack < 0 || now - lastAttack > 80) combo = 0;
        shooting = mob instanceof AbstractSkeleton && mob.getMainHandItem().getItem() instanceof BowItem;
        var type = WeaponTypeRegistry.getTypeForItem(mob.getMainHandItem());
        String weapon = type != null ? type.getId() : mob.getMainHandItem().getItem() instanceof AxeItem ? "nata" : null;
        attack = VanillaCombatRules.attack(species, weapon, combo);
        double distance = mob.distanceTo(target);
        double reach = attack.reach() + target.getBbWidth() / 2.0;
        if (!mob.hasLineOfSight(target) || (shooting
                ? !VanillaCombatRules.withinShotRange(mob.distanceToSqr(target)) : distance > reach)) {
            mob.getNavigation().moveTo(target, 1.0);
            mob.getLookControl().setLookAt(target, 30, 30);
            return;
        }
        // Bow users back away between shots when enemies close in; they cannot backpedal during windup.
        if (shooting && distance < 4 && mob.tickCount % 20 < 8) {
            Vec3 away = mob.position().subtract(target.position()).multiply(1, 0, 1).normalize();
            Vec3 destination = mob.position().add(away.scale(3));
            mob.getNavigation().moveTo(destination.x, destination.y, destination.z, 1.0);
            mob.getLookControl().setLookAt(target, 30, 30);
            return;
        }
        lockedTarget = target;
        lockedWeapon = mob.getMainHandItem().copy();
        direction = target.position().subtract(mob.position()).multiply(1, 0, 1).normalize();
        if (direction.lengthSqr() < 1.0e-6) direction = new Vec3(0, 0, 1);
        shotAim = target.position().add(0, target.getBbHeight() / 3, 0);
        strikeAt = now + (shooting ? 18 : attack.windup());
        mob.getNavigation().stop();
        mob.setAggressive(true);
        if (shooting) mob.startUsingItem(InteractionHand.MAIN_HAND);
        else mob.swing(InteractionHand.MAIN_HAND, true);
        warn();
        mob.level().playSound(null, mob.blockPosition(), SoundEvents.ARMOR_EQUIP_IRON,
                SoundSource.HOSTILE, 0.4f, shooting ? 1.5f : 0.8f);
    }

    private void strike() {
        Vec3 origin = mob.position();
        if (VanillaCombatRules.pounces(species))
            mob.setDeltaMovement(mob.getDeltaMovement().add(direction.scale(0.5)).add(0, 0.2, 0));
        mob.swing(InteractionHand.MAIN_HAND, true);
        for (LivingEntity victim : mob.level().getEntitiesOfClass(LivingEntity.class,
                mob.getBoundingBox().inflate(attack.reach() + 1, 1, attack.reach() + 1))) {
            if (victim != lockedTarget && !(victim instanceof net.minecraft.world.entity.player.Player
                    && lockedTarget instanceof net.minecraft.world.entity.player.Player)) continue;
            if (victim == mob || !victim.isAlive() || !mob.canAttack(victim) || mob.isAlliedTo(victim)
                    || !mob.hasLineOfSight(victim)) continue;
            if (victim.getBoundingBox().maxY < origin.y + 0.1
                    || victim.getBoundingBox().minY > origin.y + mob.getBbHeight()) continue;
            Vec3 offset = victim.position().subtract(origin);
            double forward = offset.x * direction.x + offset.z * direction.z;
            double side = -offset.x * direction.z + offset.z * direction.x;
            if (EnemyAttackRules.hits(attack, forward, side, victim.getBbWidth() / 2.0))
                mob.doHurtTarget(victim); // Keep cave spider poison, wither, hunger, enchantments and difficulty scaling.
        }
        mob.level().playSound(null, mob.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.HOSTILE, 0.8f, 1);
    }

    private void fireArrow() {
        var arrow = ProjectileUtil.getMobArrow(mob, mob.getProjectile(lockedWeapon), 1);
        if (mob instanceof Stray && arrow instanceof Arrow normalArrow)
            normalArrow.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600));
        Vec3 aim = shotAim.subtract(arrow.position());
        double horizontal = Math.hypot(aim.x, aim.z);
        arrow.shoot(aim.x, aim.y + horizontal * 0.2, aim.z, 1.6f,
                14 - mob.level().getDifficulty().getId() * 4);
        mob.level().addFreshEntity(arrow);
        mob.level().playSound(null, mob.blockPosition(), SoundEvents.SKELETON_SHOOT, SoundSource.HOSTILE, 1, 1);
    }

    private void warn() {
        if (!(mob.level() instanceof ServerLevel level)) return;
        if (shooting) {
            level.sendParticles(ParticleTypes.CRIT, mob.getX() + direction.x, mob.getEyeY(),
                    mob.getZ() + direction.z, 4, 0.1, 0.1, 0.1, 0);
            return;
        }
        Vec3 origin = mob.position();
        for (int i = 0; i <= 8; i++) {
            double forward;
            double side;
            if (attack.shape() == EnemyAttackRules.Shape.SWEEP) {
                double angle = Math.toRadians(-attack.halfAngle() + attack.halfAngle() * i / 4);
                forward = Math.cos(angle) * attack.reach();
                side = Math.sin(angle) * attack.reach();
            } else {
                forward = attack.reach() * i / 8;
                side = attack.halfWidth() * (i % 2 == 0 ? 1 : -1);
            }
            level.sendParticles(ParticleTypes.ENCHANT, origin.x + forward * direction.x - side * direction.z,
                    origin.y + 0.2, origin.z + forward * direction.z + side * direction.x, 1, 0, 0, 0, 0);
        }
    }

    private void cancelAttack() {
        if (shooting && mob.isUsingItem()) mob.stopUsingItem();
        mob.setAggressive(false);
        lockedTarget = null;
        lockedWeapon = ItemStack.EMPTY;
    }
    @Override
    public void stop() {
        cancelAttack();
        mob.getNavigation().stop();
    }
}
