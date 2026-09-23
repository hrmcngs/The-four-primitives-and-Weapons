package the_four_primitives_and_weapons.skill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.events.ChargedAttackHandler;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID)
public final class DualWieldAttackHandler {
    private record Pending(AttackHandContext.Snapshot context, Runnable action, long after, long expires) {}
    private static final Map<UUID, Pending> PENDING = new HashMap<>();
    private DualWieldAttackHandler() {}

    public static boolean paired(Player player) {
        return !player.level().isClientSide && !AttackHandContext.active(player)
            && ChargedAttackHandler.isWeapon(player.getMainHandItem())
            && ChargedAttackHandler.isWeapon(player.getOffhandItem());
    }
    public static boolean busy(Player player) {
        return !player.level().isClientSide && (PENDING.containsKey(player.getUUID()) || paired(player) && running(player));
    }
    private static boolean running(Player player) {
        return SpinSlashTickHandler.isSpinning(player) || SlamDownPitchHandler.isSlamming(player)
            || the_four_primitives_and_weapons.procedures.SkillComboProcedure.isComboing(player)
            || the_four_primitives_and_weapons.procedures.JsonThrustProcedure.isThrusting(player);
    }
    public static void execute(Player player, String mainMotion, String offMotion, boolean charged,
                               Runnable mainAttack, Runnable offAttack) {
        var hits = new HashSet<UUID>();
        var main = AttackHandContext.create(player, InteractionHand.MAIN_HAND, hits);
        var off = AttackHandContext.create(player, InteractionHand.OFF_HAND, hits);
        long now = player.level().getGameTime();
        int wait = DualWieldRules.recoveryTicks(mainMotion, player.getCurrentItemAttackStrengthDelay());
        main.run(() -> { player.swing(InteractionHand.MAIN_HAND, true); mainAttack.run(); });
        if (DualWieldRules.simultaneous(mainMotion, offMotion, charged) && !running(player)) {
            off.run(() -> { player.swing(InteractionHand.OFF_HAND, true); offAttack.run(); });
        } else {
            PENDING.put(player.getUUID(), new Pending(off,
                () -> { player.swing(InteractionHand.OFF_HAND, true); offAttack.run(); }, now + wait, now + 200));
        }
    }
    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;
        Player player = event.player;
        Pending pending = PENDING.get(player.getUUID());
        if (pending == null) return;
        long now = player.level().getGameTime();
        if (!pending.context.valid() || now > pending.expires) {
            PENDING.remove(player.getUUID());
        } else if (now >= pending.after && !running(player)) {
            PENDING.remove(player.getUUID());
            pending.context.run(pending.action);
        }
    }
    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent event) { PENDING.remove(event.getEntity().getUUID()); }
}
