package the_four_primitives_and_weapons.item;

import net.minecraft.nbt.CompoundTag;
import the_four_primitives_and_weapons.event.ShieldBashHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;


/**
 * パリィシールド
 *
 * ＜操作体系＞
 *
 *   【メインハンド — シールドバッシュ（チャージ式）】
 *     右クリック長押しでチャージ → 離した瞬間バッシュ発動。
 *     チャージ量でダメージ・ノックバック・射程がすべてスケール。
 *     チャージ状況はクロスヘア下のバーで確認（ShieldChargeHudRenderer）。
 *
 *       ダメージ    : 2.0 〜 8.0
 *       ノックバック: 0.5 〜 3.0
 *       射程範囲    : 2.0 〜 4.5 ブロック
 *       最低チャージ: MIN_CHARGE_TICKS tick（未満で離すと不発）
 *       フルチャージ: MAX_CHARGE_TICKS tick
 *
 *   【オフハンド — パリィ】
 *     右クリック直後 PARRY_WINDOW_TICKS tick 以内に攻撃を受けるとパリィ発動。
 *
 *   【Fキー（持ちかえ）— スワップパリィ】
 *     どちらかの手へ持ち替えた瞬間から PARRY_WINDOW_TICKS tick の間パリィ可能。
 */
public class ParryShieldItem extends ShieldItem {

    public static final String NBT_BLOCK_START = "ParryShieldBlockStart";
    public static final String NBT_SWAP_START  = "ParryShieldSwapStart";

    public static final int PARRY_WINDOW_TICKS = the_four_primitives_and_weapons.skill.CombatTimingRules.PARRY_WINDOW;
    private static final String NBT_ATTEMPT = "ParryAttemptTime";
    private static final String NBT_DIMENSION = "ParryAttemptDimension";
    public static final int MIN_CHARGE_TICKS   = ShieldBashHandler.MIN_CHARGE_TICKS;
    public static final int MAX_CHARGE_TICKS   = ShieldBashHandler.MAX_CHARGE_TICKS;
    public static final int USE_DURATION       = 72000;

    public ParryShieldItem() {
        super(new Item.Properties().durability(512).rarity(Rarity.UNCOMMON));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }

    // =========================================================
    // 右クリック押し込み
    // =========================================================
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack shield = player.getItemInHand(hand);

        if (!level.isClientSide && hand == InteractionHand.OFF_HAND) {
            // オフハンド: パリィウィンドウ開始
            if (beginParryAttempt(player, level.getGameTime()))
                player.getPersistentData().putLong(NBT_BLOCK_START, level.getGameTime());
        }
        // メインハンド: 共通の ShieldBashHandler がチャージとリリースを処理

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(shield);
    }

    // =========================================================
    // ShieldParryHandler / ShieldChargeHudRenderer から参照
    // =========================================================

    public static long getBlockStartTime(Player player) {
        CompoundTag d = player.getPersistentData();
        return d.contains(NBT_BLOCK_START) ? d.getLong(NBT_BLOCK_START) : -1L;
    }

    public static long getSwapStartTime(Player player) {
        CompoundTag d = player.getPersistentData();
        return d.contains(NBT_SWAP_START) ? d.getLong(NBT_SWAP_START) : -1L;
    }

    public static void recordSwapParry(Player player, long gameTick) {
        if (beginParryAttempt(player, gameTick)) player.getPersistentData().putLong(NBT_SWAP_START, gameTick);
    }

    private static boolean beginParryAttempt(Player player, long now) {
        CompoundTag data = player.getPersistentData();
        String dimension = player.level().dimension().location().toString();
        if (data.contains(NBT_ATTEMPT) && dimension.equals(data.getString(NBT_DIMENSION))
                && the_four_primitives_and_weapons.skill.CombatTimingRules.inWindow(now, data.getLong(NBT_ATTEMPT),
                    the_four_primitives_and_weapons.skill.CombatTimingRules.PARRY_RETRY)) return false;
        consumeParry(player);
        data.putLong(NBT_ATTEMPT, now);
        data.putString(NBT_DIMENSION, dimension);
        return true;
    }

    public static void consumeParry(Player player) {
        player.getPersistentData().remove(NBT_BLOCK_START);
        player.getPersistentData().remove(NBT_SWAP_START);
    }
}
