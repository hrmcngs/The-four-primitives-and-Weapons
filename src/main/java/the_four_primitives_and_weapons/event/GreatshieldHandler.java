package the_four_primitives_and_weapons.event;

import java.util.UUID;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.entity.PlacedGreatshieldEntity;
import the_four_primitives_and_weapons.item.GreatshieldItem;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID)
public final class GreatshieldHandler {
    private static final UUID WEIGHT = UUID.fromString("e3d6d41f-3c47-44d3-844f-50cc23fba2d1");
    private static final java.util.Map<net.minecraft.server.level.ServerLevel, java.util.Set<PlacedGreatshieldEntity>> PLACED = new java.util.WeakHashMap<>();

    @SubscribeEvent
    public static void shieldJoined(net.minecraftforge.event.entity.EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof net.minecraft.server.level.ServerLevel level
                && event.getEntity() instanceof PlacedGreatshieldEntity shield) {
            PLACED.computeIfAbsent(level, ignored -> java.util.Collections.newSetFromMap(new java.util.WeakHashMap<>())).add(shield);
        }
    }

    @SubscribeEvent
    public static void shieldLeft(net.minecraftforge.event.entity.EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof PlacedGreatshieldEntity shield) {
            var shields = PLACED.get(event.getLevel());
            if (shields != null) shields.remove(shield);
        }
    }

    @SubscribeEvent
    public static void levelUnloaded(net.minecraftforge.event.level.LevelEvent.Unload event) {
        PLACED.remove(event.getLevel());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlace(PlayerInteractEvent.RightClickBlock event) {
        var player = event.getEntity();
        if (!player.isShiftKeyDown() || player.isSpectator()) return;
        // オフハンドに大盾があるときも、メインハンドの使用に入力を消費される前に設置する。
        InteractionHand hand = GreatshieldItem.isGreatshield(player.getMainHandItem()) ? InteractionHand.MAIN_HAND
                : GreatshieldItem.isGreatshield(player.getOffhandItem()) ? InteractionHand.OFF_HAND : null;
        if (hand == null) return;
        var item = (GreatshieldItem) player.getItemInHand(hand).getItem();
        var result = item.useOn(new UseOnContext(player, hand, event.getHitVec()));
        event.setCanceled(true);
        event.setCancellationResult(result);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlaceUsingItem(PlayerInteractEvent.RightClickItem event) {
        placeInFront(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlaceTargetingEntity(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof PlacedGreatshieldEntity)) placeInFront(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlaceTargetingEntityPart(PlayerInteractEvent.EntityInteractSpecific event) {
        // 設置済みの盾を右クリックした場合は回収を優先する。
        if (!(event.getTarget() instanceof PlacedGreatshieldEntity)) placeInFront(event);
    }

    private static void placeInFront(PlayerInteractEvent event) {
        var player = event.getEntity();
        if (!player.isShiftKeyDown() || player.isSpectator()) return;
        InteractionHand hand = GreatshieldItem.isGreatshield(player.getMainHandItem()) ? InteractionHand.MAIN_HAND
                : GreatshieldItem.isGreatshield(player.getOffhandItem()) ? InteractionHand.OFF_HAND : null;
        if (hand == null) return;
        var item = (GreatshieldItem) player.getItemInHand(hand).getItem();
        // メインハンドの武器や通常の盾使用より先に、オフハンドの大盾も設置できるようにする。
        var result = item.placeInFront(player, hand);
        event.setCanceled(true);
        event.setCancellationResult(result);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;
        var player = event.player;
        var speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed == null) return;
        boolean held = !player.isSpectator() && (GreatshieldItem.isGreatshield(player.getMainHandItem())
                || GreatshieldItem.isGreatshield(player.getOffhandItem()));
        if (held && speed.getModifier(WEIGHT) == null) {
            speed.addTransientModifier(new AttributeModifier(WEIGHT, "Greatshield weight", -0.2,
                    AttributeModifier.Operation.MULTIPLY_TOTAL));
        } else if (!held && speed.getModifier(WEIGHT) != null) speed.removeModifier(WEIGHT);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onExplosion(ExplosionEvent.Detonate event) {
        if (event.getLevel().isClientSide) return;
        Vec3 origin = event.getExplosion().getPosition();
        // 爆発処理が盾を破壊する前に遮蔽物を確定する。処理順によって防御が変わらないようにする。
        var shields = event.getAffectedEntities().stream()
                .filter(entity -> entity instanceof PlacedGreatshieldEntity shield
                        && !shield.isRemoved() && !shield.getShield().isEmpty())
                .map(entity -> entity.getBoundingBox())
                .toList();
        if (shields.isEmpty()) return;
        // LivingAttackEvent のキャンセルだけでは爆発のノックバックが残るため、対象一覧から除外する。
        // 盾自体は残し、爆発本来のダメージで耐久を一度だけ消費する。
        event.getAffectedEntities().removeIf(target -> !(target instanceof PlacedGreatshieldEntity)
                && shields.stream().anyMatch(bounds -> !bounds.contains(origin)
                        && bounds.clip(origin, target.getBoundingBox().getCenter()).isPresent()));
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttack(LivingAttackEvent event) {
        var target = event.getEntity();
        if (target.level().isClientSide || event.getAmount() <= 0
                || event.getSource().is(DamageTypeTags.BYPASSES_SHIELD)
                // 爆発は Detonate で実際の爆心から判定済み。着火したプレイヤーの位置を使わない。
                || event.getSource().is(DamageTypeTags.IS_EXPLOSION)) return;
        var source = event.getSource();
        Vec3 from = source.getSourcePosition();
        if (source.sourcePositionRaw() == null
                && source.getDirectEntity() instanceof net.minecraft.world.entity.LivingEntity attacker) from = attacker.getEyePosition();
        if (from == null) return;
        Vec3 to = target.getBoundingBox().getCenter();
        var shield = firstShield(target.level(), from, to);
        if (shield != null) {
            event.setCanceled(true);
            shield.absorbDamage(event.getAmount());
        }
    }

    /** Trace the entire attack path, including shields far from the victim. */
    public static PlacedGreatshieldEntity firstShield(net.minecraft.world.level.Level level, Vec3 from, Vec3 to) {
        return firstShield(beamShields(level, new net.minecraft.world.phys.AABB(from, to)), from, to);
    }

    public static java.util.List<PlacedGreatshieldEntity> beamShields(net.minecraft.world.level.Level level, net.minecraft.world.phys.AABB bounds) {
        var shields = PLACED.get(level);
        if (shields == null || shields.isEmpty()) return java.util.List.of();
        var search = bounds.inflate(0.001);
        java.util.List<PlacedGreatshieldEntity> result = new java.util.ArrayList<>();
        for (var shield : shields) {
            if (!shield.isRemoved() && !shield.getShield().isEmpty() && shield.getBoundingBox().intersects(search)) result.add(shield);
        }
        return result;
    }

    private static PlacedGreatshieldEntity firstShield(java.util.List<PlacedGreatshieldEntity> shields, Vec3 from, Vec3 to) {
        PlacedGreatshieldEntity nearest = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (var shield : shields) {
            if (shield.isRemoved() || shield.getShield().isEmpty()) continue;
            var hit = the_four_primitives_and_weapons.util.ShieldGeometry.intersection(shield.getBoundingBox(), from, to);
            if (hit.isPresent() && from.distanceToSqr(hit.get()) < nearestDistance) {
                nearest = shield;
                nearestDistance = from.distanceToSqr(hit.get());
            }
        }
        return nearest;
    }

    /** Clip particle beams too, so their visible path cannot continue through a shield. */
    public static Vec3 clipBeam(net.minecraft.world.level.Level level, Vec3 from, Vec3 to, float damage) {
        return clipBeam(beamShields(level, new net.minecraft.world.phys.AABB(from, to)), from, to, damage);
    }

    public static Vec3 clipBeam(java.util.List<PlacedGreatshieldEntity> shields, Vec3 from, Vec3 to, float damage) {
        var shield = firstShield(shields, from, to);
        if (shield == null) return to;
        Vec3 hit = the_four_primitives_and_weapons.util.ShieldGeometry.intersection(shield.getBoundingBox(), from, to).orElse(to);
        shield.absorbDamage(damage);
        return hit;
    }
}
