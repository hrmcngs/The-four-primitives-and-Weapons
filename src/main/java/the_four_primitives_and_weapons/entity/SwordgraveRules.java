package the_four_primitives_and_weapons.entity;

/** Geometry and timing shared by the warning marks and actual impact. */
public final class SwordgraveRules {
    public static final int THRUST = 1, SWEEP = 2, FALL = 3;
    private SwordgraveRules() {}
    public static int windup(int attack, boolean enraged) {
        return (attack == FALL ? 36 : attack == SWEEP ? 30 : 26) - (enraged ? 4 : 0);
    }
    public static int recovery(int attack, boolean enraged) {
        return (attack == THRUST ? 30 : 46) - (enraged ? 6 : 0);
    }
    public static boolean hits(int attack, double forward, double side, double feetHeight) {
        if (attack == THRUST) return feetHeight > -1 && feetHeight < 3 && forward >= 0 && forward <= 7 && Math.abs(side) <= 1;
        double radiusSquared = forward * forward + side * side;
        if (attack == SWEEP) return feetHeight > -1 && feetHeight < 0.85 && radiusSquared >= 1.8 * 1.8 && radiusSquared <= 5.5 * 5.5;
        return feetHeight > -1 && feetHeight < 3 && radiusSquared <= 1.5 * 1.5;
    }
    public static float damageMultiplier(boolean exposed, double facingDot) {
        return exposed ? 1.5f : facingDot > 0.35 ? 0.25f : 0.75f;
    }
}
