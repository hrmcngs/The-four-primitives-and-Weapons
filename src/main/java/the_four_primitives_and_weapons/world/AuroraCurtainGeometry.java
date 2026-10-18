package the_four_primitives_and_weapons.world;

/** Fixed world-space curtains: camera position never changes their shape or altitude. */
public final class AuroraCurtainGeometry {
    public static final int TILE = 256, SEGMENTS = 24;
    private AuroraCurtainGeometry() {}
    public static int tile(double coordinate) { return (int) Math.floor(coordinate / TILE); }
    public static double x(int tile, double along) { return ((double) tile + along) * TILE; }
    public static double z(int tileZ, int ribbon, double x, double time, int layer) {
        return (double) tileZ * TILE + 40 + ribbon * 76
            + 22 * Math.sin(x * 0.019 + time) + 7 * Math.sin(x * 0.047 - time * 0.6) + layer * 6;
    }
    public static double y(double cloudHeight, double x, int ribbon, double time, float height, double fraction) {
        return cloudHeight + 24 + ribbon * 9 + 7 * Math.sin(x * 0.014 - time * 0.4) + fraction * height * 2.4;
    }
    public static float fade(double distance) {
        return (float) Math.max(0, Math.min(1, (240 - distance) / 64));
    }
}
