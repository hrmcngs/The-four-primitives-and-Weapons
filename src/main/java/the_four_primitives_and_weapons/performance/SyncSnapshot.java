package the_four_primitives_and_weapons.performance;

import java.util.Objects;

/** Per-owner snapshot gate. Values must be immutable snapshots, never live mutable state. */
public final class SyncSnapshot<T> {
    private Object receiver;
    private T previous;
    private boolean initialized;
    public boolean shouldSend(Object receiver, T snapshot, boolean force) {
        if (!force && initialized && this.receiver == receiver && Objects.equals(previous, snapshot)) return false;
        this.receiver = receiver;
        previous = snapshot;
        initialized = true;
        return true;
    }
}
