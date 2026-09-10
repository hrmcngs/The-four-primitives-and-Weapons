package the_four_primitives_and_weapons.entity;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.joml.Vector3f;

import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModEntities;
import the_four_primitives_and_weapons.item.GateFormula;
import the_four_primitives_and_weapons.item.GateItem;

/**
 * Gateの飛び道具 - エンチャントした金の剣の見た目+金色パーティクル。
 * エンティティやブロックに当たると爆発する。
 */
public class GateProjectileEntity extends ThrowableProjectile implements ItemSupplier {

    private static final DustParticleOptions GOLD_PARTICLE =
            new DustParticleOptions(new Vector3f(1.0f, 0.816f, 0.0f), 1.0f);
    private static final EntityDataAccessor<Integer> WARMUP = SynchedEntityData.defineId(
            GateProjectileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATAPACK_GATE = SynchedEntityData.defineId(
            GateProjectileEntity.class, EntityDataSerializers.BOOLEAN);
    private int summonAge;
    private Vec3 aimPoint = Vec3.ZERO;
    private Vec3 launchVelocity = Vec3.ZERO;
    private int life = 0;

    public GateProjectileEntity(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    public GateProjectileEntity(Level level, Player player) {
        super(TheFourPrimitivesAndWeaponsModEntities.GATE_PROJECTILE.get(), player, level);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(WARMUP, 0);
        entityData.define(DATAPACK_GATE, false);
    }

    public void prepareGateLaunch(Vec3 velocity, int delay) {
        entityData.set(DATAPACK_GATE, true);
        updateAimPoint();
        prepareLaunch(aimPoint.subtract(position()).normalize().scale(velocity.length()), delay);
    }

    private void updateAimPoint() {
        if (getOwner() instanceof Player player
                && (GateItem.isGateSword(player.getMainHandItem())
                    || GateItem.isGateSword(player.getOffhandItem()))) {
            // poof.mcfunction:18 のローカル座標 ^ ^1.5 ^50。
            aimPoint = player.position().add(player.getLookAngle().scale(50))
                    .add(player.getUpVector(1.0f).scale(1.5));
        }
    }

    public void prepareLaunch(Vec3 velocity, int delay) {
        launchVelocity = velocity;
        entityData.set(WARMUP, Math.max(0, delay));
        setDeltaMovement(delay > 0 ? Vec3.ZERO : velocity);
        setYRot((float) (Mth.atan2(velocity.x, velocity.z) * Mth.RAD_TO_DEG));
        setXRot((float) (Mth.atan2(velocity.y, velocity.horizontalDistance()) * Mth.RAD_TO_DEG));
        yRotO = getYRot();
        xRotO = getXRot();
        hasImpulse = true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("GateWarmup", entityData.get(WARMUP));
        tag.putInt("GateLife", life);
        tag.putBoolean("DatapackGate", entityData.get(DATAPACK_GATE));
        tag.putInt("GateSummonAge", summonAge);
        tag.putDouble("GateAimX", aimPoint.x);
        tag.putDouble("GateAimY", aimPoint.y);
        tag.putDouble("GateAimZ", aimPoint.z);
        tag.putDouble("GateLaunchX", launchVelocity.x);
        tag.putDouble("GateLaunchY", launchVelocity.y);
        tag.putDouble("GateLaunchZ", launchVelocity.z);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(WARMUP, tag.getInt("GateWarmup"));
        life = tag.getInt("GateLife");
        entityData.set(DATAPACK_GATE, tag.getBoolean("DatapackGate"));
        summonAge = tag.getInt("GateSummonAge");
        aimPoint = new Vec3(tag.getDouble("GateAimX"), tag.getDouble("GateAimY"), tag.getDouble("GateAimZ"));
        launchVelocity = new Vec3(tag.getDouble("GateLaunchX"), tag.getDouble("GateLaunchY"),
                tag.getDouble("GateLaunchZ"));
    }

    @Override
    public ItemStack getItem() {
        ItemStack stack = new ItemStack(Items.GOLDEN_SWORD);
        stack.enchant(Enchantments.KNOCKBACK, 5);
        stack.getOrCreateTag().putInt("CustomModelData", 827373);
        return stack;
    }

    // 爆風で吹き飛ばされない（向きが変わらない）
    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    // プッシュされない（他エンティティや爆風による速度変更を防止）
    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void tick() {
        if (entityData.get(DATAPACK_GATE)) {
            summonAge++;
            if (!level().isClientSide) {
                updateAimPoint();
                // poof.mcfunction:32,36。寿命は待機を含む。タイムアウト爆発はない。
                if (summonAge >= 50 || position().distanceToSqr(aimPoint) <= 25) {
                    discard();
                    return;
                }
                if (entityData.get(WARMUP) > 0) {
                    prepareLaunch(aimPoint.subtract(position()).normalize().scale(launchVelocity.length()),
                            entityData.get(WARMUP));
                } else {
                    // tp に相当する一定速度。ThrowableProjectileの空気抵抗を次tickへ持ち越さない。
                    setDeltaMovement(launchVelocity);
                }
            }
        }
        if (entityData.get(WARMUP) > 0) {
            // 待機中も金色の粒子は出す。移動・衝突判定は射出後に開始。
            spawnTrail();
            baseTick();
            if (!level().isClientSide) {
                int remaining = entityData.get(WARMUP) - 1;
                entityData.set(WARMUP, remaining);
                if (remaining == 0) {
                    setDeltaMovement(launchVelocity);
                    hasImpulse = true;
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.DROWNED_SHOOT,
                            SoundSource.PLAYERS, 2.0f, 2.0f);
                }
            }
            return;
        }
        super.tick();
        if (isRemoved()) return;
        life++;

        spawnTrail();

        // 自動消滅: lifetime tick 後に爆発エフェクト付きで discard
        if (!entityData.get(DATAPACK_GATE) && life > GateFormula.projLifetime()) {
            if (!level().isClientSide) {
                level().explode(this, getX(), getY(), getZ(),
                        GateFormula.projEndRadius(), Level.ExplosionInteraction.NONE);
            }
            discard();
        }
    }

    private void spawnTrail() {
        int particles = GateFormula.projParticlesPerTick();
        if (particles > 0 && level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(GOLD_PARTICLE,
                    getX(), getY(), getZ(), particles, 0.2, 0.2, 0.2, 0.0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (level().isClientSide) return;
        if (result.getEntity() == getOwner()) return;

        // ダメージ (lisp 設定)
        if (result.getEntity() instanceof LivingEntity target) {
            target.hurt(damageSources().explosion(this, getOwner()), GateFormula.projHitDamage());
        }

        // 爆発エフェクト（地形破壊なし）
        level().explode(this, getX(), getY(), getZ(),
                GateFormula.projHitRadius(), Level.ExplosionInteraction.NONE);
        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // 爆発エフェクト（地形破壊なし）
        if (!level().isClientSide) {
            level().explode(this, getX(), getY(), getZ(),
                    GateFormula.projHitRadius(), Level.ExplosionInteraction.NONE);
        }
        discard();
    }

    @Override
    protected float getGravity() {
        return GateFormula.projGravity();  // 0 = 完全直進
    }
}
