package the_four_primitives_and_weapons.skill;

import net.minecraft.core.particles.ParticleTypes;
import the_four_primitives_and_weapons.damage.ElementalParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/** 選択したLunaの技へ範囲・粒子を適用し、通常技だけ固有の命中処理を使う。 */
public final class LunaSkillEffects {
    private record Context(Player player, boolean normal) {}
    private static final ThreadLocal<Context> ACTIVE = new ThreadLocal<>();

    private LunaSkillEffects() {}

    public static void execute(String motionId, Player player, float chargeScale) {
        execute(motionId, player, 0.0F, chargeScale);
    }

    public static void execute(String motionId, Player player, float chargePercent, float chargeScale) {
        Context previous = ACTIVE.get();
        ACTIVE.set(new Context(player, chargePercent <= 0.0F));
        try {
            MotionExecutor.executeMotion(motionId, player, chargePercent, chargeScale);
        } finally {
            if (previous == null) ACTIVE.remove();
            else ACTIVE.set(previous);
        }
    }

    public static boolean isActive(Player player) {
        return ACTIVE.get() != null && ACTIVE.get().player() == player;
    }

    public static boolean usesNormalDamage(Player player) {
        return isActive(player) && ACTIVE.get().normal();
    }

    static double rangeBonus(ItemStack weapon) {
        Player player = ACTIVE.get() == null ? null : ACTIVE.get().player();
        return WeaponStatsRegistry.attackRangeBonus(weapon)
                + (player != null && player.getMainHandItem() == weapon ? 0.5 : 0.0);
    }

    /** 命中範囲の内側へ均等に撒く。粒子数に比例した追加ダメージは与えない。 */
    static void fillLane(Player player, Vec3 forward, double depth, double halfWidth, double tilt) {
        if (!isActive(player) || !(player.level() instanceof ServerLevel level)) return;
        Vec3 right = new Vec3(-forward.z, 0, forward.x).normalize();
        for (int row = 1; row <= 6; row++) {
            for (int column = -3; column <= 3; column++) {
                double side = halfWidth * column / 4.0;
                Vec3 point = player.position().add(forward.scale(depth * row / 7.0))
                        .add(right.scale(side)).add(0, 1.2 + tilt * column / 3.0, 0);
                ElementalParticles.sendForced(level, ParticleTypes.END_ROD, point.x, point.y, point.z,
                        1, 0, 0, 0, 0);
            }
        }
    }
}
