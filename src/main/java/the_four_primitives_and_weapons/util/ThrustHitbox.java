package the_four_primitives_and_weapons.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** 突きの中心線と敵の当たり判定の交差を調べる。距離で横幅を広げない。 */
public final class ThrustHitbox {
    private static final double HALF_WIDTH = 0.2;

    private ThrustHitbox() {}

    public static AABB bounds(Vec3 start, Vec3 end) {
        return new AABB(start, end).inflate(HALF_WIDTH);
    }

    public static boolean intersects(LivingEntity target, Vec3 start, Vec3 end) {
        AABB box = target.getBoundingBox().inflate(HALF_WIDTH);
        return box.contains(start) || box.clip(start, end).isPresent();
    }
}
