package the_four_primitives_and_weapons.skill;

/** Conservative rules: only matching, stationary one-handed attacks overlap. */
public final class DualWieldRules {
    private DualWieldRules() {}

    public static boolean simultaneous(String main, String off, boolean charged) {
        if (charged || main == null || off == null) return false;
        if (main.equals("thrust") && off.equals("thrust")) return true;
        return diagonal(main) && diagonal(off) || "horizontal_slash".equals(main) && main.equals(off);
    }

    private static boolean diagonal(String motion) {
        return "upper_left_slash".equals(motion) || "upper_right_slash".equals(motion);
    }

    public static int recoveryTicks(String motion, double attackInterval) {
        int recovery = (int) Math.ceil(Math.max(6, Math.min(40, attackInterval)));
        if ("spin_slash".equals(motion)) return Math.max(17, recovery);
        if ("thrust_combo".equals(motion)) return Math.max(36, recovery);
        if ("slam_down".equals(motion)) return Math.max(6, recovery);
        return recovery;
    }

    public static double mirroredTilt(double tilt, boolean mirrored) {
        return mirrored ? -tilt : tilt;
    }

    public static int side(boolean leftDominant, boolean offHand) {
        return leftDominant != offHand ? -1 : 1;
    }
}
