package the_four_primitives_and_weapons.world;

/** A visual lunar-distance cycle, independent of vanilla's eight-day moon phases. */
public final class LunarCycle {
    public static final long PERIOD_TICKS = 32L * 24000L;
    private static final long FIRST_PEAK_TICKS = 18000L;

    private LunarCycle() {
    }

    /** World day time keeps this deterministic across clients, saves and time commands. */
    public static float sizeMultiplier(long dayTime) {
        // Reduce before subtracting to avoid overflow or loss of precision in old worlds.
        long cycleTime = Math.floorMod(dayTime, PERIOD_TICKS) - FIRST_PEAK_TICKS;
        double angle = cycleTime * (Math.PI * 2.0 / PERIOD_TICKS);
        return (float) (1.0 + 0.2 * Math.cos(angle));
    }

    /** Special names apply only to full moons near either end of the distance cycle. */
    public static String fullMoonEventName(long dayTime, int moonPhase) {
        if (moonPhase != 0) return "";
        float size = sizeMultiplier(dayTime);
        if (size >= 1.19F) return "スーパームーン";
        if (size <= 0.81F) return "マイクロムーン";
        return "";
    }
}
