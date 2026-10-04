package the_four_primitives_and_weapons.events;

import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.skill.PostureRules;
import the_four_primitives_and_weapons.skill.WeaponTypeRegistry;
import the_four_primitives_and_weapons.util.DamageCalculator;

/** Transient server-side pressure. Weak entity keys cannot leak across worlds or respawns. */
@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons")
public final class PostureCombatHandler {
    private static final Map<LivingEntity, State> STATES = new WeakHashMap<>();
    private static final class State {
        ResourceLocation dimension;
        float pressure;
        long lastHit = -1;
        long staggerUntil;
        long immuneUntil;
    }
    private PostureCombatHandler() {}

    private static State state(LivingEntity target) {
        State state = STATES.computeIfAbsent(target, ignored -> new State());
        ResourceLocation dimension = target.level().dimension().location();
        long now = target.level().getGameTime();
        if (!dimension.equals(state.dimension) || now < state.lastHit) {
            state = new State();
            state.dimension = dimension;
            STATES.put(target, state);
        }
        return state;
    }

    public static boolean isStaggered(LivingEntity target) {
        if (target.level().isClientSide()) return false;
        State state = STATES.get(target);
        return state != null && target.level().dimension().location().equals(state.dimension)
                && target.level().getGameTime() >= state.lastHit
                && target.level().getGameTime() < state.staggerUntil;
    }

    private static boolean melee(DamageSource source) {
        return source.getEntity() instanceof LivingEntity
                && source.getDirectEntity() == source.getEntity()
                && (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK));
    }

    public static void addPressure(LivingEntity target, LivingEntity attacker, float amount) {
        if (target.level().isClientSide() || !target.isAlive() || target == attacker
                || !(target instanceof Mob || target instanceof Player)
                || !Float.isFinite(amount) || amount <= 0
                || target instanceof Player player && (player.isCreative() || player.isSpectator())) return;
        State state = state(target);
        long now = target.level().getGameTime();
        if (!PostureRules.canAccumulate(now, state.lastHit, state.immuneUntil)) return;
        state.pressure = PostureRules.decay(state.pressure, state.lastHit, now) + amount;
        state.lastHit = now;
        if (state.pressure >= PostureRules.LIMIT) {
            state.pressure = 0;
            state.staggerUntil = now + PostureRules.STAGGER_TICKS;
            state.immuneUntil = now + PostureRules.BREAK_IMMUNITY_TICKS;
            if (target instanceof Mob mob) mob.getNavigation().stop();
            if (target instanceof Player player && player.getPersistentData().getInt("SwordGuardTicks") > 0)
                SwordGuardHandler.endGuard(player, player.getPersistentData());
            if (target.level() instanceof ServerLevel level) {
                level.sendParticles(ParticleTypes.CRIT, target.getX(), target.getY() + 1, target.getZ(),
                        18, 0.4, 0.5, 0.4, 0.1);
                level.playSound(null, target.blockPosition(), SoundEvents.SHIELD_BREAK,
                        SoundSource.PLAYERS, 1, 0.8f);
            }
            if (attacker instanceof Player player)
                player.displayClientMessage(Component.literal("§6体勢崩し！ 相手の攻撃が一時中断"), true);
            if (target instanceof Player player)
                player.displayClientMessage(Component.literal("§c体勢を崩された！"), true);
        } else if (attacker instanceof Player player) {
            player.displayClientMessage(Component.literal("§e" + target.getName().getString()
                    + " §7崩し " + Math.round(state.pressure) + "/100"), true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void interruptAttack(LivingAttackEvent event) {
        if (melee(event.getSource()) && isStaggered((LivingEntity) event.getSource().getEntity()))
            event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void interruptPlayerInput(AttackEntityEvent event) {
        if (isStaggered(event.getEntity())) event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void punishOpening(LivingHurtEvent event) {
        if (melee(event.getSource()) && isStaggered(event.getEntity())) event.setAmount(event.getAmount() * 1.25f);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMeleeDamage(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide() || event.getAmount() <= 0 || !melee(event.getSource())) return;
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        var weapon = attacker.getMainHandItem();
        if (!(weapon.getItem() instanceof SwordItem || weapon.getItem() instanceof TridentItem || weapon.getItem() instanceof net.minecraft.world.item.ShieldItem)
                && !(attacker instanceof Mob)) return;
        var type = WeaponTypeRegistry.getTypeForItem(weapon);
        Float context = DamageCalculator.getCooldownScaleContext();
        float gauge = context != null ? context : attacker instanceof Player player
                ? player.getAttackStrengthScale(0.5f) : 1;
        float pressure = PostureRules.impact(type == null ? null : type.getId(), gauge,
                DamageCalculator.getChargePercentContext(), event.getEntity().getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        pressure *= 1 + .2f * the_four_primitives_and_weapons.skill.WeaponGrowth.rank(weapon,
                the_four_primitives_and_weapons.skill.WeaponGrowthRules.Perk.BREAKER);
        addPressure(event.getEntity(), attacker, pressure);
    }
}
