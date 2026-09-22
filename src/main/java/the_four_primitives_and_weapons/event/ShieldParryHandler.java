package the_four_primitives_and_weapons.event;

import the_four_primitives_and_weapons.item.ParryShieldItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 専用盾の右クリックパリィと、すべての盾のFキースワップパリィ。
 *
 * ＜パリィ発動の2経路＞
 *
 *   A. オフハンドパリィ
 *      オフハンドで右クリック（ブロック開始）後 PARRY_WINDOW_TICKS 以内に
 *      攻撃を受けると発動。
 *
 *   B. スワップパリィ（Fキー）
 *      Fキーで盾をどちらかの手へ持ち替えた直後 PARRY_WINDOW_TICKS の間に
 *      攻撃を受けると発動。実際の持ち替えイベントをサーバーで検出する。
 *
 * ＜パリィ成功時＞
 *   - 受けたダメージをキャンセル
 *   - 攻撃者に受けたダメージ×1.5＋盾の攻撃力の魔法ダメージを反射
 *   - クリットパーティクル＋高音のシールドSE
 */
@Mod.EventBusSubscriber
public class ShieldParryHandler {

    // 実際のFキー持ち替えだけで開始する。ホットバー変更やログインでは開始しない。
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSwapHands(LivingSwapItemsEvent.Hands event) {
        if (event.isCanceled()) return;
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) return;
        if (isShield(event.getItemSwappedToMainHand()) || isShield(event.getItemSwappedToOffHand())) {
            ParryShieldItem.recordSwapParry(player, player.level().getGameTime());
        } else {
            player.getPersistentData().remove(ParryShieldItem.NBT_SWAP_START);
        }
    }

    // ===================================================================
    // パリィ判定（被ダメージ時）
    // ===================================================================
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        long now = player.level().getGameTime();

        // --- 経路A: オフハンドパリィ ---
        ItemStack offhand = player.getItemInHand(InteractionHand.OFF_HAND);
        if (offhand.getItem() instanceof ParryShieldItem && player.isUsingItem()
                && player.getUsedItemHand() == InteractionHand.OFF_HAND) {
            long blockStart = ParryShieldItem.getBlockStartTime(player);
            if (isInParryWindow(now, blockStart)) {
                triggerParry(event, player, InteractionHand.OFF_HAND);
                return;
            }
        }

        // --- 経路B: スワップパリィ（Fキー）---
        ItemStack mainhand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if ((isShield(mainhand) || isShield(offhand))
                && event.getSource().getEntity() instanceof LivingEntity) {
            long swapStart = ParryShieldItem.getSwapStartTime(player);
            if (isInParryWindow(now, swapStart)) {
                triggerParry(event, player, isShield(mainhand) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
            }
        }
    }

    // ===================================================================
    // パリィ実行
    // ===================================================================
    private static void triggerParry(LivingHurtEvent event, Player player, InteractionHand hand) {
        float incoming = event.getAmount();
        // このパリーで盾が壊れても、使った盾の攻撃力を反射に適用する。
        ItemStack shield = player.getItemInHand(hand);
        float reflectedDamage = incoming * 1.5f + ShieldBashHandler.getAttackDamage(shield);
        event.setCanceled(true);

        // キャンセルした攻撃は通常の盾耐久処理を通らないので、ここで消費する。
        if (incoming > 0) {
            shield.hurtAndBreak(1 + (int) Math.floor(incoming), player,
                    entity -> entity.broadcastBreakEvent(hand));
            if (shield.isEmpty()) player.stopUsingItem();
        }

        // 攻撃者へ反射ダメージ
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            attacker.hurt(player.damageSources().magic(), reflectedDamage);
        }

        // エフェクト
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.CRIT,
                player.getX(), player.getY() + 1.0, player.getZ(),
                20, 0.4, 0.4, 0.4, 0.25);
        }

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.5f, 2.0f);
    }

    // ===================================================================
    // ユーティリティ
    // ===================================================================

    private static boolean isShield(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof ShieldItem
                || stack.canPerformAction(ToolActions.SHIELD_BLOCK));
    }

    private static boolean isInParryWindow(long now, long start) {
        return start >= 0 && now >= start && now - start < ParryShieldItem.PARRY_WINDOW_TICKS;
    }
}
