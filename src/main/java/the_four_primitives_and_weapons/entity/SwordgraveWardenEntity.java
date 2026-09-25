package the_four_primitives_and_weapons.entity;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/** A stationary arena is not required: all attacks announce and lock their aim before impact. */
public class SwordgraveWardenEntity extends IronGolem {
    private final ServerBossEvent bar = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS);
    private int attack, timer = 40, sequence, idleTicks;
    private boolean recovering;
    private Vec3 anchor, aim = Vec3.ZERO, direction = new Vec3(0, 0, 1);
    private static final DustParticleOptions WARNING = new DustParticleOptions(new Vector3f(1, 0.5f, 0.12f), 1.4f);
    private static final DustParticleOptions OPEN = new DustParticleOptions(new Vector3f(0.25f, 0.9f, 1), 1.3f);

    public SwordgraveWardenEntity(EntityType<? extends IronGolem> type, Level level) {
        super(type, level);
        xpReward = 80;
        setPersistenceRequired();
    }
    public static AttributeSupplier.Builder attributes() {
        return IronGolem.createAttributes().add(Attributes.MAX_HEALTH, 280)
                .add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.FOLLOW_RANGE, 28).add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }
    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        targetSelector.addGoal(0, new HurtByTargetGoal(this));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    @Override public boolean removeWhenFarAway(double distance) { return false; }
    @Override public void setTarget(LivingEntity target) {
        // Retaliate against attackers, without inheriting IronGolem's aggression
        // against unrelated monsters merely touching its body.
        if (target == null || target instanceof Player || target == getLastHurtByMob()) super.setTarget(target);
    }
    @Override public boolean canAttackType(EntityType<?> type) {
        return type == EntityType.CREEPER || super.canAttackType(type);
    }
    @Override public net.minecraft.world.InteractionResult mobInteract(Player player, net.minecraft.world.InteractionHand hand) {
        return net.minecraft.world.InteractionResult.PASS;
    }
    @Override public void startSeenByPlayer(ServerPlayer player) { super.startSeenByPlayer(player); bar.addPlayer(player); }
    @Override public void stopSeenByPlayer(ServerPlayer player) { super.stopSeenByPlayer(player); bar.removePlayer(player); }
    private boolean enraged() { return getHealth() <= getMaxHealth() * 0.5f; }
    private void title(String state) {
        bar.setName(Component.translatable("boss.the_four_primitives_and_weapons.swordgrave." + state, getDisplayName()));
        bar.setColor(recovering ? BossEvent.BossBarColor.BLUE : enraged() ? BossEvent.BossBarColor.RED : BossEvent.BossBarColor.YELLOW);
    }
    @Override public void aiStep() {
        super.aiStep();
        if (!(level() instanceof ServerLevel server) || !isAlive()) return;
        bar.setProgress(getHealth() / getMaxHealth());
        if (anchor == null) anchor = position();
        var target = getTarget();
        if (target == null || !target.isAlive() || target instanceof Player p && (p.isCreative() || p.isSpectator())
                || target.distanceToSqr(anchor) > 32 * 32 || distanceToSqr(anchor) > 32 * 32) {
            setTarget(null);
            attack = 0;
            recovering = false;
            timer = 40;
            if (++idleTicks % 20 == 0) {
                title("ready");
                if (distanceToSqr(anchor) > 4) getNavigation().moveTo(anchor.x, anchor.y, anchor.z, 1);
            }
            return;
        }
        idleTicks = 0;
        if (attack != 0) {
            getNavigation().stop();
            setYRot((float) Math.toDegrees(Math.atan2(-direction.x, direction.z)));
            setYBodyRot(getYRot());
            if (tickCount % 4 == 0) warning(server);
            if (--timer <= 0) impact(server);
            return;
        }
        if (recovering) {
            getNavigation().stop();
            if (tickCount % 8 == 0) server.sendParticles(OPEN, getX(), getY() + 2, getZ(), 5, 0.4, 0.5, 0.4, 0);
            if (--timer <= 0) { recovering = false; timer = 14; title("ready"); }
            return;
        }
        getLookControl().setLookAt(target, 20, 20);
        if (tickCount % 10 == 0) getNavigation().moveTo(target, 1);
        if (--timer > 0 || distanceToSqr(target) > 18 * 18) return;
        attack = distanceToSqr(target) > 8 * 8 ? SwordgraveRules.FALL : 1 + sequence++ % 3;
        direction = new Vec3(target.getX() - getX(), 0, target.getZ() - getZ()).normalize();
        if (direction.lengthSqr() < 0.01) direction = new Vec3(0, 0, 1);
        aim = attack == SwordgraveRules.FALL ? target.position() : position();
        timer = SwordgraveRules.windup(attack, enraged());
        getNavigation().stop();
        title(attack == SwordgraveRules.THRUST ? "thrust" : attack == SwordgraveRules.SWEEP ? "sweep" : "fall");
        playSound(SoundEvents.ANVIL_PLACE, 1, attack == SwordgraveRules.SWEEP ? 0.7f : 1.3f);
        warning(server);
    }
    private void warning(ServerLevel server) {
        if (attack == SwordgraveRules.THRUST) {
            Vec3 side = new Vec3(-direction.z, 0, direction.x);
            for (int i = 0; i <= 7; i++) for (int sign : new int[] {-1, 1}) {
                Vec3 p = aim.add(direction.scale(i)).add(side.scale(sign));
                server.sendParticles(WARNING, p.x, p.y + 0.15, p.z, 1, 0, 0, 0, 0);
            }
        } else {
            for (int i = 0; i < 20; i++) {
                double angle = i * Math.PI / 10;
                double radius = attack == SwordgraveRules.SWEEP ? 5.5 : 1.5;
                server.sendParticles(WARNING, aim.x + Math.cos(angle) * radius, aim.y + 0.15, aim.z + Math.sin(angle) * radius, 1, 0, 0, 0, 0);
                if (attack == SwordgraveRules.SWEEP) server.sendParticles(OPEN, aim.x + Math.cos(angle) * 1.8, aim.y + 0.15, aim.z + Math.sin(angle) * 1.8, 1, 0, 0, 0, 0);
            }
        }
    }
    private void impact(ServerLevel server) {
        int releasedAttack = attack;
        attack = 0;
        recovering = true;
        timer = SwordgraveRules.recovery(releasedAttack, enraged());
        title("open");
        server.broadcastEntityEvent(this, (byte) 4);
        playSound(SoundEvents.IRON_GOLEM_ATTACK, 1, 0.7f);
        if (releasedAttack == SwordgraveRules.FALL) {
            for (int i = 0; i < 8; i++) server.sendParticles(ParticleTypes.CRIT, aim.x, aim.y + i * 0.5, aim.z, 2, 0.1, 0.1, 0.1, 0);
        } else if (releasedAttack == SwordgraveRules.THRUST) {
            for (int i = 1; i <= 7; i++) {
                Vec3 point = aim.add(direction.scale(i));
                server.sendParticles(ParticleTypes.SWEEP_ATTACK, point.x, point.y + 1, point.z, 1, 0, 0, 0, 0);
            }
        } else {
            for (int i = 0; i < 12; i++) {
                double angle = i * Math.PI / 6;
                server.sendParticles(ParticleTypes.SWEEP_ATTACK, aim.x + Math.cos(angle) * 3.6, aim.y + 0.4, aim.z + Math.sin(angle) * 3.6, 1, 0, 0, 0, 0);
            }
        }
        var bounds = new net.minecraft.world.phys.AABB(aim, aim).inflate(8, 4, 8);
        for (LivingEntity victim : server.getEntitiesOfClass(LivingEntity.class, bounds,
                entity -> entity != this && entity.isAlive() && !isAlliedTo(entity)
                        && (entity instanceof Player player ? !player.isCreative() && !player.isSpectator() : entity == getTarget()))) {
            Vec3 offset = victim.position().subtract(aim);
            if (!SwordgraveRules.hits(releasedAttack, offset.dot(direction), offset.x * -direction.z + offset.z * direction.x, offset.y)
                    || !hasLineOfSight(victim)) continue;
            DamageSource source = new DamageSource(damageSources().mobAttack(this).typeHolder(), this, this,
                    releasedAttack == SwordgraveRules.FALL ? aim.add(0, 3, 0) : position());
            if (victim.hurt(source, releasedAttack == SwordgraveRules.THRUST ? 12 : 10)) {
                victim.knockback(0.65, -direction.x, -direction.z);
            }
        }
    }
    public void onParried() {
        attack = 0;
        recovering = true;
        timer = 70;
        getNavigation().stop();
        title("broken");
        playSound(SoundEvents.ANVIL_LAND, 1, 1.4f);
    }
    @Override public boolean hurt(DamageSource source, float amount) {
        if (!source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY) && source.getSourcePosition() != null) {
            Vec3 incoming = source.getSourcePosition().subtract(position()).multiply(1, 0, 1).normalize();
            Vec3 facing = Vec3.directionFromRotation(0, getYRot());
            amount *= SwordgraveRules.damageMultiplier(recovering, incoming.dot(facing));
            if (!recovering && incoming.dot(facing) > 0.35 && tickCount % 5 == 0) playSound(SoundEvents.SHIELD_BLOCK, 0.7f, 0.65f);
        }
        return super.hurt(source, amount);
    }
    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (anchor != null) { tag.putDouble("ArenaX", anchor.x); tag.putDouble("ArenaY", anchor.y); tag.putDouble("ArenaZ", anchor.z); }
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ArenaX")) anchor = new Vec3(tag.getDouble("ArenaX"), tag.getDouble("ArenaY"), tag.getDouble("ArenaZ"));
        attack = 0; recovering = false; timer = 40;
    }
    @Override public net.minecraft.network.protocol.Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket() {
        return net.minecraftforge.network.NetworkHooks.getEntitySpawningPacket(this);
    }
}
