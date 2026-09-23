package the_four_primitives_and_weapons.skill;

/** Luna beams require a full attack cooldown gauge; mouse hold duration is irrelevant. */
public final class LunaChargeRules {
    private LunaChargeRules() {}
    public static boolean beamEnabled(float attackGauge) {
        return Float.isFinite(attackGauge) && attackGauge == 1.0F;
    }
}
