package the_four_primitives_and_weapons.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** A small peaceful flier. Flower searches are bounded and only touch loaded blocks. */
public class ButterflyEntity extends FlyingMob {
    private Vec3 flightTarget;
    private int flightTicks;

    public ButterflyEntity(EntityType<? extends ButterflyEntity> type, Level level) {
        super(type, level);
        setNoGravity(true);
        xpReward = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4)
                .add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.FLYING_SPEED, 0.3);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (--flightTicks <= 0 || flightTarget == null || horizontalCollision
                || position().distanceToSqr(flightTarget) < 0.4) {
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
        if (flightTarget != null) {
            Vec3 direction = flightTarget.subtract(position()).normalize().scale(0.1);
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
