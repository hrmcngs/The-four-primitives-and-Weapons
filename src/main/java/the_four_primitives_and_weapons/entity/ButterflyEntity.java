package the_four_primitives_and_weapons.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

/** A small peaceful flier. Flower searches are bounded and only touch loaded blocks. */
public class ButterflyEntity extends FlyingMob {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(
            ButterflyEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<CompoundTag> APPEARANCE = SynchedEntityData.defineId(
            ButterflyEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private Vec3 flightTarget;
    private int flightTicks;

    public ButterflyEntity(EntityType<? extends ButterflyEntity> type, Level level) {
        super(type, level);
        setNoGravity(true);
        xpReward = 0;
        if (!level.isClientSide) entityData.set(VARIANT, random.nextInt(ButterflyVariant.count()));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(VARIANT, 0);
        entityData.define(APPEARANCE, new CompoundTag());
    }

    public ButterflyVariant getVariant() { return ButterflyVariant.byId(entityData.get(VARIANT)); }
    private int color(String key, int fallback) {
        CompoundTag tag = entityData.get(APPEARANCE);
        return tag.contains(key) ? tag.getInt(key) : fallback;
    }
    private float setting(String key, float fallback) {
        CompoundTag tag = entityData.get(APPEARANCE);
        return tag.contains(key) ? tag.getFloat(key) : fallback;
    }
    // Rebuild only after synchronized appearance data changes, not on every rendered frame.
    private Appearance cachedAppearance;
    private record Appearance(int wing, int edge, int accent, int body, int pattern, boolean tails,
                              float size, float flapSpeed, float flapAmount, float width, float length, float flightSpeed,
                              float antennaLength, float antennaSpread, float antennaTilt, float bodyWidth, float bodyLength, float flapRestAngle) {}
    private Appearance appearance() {
        if (cachedAppearance == null) {
            ButterflyVariant variant = getVariant();
            cachedAppearance = new Appearance(color("WingColor",variant.wingColor), color("EdgeColor",variant.edgeColor),
                    color("AccentColor",variant.accentColor),color("BodyColor",0x261F1F),color("Pattern",variant.pattern),
                    color("Tails",variant.tails?1:0)==1,setting("Size",1),setting("FlapSpeed",1),
                    setting("FlapAmount",0.9F),setting("WingWidth",1),setting("WingLength",1),setting("FlightSpeed",1),
                    setting("AntennaLength",1),setting("AntennaSpread",25),setting("AntennaTilt",20),
                    setting("BodyWidth",1),setting("BodyLength",1),setting("FlapRestAngle",14.323945F));
        }
        return cachedAppearance;
    }
    public int getWingColor() { return appearance().wing(); }
    public int getEdgeColor() { return appearance().edge(); }
    public int getAccentColor() { return appearance().accent(); }
    public int getBodyColor() { return appearance().body(); }
    public int getPattern() { return appearance().pattern(); }
    public boolean hasTails() { return appearance().tails(); }
    public float getButterflySize() { return appearance().size(); }
    public float getFlapSpeed() { return appearance().flapSpeed(); }
    public float getFlapAmount() { return appearance().flapAmount(); }
    public float getWingWidth() { return appearance().width(); }
    public float getWingLength() { return appearance().length(); }
    public float getAntennaLength() { return appearance().antennaLength(); }
    public float getAntennaSpread() { return appearance().antennaSpread(); }
    public float getAntennaTilt() { return appearance().antennaTilt(); }
    public float getBodyWidth() { return appearance().bodyWidth(); }
    public float getBodyLength() { return appearance().bodyLength(); }
    public float getFlapRestAngle() { return appearance().flapRestAngle(); }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(getButterflySize());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (APPEARANCE.equals(key) || VARIANT.equals(key)) cachedAppearance = null;
        super.onSyncedDataUpdated(key);
        if (APPEARANCE.equals(key)) refreshDimensions();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", getVariant().ordinal());
        CompoundTag appearance = entityData.get(APPEARANCE);
        for (String key : appearance.getAllKeys()) tag.put(key, appearance.get(key).copy());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Variant", 99)) entityData.set(VARIANT, ButterflyVariant.byId(tag.getInt("Variant")).ordinal());
        else if (tag.contains("Variant", 8)) entityData.set(VARIANT, ButterflyVariant.byName(tag.getString("Variant")).ordinal());
        CompoundTag appearance = ButterflyAppearanceSettings.readAppearance(tag);
        entityData.set(APPEARANCE,appearance);
        flightTarget=null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4)
                .add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.FLYING_SPEED, 0.3);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        // Failed searches and continuous wall contact must also respect the retry interval.
        if (--flightTicks <= 0) {
            flightTicks = 40 + random.nextInt(40);
            flightTarget = null;
            BlockPos origin = blockPosition();
            for (int i = 0; i < 20; i++) {
                BlockPos candidate = origin.offset(random.nextInt(11)-5, random.nextInt(7)-4,
                        random.nextInt(11)-5);
                if (!level().hasChunkAt(candidate) || !level().isInWorldBounds(candidate)) continue;
                if (level().getBlockState(candidate).is(BlockTags.FLOWERS)) candidate = candidate.above();
                else if (i < 12) continue;
                if (level().isEmptyBlock(candidate) && level().getFluidState(candidate).isEmpty()) {
                    flightTarget = Vec3.atCenterOf(candidate);
                    break;
                }
            }
        }
        if (flightTarget != null && (horizontalCollision || position().distanceToSqr(flightTarget) < 0.4)) {
            flightTarget = null;
            flightTicks = Math.min(flightTicks, 5);
        }
        if (flightTarget != null) {
            Vec3 direction = flightTarget.subtract(position()).normalize().scale(0.1 * appearance().flightSpeed());
            setDeltaMovement(getDeltaMovement().scale(0.75).add(direction.scale(0.25)));
            Vec3 motion = getDeltaMovement();
            if (motion.horizontalDistanceSqr() > 0.0001) {
                float yaw = (float)(Mth.atan2(motion.z, motion.x) * Mth.RAD_TO_DEG) - 90;
                setYRot(getYRot() + Mth.wrapDegrees(yaw-getYRot()) * 0.2F);
                yBodyRot = getYRot();
            }
        } else {
            setDeltaMovement(getDeltaMovement().scale(0.8));
        }
        if (isInWater()) setDeltaMovement(getDeltaMovement().add(0, 0.06, 0));
    }

    @Override
    public boolean removeWhenFarAway(double distance) { return false; }
}
