import the_four_primitives_and_weapons.entity.SwordgraveRules;
public class SwordgraveRulesTest {
    public static void main(String[] args) {
        check(SwordgraveRules.hits(1, 6, 0, 0), "thrust reaches marked lane");
        check(!SwordgraveRules.hits(1, 6, 1.1, 0), "sidestep evades thrust");
        check(!SwordgraveRules.hits(1, -1, 0, 0), "rear is safe from thrust");
        check(!SwordgraveRules.hits(1, 7.1, 0, 0), "beyond marked range");
        check(SwordgraveRules.hits(2, 3, 0, 0), "sweep hits ring");
        check(!SwordgraveRules.hits(2, 1, 0, 0), "sweep inner safe zone");
        check(!SwordgraveRules.hits(2, 3, 0, 1), "timed jump evades sweep");
        check(!SwordgraveRules.hits(2, 6, 0, 0), "sweep outer safe zone");
        check(SwordgraveRules.hits(3, 0, 0, 0), "fall hits marked spot");
        check(!SwordgraveRules.hits(3, 1.6, 0, 0), "leave circle evades fall");
        check(SwordgraveRules.damageMultiplier(false, 1) < SwordgraveRules.damageMultiplier(false, -1), "flank advantage");
        check(SwordgraveRules.damageMultiplier(true, 1) == 1.5f, "recovery is vulnerable from front too");
        for (int attack = 1; attack <= 3; attack++) {
            check(SwordgraveRules.windup(attack, true) >= 22, "phase two remains readable");
            check(SwordgraveRules.recovery(attack, true) >= 24, "phase two retains punish window");
        }
        System.out.println("SwordgraveRulesTest passed (18 checks)");
    }
    private static void check(boolean condition, String name) { if (!condition) throw new AssertionError(name); }
}
