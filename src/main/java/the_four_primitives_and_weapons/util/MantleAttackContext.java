package the_four_primitives_and_weapons.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** Exposes the equipped mantle to existing hit hooks without moving inventory items. */
public final class MantleAttackContext {
    private record Hit(Player player, ItemStack mantle) {}
    private static final ThreadLocal<Hit> ACTIVE = new ThreadLocal<>();
    private MantleAttackContext() {}
    public static ItemStack weapon(LivingEntity entity) {
        Hit hit = ACTIVE.get();
        return hit != null && hit.player == entity ? hit.mantle : ItemStack.EMPTY;
    }
    public static void run(Player player, ItemStack mantle, Runnable action) {
        Hit previous = ACTIVE.get();
        ACTIVE.set(new Hit(player, mantle));
        try { action.run(); }
        finally {
            if (previous == null) ACTIVE.remove(); else ACTIVE.set(previous);
        }
    }
}
