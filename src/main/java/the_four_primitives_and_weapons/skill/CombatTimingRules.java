package the_four_primitives_and_weapons.skill;

/** Server-tick timings; player experience levels never widen these windows. */
public final class CombatTimingRules {
    public static final int PARRY_WINDOW = 4;
    public static final int PARRY_RETRY = 12;
    private CombatTimingRules() {}

    public static boolean inWindow(long now, long start, int duration) {
        return start >= 0 && now >= start && now - start < duration;
    }

    public static int recoveryTicks(double attackInterval, boolean charged) {
        if (!Double.isFinite(attackInterval)) attackInterval = 20;
        return (int) Math.ceil(Math.max(charged ? 6 : 2,
                Math.min(charged ? 24 : 10, attackInterval * (charged ? 0.9 : 0.4))));
    }

    public static boolean facingAttack(double lookX, double lookZ, double sourceX, double sourceZ) {
        double length = Math.sqrt((lookX * lookX + lookZ * lookZ) * (sourceX * sourceX + sourceZ * sourceZ));
        return length > 1.0e-8 && (lookX * sourceX + lookZ * sourceZ) / length >= 0.5;
    }
}
