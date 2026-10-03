package the_four_primitives_and_weapons.ai;

import java.util.Set;

/** Species determine tactics; equipment retains vanilla damage, enchantments and hit effects. */
public final class VanillaCombatRules {
    private static final Set<String> SUPPORTED = Set.of("zombie", "husk", "drowned", "skeleton", "stray",
            "wither_skeleton", "spider", "cave_spider", "enderman", "zombified_piglin",
            "vindicator", "ravager", "silverfish", "endermite");
    private VanillaCombatRules() {}
    public static boolean supports(String namespace, String species) {
        return "minecraft".equals(namespace) && SUPPORTED.contains(species);
    }
    public static boolean pounces(String species) {
        return "spider".equals(species) || "cave_spider".equals(species);
    }
    public static EnemyAttackRules.Attack attack(String species, String weapon, int combo) {
        if (pounces(species)) return new EnemyAttackRules.Attack(EnemyAttackRules.Shape.THRUST, 3.1, 0.5, 0, 0, 16, 20);
        if ("ravager".equals(species) || "hoglin".equals(species) || "zoglin".equals(species))
            return new EnemyAttackRules.Attack(EnemyAttackRules.Shape.SWEEP, 3.0, 0, 55, 0, 20, 24);
        if ("silverfish".equals(species) || "endermite".equals(species))
            return new EnemyAttackRules.Attack(EnemyAttackRules.Shape.THRUST, 1.2, 0.3, 0, 0, 8, 12);
        if (weapon != null) return EnemyAttackRules.choose(weapon, combo, false, false);
        if ("vindicator".equals(species) || "piglin_brute".equals(species))
            return new EnemyAttackRules.Attack(EnemyAttackRules.Shape.SMASH, 2.8, 0.6, 0, 0, 18, 22);
        if (Math.floorMod(combo, 3) == 2)
            return new EnemyAttackRules.Attack(EnemyAttackRules.Shape.SWEEP, 2.4, 0, 60, 0, 18, 22);
        return new EnemyAttackRules.Attack(EnemyAttackRules.Shape.THRUST, 2.0, 0.55, 0, 0, 12, 12);
    }
    public static boolean withinShotRange(double distanceSquared) {
        return Double.isFinite(distanceSquared) && distanceSquared <= 16 * 16;
    }
}
