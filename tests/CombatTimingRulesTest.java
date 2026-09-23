import the_four_primitives_and_weapons.skill.CombatTimingRules;

public class CombatTimingRulesTest {
    public static void main(String[] args) {
        check(CombatTimingRules.inWindow(100, 100, 4), "first parry tick");
        check(CombatTimingRules.inWindow(103, 100, 4), "last parry tick");
        check(!CombatTimingRules.inWindow(104, 100, 4), "late input");
        check(!CombatTimingRules.inWindow(99, 100, 4), "world clock reset");
        check(!CombatTimingRules.inWindow(0, -1, 4), "missing input");
        check(CombatTimingRules.inWindow(111, 100, CombatTimingRules.PARRY_RETRY), "retry still locked");
        check(!CombatTimingRules.inWindow(112, 100, CombatTimingRules.PARRY_RETRY), "retry ready");
        check(CombatTimingRules.facingAttack(0, 1, 0, 5), "front");
        check(CombatTimingRules.facingAttack(0, 1, 1, 1), "front diagonal");
        check(!CombatTimingRules.facingAttack(0, 1, 5, 0), "flank");
        check(!CombatTimingRules.facingAttack(0, 1, 0, -5), "rear");
        check(!CombatTimingRules.facingAttack(0, 1, 0, 0), "no direction");
        check(!CombatTimingRules.facingAttack(0, 0, 0, 5), "vertical look");
        for (double speed : new double[] {1, 1.6, 2.4, 4}) {
            double interval = 20 / speed;
            int normal = CombatTimingRules.recoveryTicks(interval, false);
            check(normal < interval, "ordinary motion remains available below full gauge");
            check(CombatTimingRules.recoveryTicks(interval, true) > normal, "charge commitment");
        }
        check(CombatTimingRules.recoveryTicks(0, false) == 2, "same tick spam limit");
        check(CombatTimingRules.recoveryTicks(10000, true) == 24, "slow weapon recovery cap");
        check(CombatTimingRules.recoveryTicks(Double.NaN, false) > 0, "invalid interval");
        System.out.println("CombatTimingRulesTest passed");
    }
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
