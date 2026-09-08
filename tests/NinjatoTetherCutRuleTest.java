import the_four_primitives_and_weapons.util.NinjatoTetherCutRule;

public final class NinjatoTetherCutRuleTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    public static void main(String[] args) {
        check(NinjatoTetherCutRule.canCut(false, false, 1), "Normal attack cuts string");
        check(NinjatoTetherCutRule.canCut(false, true, 1), "Skill cuts string");
        check(!NinjatoTetherCutRule.canCut(true, false, 100), "High normal damage must not cut chain");
        check(!NinjatoTetherCutRule.canCut(true, true, 19.99F), "Below threshold must not cut chain");
        check(NinjatoTetherCutRule.canCut(true, true, 20), "Exact threshold cuts chain");
        check(NinjatoTetherCutRule.canCut(true, true, 21), "Above threshold cuts chain");
        for (int i = 0; i < 5; i++)
            check(!NinjatoTetherCutRule.canCut(true, true, 10), "Weak hits must not accumulate");
        check(!NinjatoTetherCutRule.canCut(false, false, 0), "Zero damage does not cut");
        check(!NinjatoTetherCutRule.canCut(true, true, Float.NaN), "Invalid damage does not cut");
        check(!NinjatoTetherCutRule.inSkill(), "Initially not a skill");
        NinjatoTetherCutRule.beginSkill();
        try {
            NinjatoTetherCutRule.beginSkill();
            try { check(NinjatoTetherCutRule.inSkill(), "Nested skill recognized"); }
            finally { NinjatoTetherCutRule.endSkill(); }
            check(NinjatoTetherCutRule.inSkill(), "Outer skill preserved");
        } finally { NinjatoTetherCutRule.endSkill(); }
        check(!NinjatoTetherCutRule.inSkill(), "Normal attack after skill must not inherit its context");
        System.out.println("Tether cutting boundary and skill context checks passed");
    }
}
