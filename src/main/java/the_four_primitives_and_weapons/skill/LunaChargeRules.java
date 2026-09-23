package the_four_primitives_and_weapons.skill;

/** The client and server agree on the minimum charge needed for Luna's beam. */
public final class LunaChargeRules {
    public static final int MIN_TICKS = 20;
    public static final float MIN_PERCENT = MIN_TICKS / 60.0F;
    private LunaChargeRules() {}
    public static boolean beamEnabled(float chargePercent) {
        return Float.isFinite(chargePercent) && chargePercent >= MIN_PERCENT && chargePercent <= 1.0F;
    }
}
