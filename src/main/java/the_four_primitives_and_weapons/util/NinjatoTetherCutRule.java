package the_four_primitives_and_weapons.util;

public final class NinjatoTetherCutRule {
    private static final ThreadLocal<Integer> SKILL_DEPTH = ThreadLocal.withInitial(() -> 0);
    private NinjatoTetherCutRule() {}
    public static void beginSkill() { SKILL_DEPTH.set(SKILL_DEPTH.get() + 1); }
    public static void endSkill() {
        int depth = SKILL_DEPTH.get() - 1;
        if (depth <= 0) SKILL_DEPTH.remove(); else SKILL_DEPTH.set(depth);
    }
    public static boolean inSkill() { return SKILL_DEPTH.get() > 0; }
    public static boolean canCut(boolean chain, boolean skill, float damage) {
        return Float.isFinite(damage) && damage > 0 && (!chain || (skill && damage >= 20.0F));
    }
}
