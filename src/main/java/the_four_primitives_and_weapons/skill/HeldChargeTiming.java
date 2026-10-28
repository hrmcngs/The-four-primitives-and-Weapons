package the_four_primitives_and_weapons.skill;

/** Measures held charge in world ticks; repeated input/tick callbacks cannot speed it up. */
public final class HeldChargeTiming {
    private HeldChargeTiming() {}
    public static int elapsed(long start, long now) {
        if (start < 0 || now < start) return 0;
        return (int)Math.min(Integer.MAX_VALUE, now - start);
    }
    public static float releasedPercent(long start, long now, int minimum, int maximum) {
        int ticks = elapsed(start, now);
        if (start < 0 || ticks < minimum || maximum <= 0) return -1;
        return Math.min(1.0f, ticks / (float)maximum);
    }
}
