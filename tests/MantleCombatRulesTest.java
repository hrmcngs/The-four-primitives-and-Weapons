import the_four_primitives_and_weapons.util.MantleCombatRules;

public class MantleCombatRulesTest {
    private static void check(boolean value, String message) {
        if (!value) throw new AssertionError(message);
    }
    public static void main(String[] args) {
        // Regression: every intermediate position must go around the body,
        // including the return stroke and the left-handed mirror.
        for (int side : new int[]{-1, 1}) {
            for (int tick = 0; tick <= 24; tick++) {
                float progress = MantleCombatRules.guardGesture(tick);
                for (int panel = -3; panel <= 3; panel++) {
                    float[] p = MantleCombatRules.clothAroundBody(panel * .14f, .26f,
                        side * .6f, -.4f, progress, side);
                    float clearance = p[0]*p[0]/(.42f*.42f) + p[1]*p[1]/(.24f*.24f);
                    check(clearance >= .999f, "cloth crossed the torso during guard/return");
                }
            }
            float[] mid = MantleCombatRules.clothAroundBody(0, .26f, side*.6f, -.4f, .5f, side);
            check(mid[0] * side > .42f, "cape must pass the dominant side");
        }
        check(MantleCombatRules.guardGesture(14) == 0, "gesture returns to the back");
        check(MantleCombatRules.guardGesture(3) == 1, "parry reaches the side in three ticks");
        check(MantleCombatRules.guardGesture(1) > .65f, "parry starts with momentum");
        check(MantleCombatRules.guardGesture(10) > 0, "return has a short follow-through");
        check(MantleCombatRules.freeHand(true, true) == 0, "both empty chooses main hand");
        check(MantleCombatRules.freeHand(false, true) == 1, "main item wraps off hand");
        check(MantleCombatRules.freeHand(true, false) == 0, "off-hand item wraps main hand");
        check(MantleCombatRules.freeHand(false, false) == -1, "both items must forbid thrust");
        check(MantleCombatRules.thrustExtension(0) == 0, "no instant thrust before winding");
        check(MantleCombatRules.thrustExtension(MantleCombatRules.THRUST_HIT_TICK) == 1, "hit matches full visual extension");
        check(MantleCombatRules.thrustExtension(12) == 0, "blade retracts after hit");
        check(MantleCombatRules.envelope(0, 60) == 0, "starts at resting shape");
        check(MantleCombatRules.envelope(8, 60) == 1, "guard remains active for its duration");
        check(MantleCombatRules.envelope(60, 60) == 0, "returns to rest after guard");
        check(MantleCombatRules.envelope(90, 60) == 0, "no negative scale after expiry");
        // Server time may be ahead or behind the client's world clock. Reception must
        // always yield a playable local animation rather than discarding a future start.
        check(MantleCombatRules.localStart(100, 0, 20) == 100, "fresh action uses local reception clock");
        check(MantleCombatRules.localStart(100, 8, 20) == 92, "late observer resumes elapsed pose");
        check(MantleCombatRules.localStart(5, 8, 20) == -3, "elapsed pose works even on a younger client clock");
        check(MantleCombatRules.localStart(100, -8, 20) == 100, "negative elapsed does not create a future start");
        check(MantleCombatRules.localStart(100, 90, 20) == 80, "expired poses stay expired");
        check(MantleCombatRules.envelope(100 - MantleCombatRules.localStart(100, 4, 60), 60) == 1,
            "guard pose is visible at its transferred elapsed time");
        check(!MantleCombatRules.shouldStrike(7, 20, false), "thrust waits for wind-up");
        check(MantleCombatRules.shouldStrike(9, 20, false), "thrust still hits if exact hit tick was skipped");
        check(!MantleCombatRules.shouldStrike(9, 20, true), "strike is never repeated");
        check(!MantleCombatRules.shouldStrike(20, 20, false), "expired thrust cannot deal damage");
        check(MantleCombatRules.guards(MantleCombatRules.DamageKind.MELEE), "melee hit is rejected before its on-hit debuff");
        check(MantleCombatRules.guards(MantleCombatRules.DamageKind.PROJECTILE), "tipped arrow hit is rejected before poison application");
        check(!MantleCombatRules.guards(MantleCombatRules.DamageKind.POTION), "splash and lingering potions pass guard");
        check(!MantleCombatRules.guards(MantleCombatRules.DamageKind.MAGIC), "instant harming and poison damage are not guarded");
        check(!MantleCombatRules.guards(MantleCombatRules.DamageKind.ENVIRONMENT), "guard does not negate falling or drowning");
        check(!MantleCombatRules.guards(MantleCombatRules.DamageKind.BYPASS), "void and administrative damage still pass");
        check(MantleCombatRules.deflection(0) == 0, "cloth starts sweeping from rest");
        check(MantleCombatRules.deflection(5) > .99f, "cloth billows at the middle of a deflection");
        check(MantleCombatRules.deflection(10) == 0, "cloth settles after a deflection");
        check(MantleCombatRules.deflection(20) == 0, "old deflections cannot remain stuck");
        check(Math.abs(MantleCombatRules.guardWave(5)) <= 1, "floating cloth motion stays bounded");
        System.out.println("Mantle hand and animation timing tests passed.");
    }
}
