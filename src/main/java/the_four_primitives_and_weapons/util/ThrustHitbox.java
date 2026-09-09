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

    /** 横切りと同じく対象の中心で奥行きを制限し、敵の幅で射程が延びないようにする。 */
    public static boolean intersects(LivingEntity target, Vec3 start, Vec3 end, boolean limitDepth) {
        if (limitDepth) {
            Vec3 axis = end.subtract(start);
            if (target.getBoundingBox().getCenter().subtract(start).dot(axis) > axis.lengthSqr()) return false;
        }
        return intersects(target, start, end);
    }
}
