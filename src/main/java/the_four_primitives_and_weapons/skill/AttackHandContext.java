package the_four_primitives_and_weapons.skill;

import java.util.Set;
import java.util.UUID;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** Virtual attack hand only during skill execution; never swaps inventory stacks. */
public final class AttackHandContext {
    private static final ThreadLocal<Snapshot> ACTIVE = new ThreadLocal<>();
    private AttackHandContext() {}

    public record Snapshot(Player player, Level level, ItemStack main, ItemStack off,
                           InteractionHand hand, Set<UUID> mainHits) {
        public ItemStack weapon() { return hand == InteractionHand.MAIN_HAND ? main : off; }
        public boolean valid() {
            // Read actual equipment, bypassing the virtual getters below.
            return player.isAlive() && !player.isRemoved() && player.level() == level
                && player.getInventory().getSelected() == main && player.getInventory().offhand.get(0) == off
                && !weapon().isEmpty();
        }
        public void run(Runnable action) {
            if (!valid()) return;
            Snapshot previous = ACTIVE.get();
            ACTIVE.set(this);
            try { action.run(); }
            finally { if (previous == null) ACTIVE.remove(); else ACTIVE.set(previous); }
        }
    }

    public static Snapshot create(Player player, InteractionHand hand, Set<UUID> hits) {
        return new Snapshot(player, player.level(), player.getMainHandItem(), player.getOffhandItem(), hand, hits);
    }
    public static Snapshot capture() { return ACTIVE.get(); }
    public static boolean active(Player player) { return forEntity(player) != null; }
    public static Snapshot forEntity(LivingEntity entity) {
        Snapshot snapshot = ACTIVE.get();
        return snapshot != null && snapshot.player() == entity ? snapshot : null;
    }
    public static boolean mirrored(Player player) {
        Snapshot snapshot = forEntity(player);
        return snapshot != null && DualWieldRules.side(player.getMainArm() == HumanoidArm.LEFT,
            snapshot.hand() == InteractionHand.OFF_HAND) < 0;
    }
    public static Vec3 origin(Player player, Vec3 center) {
        Snapshot snapshot = forEntity(player);
        if (snapshot == null) return center;
        Vec3 forward = MotionExecutor.horizontalLook(player);
        int side = DualWieldRules.side(player.getMainArm() == HumanoidArm.LEFT, snapshot.hand() == InteractionHand.OFF_HAND);
        return center.add(-forward.z * 0.35 * side, 0, forward.x * 0.35 * side);
    }
    public static Runnable carry(Runnable action) {
        Snapshot snapshot = capture();
        return snapshot == null ? action : () -> snapshot.run(action);
    }

    /** Let the second weapon connect once without disabling unrelated hurt immunity. */
    public static boolean hurt(LivingEntity attacker, LivingEntity target,
                               net.minecraft.world.damagesource.DamageSource source, float damage) {
        Snapshot context = forEntity(attacker);
        int previous = target.invulnerableTime;
        boolean pairedHit = context != null && context.hand() == InteractionHand.OFF_HAND
            && context.mainHits().remove(target.getUUID());
        if (pairedHit) target.invulnerableTime = 0;
        boolean accepted = false;
        try { accepted = target.hurt(source, damage); }
        finally { if (pairedHit && !accepted) target.invulnerableTime = previous; }
        if (accepted && context != null && context.hand() == InteractionHand.MAIN_HAND)
            context.mainHits().add(target.getUUID());
        return accepted;
    }

    public static double attributeValue(Snapshot context, net.minecraft.world.entity.ai.attributes.Attribute attribute) {
        var current = context.player().getAttribute(attribute);
        if (current == null) return attribute.getDefaultValue();
        var adjusted = new net.minecraft.world.entity.ai.attributes.AttributeInstance(attribute, ignored -> {});
        adjusted.replaceFrom(current);
        context.main().getAttributeModifiers(net.minecraft.world.entity.EquipmentSlot.MAINHAND).get(attribute)
            .forEach(modifier -> adjusted.removeModifier(modifier.getId()));
        context.off().getAttributeModifiers(net.minecraft.world.entity.EquipmentSlot.OFFHAND).get(attribute)
            .forEach(modifier -> adjusted.removeModifier(modifier.getId()));
        context.off().getAttributeModifiers(net.minecraft.world.entity.EquipmentSlot.MAINHAND).get(attribute)
            .forEach(modifier -> {
                adjusted.removeModifier(modifier.getId());
                adjusted.addTransientModifier(modifier);
            });
        return adjusted.getValue();
    }
}
