package the_four_primitives_and_weapons.ai;

/** Weapon-specific, committed attack shapes. Coordinates are relative to the announced facing. */
public final class EnemyAttackRules {
    public enum Shape { SWEEP, THRUST, SMASH }
    public record Attack(Shape shape, double reach, double halfWidth, double halfAngle,
                         float damage, int windup, int recovery) {}
    private EnemyAttackRules() {}

    public static Attack choose(String weapon, int combo, boolean charged, boolean dash) {
        if (dash) return new Attack(Shape.THRUST, 4.8, 0.4, 0, 13, 14, 20);
        if (charged) {
            if ("rapier".equals(weapon) || "trident".equals(weapon))
                return new Attack(Shape.THRUST, 4.6, 0.35, 0, 14, 18, 22);
            return new Attack(Shape.SMASH, 3.4, 0.65, 0, 16, 22, 24);
        }
        return switch (weapon == null ? "" : weapon) {
            case "rapier", "trident" -> new Attack(Shape.THRUST, 4.0, 0.3, 0, 8, 12, 12);
            case "greatsword", "nata" -> Math.floorMod(combo, 2) == 0
                    ? new Attack(Shape.SMASH, 3.2, 0.65, 0, 12, 18, 20)
                    : new Attack(Shape.SWEEP, 3.2, 0, 70, 10, 16, 18);
            case "dagger", "small_sword" -> new Attack(Shape.SWEEP, 1.8, 0, 40, 6, 6, 8);
            case "katana" -> switch (Math.floorMod(combo, 3)) {
                case 0 -> new Attack(Shape.SWEEP, 2.8, 0, 45, 8, 10, 10);
                case 1 -> new Attack(Shape.THRUST, 3.4, 0.35, 0, 9, 12, 12);
                default -> new Attack(Shape.SWEEP, 3.0, 0, 65, 11, 16, 18);
            };
            default -> new Attack(Shape.SWEEP, 2.6, 0, 50, 8, 10, 12);
        };
    }

    public static boolean hits(Attack attack, double forward, double side, double radius) {
        if (!Double.isFinite(forward) || !Double.isFinite(side) || !Double.isFinite(radius) || radius < 0) return false;
        if (forward < -radius) return false;
        if (attack.shape != Shape.SWEEP)
            return forward <= attack.reach + radius && Math.abs(side) <= attack.halfWidth + radius;
        double distance = Math.hypot(forward, side);
        if (distance > attack.reach + radius) return false;
        if (distance <= radius) return true;
        double tolerance = Math.toDegrees(Math.asin(Math.min(1, radius / distance)));
        return Math.abs(Math.toDegrees(Math.atan2(side, forward))) <= attack.halfAngle + tolerance;
    }
}
