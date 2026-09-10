import java.util.UUID;
import the_four_primitives_and_weapons.util.GateDropContext;
import the_four_primitives_and_weapons.util.GateDropContext.Reason;

public final class GateDropContextTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) throws InterruptedException {
        UUID alice = UUID.randomUUID();
        UUID bob = UUID.randomUUID();
        check(GateDropContext.current(alice) == Reason.UNKNOWN, "No guessed drop reason");
        GateDropContext.during(alice, Reason.DROP_KEY, () -> {
            check(GateDropContext.current(alice) == Reason.DROP_KEY, "Drop key belongs to its UUID");
            check(GateDropContext.current(bob) == Reason.UNKNOWN, "Nearby player must not inherit summon");
            GateDropContext.during(bob, Reason.DEATH, () -> {
                check(GateDropContext.current(bob) == Reason.DEATH, "Second player death");
                check(GateDropContext.current(alice) == Reason.DROP_KEY, "First player context preserved");
                return null;
            });
            GateDropContext.during(alice, Reason.INVENTORY, () -> {
                check(GateDropContext.current(alice) == Reason.INVENTORY, "Nested drop has its own reason");
                return null;
            });
            check(GateDropContext.current(alice) == Reason.DROP_KEY, "Outer context restored");
            return null;
        });
        check(GateDropContext.current(alice) == Reason.UNKNOWN, "No stale context after drop");
        check(GateDropContext.current(bob) == Reason.UNKNOWN, "No stale context after death");
        try {
            GateDropContext.during(alice, Reason.DROP_KEY, () -> { throw new IllegalStateException("canceled handler"); });
        } catch (IllegalStateException expected) {
            check(GateDropContext.current(alice) == Reason.UNKNOWN, "Exception must clear summon eligibility");
        }
        for (Reason reason : Reason.values()) {
            check(GateDropContext.canSummon(reason, true, true, false) == (reason == Reason.DROP_KEY),
                    "Only explicit hotbar drop summons: " + reason);
            check(!GateDropContext.canSummon(reason, false, true, false), "Dead players never summon");
            check(!GateDropContext.canSummon(reason, true, false, false), "Disconnected players never summon");
            check(!GateDropContext.canSummon(reason, true, true, true), "Spectators never summon");
        }
        final Reason[] otherThread = new Reason[1];
        GateDropContext.during(alice, Reason.DROP_KEY, () -> {
            Thread thread = new Thread(() -> otherThread[0] = GateDropContext.current(alice));
            thread.start();
            try { thread.join(); } catch (InterruptedException e) { throw new AssertionError(e); }
            return null;
        });
        check(otherThread[0] == Reason.UNKNOWN, "Client/server threads cannot share summon context");
        System.out.println("Gate drop UUID, nesting, cancellation, death and ordinary-drop checks passed");
    }
}
