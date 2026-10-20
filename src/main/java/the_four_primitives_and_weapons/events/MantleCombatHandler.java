package the_four_primitives_and_weapons.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import the_four_primitives_and_weapons.network.MantleDeflectPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.init.KnifeExtrasRegistrar;
import the_four_primitives_and_weapons.network.MantleActionPacket;
import the_four_primitives_and_weapons.network.MantleAnimationPacket;
import the_four_primitives_and_weapons.util.MantleCombatRules;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID)
public final class MantleCombatHandler {
    private record Active(MantleActionPacket.Action action, InteractionHand hand, long start, int duration, boolean struck) {}
    private static final Map<UUID, Active> ACTIVE = new HashMap<>();
    public static InteractionHand freeHand(Player player) {
        return switch (MantleCombatRules.freeHand(player.getMainHandItem().isEmpty(), player.getOffhandItem().isEmpty())) {
            case 0 -> InteractionHand.MAIN_HAND;
            case 1 -> InteractionHand.OFF_HAND;
            default -> null;
        };
    }
    public static void start(ServerPlayer player, MantleActionPacket.Action action) {
        if (!player.isAlive() || player.isSpectator()) return;
        if (MantleActionPacket.equipped(player).isEmpty()) {
            player.displayClientMessage(Component.translatable("message.the_four_primitives_and_weapons.mantle.not_equipped"), true);
            return;
        }
        if (player.getCooldowns().isOnCooldown(KnifeExtrasRegistrar.FANTASY_MANTLE.get())) {
            player.displayClientMessage(Component.translatable("message.the_four_primitives_and_weapons.mantle.cooldown"), true);
            return;
        }
        InteractionHand hand = freeHand(player);
        if (action == MantleActionPacket.Action.THRUST && hand == null) {
            player.displayClientMessage(Component.translatable("message.the_four_primitives_and_weapons.mantle.hands_full"), true);
            return;
        }
        int duration = action == MantleActionPacket.Action.GUARD ? MantleCombatRules.GUARD_TICKS : MantleCombatRules.THRUST_TICKS;
        Active active = new Active(action, hand == null ? InteractionHand.MAIN_HAND : hand,
            player.level().getGameTime(), duration, false);
        ACTIVE.put(player.getUUID(), active);
        player.getCooldowns().addCooldown(KnifeExtrasRegistrar.FANTASY_MANTLE.get(), action == MantleActionPacket.Action.GUARD ? 60 : 20);
        sync(player, active, duration);
        if (action == MantleActionPacket.Action.GUARD)
            player.level().playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1, .7f);
    }
    private static void sync(ServerPlayer player, Active active, int duration) {
        TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
            new MantleAnimationPacket(player.getUUID(), active.action, active.hand, Math.max(0, player.level().getGameTime() - active.start), duration));
    }
    @SubscribeEvent public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) return;
        Active active = ACTIVE.get(player.getUUID());
        if (active == null) return;
        long age = player.level().getGameTime() - active.start;
        boolean cancelled = !player.isAlive() || player.isSpectator() || MantleActionPacket.equipped(player).isEmpty()
            || (active.action == MantleActionPacket.Action.THRUST && !player.getItemInHand(active.hand).isEmpty());
        if (cancelled || age >= active.duration || age < 0) {
            ACTIVE.remove(player.getUUID()); sync(player, active, 0);
            if (cancelled && active.action == MantleActionPacket.Action.THRUST && !active.struck) {
                player.getCooldowns().removeCooldown(KnifeExtrasRegistrar.FANTASY_MANTLE.get());
                if (!player.getItemInHand(active.hand).isEmpty())
                    player.displayClientMessage(Component.translatable("message.the_four_primitives_and_weapons.mantle.hand_changed"), true);
            }
            return;
        }
        if (active.action == MantleActionPacket.Action.THRUST && MantleCombatRules.shouldStrike(age, active.duration, active.struck)) {
            ACTIVE.put(player.getUUID(), new Active(active.action, active.hand, active.start, active.duration, true));
            thrust(player, active.hand);
        }
    }
    private static void thrust(ServerPlayer player, InteractionHand hand) {
        if (!player.getItemInHand(hand).isEmpty()) return;
        Vec3 direction = player.getLookAngle().normalize();
        Vec3 origin = player.getEyePosition().add(0, -.35, 0);
        Vec3 end = origin.add(direction.scale(7));
        Vec3 wall = player.level().clip(new ClipContext(origin, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation();
        double range = Math.min(7, origin.distanceTo(wall));
        for (LivingEntity target : player.level().getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(7), target -> target != player && target.isAlive())) {
            var box = target.getBoundingBox().inflate(.3);
            boolean intersects = box.contains(origin) || box.clip(origin, origin.add(direction.scale(range))).isPresent();
            if (!intersects || player.isAlliedTo(target) || target.isAlliedTo(player) || !player.hasLineOfSight(target)) continue;
            if (target.hurt(player.damageSources().playerAttack(player), 10))
                target.knockback(.6, player.getX() - target.getX(), player.getZ() - target.getZ());
        }
        player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1, .8f);
    }
    public static MantleCombatRules.DamageKind damageKind(DamageSource source) {
        var direct = source.getDirectEntity();
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return MantleCombatRules.DamageKind.BYPASS;
        if (direct instanceof ThrownPotion || direct instanceof AreaEffectCloud || source.getEntity() instanceof AreaEffectCloud)
            return MantleCombatRules.DamageKind.POTION;
        if (source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC)
                || source.is(DamageTypes.WITHER) || source.is(DamageTypes.DRAGON_BREATH)
                || source.is(DamageTypeTags.BYPASSES_ARMOR) || source.is(DamageTypeTags.IS_FIRE)
                || source.is(DamageTypeTags.IS_EXPLOSION)) return MantleCombatRules.DamageKind.MAGIC;
        if (direct instanceof Projectile) return MantleCombatRules.DamageKind.PROJECTILE;
        if (direct instanceof LivingEntity) return MantleCombatRules.DamageKind.MELEE;
        return MantleCombatRules.DamageKind.ENVIRONMENT;
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void attack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || event.getAmount() <= 0
                || !player.isAlive() || player.isSpectator() || !MantleCombatRules.guards(damageKind(event.getSource()))) return;
        Active active = ACTIVE.get(player.getUUID());
        if (active == null || active.action != MantleActionPacket.Action.GUARD || MantleActionPacket.equipped(player).isEmpty()) return;
        long age = player.level().getGameTime() - active.start;
        if (age < 0 || age >= active.duration) return;
        // Reject the hit before damage, knockback and success-dependent on-hit effects.
        // No potion/effect event is intercepted: splash, lingering and drinking remain intact.
        event.setCanceled(true);
        var origin = event.getSource().getSourcePosition();
        var incoming = origin == null ? Vec3.ZERO : origin.subtract(player.position());
        var right = player.getLookAngle().cross(new Vec3(0, 1, 0));
        int side = incoming.dot(right) < 0 ? -1 : 1;
        TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
            new MantleDeflectPacket(player.getUUID(), side));
        player.level().playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, .7f, 1.25f);
    }
    @SubscribeEvent public static void tracking(PlayerEvent.StartTracking event) {
        if (!(event.getEntity() instanceof ServerPlayer observer) || !(event.getTarget() instanceof ServerPlayer target)) return;
        Active active = ACTIVE.get(target.getUUID());
        if (active != null) TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> observer),
            new MantleAnimationPacket(target.getUUID(), active.action, active.hand, Math.max(0, target.level().getGameTime() - active.start), active.duration));
    }
    @SubscribeEvent public static void logout(PlayerEvent.PlayerLoggedOutEvent event) { ACTIVE.remove(event.getEntity().getUUID()); }
    @SubscribeEvent public static void stopped(ServerStoppedEvent event) { ACTIVE.clear(); }
}
