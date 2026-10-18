import the_four_primitives_and_weapons.world.AuroraCurtainGeometry;

public final class AuroraCurtainGeometryTest {
    public static void main(String[] args) {
        check(AuroraCurtainGeometry.tile(-0.1) == -1, "Negative coordinates use floor");
        check(AuroraCurtainGeometry.tile(256) == 1, "Tile boundary");
        check(AuroraCurtainGeometry.x(-1, 1) == AuroraCurtainGeometry.x(0, 0), "World grid joins");
        double x = AuroraCurtainGeometry.x(0, 0.5);
        double y = AuroraCurtainGeometry.y(192, x, 0, 1, 20, 0);
        check(y > 192, "Curtain starts above clouds");
        double top = AuroraCurtainGeometry.y(192, x, 0, 1, 20, 1);
        check(Math.abs(top - y - 48) < 0.0001, "World-space height remains fixed");
        double z = AuroraCurtainGeometry.z(0, 0, x, 1, 0);
        check(Math.abs(AuroraCurtainGeometry.z(0, 0, x, 1, 1) - z - 6) < 0.0001, "Sheets have physical thickness");
        check(AuroraCurtainGeometry.fade(240) == 0 && AuroraCurtainGeometry.fade(256) == 0, "Tiles fade before leaving window");
        check(AuroraCurtainGeometry.fade(0) == 1 && AuroraCurtainGeometry.fade(200) < 1, "Nearby curtain stays visible");
        check(AuroraCurtainGeometry.z(0, 0, x, 2, 0) != z, "Curtain animates over time");
        System.out.println("World-space aurora geometry checks passed.");
    }
    private static void check(boolean result, String label) {
        if (!result) throw new AssertionError(label);
    }
}
