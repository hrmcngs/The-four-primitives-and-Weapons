import the_four_primitives_and_weapons.skill.LunaChargeRules;
import the_four_primitives_and_weapons.skill.ChargeReleaseRules;

public final class LunaChargeRulesTest {
    public static void main(String[] args) {
        for (int ticks = 0; ticks <= 60; ticks++) {
            boolean expected = ticks >= 20;
            check(LunaChargeRules.beamEnabled(ticks / 60.0F) == expected, "beam threshold tick " + ticks);
            check((ChargeReleaseRules.action(true, ticks, LunaChargeRules.MIN_TICKS)
                == ChargeReleaseRules.Action.CHARGED) == expected, "input and beam gates agree " + ticks);
        }
        check(!LunaChargeRules.beamEnabled(Math.nextDown(LunaChargeRules.MIN_PERCENT)), "just below threshold");
        for (float invalid : new float[]{-1, Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 2})
            check(!LunaChargeRules.beamEnabled(invalid), "invalid charge rejected");
        System.out.println("LunaChargeRulesTest: passed");
    }
    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }
}
