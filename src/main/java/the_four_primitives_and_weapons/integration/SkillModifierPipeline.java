package the_four_primitives_and_weapons.integration;

import java.util.function.DoublePredicate;

/** One modifier lifecycle per skill hit, with recursive modifier damage kept outside the lifecycle. */
public final class SkillModifierPipeline {
    private static final ThreadLocal<Boolean> ACTIVE = ThreadLocal.withInitial(() -> false);
    private SkillModifierPipeline() {}
    public interface Hooks {
        float damage(float base) throws ReflectiveOperationException;
        void before(float damage) throws ReflectiveOperationException;
        void after(float damage) throws ReflectiveOperationException;
        void failed(float damage) throws ReflectiveOperationException;
    }
    public static boolean active() { return ACTIVE.get(); }
    public static boolean hit(float base, Hooks hooks, DoublePredicate hit) throws ReflectiveOperationException {
        if (ACTIVE.get()) return hit.test(base);
        ACTIVE.set(true);
        try {
            float damage = hooks.damage(base);
            if (!Float.isFinite(damage) || damage <= 0) return false;
            hooks.before(damage);
            boolean accepted = hit.test(damage);
            if (accepted) hooks.after(damage); else hooks.failed(damage);
            return accepted;
        } finally { ACTIVE.remove(); }
    }
}
