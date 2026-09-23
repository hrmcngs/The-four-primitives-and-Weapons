import the_four_primitives_and_weapons.skill.HandSwingTiming;

public final class HandSwingTimingTest {
    public static void main(String[] args) {
        near(0, HandSwingTiming.progress(100, 100, 0, 6), "swing starts at zero");
        near(0.5F, HandSwingTiming.progress(103, 100, 0, 6), "middle of swing");
        near(0.25F, HandSwingTiming.progress(101, 100, 0.5F, 6), "frame interpolation");
        near(-1, HandSwingTiming.progress(106, 100, 0, 6), "swing expires");
        near(-1, HandSwingTiming.progress(100, Long.MIN_VALUE, 0, 6), "no swing");
        near(-1, HandSwingTiming.progress(99, 100, 0, 6), "world time reset");
        near(-1, HandSwingTiming.progress(100, 100, 0, 0), "invalid duration");
        // Deferred off-hand attacks get their own timeline after the main hand finishes.
        near(-1, HandSwingTiming.progress(110, 100, 0, 6), "main hand complete before deferred swing");
        near(0.5F, HandSwingTiming.progress(110, 107, 0, 6), "off hand still moving");
        near(0, HandSwingTiming.progress(111, 111, 0, 6), "next combo hit restarts swing");
        System.out.println("HandSwingTimingTest: passed");
    }
    private static void near(float expected, float actual, String message) {
        if (Math.abs(expected - actual) > 0.00001F) throw new AssertionError(message + ": " + actual);
    }
}
