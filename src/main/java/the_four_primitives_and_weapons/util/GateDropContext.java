package the_four_primitives_and_weapons.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/** Shared Gate/Luna drop provenance. No tick-based guesses or retained player references. */
public final class GateDropContext {
    public enum Reason {
        DROP_KEY, SNEAK_DROP_KEY, INVENTORY, MENU_CLEANUP, DISCONNECT, DEATH,
        INVENTORY_OVERFLOW, CONTAINER, DISPENSER, BLOCK, COMPANION_RETURN, PLAYER_TOSS_OTHER, UNKNOWN
    }

    private static final ThreadLocal<Map<UUID, Reason>> ACTIVE = new ThreadLocal<>();

    private GateDropContext() {}

    public static Reason current(UUID playerId) {
        Map<UUID, Reason> active = ACTIVE.get();
        return active == null ? Reason.UNKNOWN : active.getOrDefault(playerId, Reason.UNKNOWN);
    }

    public static <T> T during(UUID playerId, Reason reason, Supplier<T> action) {
        Map<UUID, Reason> active = ACTIVE.get();
        if (active == null) {
            active = new HashMap<>();
            ACTIVE.set(active);
        }
        Reason previous = active.put(playerId, reason);
        try {
            return action.get();
        } finally {
            if (previous == null) active.remove(playerId);
            else active.put(playerId, previous);
            if (active.isEmpty()) ACTIVE.remove();
        }
    }

    public static boolean canSummon(Reason reason, boolean alive, boolean connected, boolean spectator) {
        return reason == Reason.DROP_KEY && alive && connected && !spectator;
    }
}
