package the_four_primitives_and_weapons.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import the_four_primitives_and_weapons.damage.ElementType;
import the_four_primitives_and_weapons.damage.ElementalDamageUtils;
import the_four_primitives_and_weapons.damage.ElementalParticles;
import the_four_primitives_and_weapons.damage.IElementalDamageSource;
import the_four_primitives_and_weapons.damage.ModDamageSources;
import the_four_primitives_and_weapons.entity.NinjatoTetherSegmentEntity;
import the_four_primitives_and_weapons.entity.StabbedWeaponEntity;

/** 設置した鞘と手元をつなぐチェーンの通電。紐や切断済みの接続からは呼ばない。 */
public final class NinjatoChainPower {
    private NinjatoChainPower() {}
    private static final String SHOCK_UNTIL = "NinjatoChainShockUntil";
    private static final String THUNDER_UNTIL = "NinjatoThunderUntil";

    private static int storedLevel(ItemStack sheath, ElementType type) {
        ItemStack blade = sheath.hasTag() ? ItemStack.of(sheath.getTag().getCompound("StoredKatana")) : ItemStack.EMPTY;
        return Math.max(elementLevel(sheath, type), elementLevel(blade, type));
    }

    public static void tickBlade(StabbedWeaponEntity planted, Player owner) {
        if (!(planted.level() instanceof ServerLevel level) || planted.isRemoved()) return;
        ItemStack item = planted.getItem();
        if (!item.is(the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems.NINJATOU.get())
                && !NinjatoVault.isLoaded(item)) return;
        int thunder = storedLevel(item, ElementType.THUNDER);
        if (thunder == 0) return;
        Vec3[] axis = planted.weaponSegment();
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class,
                new AABB(axis[0], axis[1]).inflate(0.2), e -> canHit(owner, e))) {
            AABB body = target.getBoundingBox().inflate(0.15);
            if (body.contains(axis[0]) || body.clip(axis[0], axis[1]).isPresent()) {
                strike(planted, owner, target, thunder);
                break;
            }
        }
    }

    private static void strike(StabbedWeaponEntity planted, Player owner, LivingEntity target, int rank) {
        if (!(planted.level() instanceof ServerLevel level)) return;
        long now = level.getGameTime();
        if (planted.getPersistentData().getLong(THUNDER_UNTIL) > now) return;
        planted.getPersistentData().putLong(THUNDER_UNTIL, now + 80);
        net.minecraft.world.entity.LightningBolt bolt = net.minecraft.world.entity.EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(planted.getTetherAnchor());
            bolt.setVisualOnly(true);
            level.addFreshEntity(bolt);
        }
        hurt(level, planted, owner, target, ElementType.THUNDER, rank,
                Math.min(12.0F, 4.0F + Math.max(0, rank - 1)));
        spark(level, target.getBoundingBox().getCenter());
    }

    public static int elementLevel(ItemStack stack, ElementType type) {
        int level = ElementalDamageUtils.getElementType(stack) == type
                ? Math.max(1, ElementalDamageUtils.getElementLevel(stack)) : 0;
        if (ElementalDamageUtils.getSecondaryElementType(stack) == type)
            level = Math.max(level, Math.max(1, ElementalDamageUtils.getSecondaryElementLevel(stack)));
        return level;
    }

    public static void tick(StabbedWeaponEntity tether, Player owner, Vec3 anchor, Vec3 hand) {
        if (!tether.isChainTether()
                || !(tether.level() instanceof ServerLevel level) || owner.isSpectator()
                || anchor.distanceToSqr(hand) > 100) return;
        ItemStack sheath = tether.getItem();
        int electric = storedLevel(sheath, ElementType.ELECTRIC);
        int thunder = storedLevel(sheath, ElementType.THUNDER);
        if (electric == 0 && thunder == 0) return;

        boolean conductionPulse = tether.tickCount % 10 == 0;
        Vec3 line = hand.subtract(anchor);
        int steps = Math.max(1, (int)Math.ceil(line.length() / 0.25));
        Set<BlockPos> waterSeeds = new LinkedHashSet<>();
        List<Vec3> rainyPoints = new ArrayList<>();
        for (int i = 0; conductionPulse && electric > 0 && i <= steps; i++) {
            Vec3 point = anchor.add(line.scale(i / (double)steps));
            BlockPos pos = BlockPos.containing(point);
            if (level.hasChunkAt(pos) && level.getFluidState(pos).is(FluidTags.WATER)) waterSeeds.add(pos);
            if (level.isRainingAt(pos)) rainyPoints.add(point);
            if (i % 2 == 0) spark(level, point);
        }
        Set<BlockPos> water = BoundedConduction.collect(waterSeeds,
                pos -> Arrays.stream(Direction.values()).map(pos::relative).toList(),
                pos -> level.hasChunkAt(pos) && level.getFluidState(pos).is(FluidTags.WATER), 4, 512);
        int shown = 0;
        for (BlockPos pos : water) {
            if (shown++ % 12 == 0) spark(level, Vec3.atCenterOf(pos));
        }
        long now = level.getGameTime();
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class,
                new AABB(anchor, hand).inflate(conductionPulse && electric > 0 ? 5 : 0.2), e -> canHit(owner, e))) {
            AABB body = target.getBoundingBox();
            boolean touching = body.inflate(0.12).contains(anchor) || body.inflate(0.12).clip(anchor, hand).isPresent();
            if (touching && thunder > 0) strike(tether, owner, target, thunder);
            if (electric == 0 || target.getPersistentData().getLong(SHOCK_UNTIL) > now) continue;
            boolean inConductingWater = target.isInWaterOrBubble()
                    && water.stream().anyMatch(pos -> new AABB(pos).intersects(body));
            boolean inConductingRain = target.isInWaterOrRain() && rainyPoints.stream()
                    .anyMatch(point -> point.distanceToSqr(body.getCenter()) <= 4
                        && level.clip(new net.minecraft.world.level.ClipContext(point, body.getCenter(),
                            net.minecraft.world.level.ClipContext.Block.COLLIDER,
                            net.minecraft.world.level.ClipContext.Fluid.NONE, tether)).getType()
                                == net.minecraft.world.phys.HitResult.Type.MISS);
            if (!touching && !inConductingWater && !inConductingRain) continue;
            target.getPersistentData().putLong(SHOCK_UNTIL, now + 20);
            hurt(level, tether, owner, target, ElementType.ELECTRIC, electric,
                    Math.min(6.0F, 2.0F + Math.max(0, electric - 1) * 0.5F));
        }
    }

    public static boolean canHit(Player owner, LivingEntity target) {
        if (owner == null) return target instanceof net.minecraft.world.entity.monster.Monster && target.isAlive();
        if (owner.isSpectator()) return false;
        return target != owner && target.isAlive() && !target.isSpectator()
                && !(target instanceof NinjatoTetherSegmentEntity) && target.isAttackable()
                && !owner.isAlliedTo(target) && !target.isAlliedTo(owner)
                && (!(target instanceof Player other) || owner.canHarmPlayer(other));
    }

    public static void hurt(ServerLevel level, net.minecraft.world.entity.Entity direct, Player owner,
            LivingEntity target, ElementType type, int rank, float damage) {
        if (ElementalDamageUtils.isElementNullifiedByBook(target, type)) return;
        DamageSource source = ModDamageSources.of(level, ModDamageSources.keyFor(type), direct, owner);
        if (source instanceof IElementalDamageSource elemental) {
            elemental.setElementType(type);
            elemental.setElementLevel(rank);
        }
        target.hurt(source, damage);
    }

    private static void spark(ServerLevel level, Vec3 point) {
        ElementalParticles.sendForced(level, ParticleTypes.ELECTRIC_SPARK,
                point.x, point.y, point.z, 2, 0.04, 0.04, 0.04, 0.01);
    }
}
