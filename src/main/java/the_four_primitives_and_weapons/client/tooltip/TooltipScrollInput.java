package the_four_primitives_and_weapons.client.tooltip;

/** GLFW keeps both trackpad axes; vanilla's scrolling event keeps only Y. */
public final class TooltipScrollInput {
    public record Movement(double horizontal, double vertical) {}
    private TooltipScrollInput() {}
    public static Movement route(double x, double y, boolean option) {
        if (!Double.isFinite(x) || !Double.isFinite(y)) return new Movement(0, 0);
        if (option) return new Movement(x != 0 ? x : y, 0);
        return new Movement(x, y); // Shift or Command: follow the actual scroll direction.
    }
}
