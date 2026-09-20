package the_four_primitives_and_weapons.performance;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/** Thread-safe submission; tick and clear are called on the server thread. */
public final class TickWorkQueue {
    private record Task(long due, long sequence, Runnable action) implements Comparable<Task> {
        @Override public int compareTo(Task other) {
            int time = Long.compare(due, other.due);
            return time != 0 ? time : Long.compare(sequence, other.sequence);
        }
    }
    private final ConcurrentLinkedQueue<Task> incoming = new ConcurrentLinkedQueue<>();
    private final PriorityQueue<Task> waiting = new PriorityQueue<>();
    private final AtomicLong sequence = new AtomicLong();
    private volatile long clock;
    public void add(int delay, Runnable action) {
        incoming.add(new Task(clock + Math.max(1, delay), sequence.getAndIncrement(), action));
    }
    public void tick() {
        // Transfer before advancing time: delay=1 submitted between ticks runs now.
        Task task;
        while ((task = incoming.poll()) != null) waiting.add(task);
        long now = ++clock;
        while (!waiting.isEmpty() && waiting.peek().due <= now) waiting.remove().action.run();
    }
    public void clear() { incoming.clear(); waiting.clear(); }
}
