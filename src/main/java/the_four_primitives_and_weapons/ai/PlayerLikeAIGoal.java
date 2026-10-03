package the_four_primitives_and_weapons.ai;

import the_four_primitives_and_weapons.util.VersionHelper;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModMobEffects;
import the_four_primitives_and_weapons.util.DamageCalculator;

import java.util.List;
import java.util.EnumSet;
import java.util.Random;

/**
 * プレイヤーのような動作をするAI Goal
 *
 * このGoalは、ALifeAIBridgeからのアクションを受け取り、
 * プレイヤーと同じような動作（回避、チャージ攻撃など）を実行します
 */
public class PlayerLikeAIGoal extends Goal {

    private final Mob entity;
    private final ALifeAIBridge aiBridge;
    private final int tier;
    private final Random random = new Random();

    // 状態管理
    private ALifeAIBridge.AIAction currentAction = null;
    private int actionTicks = 0;
    private boolean isExecutingAction = false;

    // 落下ダメージ無効時間
    private int fallDamageImmunityTicks = 0;

    // コンボカウンター
    private int comboCounter = 0;
    private long lastAttackTime = -1;
    private EnemyAttackRules.Attack committedAttack;
    private Vec3 committedDirection = Vec3.ZERO;
    private ALifeAIBridge.AIAction pendingAttack;
    private LivingEntity pendingTarget;
    private ItemStack pendingWeapon = ItemStack.EMPTY;
    private long strikeAt;
    private long recoverUntil;

