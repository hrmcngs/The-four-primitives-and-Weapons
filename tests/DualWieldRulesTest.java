import the_four_primitives_and_weapons.skill.DualWieldRules;

public final class DualWieldRulesTest {
    public static void main(String[] args) {
        check(DualWieldRules.simultaneous("upper_left_slash", "upper_left_slash", false), "matching diagonal cuts");
        check(DualWieldRules.simultaneous("upper_left_slash", "upper_right_slash", false), "diagonal pair");
        check(DualWieldRules.simultaneous("thrust", "thrust", false), "paired thrusts");
        check(DualWieldRules.simultaneous("horizontal_slash", "horizontal_slash", false), "paired sweeps");
        String[] motions = {"thrust", "upper_left_slash", "upper_right_slash", "horizontal_slash", "slam_down", "spin_slash", "thrust_combo", "addon_skill"};
        for (String a : motions) for (String b : motions) {
            check(DualWieldRules.simultaneous(a, b, false) == DualWieldRules.simultaneous(b, a, false), "symmetric compatibility");
            check(!DualWieldRules.simultaneous(a, b, true), "charged motions must finish first");
            if (a.equals("slam_down") || a.equals("spin_slash") || a.equals("thrust_combo") || a.equals("addon_skill"))
                check(!DualWieldRules.simultaneous(a, b, false), "whole-body, sustained and unknown skills are sequential");
        }
        check(!DualWieldRules.simultaneous("slam_down", "thrust", false), "vertical cut followed by thrust");
        check(!DualWieldRules.simultaneous("thrust", "horizontal_slash", false), "mixed thrust and sweep");
        check(!DualWieldRules.simultaneous(null, "thrust", false), "missing skill");
        for (boolean left : new boolean[]{false, true}) {
            check(DualWieldRules.side(left, false) == -DualWieldRules.side(left, true), "opposite origins for either dominant hand");
        }
        check(DualWieldRules.side(false, false) == 1, "right dominant");
        check(DualWieldRules.side(true, false) == -1, "left dominant");
        check(DualWieldRules.mirroredTilt(-0.5, true) == 0.5, "mirror diagonal geometry");
        check(DualWieldRules.mirroredTilt(-0.5, false) == -0.5, "preserve normal geometry");
        check(DualWieldRules.recoveryTicks("slam_down", 2) > 5, "wait through slam animation");
        check(DualWieldRules.recoveryTicks("spin_slash", 2) > 16, "wait through spin");
        check(DualWieldRules.recoveryTicks("thrust_combo", 2) >= 36, "wait through combo");
        check(DualWieldRules.recoveryTicks("thrust", 24) == 24, "slow weapon recovery");
        System.out.println("DualWieldRulesTest: passed");
    }
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
