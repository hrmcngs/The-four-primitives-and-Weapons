import the_four_primitives_and_weapons.client.tooltip.PageScrollAccumulator;

public class PageScrollAccumulatorTest {
    public static void main(String[] args) {
        Object screen = new Object();
        var input = new PageScrollAccumulator();
        for (int i = 0; i < 9; i++) check(!input.shouldTurn(.1, i * 10, screen), "micro-input must not turn a page");
        check(input.shouldTurn(.1, 90, screen), "one accumulated wheel unit turns a page");
        check(input.shouldTurn(1, 100, screen), "normal wheel still turns immediately");
        check(input.shouldTurn(-1, 110, screen), "normal reverse wheel works");
        check(!input.shouldTurn(.6, 120, screen), "partial forward input");
        check(!input.shouldTurn(-.6, 130, screen), "direction change resets accumulated motion");
        check(!input.shouldTurn(-.6, 500, screen), "old partial gesture expires");
        check(!input.shouldTurn(-.6, 510, new Object()), "screen change resets motion");
        check(!input.shouldTurn(Double.NaN, 520, screen), "invalid input ignored");
        System.out.println("JEI page scroll accumulation passed");
    }
    private static void check(boolean value, String message) { if (!value) throw new AssertionError(message); }
}
