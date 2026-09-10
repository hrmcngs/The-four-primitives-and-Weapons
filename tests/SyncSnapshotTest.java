import the_four_primitives_and_weapons.performance.SyncSnapshot;
public class SyncSnapshotTest {
    public static void main(String[] args) {
        var gate = new SyncSnapshot<String>();
        Object connection = new Object();
        check(gate.shouldSend(connection, "health=10", false), "initial sync");
        int packets = 0;
        for (int i = 0; i < 10000; i++) if (gate.shouldSend(connection, "health=10", false)) packets++;
        check(packets == 0, "unchanged data is suppressed");
        check(gate.shouldSend(connection, "health=9", false), "changes arrive immediately");
        check(gate.shouldSend(connection, "health=10", false), "reverting also arrives");
        check(gate.shouldSend(connection, "health=10", true), "dimension/respawn force sync");
        check(gate.shouldSend(new Object(), "health=10", false), "reconnect sync");
        System.out.println("SyncSnapshot: initial/change/revert/forced/reconnect passed; 10000 redundant sends suppressed.");
    }
    private static void check(boolean value, String name) { if (!value) throw new AssertionError(name); }
}