    public PlayerLikeAIGoal(Mob entity, int tier) {
        this.entity = entity;
        this.tier = tier;
        this.aiBridge = new ALifeAIBridge(entity, tier);

        // このGoalは他のGoalと並行して実行可能
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        // 常に実行可能
        return entity.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        // エンティティの生存確認
        if (!entity.isAlive() || entity.isRemoved()) {
            return;
        }

        if (the_four_primitives_and_weapons.events.PostureCombatHandler.isStaggered(entity)) {
            entity.getNavigation().stop();
            pendingAttack = null;
            pendingTarget = null;
            pendingWeapon = ItemStack.EMPTY;
            return;
        }

        long now = entity.level().getGameTime();
        if (fallDamageImmunityTicks > 0) fallDamageImmunityTicks--;
        if (pendingAttack != null) {
            entity.getNavigation().stop();
            if (pendingTarget == null || !pendingTarget.isAlive() || entity.getTarget() != pendingTarget
                    || !ItemStack.matches(pendingWeapon, entity.getMainHandItem()) || !entity.hasLineOfSight(pendingTarget)) {
                pendingAttack = null;
                pendingTarget = null;
                pendingWeapon = ItemStack.EMPTY;
                recoverUntil = now + 6;
                return;
            }
            Vec3 aim = entity.position().add(committedDirection.scale(4));
            entity.getLookControl().setLookAt(aim.x, entity.getEyeY(), aim.z, 30, 30);
            if (now < strikeAt) {
                if (now % 4 == 0) showAttackShape(true);
                return;
            }
            ALifeAIBridge.AIAction attack = pendingAttack;
            pendingAttack = null;
            pendingTarget = null;
            pendingWeapon = ItemStack.EMPTY;
            recoverUntil = now + committedAttack.recovery();
            executeAction(attack);
            return;
        }
        if (now < recoverUntil) {
            entity.getNavigation().stop();
            return;
        }

        try {
            // AIを更新してアクションを取得
            currentAction = aiBridge.update();

            // アクションを実行
            if (currentAction != null) {
                if (isOffensiveAction(currentAction.action) && entity.getTarget() != null) {
                    pendingAttack = currentAction;
                    pendingTarget = entity.getTarget();
                    pendingWeapon = entity.getMainHandItem().copy();
                    if (lastAttackTime < 0 || now - lastAttackTime > 80) comboCounter = 0;
                    var weaponType = the_four_primitives_and_weapons.skill.WeaponTypeRegistry.getTypeForItem(pendingWeapon);
                    committedAttack = EnemyAttackRules.choose(weaponType == null ? null : weaponType.getId(),
                            comboCounter, "charge_attack".equals(currentAction.action), "dash_attack".equals(currentAction.action));
                    if (entity.distanceTo(pendingTarget) > committedAttack.reach() + pendingTarget.getBbWidth() / 2.0 + 0.25) {
                        entity.getNavigation().moveTo(pendingTarget, 1.0);
                        pendingAttack = null;
                        pendingTarget = null;
                        pendingWeapon = ItemStack.EMPTY;
                        return;
                    }
                    committedDirection = pendingTarget.position().subtract(entity.position()).multiply(1, 0, 1).normalize();
                    if (committedDirection.lengthSqr() < 1.0e-6)
                        committedDirection = new Vec3(0, 0, 1);
                    strikeAt = now + committedAttack.windup();
                    entity.getNavigation().stop();
                    if (entity.level() instanceof ServerLevel level) {
                        boolean heavy = !"attack".equals(currentAction.action);
                        level.sendParticles(heavy ? ParticleTypes.CRIT : ParticleTypes.ENCHANT,
                                entity.getX(), entity.getEyeY(), entity.getZ(), 8, 0.3, 0.2, 0.3, 0.02);
                        level.playSound(null, entity.blockPosition(), SoundEvents.ARMOR_EQUIP_IRON,
                                SoundSource.HOSTILE, 0.6f, heavy ? 0.7f : 1.3f);
                    }
                } else executeAction(currentAction);
            }

            actionTicks++;
        } catch (Throwable e) {
            System.err.println("[PlayerLikeAI] Critical error in tick for " + entity.getName().getString() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isOffensiveAction(String action) {
        return "attack".equals(action) || "charge_attack".equals(action)
                || "dash_attack".equals(action);
    }

    @Override
    public void stop() {
        pendingAttack = null;
        pendingTarget = null;
        pendingWeapon = ItemStack.EMPTY;
        recoverUntil = 0;
        entity.getNavigation().stop();
    }

    /**
     * アクションを実行
     */
    private void executeAction(ALifeAIBridge.AIAction action) {
        try {
            switch (action.action) {
                case "dodge":
                    executeDodge(action);
                    break;
                case "dash_attack":
                    executeWeaponAttack(action);
                    break;
                case "charge_attack":
                    executeWeaponAttack(action);
                    break;
                case "use_weapon_skill":
                    executeWeaponSkill(action);
                    break;
                case "attack":
                    executeWeaponAttack(action);
                    break;
                case "move_to_target":
                    moveToTarget(action);
                    break;
                case "move_away":
                    moveAway(action);
                    break;
                case "strafe":
                    strafe(action);
                    break;
                case "heal":
                    executeHeal(action);
                    break;
                case "retreat":
                    executeRetreat(action);
                    break;
                case "guard":
                    executeGuard(action);
                    break;
                case "idle":
                default:
                    // 何もしない
                    break;
            }
        } catch (Throwable e) {
            System.err.println("[PlayerLikeAI] Error executing action " + action.action + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Resolve the announced attack without tracking a dodging target at impact. */
    private void executeWeaponAttack(ALifeAIBridge.AIAction action) {
        if (committedAttack == null || entity.level().isClientSide()
                || entity.getTarget() == null || !entity.getTarget().isAlive()) return;
        Vec3 origin = entity.position();
        double reach = committedAttack.reach();
        entity.swing(InteractionHand.MAIN_HAND, true);
        if ("dash_attack".equals(action.action))
            entity.setDeltaMovement(entity.getDeltaMovement().add(committedDirection.scale(0.6)));
        showAttackShape(false);
        ItemStack weapon = entity.getMainHandItem();
        for (LivingEntity victim : entity.level().getEntitiesOfClass(LivingEntity.class,
                entity.getBoundingBox().inflate(reach + 1, 1, reach + 1))) {
            if (victim == entity || !entity.canAttack(victim) || entity.isAlliedTo(victim)
                    || !entity.hasLineOfSight(victim) || !victim.isAlive()) continue;
            // The full target bounds may overlap the attack height; terrain still blocks sight.
            if (victim.getBoundingBox().maxY < origin.y + 0.2
                    || victim.getBoundingBox().minY > origin.y + entity.getBbHeight()) continue;
            Vec3 offset = victim.position().subtract(origin);
            double forward = offset.x * committedDirection.x + offset.z * committedDirection.z;
            double side = offset.x * -committedDirection.z + offset.z * committedDirection.x;
            if (!EnemyAttackRules.hits(committedAttack, forward, side, victim.getBbWidth() / 2.0)) continue;
            float dealt = DamageCalculator.dealDamage(entity, victim, committedAttack.damage(), weapon);
            // A successful parry or dodge must also avoid the attack's extra knockback.
            if (dealt > 0) {
                DamageCalculator.addKnockbackVelocity(victim, committedDirection.scale(
                        committedAttack.shape() == EnemyAttackRules.Shape.SMASH ? 0.45 : 0.2).add(0, 0.08, 0));
            }
        }
        entity.level().playSound(null, entity.blockPosition(),
                committedAttack.shape() == EnemyAttackRules.Shape.THRUST ? SoundEvents.PLAYER_ATTACK_CRIT
                        : SoundEvents.PLAYER_ATTACK_SWEEP,
                SoundSource.HOSTILE, 1, committedAttack.shape() == EnemyAttackRules.Shape.SMASH ? 0.7f : 1.1f);
        comboCounter++;
        lastAttackTime = entity.level().getGameTime();
    }

    private void showAttackShape(boolean warning) {
        if (!(entity.level() instanceof ServerLevel level) || committedAttack == null) return;
        Vec3 origin = entity.position();
        if (committedAttack.shape() == EnemyAttackRules.Shape.SWEEP) {
            for (int i = 0; i <= 12; i++) {
                double angle = Math.toRadians(-committedAttack.halfAngle() + 2 * committedAttack.halfAngle() * i / 12);
                double forward = Math.cos(angle) * committedAttack.reach();
                double side = Math.sin(angle) * committedAttack.reach();
                Vec3 point = origin.add(committedDirection.scale(forward))
                        .add(-committedDirection.z * side, warning ? 0.2 : 1, committedDirection.x * side);
                level.sendParticles(warning ? ParticleTypes.ENCHANT : ParticleTypes.SWEEP_ATTACK,
                        point.x, point.y, point.z, 1, 0, 0, 0, 0);
            }
        } else {
            for (int i = 1; i <= 10; i++) {
                Vec3 center = origin.add(committedDirection.scale(committedAttack.reach() * i / 10));
                for (int side : new int[]{-1, 1}) {
                    double width = committedAttack.halfWidth() * side;
                    level.sendParticles(warning ? ParticleTypes.ENCHANT : ParticleTypes.CRIT,
                            center.x - committedDirection.z * width, center.y + (warning ? 0.2 : 1),
                            center.z + committedDirection.x * width, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    private void executeDodge(ALifeAIBridge.AIAction action) {
        if (action.direction == null) {
            return;
        }

        // 盲目効果時：回避が70%の確率で失敗
        if (entity.hasEffect(MobEffects.BLINDNESS)) {
            if (random.nextDouble() < 0.7) {                return;
            }
        }

        // 混乱効果時：回避方向がランダムになる
        Vec3 dodgeDirection = action.direction;
        if (entity.hasEffect(MobEffects.CONFUSION)) {
            double angle = random.nextDouble() * Math.PI * 2;
            dodgeDirection = new Vec3(Math.cos(angle), 0, Math.sin(angle)).normalize();        }

        Vec3 dodgeVec = dodgeDirection.scale(action.speed);

        // 回避移動（上方向の加速も含む）
        entity.setDeltaMovement(
            dodgeVec.x,
            entity.getDeltaMovement().y + action.verticalBoost,
            dodgeVec.z
        );

        // 落下ダメージ無効（1.5秒 = 30 ticks）
        fallDamageImmunityTicks = 30;

        // エフェクト
        Level world = VersionHelper.getLevel(entity);
        if (!world.isClientSide) {
            ServerLevel serverWorld = (ServerLevel) world;
            Vec3 pos = entity.position();

            // 煙のエフェクト
            serverWorld.sendParticles(
                ParticleTypes.CLOUD,
                pos.x, pos.y, pos.z,
                20, 0.3, 0.5, 0.3, 0.05
            );
        }

        // サウンド
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
            SoundEvents.ENDER_PEARL_THROW, SoundSource.HOSTILE, 0.8f, 1.5f);
    }

    /**
     * チャージ攻撃を実行（ChargedAttackHandlerと同じロジック）
     * 武器タイプにより異なる攻撃を実行：
     * - 刀（Katana）: 周囲回転斬り
     * - その他: 貫通突き（デフォルト）
     */
    private void executeWeaponSkill(ALifeAIBridge.AIAction action) {
        // 盲目効果時：スキルが60%の確率で失敗
        if (entity.hasEffect(MobEffects.BLINDNESS)) {
            if (random.nextDouble() < 0.6) {                return;
            }
        }

        // 混乱効果時：スキルが40%の確率で失敗
        if (entity.hasEffect(MobEffects.CONFUSION)) {
            if (random.nextDouble() < 0.4) {                return;
            }
        }

        // スキルタイプに応じた処理
        if ("guard".equals(action.skillType)) {
            // ガードスキル
            int duration = (int)(action.duration * 20); // 秒をティックに変換

            // GUARDエフェクトを付与
            if (TheFourPrimitivesAndWeaponsModMobEffects.GUARD.get() != null) {
                entity.addEffect(new MobEffectInstance(
                    TheFourPrimitivesAndWeaponsModMobEffects.GUARD.get(),
                    duration,
                    0
                ));
            }

            // LONG_RANGE_WEAPON_CUTエフェクトを付与
            if (TheFourPrimitivesAndWeaponsModMobEffects.LONG_RANGE_WEAPON_CUT.get() != null) {
                entity.addEffect(new MobEffectInstance(
                    TheFourPrimitivesAndWeaponsModMobEffects.LONG_RANGE_WEAPON_CUT.get(),
                    2,
                    1
                ));
            }

            // エフェクト
            Level world = VersionHelper.getLevel(entity);
            if (!world.isClientSide) {
                ServerLevel serverWorld = (ServerLevel) world;
                serverWorld.sendParticles(
                    ParticleTypes.ENCHANT,
                    entity.getX(), entity.getY() + 1, entity.getZ(),
                    10, 0.5, 0.5, 0.5, 0.1
                );
            }

            // サウンド
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.HOSTILE, 1.0f, 1.0f);
        }
    }

    /** Approach the current target. */
    private void moveToTarget(ALifeAIBridge.AIAction action) {
        if (action.target == null || entity.getNavigation() == null) {
            return;
        }

        try {
            // スプリント状態を設定
            entity.setSprinting(action.isSprinting);

            // 移動速度係数を計算
            // Navigation APIは基本速度に対する係数を期待する
            // 通常移動: 1.0倍、スプリント: 1.3倍
            float speedModifier = action.isSprinting ? 1.3f : 1.0f;

            // Navigation APIを安全に呼び出す
            if (entity.getNavigation() != null) {
                entity.getNavigation().moveTo(action.target.x, action.target.y, action.target.z, speedModifier);
            }

            // LookControlを安全に呼び出す
            if (entity.getLookControl() != null) {
                entity.getLookControl().setLookAt(action.target.x, action.target.y, action.target.z);
            }
        } catch (Exception e) {
            System.err.println("[PlayerLikeAI] Error in moveToTarget: " + e.getMessage());
        }
    }

    /**
     * ターゲットから離れる
     */
    private void moveAway(ALifeAIBridge.AIAction action) {
        if (action.target != null) {
            Vec3 entityPos = entity.position();
            Vec3 targetPos = action.target;
            Vec3 awayVec = entityPos.subtract(targetPos).normalize().scale(5.0);
            Vec3 destination = entityPos.add(awayVec);

            entity.getNavigation().moveTo(destination.x, destination.y, destination.z, action.speed);
        }
    }

    /**
     * ターゲットの周囲を移動
     */
    private void strafe(ALifeAIBridge.AIAction action) {
        if (action.target != null) {
            // 円周上を移動する簡易実装
            Vec3 entityPos = entity.position();
            Vec3 targetPos = action.target;
            Vec3 toTarget = targetPos.subtract(entityPos);

            // 接線方向に移動（反時計回り）
            Vec3 tangent = new Vec3(-toTarget.z, 0, toTarget.x).normalize();
            Vec3 destination = entityPos.add(tangent.scale(2.0));

            entity.getNavigation().moveTo(destination.x, destination.y, destination.z, action.speed);
            entity.getLookControl().setLookAt(targetPos.x, targetPos.y, targetPos.z);
        }
    }

    /**
     * 回復を実行
     */
    private void executeHeal(ALifeAIBridge.AIAction action) {
        float healAmount = action.damageMultiplier; // heal_amountを再利用
        entity.heal(healAmount);

        // 回復エフェクト
        Level world = VersionHelper.getLevel(entity);
        if (!world.isClientSide) {
            ServerLevel serverWorld = (ServerLevel) world;
            serverWorld.sendParticles(
                ParticleTypes.HEART,
                entity.getX(), entity.getY() + 1.5, entity.getZ(),
                5, 0.3, 0.3, 0.3, 0.1
            );
        }

        // 回復サウンド
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
            SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.HOSTILE, 0.5f, 1.5f);
    }

    /**
     * 撤退を実行
     */
    private void executeRetreat(ALifeAIBridge.AIAction action) {
        if (action.direction == null) {
            return;
        }

        // スプリント状態を設定（撤退時は常にスプリント）
        entity.setSprinting(true);

        // 撤退方向に移動（通常より速く、スプリント時はさらに速く）
        float speed = action.speed;
        if (action.isSprinting) {
            speed *= 1.3f;
        }
        Vec3 retreatVec = action.direction.scale(speed);
        entity.setDeltaMovement(retreatVec.x, entity.getDeltaMovement().y, retreatVec.z);

        // 煙エフェクト
        Level world = VersionHelper.getLevel(entity);
        if (!world.isClientSide && random.nextInt(5) == 0) {
            ServerLevel serverWorld = (ServerLevel) world;
            serverWorld.sendParticles(
                ParticleTypes.POOF,
                entity.getX(), entity.getY(), entity.getZ(),
                1, 0.1, 0.1, 0.1, 0.02
            );
        }
    }

    /**
     * 防御姿勢を実行
     */
    private void executeGuard(ALifeAIBridge.AIAction action) {
        // ガード効果を付与
        if (TheFourPrimitivesAndWeaponsModMobEffects.GUARD.get() != null) {
            entity.addEffect(new MobEffectInstance(
                TheFourPrimitivesAndWeaponsModMobEffects.GUARD.get(),
                60, // 3秒
                0
            ));
        }

        // ガードエフェクト
        Level world = VersionHelper.getLevel(entity);
        if (!world.isClientSide && actionTicks % 10 == 0) {
            ServerLevel serverWorld = (ServerLevel) world;
            serverWorld.sendParticles(
                ParticleTypes.ENCHANTED_HIT,
                entity.getX(), entity.getY() + 1, entity.getZ(),
                3, 0.3, 0.3, 0.3, 0
            );
        }
    }

    /**
     * 落下ダメージを無効化するかチェック
     */
    public boolean isFallDamageImmune() {
        return fallDamageImmunityTicks > 0;
    }

    /**
     * 攻撃経路上の竹を破壊する（DodgeAndBattouHandler.breakBambooInPathと同じ）
     */
    private void breakBambooInPath(Vec3 startPos, Vec3 direction, double range) {
        Level world = VersionHelper.getLevel(entity);
        if (world.isClientSide) return;

        // 攻撃経路に沿って竹をチェック
        for (double d = 0; d <= range; d += 0.5) {
            Vec3 checkPos = startPos.add(direction.scale(d));

            // 上下左右も含めて範囲をチェック
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 2; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos pos = new BlockPos(
                            (int)(checkPos.x + dx),
                            (int)(checkPos.y + dy),
                            (int)(checkPos.z + dz)
                        );

                        BlockState state = world.getBlockState(pos);

                        // 竹または竹の苗をチェック
                        if (state.getBlock() == Blocks.BAMBOO ||
                            state.getBlock() == Blocks.BAMBOO_SAPLING) {
                            // 竹を破壊（ドロップあり）
                            world.destroyBlock(pos, true);
                        }
                    }
                }
            }
        }
    }
}
