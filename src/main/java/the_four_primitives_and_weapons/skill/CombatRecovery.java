package the_four_primitives_and_weapons.skill;

import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/** Short commitment after a skill, without scanning players or counting down each tick. */
@net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons")
public final class CombatRecovery {
    private record Recovery(ResourceLocation dimension, long started, int ticks) {}
    private static final Map<Player, Recovery> RECOVERIES = new WeakHashMap<>();
    private CombatRecovery() {}

    public static boolean ready(Player player) {
        Recovery recovery = RECOVERIES.get(player);
        return recovery == null || !recovery.dimension.equals(player.level().dimension().location())
                || !CombatTimingRules.inWindow(player.level().getGameTime(), recovery.started, recovery.ticks);
    }

    public static void begin(Player player, boolean charged) {
        RECOVERIES.put(player, new Recovery(player.level().dimension().location(), player.level().getGameTime(),
                CombatTimingRules.recoveryTicks(player.getCurrentItemAttackStrengthDelay(), charged)));
    }

    @net.minecraftforge.eventbus.api.SubscribeEvent(priority = net.minecraftforge.eventbus.api.EventPriority.HIGHEST)
    public static void onVanillaAttack(net.minecraftforge.event.entity.player.AttackEntityEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide
                && the_four_primitives_and_weapons.events.ChargedAttackHandler.isWeapon(player.getMainHandItem())
                && (!ready(player) || DualWieldAttackHandler.busy(player) || player.isBlocking())) {
            event.setCanceled(true);
        }
    }
}
