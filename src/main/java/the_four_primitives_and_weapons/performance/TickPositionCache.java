package the_four_primitives_and_weapons.performance;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/** Bounded, allocation-free position lookup within a single client tick/state. */
public final class TickPositionCache<T> {
    private final long[] keys = new long[4096];
    private final Object[] values = new Object[4096];
    private final int[] generations = new int[4096];
    private WeakReference<Object> world = new WeakReference<>(null);
    private long tick, day;
    private int state, generation = 1;
    public void begin(Object world, long tick, long day, int state) {
        if (this.world.get() == world && this.tick == tick && this.day == day && this.state == state) return;
        if (this.world.get() != world) this.world = new WeakReference<>(world);
        this.tick = tick; this.day = day; this.state = state;
        if (++generation == 0) { Arrays.fill(generations, 0); generation = 1; }
    }
    private int slot(long key) {
        key ^= key >>> 33;
        key *= 0xff51afd7ed558ccdL;
        key ^= key >>> 33;
        return (int)key & (keys.length - 1);
    }
    @SuppressWarnings("unchecked")
    public T get(long key) {
        int slot = slot(key);
        return generations[slot] == generation && keys[slot] == key ? (T)values[slot] : null;
    }
    public void put(long key, T value) {
        int slot = slot(key);
        keys[slot] = key; values[slot] = value; generations[slot] = generation;
    }
}
