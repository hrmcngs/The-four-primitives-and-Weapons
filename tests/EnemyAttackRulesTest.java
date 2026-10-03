import the_four_primitives_and_weapons.ai.EnemyAttackRules;

public class EnemyAttackRulesTest {
    public static void main(String[] args) {
        var thrust = EnemyAttackRules.choose("trident", 0, false, false);
        check(EnemyAttackRules.hits(thrust, 3.5, 0, 0.3), "long thrust reaches its announced lane");
        check(!EnemyAttackRules.hits(thrust, 3, 1, 0.3), "sidestepping avoids thrust");
        check(!EnemyAttackRules.hits(thrust, -1, 0, 0.3), "thrust cannot hit behind attacker");
        var slash = EnemyAttackRules.choose("katana", 0, false, false);
        check(EnemyAttackRules.hits(slash, 2, 1, 0.3), "slash covers front arc");
        check(!EnemyAttackRules.hits(slash, 4, 0, 0.3), "retreating avoids slash");
        check(!EnemyAttackRules.hits(slash, 0, 2, 0.3), "flank outside slash arc");
        var smash = EnemyAttackRules.choose("greatsword", 0, false, false);
        check(!EnemyAttackRules.hits(smash, 2, 1.2, 0.3), "lateral dodge avoids overhead smash");
        check(smash.windup() > slash.windup() && smash.recovery() > slash.recovery(), "heavy attacks expose commitment");
        check(EnemyAttackRules.choose("katana", 1, false, false).shape() != slash.shape(), "katana combo changes defense requirement");
        check(EnemyAttackRules.choose("dagger", 0, false, false).reach() < slash.reach(), "daggers require close range");
        check(EnemyAttackRules.choose("katana", 0, true, false).recovery() > slash.recovery(), "charged whiffs are punishable");
        check(!EnemyAttackRules.hits(slash, Double.NaN, 0, 0.3), "invalid coordinates rejected");
        // A locked attack is tested against the old aim even when its target changes position.
        check(!EnemyAttackRules.hits(thrust, 2, 2, 0.3), "moving out of committed aim causes a miss");
        System.out.println("EnemyAttackRulesTest passed");
    }
    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }
}
