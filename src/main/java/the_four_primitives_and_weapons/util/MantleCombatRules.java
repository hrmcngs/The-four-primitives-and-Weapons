package the_four_primitives_and_weapons.util;

/** Shared timing and empty-hand rules; independent of client rendering and server entities. */
public final class MantleCombatRules {
    public static final int THRUST_HIT_TICK = 8;
    public static final int THRUST_TICKS = 20;
    public static final int GUARD_TICKS = 60;
    public enum DamageKind { MELEE, PROJECTILE, POTION, MAGIC, ENVIRONMENT, BYPASS }
    private MantleCombatRules() {}
    public static boolean guards(DamageKind kind) {
        return kind == DamageKind.MELEE || kind == DamageKind.PROJECTILE;
    }
    public static float deflection(float elapsed) {
        return elapsed < 0 || elapsed >= 10 ? 0 : (float)Math.sin(elapsed * Math.PI / 10);
    }
    /** One gesture: lift sideways, pass diagonally forward, sweep back, release. */
    public static float guardGesture(float age) {
        if (age <= 0 || age >= 24) return 0;
        float phase = age / 24f;
        return (float)Math.sin(Math.PI * phase) * (float)Math.sin(Math.PI * phase);
    }
    public static float guardForward(float age) {
        return age <= 0 || age >= 24 ? 0 : (float)Math.sin(Math.PI * age / 24f);
    }
    public static float guardWave(float age) {
        return (float)Math.sin(age * .22f);
    }
    public static int freeHand(boolean mainEmpty, boolean offEmpty) {
        return mainEmpty ? 0 : offEmpty ? 1 : -1;
    }
    /** Animation packets carry elapsed server ticks, never a server clock timestamp. */
    public static long localStart(long localNow, long elapsedTicks, int duration) {
        return localNow - Math.max(0, Math.min(elapsedTicks, Math.max(0, duration)));
    }
    public static boolean shouldStrike(long age, int duration, boolean struck) {
        return !struck && age >= THRUST_HIT_TICK && age < duration;
    }
    public static float envelope(float age, int duration) {
        return Math.max(0, Math.min(1, Math.min(age / 4f, (duration - age) / 4f)));
    }
    public static float thrustExtension(float age) {
        return Math.max(0, 1 - Math.abs(age - THRUST_HIT_TICK) / 4f);
    }
}
