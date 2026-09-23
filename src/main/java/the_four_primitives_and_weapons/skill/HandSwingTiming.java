package the_four_primitives_and_weapons.skill;

/** Shared bounded swing timing, independent of the renderer. */
public final class HandSwingTiming {
    private HandSwingTiming() {}
    public static float progress(long now, long start, float partial, int duration) {
        if (start == Long.MIN_VALUE || now < start || duration < 1) return -1;
        double elapsed = (now - start) + Math.max(0, Math.min(1, partial));
        return elapsed >= duration ? -1 : (float) (elapsed / duration);
    }
}
