package the_four_primitives_and_weapons.util;

import java.util.Optional;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** Closed shield surfaces include their edges and an attack starting inside them. */
public final class ShieldGeometry {
    private ShieldGeometry() {}

    public static Optional<Vec3> intersection(AABB shield, Vec3 from, Vec3 to) {
        AABB surface = shield.inflate(0.001);
        return surface.contains(from) ? Optional.of(from) : surface.clip(from, to);
    }
}
