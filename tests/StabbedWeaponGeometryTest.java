import java.lang.reflect.Method;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import the_four_primitives_and_weapons.util.StabbedWeaponGeometry;

public class StabbedWeaponGeometryTest {
    private static void near(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > 1.0E-5) throw new AssertionError(message + ": " + actual);
    }

    public static void main(String[] args) throws Exception {
        Method read = StabbedWeaponGeometry.class.getDeclaredMethod("readAxis", ResourceLocation.class);
        read.setAccessible(true);
        Vec3[] model = (Vec3[]) read.invoke(null, new ResourceLocation("the_four_primitives_and_weapons",
            "custom/saya/ninjato/ninzyatousayatuki"));
        if (model == null) throw new AssertionError("Loaded ninjato model must provide geometry");
        // Actual model: Y=4.55..16.75, third-person Y scale=1.5, rotation Z=-180.
        near(model[0].y, (8 - 4.55) * 1.5 / 16 + 0.5 / 16, "Handle endpoint follows display transform");
        near(model[1].y, (8 - 16.75) * 1.5 / 16 + 0.5 / 16, "Sheath tip follows display transform");
        Vec3 shift = new Vec3(12, 64 - 0.04, -8).subtract(model[1]);
        Vec3[] planted = {model[0].add(shift), model[1].add(shift)};
        List<VoxelShape> upright = StabbedWeaponGeometry.collisionPieces(planted, 0.05);
        AABB player = new AABB(11.7, 66, -8.3, 12.3, 67.8, -7.7);
        near(Shapes.collide(Direction.Axis.Y, player, upright, -3), planted[0].y + 0.05 - 66,
            "Falling player lands on the handle at the planted location");
        near(Shapes.collide(Direction.Axis.Y, player.move(0.5, 0, 0), upright, -3), -3,
            "No wide invisible platform beside upright weapon");

        Vec3[] tilted = {new Vec3(0,0,0), new Vec3(1,1,1)};
        List<VoxelShape> diagonal = StabbedWeaponGeometry.collisionPieces(tilted, 0.05);
        AABB emptyCorner = new AABB(0.7,1.2,-0.1, 0.9,3,0.1);
        near(Shapes.collide(Direction.Axis.Y, emptyCorner, diagonal, -0.8), -0.8,
            "Empty corner of diagonal bounding box does not act as a platform");
        AABB aboveHandle = new AABB(0.9,1.2,0.9,1.1,3,1.1);
        near(Shapes.collide(Direction.Axis.Y, aboveHandle, diagonal, -0.8), -0.15,
            "Tilted handle supports a falling player");
        near(Shapes.collide(Direction.Axis.Y, aboveHandle.move(0,-0.15,0), diagonal, -0.08), 0,
            "Standing player stays supported next tick");
        System.out.println("Planted weapon model alignment and Minecraft collision checks passed");
    }
}
