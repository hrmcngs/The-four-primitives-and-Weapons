import the_four_primitives_and_weapons.skill.PostureRules;

public final class PostureRulesTest {
    public static void main(String[] args) {
        check(PostureRules.decay(60, 100, 140) == 60, "pressure persists during a short opening");
        check(PostureRules.decay(60, 100, 160) == 50, "disengaging recovers pressure");
        check(PostureRules.decay(60, 100, 300) == 0, "long disengagement fully resets pressure");
        check(PostureRules.decay(60, 100, 90) == 0, "world clock reset cannot preserve pressure");
        float heavy = PostureRules.impact("greatsword", 1, 0, 0);
        float light = PostureRules.impact("dagger", 1, 0, 0);
        check(heavy > light, "heavy weapons break posture faster per hit");
        check(PostureRules.impact("greatsword", 1, 1, 0) == 2 * heavy, "full charge rewards commitment");
        check(PostureRules.impact("greatsword", 0.2f, 0, 0) < heavy / 10, "spam cannot replace deliberate hits");
        check(PostureRules.impact("greatsword", 1, 0, 1) == heavy / 2, "resistant enemies still break with effort");
        check(PostureRules.impact(null, 1, 0, 0) > 0, "unregistered swords remain usable");
        check(PostureRules.impact("katana", Float.NaN, 0, 0) == 0, "invalid gauge is rejected");
        check(PostureRules.impact("katana", 1, 0, Double.NaN) == 0, "invalid resistance is rejected");
        check(PostureRules.impact("katana", -1, 0, 0) == 0, "negative gauge is clamped");
        check(PostureRules.impact("katana", 2, 2, -1) == PostureRules.impact("katana", 1, 1, 0), "inputs are bounded");
        int deliberate = (int) Math.ceil(PostureRules.LIMIT / heavy);
        int spam = (int) Math.ceil(PostureRules.LIMIT / PostureRules.impact("greatsword", 0.2f, 0, 0));
        check(deliberate == 4 && spam > 90, "full hits break in four strikes while spam takes many more");
        check(PostureRules.canAccumulate(100, -1, 0), "first impact accepted");
        check(!PostureRules.canAccumulate(103, 100, 0), "multi-hit skills cannot instantly break posture");
        check(PostureRules.canAccumulate(104, 100, 0), "next independent hit accepted");
        check(!PostureRules.canAccumulate(159, 100, 160), "chain stagger protection");
        check(PostureRules.canAccumulate(160, 100, 160), "protection expires at boundary");
        check(!PostureRules.canAccumulate(90, 100, 0), "clock reversal rejected");
        for (int tier = 1; tier <= 10; tier++) {
            check(PostureRules.windupTicks("charge_attack", tier) > PostureRules.windupTicks("attack", tier), "heavy intent remains readable");
            check(PostureRules.windupTicks("attack", tier) >= 6, "elite enemies never attack without warning");
        }
        check(PostureRules.recoveryTicks("charge_attack") > PostureRules.recoveryTicks("attack"), "heavy enemy attacks leave larger openings");
        System.out.println("PostureRulesTest passed");
    }
    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }
}
