import the_four_primitives_and_weapons.skill.ChargeReleaseRules;
import static the_four_primitives_and_weapons.skill.ChargeReleaseRules.Action.*;

public final class ChargeReleaseRulesTest {
    public static void main(String[] args) {
        for (int ticks : new int[]{0, 1, 10, 19}) {
            check(ChargeReleaseRules.action(true, ticks, 20) == NORMAL, "short Luna hold: " + ticks);
            check(ChargeReleaseRules.action(false, ticks, 20) == NONE, "other weapons already attacked on press: " + ticks);
        }
        for (int ticks : new int[]{20, 21, 59, 60, 200}) {
            check(ChargeReleaseRules.action(true, ticks, 20) == CHARGED, "Luna threshold: " + ticks);
            check(ChargeReleaseRules.action(false, ticks, 20) == CHARGED, "other weapons retain charged release: " + ticks);
        }
        System.out.println("ChargeReleaseRulesTest: passed");
    }
    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }
}
