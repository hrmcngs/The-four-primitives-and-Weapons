package the_four_primitives_and_weapons.performance;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Track stands, including untagged ones: commands may add the guard tag after spawn. */
@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons")
public final class GuardStandIndex {
    private static final Map<ServerLevel, Set<ArmorStand>> STANDS = new WeakHashMap<>();
    @SubscribeEvent
    public static void join(EntityJoinLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof ArmorStand stand) {
            STANDS.computeIfAbsent(level, ignored -> Collections.newSetFromMap(new WeakHashMap<>())).add(stand);
        }
    }
    @SubscribeEvent
    public static void leave(EntityLeaveLevelEvent event) {
        if (event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof ArmorStand stand) {
            Set<ArmorStand> stands = STANDS.get(level);
            if (stands != null) stands.remove(stand);
        }
    }
    @SubscribeEvent
    public static void unload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level) STANDS.remove(level);
    }
    public static void rotate(ServerLevel level) {
        Set<ArmorStand> stands = STANDS.get(level);
        if (stands == null) return;
        for (ArmorStand stand : stands) {
            if (stand.isRemoved() || level.getEntity(stand.getId()) != stand
                    || !stand.getTags().contains("the_four_primitives_and_weapons_guard_bind")) continue;
            float yaw = stand.getYRot() + 40;
            stand.setYRot(yaw);
            stand.setYBodyRot(yaw);
            stand.setYHeadRot(yaw);
        }
    }
    private GuardStandIndex() {}
}
