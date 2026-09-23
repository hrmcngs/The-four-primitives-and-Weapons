import the_four_primitives_and_weapons.skill.LunaChargeRules;

public final class LunaChargeRulesTest {
    public static void main(String[] args) {
        for (float gauge : new float[]{0, 0.2F, 1.0F / 3, 0.5F, 0.99F, Math.nextDown(1.0F)})
            check(!LunaChargeRules.beamEnabled(gauge), "partial attack gauge must not fire: " + gauge);
        check(LunaChargeRules.beamEnabled(1), "full gauge fires without holding the mouse");
        // Readiness follows weapon attack speed, not a fixed 20-tick held-input threshold.
        for (double speed : new double[]{1.0, 1.6, 2.4, 4.0}) {
            double interval = 20.0 / speed;
            int readyTick = (int) Math.ceil(interval);
            check(!LunaChargeRules.beamEnabled((float) ((readyTick - 1) / interval)), "not yet recovered: " + speed);
            check(LunaChargeRules.beamEnabled((float) Math.min(1, readyTick / interval)), "recovered: " + speed);
        }
        for (float invalid : new float[]{-1, Float.NaN, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 2})
            check(!LunaChargeRules.beamEnabled(invalid), "invalid gauge rejected");
        System.out.println("LunaChargeRulesTest: passed");
    }
    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }
}
