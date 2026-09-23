import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import the_four_primitives_and_weapons.util.ShieldGeometry;

public class ShieldGeometryTest {
    public static void main(String[] args) {
        AABB shield = new AABB(-0.5, 0, -0.0625, 0.5, 1.875, 0.0625);
        check(shield, new Vec3(0, 1, -20), new Vec3(0, 1, 20), true, "distant beam");
        check(shield, new Vec3(0.5, 1, -5), new Vec3(0.5, 1, 5), true, "right edge");
        check(shield, new Vec3(-0.5, 1, -5), new Vec3(-0.5, 1, 5), true, "left edge");
        check(shield, new Vec3(0, 1.875, -5), new Vec3(0, 1.875, 5), true, "top edge");
        check(shield, new Vec3(0.51, 1, -5), new Vec3(0.51, 1, 5), false, "around side");
        check(shield, new Vec3(0, 1.9, -5), new Vec3(0, 1.9, 5), false, "over top");
        check(shield, new Vec3(0, 1, -5), new Vec3(0, 1, -1), false, "target before shield");
        check(shield, new Vec3(0, 1, 0), new Vec3(0, 1, 5), true, "starts inside");
        check(shield, new Vec3(0, 1, 5), new Vec3(0, 1, -5), true, "opposite direction");
        check(shield, new Vec3(0, 4, 0), new Vec3(0, -2, 0), true, "vertical beam");
        // A curve can hit a shield even when the straight chord between endpoints misses it.
        Vec3 start = new Vec3(2, 1, -5), bend = new Vec3(0, 1, -1), end = new Vec3(2, 1, 5);
        check(shield, start, end, false, "curve chord");
        check(shield, bend, end, true, "curve incoming segment");
        System.out.println("ShieldGeometryTest passed (12 cases)");
    }

    private static void check(AABB bounds, Vec3 from, Vec3 to, boolean expected, String name) {
        if (ShieldGeometry.intersection(bounds, from, to).isPresent() != expected) throw new AssertionError(name);
    }
}
