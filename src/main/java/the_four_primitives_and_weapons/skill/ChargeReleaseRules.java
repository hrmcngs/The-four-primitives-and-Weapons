package the_four_primitives_and_weapons.skill;

/** What to emit when a held attack is released; short Luna holds become normal skills. */
public final class ChargeReleaseRules {
    public enum Action { NONE, NORMAL, CHARGED }
    private ChargeReleaseRules() {}
    public static Action action(boolean luna, int heldTicks, int requiredTicks) {
        if (heldTicks >= requiredTicks) return Action.CHARGED;
        return luna ? Action.NORMAL : Action.NONE;
    }
}
