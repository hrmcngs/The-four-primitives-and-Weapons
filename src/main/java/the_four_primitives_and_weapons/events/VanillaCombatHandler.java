package the_four_primitives_and_weapons.events;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import the_four_primitives_and_weapons.ai.VanillaCombatRules;
import the_four_primitives_and_weapons.ai.VanillaTacticalGoal;

@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons")
public final class VanillaCombatHandler {
    private VanillaCombatHandler() {}
    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Mob mob)) return;
        var type = ForgeRegistries.ENTITY_TYPES.getKey(mob.getType());
        if (type == null || !VanillaCombatRules.supports(type.getNamespace(), type.getPath())) return;
        if (mob.goalSelector.getAvailableGoals().stream().anyMatch(goal -> goal.getGoal() instanceof VanillaTacticalGoal)) return;
        // ZombieAttackGoal already uses priority 2. Remove that competing melee timeline,
        // including subclass goals, while leaving target selectors and ranged fallback goals intact.
        var meleeGoals = mob.goalSelector.getAvailableGoals().stream()
                .map(goal -> goal.getGoal())
                .filter(goal -> goal instanceof net.minecraft.world.entity.ai.goal.MeleeAttackGoal).toList();
        meleeGoals.forEach(mob.goalSelector::removeGoal);
        // Keep target selection, swimming, door breaking, and fallback ranged weapon AI.
        mob.goalSelector.addGoal(2, new VanillaTacticalGoal(mob, type.getPath()));
    }
}
