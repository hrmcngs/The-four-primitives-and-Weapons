import the_four_primitives_and_weapons.ai.VanillaCombatRules;
import the_four_primitives_and_weapons.ai.EnemyAttackRules;

public class VanillaCombatRulesTest {
    public static void main(String[] args) {
        check(VanillaCombatRules.supports("minecraft", "husk"), "zombie variants supported");
        check(VanillaCombatRules.supports("minecraft", "stray"), "skeleton variants supported");
        check(!VanillaCombatRules.supports("other_mod", "zombie"), "other mods retain their AI");
        check(!VanillaCombatRules.supports("minecraft", "villager"), "passive mobs unaffected");
        var claw = VanillaCombatRules.attack("zombie", null, 0);
        var finisher = VanillaCombatRules.attack("zombie", null, 2);
        check(finisher.windup() > claw.windup() && finisher.recovery() > claw.recovery(), "zombie finisher exposes a longer opening");
        var spider = VanillaCombatRules.attack("spider", null, 0);
        check(VanillaCombatRules.pounces("cave_spider"), "cave spiders pounce too");
        check(!EnemyAttackRules.hits(spider, 2, 1.4, 0.3), "sidestep avoids spider lunge");
        check(VanillaCombatRules.attack("vindicator", null, 0).shape() == EnemyAttackRules.Shape.SMASH, "axe enemy uses overhead attack");
        check(VanillaCombatRules.withinShotRange(256), "maximum bow engagement range");
        check(!VanillaCombatRules.withinShotRange(257), "far skeleton must approach");
        check(!VanillaCombatRules.withinShotRange(Double.NaN), "invalid range rejected");
        System.out.println("VanillaCombatRulesTest passed");
    }
    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }
}
