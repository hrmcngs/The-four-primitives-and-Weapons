package the_four_primitives_and_weapons.events;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Server-authoritative timing defenses; rewards are tied to the avoided attacker. */
@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons")
public final class TacticalDefenseHandler {
    private static final String GUARD_RETRY = "TacticalGuardRetry";
    private static final String GUARD_START = "TacticalGuardStart";
    private static final String DODGE_START = "TacticalDodgeStart";
    private static final String COUNTER_UNTIL = "TacticalCounterUntil";
    private static final String COUNTER_TARGET = "TacticalCounterTarget";

    private TacticalDefenseHandler() {}

    public static boolean canBeginGuard(Player player) {
        CompoundTag data = player.getPersistentData();
        return !data.contains(GUARD_RETRY)
                || !the_four_primitives_and_weapons.skill.CombatTimingRules.inWindow(
                        player.level().getGameTime(), data.getLong(GUARD_RETRY),
                        the_four_primitives_and_weapons.skill.CombatTimingRules.PARRY_RETRY);
    }

    public static void beginGuard(Player player) {
        player.getPersistentData().putLong(GUARD_RETRY, player.level().getGameTime());
        player.getPersistentData().putLong(GUARD_START, player.level().getGameTime());
    }

    public static void beginDodge(Player player) {
        player.getPersistentData().putLong(DODGE_START, player.level().getGameTime());
    }

    public static boolean canBlock(Player player, DamageSource source) {
        if (!(player.getMainHandItem().getItem() instanceof SwordItem)
                || SwordGuardHandler.hasShieldInHands(player)
                || source.is(DamageTypeTags.BYPASSES_ARMOR)
                || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || !(source.getEntity() instanceof LivingEntity attacker) || attacker == player) return false;
        Vec3 origin = source.getSourcePosition();
        if (origin == null) return false;
        Vec3 direction = origin.subtract(player.position()).multiply(1, 0, 1);
        Vec3 facing = player.getLookAngle().multiply(1, 0, 1);
        return the_four_primitives_and_weapons.skill.CombatTimingRules.facingAttack(
                facing.x, facing.z, direction.x, direction.z);
    }

    private static boolean inWindow(CompoundTag data, String key, long now) {
        return data.contains(key) && the_four_primitives_and_weapons.skill.CombatTimingRules.inWindow(
                now, data.getLong(key), the_four_primitives_and_weapons.skill.CombatTimingRules.PARRY_WINDOW);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onIncomingAttack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide()
                || event.getAmount() <= 0 || !player.isAlive() || PostureCombatHandler.isStaggered(player)) return;
        DamageSource source = event.getSource();
        // Only direct melee strikes: explosions, projectiles and environmental damage do not grant counters.
        if (!(source.getEntity() instanceof LivingEntity attacker) || attacker == player
                || source.getDirectEntity() != attacker || source.is(DamageTypeTags.IS_EXPLOSION)
                || source.is(DamageTypeTags.BYPASSES_ARMOR)
                || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
        CompoundTag data = player.getPersistentData();
        long now = player.level().getGameTime();
        boolean parry = data.getInt("SwordGuardTicks") > 0 && canBlock(player, source)
                && inWindow(data, GUARD_START, now);
        boolean evade = data.getInt("SwordGuardTicks") <= 0 && inWindow(data, DODGE_START, now);
        if (!parry && !evade) return;
        event.setCanceled(true);
        data.remove(GUARD_START);
        data.remove(DODGE_START);
        data.putLong(COUNTER_UNTIL, now + 20);
        data.putUUID(COUNTER_TARGET, attacker.getUUID());
        if (parry) {
            SwordGuardHandler.endGuard(player, data);
            player.getCooldowns().removeCooldown(player.getMainHandItem().getItem());
            PostureCombatHandler.addPressure(attacker, player, 35);
        }
        player.displayClientMessage(Component.literal(parry
                ? "§eジャストガード！ 相手への次の近接攻撃で反撃"
                : "§bジャスト回避！ 相手への次の近接攻撃で反撃"), true);
        if (player.level() instanceof ServerLevel level) {
            level.sendParticles(ParticleTypes.CRIT, player.getX(), player.getY() + 1, player.getZ(),
                    12, 0.4, 0.4, 0.4, 0.05);
            level.playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK,
                    SoundSource.PLAYERS, 1.0f, parry ? 1.5f : 2.0f);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCounterHit(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player) || player.level().isClientSide()
                || event.getSource().getDirectEntity() != player
                || !event.getSource().is(DamageTypes.PLAYER_ATTACK)
                || !(player.getMainHandItem().getItem() instanceof SwordItem)
                || event.getAmount() <= 0) return;
        CompoundTag data = player.getPersistentData();
        if (!data.hasUUID(COUNTER_TARGET)) return;
        if (player.level().getGameTime() >= data.getLong(COUNTER_UNTIL)) {
            clearCounter(data);
            return;
        }
        if (!data.getUUID(COUNTER_TARGET).equals(event.getEntity().getUUID())) return;
        clearCounter(data);
        event.setAmount(event.getAmount() * 1.5f);
        player.displayClientMessage(Component.literal("§6反撃成功！"), true);
    }

    private static void clearCounter(CompoundTag data) {
        data.remove(COUNTER_UNTIL);
        data.remove(COUNTER_TARGET);
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        CompoundTag data = event.getEntity().getPersistentData();
        data.remove(GUARD_START);
        data.remove(GUARD_RETRY);
        data.remove(DODGE_START);
        clearCounter(data);
    }
}
