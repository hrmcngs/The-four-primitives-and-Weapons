package the_four_primitives_and_weapons.client;

import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import the_four_primitives_and_weapons.skill.HandSwingTiming;

/** Separate timelines because vanilla only stores one swingingArm per entity. */
public final class DualWieldSwingAnimation {
    private static final Map<Player, Swing[]> SWINGS = new WeakHashMap<>();
    private record Swing(long start, int duration, Item item) {}
    private DualWieldSwingAnimation() {}

    public static void receive(int entityId, boolean offHand, int duration) {
        var level = Minecraft.getInstance().level;
        if (level == null || !(level.getEntity(entityId) instanceof Player player)) return;
        var hand = offHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        Swing[] swings = SWINGS.computeIfAbsent(player, ignored -> new Swing[2]);
        swings[offHand ? 1 : 0] = new Swing(level.getGameTime(), Math.max(1, Math.min(40, duration)),
            player.getItemInHand(hand).getItem());
        // Luna fires on release rather than vanilla's click path. Restore the local HUD cooldown.
        // An off-hand follow-up must not restart the main-hand gauge a second time.
        if (!offHand && player == Minecraft.getInstance().player) player.resetAttackStrengthTicker();
    }

    /** -1 means leave vanilla rendering alone. */
    public static float progress(Player player, InteractionHand hand, float partial) {
        Swing[] swings = SWINGS.get(player);
        if (swings == null) return -1;
        int index = hand == InteractionHand.OFF_HAND ? 1 : 0;
        Swing swing = swings[index];
        if (swing == null) return -1;
        float progress = HandSwingTiming.progress(player.level().getGameTime(), swing.start, partial, swing.duration);
        if (!player.isAlive() || player.getItemInHand(hand).isEmpty()
                || player.getItemInHand(hand).getItem() != swing.item || progress < 0) {
            swings[index] = null;
            if (swings[0] == null && swings[1] == null) SWINGS.remove(player);
            return -1;
        }
        return progress;
    }
}
