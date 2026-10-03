package the_four_primitives_and_weapons.skill;

/** Pure balance rules shared by combat and standalone regression tests. */
public final class PostureRules {
    public static final float LIMIT = 100;
    public static final int STAGGER_TICKS = 12;
    public static final int BREAK_IMMUNITY_TICKS = 60;
    public static final int HIT_INTERVAL = 4;
    private PostureRules() {}

    public static boolean canAccumulate(long now, long lastHit, long immuneUntil) {
        return now >= immuneUntil && (lastHit < 0 || now >= lastHit && now - lastHit >= HIT_INTERVAL);
    }

    public static int windupTicks(String action, int tier) {
        boolean heavy = !"attack".equals(action);
        return Math.max(heavy ? 10 : 6, (heavy ? 16 : 10) - Math.max(0, tier - 1));
    }

    public static int recoveryTicks(String action) {
        return "attack".equals(action) ? 10 : 18;
    }

    public static float decay(float pressure, long lastHit, long now) {
        if (!Float.isFinite(pressure) || now < lastHit) return 0;
        long idle = Math.max(0, now - lastHit - 40);
        return Math.max(0, Math.min(LIMIT, pressure) - idle * 0.5f);
    }

    public static float impact(String weaponType, float gauge, float charge, double resistance) {
        if (!Float.isFinite(gauge) || !Float.isFinite(charge) || !Double.isFinite(resistance)) return 0;
        float base = switch (weaponType == null ? "" : weaponType) {
            case "greatsword" -> 26;
            case "nata" -> 22;
            case "dagger", "small_sword", "rapier" -> 12;
            case "trident" -> 18;
            default -> 16;
        };
        gauge = Math.max(0, Math.min(1, gauge));
        charge = Math.max(0, Math.min(1, charge));
        // Rapid, uncharged clicks cannot build pressure as efficiently as deliberate hits.
        return base * gauge * gauge * (1 + charge) * (float) (1 - Math.max(0, Math.min(1, resistance)) * 0.5);
    }
}
