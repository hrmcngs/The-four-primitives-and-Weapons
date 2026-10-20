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
        if (age <= 0 || age >= 14) return 0;
        if (age < 3) {
            float t = age / 3f;
            return 1 - (1 - t) * (1 - t) * (1 - t);
        }
        if (age <= 5) return 1;
        float t = (age - 5) / 9f;
        return 1 - t * t * (3 - 2 * t);
    }
    public static float guardForward(float age) {
        if (age <= 0 || age >= 14) return 0;
        float t = Math.max(0, Math.min(1, (age - 1) / 4f));
        return t * t * (3 - 2 * t);
    }
    /** Elliptical route around the torso, rather than a chord through its centre.
     * Model-space units are blocks; both endpoints must already clear the body.
     */
    public static float[] clothAroundBody(float startX, float startZ, float endX, float endZ, float progress, int side) {
        if (progress <= 0) return new float[]{startX, startZ};
        if (progress >= 1) return new float[]{endX, endZ};
        double a = Math.atan2(startX / .42, startZ / .24);
        double b = Math.atan2(endX / .42, endZ / .24);
        // Unwrap across the dominant side. A panel on the far side first travels
        // across the back, then follows the others around the same shoulder.
        if (side < 0 && b > a) b -= Math.PI * 2;
        if (side > 0 && b < a) b += Math.PI * 2;
        double angle = a + (b - a) * progress;
        double r0 = Math.hypot(startX / .42, startZ / .24);
        double r1 = Math.hypot(endX / .42, endZ / .24);
        double radius = Math.max(1.08, r0 + (r1 - r0) * progress);
        return new float[]{(float)(Math.sin(angle) * .42 * radius), (float)(Math.cos(angle) * .24 * radius)};
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
