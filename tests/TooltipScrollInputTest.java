import the_four_primitives_and_weapons.client.tooltip.TooltipScrollInput;

public class TooltipScrollInputTest {
    public static void main(String[] args) {
        check(TooltipScrollInput.route(0, 1, false).vertical() == 1, "Shift vertical input moves vertically");
        var horizontal = TooltipScrollInput.route(1, 0, false);
        check(horizontal.horizontal() == 1 && horizontal.vertical() == 0, "Shift horizontal input moves horizontally, not vertically");
        check(TooltipScrollInput.route(0, 1, true).horizontal() == 1, "Option wheel moves horizontally");
        check(TooltipScrollInput.route(0.25, 0, true).horizontal() == 0.25, "native horizontal trackpad preserves fractions");
        var diagonal = TooltipScrollInput.route(0.15, -0.2, false);
        check(diagonal.horizontal() == 0.15 && diagonal.vertical() == -0.2, "Shift or Command diagonal scrolling retains both axes");
        check(TooltipScrollInput.route(-0.25, 0, false).horizontal() == -0.25, "left scrolling preserves sign and fractional motion");
        check(TooltipScrollInput.route(1, 0, true).horizontal() == 1, "old Shift Option shortcut remains usable");
        check(TooltipScrollInput.route(Double.NaN, 1, false).vertical() == 0, "invalid native delta ignored");
        System.out.println("TooltipScrollInputTest passed");
    }
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
