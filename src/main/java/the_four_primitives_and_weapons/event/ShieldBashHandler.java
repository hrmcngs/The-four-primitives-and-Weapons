package the_four_primitives_and_weapons.event;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.skill.WeaponStatsRegistry;

/** 全盾共通。利き手で使用中にチャージし、使用キーを離したときだけバッシュする。 */
@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID)
public final class ShieldBashHandler {
    public static final int MIN_CHARGE_TICKS = 5;
    public static final int MAX_CHARGE_TICKS = 40;
    private static final String FORWARD_INPUT = "the_four_primitives_and_weapons:shield_forward_input";
    private static final String FORWARD_INPUT_TIME = "the_four_primitives_and_weapons:shield_forward_input_time";
    public record Settings(float damage, int cooldown) {}
    public static final java.util.Map<net.minecraft.resources.ResourceLocation, Settings> CLIENT_SETTINGS
            = new java.util.concurrent.ConcurrentHashMap<>();

    public static Settings getDisplaySettings(ItemStack stack) {
        Settings synced = CLIENT_SETTINGS.get(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem()));
        return synced != null ? synced : new Settings(getAttackDamage(stack), getCooldownTicks(stack));
    }

    public static float getAttackDamage(ItemStack stack) {
        var stats = WeaponStatsRegistry.getStats(stack);
        return stats != null && Float.isFinite(stats.attackDamage) ? Math.max(0, stats.attackDamage)
                : 8.0f;
    }

    public static int getCooldownTicks(ItemStack stack) {
        var stats = WeaponStatsRegistry.getStats(stack);
        return stats != null && stats.cooldown >= 0 ? stats.cooldown : 20;
    }

    public static float getDamage(ItemStack stack, int heldTicks) {
        return scaleDamage(getAttackDamage(stack), heldTicks);
    }

    public static float getDisplayDamage(ItemStack stack, int heldTicks) {
        return scaleDamage(getDisplaySettings(stack).damage(), heldTicks);
    }

    private static float scaleDamage(float maxDamage, int heldTicks) {
        if (heldTicks < MIN_CHARGE_TICKS) return 0;
        float charge = Math.min((float) (heldTicks - MIN_CHARGE_TICKS)
                / (MAX_CHARGE_TICKS - MIN_CHARGE_TICKS), 1.0f);
        return maxDamage * (0.25f + 0.75f * charge);
    }

    public static boolean isShield(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof ShieldItem
                || stack.canPerformAction(ToolActions.SHIELD_BLOCK));
    }

    private static boolean canCharge(Player player, ItemStack stack) {
        return player.isAlive() && !player.isSpectator() && player.isUsingItem()
                && player.getUsedItemHand() == InteractionHand.MAIN_HAND
                && player.getMainHandItem() == stack && isShield(stack)
                && !player.getCooldowns().isOnCooldown(stack.getItem());
    }

    public static void updateForwardInput(Player player, boolean forward) {
        boolean allowed = isShield(player.getMainHandItem())
                && !the_four_primitives_and_weapons.item.GreatshieldItem.isGreatshield(player.getMainHandItem());
        player.getPersistentData().putBoolean(FORWARD_INPUT, allowed && forward);
        player.getPersistentData().putLong(FORWARD_INPUT_TIME, player.level().getGameTime());
    }

    public static boolean isDashBashCharging(Player player) {
        long age = player.level().getGameTime() - player.getPersistentData().getLong(FORWARD_INPUT_TIME);
        return isShield(player.getMainHandItem())
                && !the_four_primitives_and_weapons.item.GreatshieldItem.isGreatshield(player.getMainHandItem())
                && player.getPersistentData().getBoolean(FORWARD_INPUT) && age >= 0 && age <= 10
                && player.onGround() && !player.isPassenger() && !player.isInWaterOrBubble();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onUseTick(LivingEntityUseItemEvent.Tick event) {
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) return;
        if (event.isCanceled() || event.getDuration() <= 0 || !canCharge(player, event.getItem())) return;
        int held = event.getItem().getUseDuration() - event.getDuration();
        if (held == MIN_CHARGE_TICKS || held == MAX_CHARGE_TICKS / 2) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK,
                    SoundSource.PLAYERS, 0.5f, held == MIN_CHARGE_TICKS ? 1.5f : 1.1f);
        } else if (held == MAX_CHARGE_TICKS) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_LAND,
                    SoundSource.PLAYERS, 0.4f, 1.8f);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRelease(LivingEntityUseItemEvent.Stop event) {
        if (!(event.getEntity() instanceof Player player)) return;
        boolean dash = isDashBashCharging(player);
        if (player.level().isClientSide) return;
        if (event.isCanceled() || !canCharge(player, event.getItem())) return;
        int held = event.getItem().getUseDuration() - event.getDuration();
        if (held < MIN_CHARGE_TICKS) return;

        float charge = Math.min((float) (held - MIN_CHARGE_TICKS)
                / (MAX_CHARGE_TICKS - MIN_CHARGE_TICKS), 1.0f);
        float damage = getDamage(event.getItem(), held);
        // 空振りでも発動後は待ち時間を適用。盾が壊れる前に対象アイテムを登録する。
        int cooldown = getCooldownTicks(event.getItem());
        if (cooldown > 0) player.getCooldowns().addCooldown(event.getItem().getItem(), cooldown);
        float knockback = 0.5f + charge * 2.5f;
        double range = 2.0 + charge * 2.5;
        Vec3 origin = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        if (dash) {
            // 水平方向への踏み込み。位置を直接移さず、通常の衝突判定で壁に止まる。
            Vec3 forward = Vec3.directionFromRotation(0, player.getYRot());
            double speed = 0.75 + charge * 0.6;
            player.setDeltaMovement(forward.x * speed, player.getDeltaMovement().y, forward.z * speed);
            player.hurtMarked = true;
            knockback += 0.75f;
            range += 1.0;
            player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP,
                    SoundSource.PLAYERS, 0.8f, 0.8f);
        }
        boolean hit = false;
        for (LivingEntity target : player.level().getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(range),
                entity -> entity != player && entity.isAlive() && !entity.isSpectator())) {
            if (player.isAlliedTo(target) || !player.hasLineOfSight(target)) continue;
            if (target instanceof Player other && !player.canHarmPlayer(other)) continue;
            Vec3 offset = target.getBoundingBox().getCenter().subtract(origin);
            // 前方120度の範囲のみ。背後や壁越しには当てない。
            if (offset.lengthSqr() > range * range || offset.normalize().dot(look) < 0.5) continue;
            if (target.hurt(player.damageSources().playerAttack(player), damage)) {
                target.knockback(knockback, player.getX() - target.getX(), player.getZ() - target.getZ());
                hit = true;
            }
        }
        // 範囲内の命中数によらず、バッシュ1回の命中で耐久を1消費する。
        if (hit) event.getItem().hurtAndBreak(1, player,
                entity -> entity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        player.swing(InteractionHand.MAIN_HAND, true);
        player.level().playSound(null, player.blockPosition(), SoundEvents.SHIELD_BLOCK,
                SoundSource.PLAYERS, 0.8f + charge * 0.7f, 0.6f + charge * 0.6f);
    }
}
