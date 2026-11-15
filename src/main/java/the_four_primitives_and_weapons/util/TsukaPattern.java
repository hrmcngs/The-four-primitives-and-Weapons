package the_four_primitives_and_weapons.util;

/** Stylized wrap masks in normalized handle coordinates; true exposes the underlay. */
public final class TsukaPattern {
    private TsukaPattern() {}
    public static boolean supported(String style) {
        return "hineri".equals(style) || "katate".equals(style) || "hira".equals(style)
            || "spiral".equals(style) || "basket".equals(style);
    }
    public static boolean underlay(String style, double across, double along) {
        if ("katate".equals(style) && along > .28 && along < .72) return false;
        double phase = along * 6 % 1;
        double diagonal = (along * 6 + across) % 1;
        if ("spiral".equals(style)) return diagonal > .58;
        if ("basket".equals(style)) {
            double reverse = (along * 6 + 1 - across) % 1;
            return diagonal > .32 && reverse > .32;
        }
        if ("hira".equals(style)) return phase > .80;
        return Math.abs(across - .5) * 2 + Math.abs(phase - .5) * 2 < .72;
    }
}
