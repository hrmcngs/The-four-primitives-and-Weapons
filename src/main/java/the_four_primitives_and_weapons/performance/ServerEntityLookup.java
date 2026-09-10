package the_four_primitives_and_weapons.performance;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

/** Server-thread-only, positive lookups shared by status effects within a single tick. */
public final class ServerEntityLookup {
    private static final Map<MinecraftServer, TickCache> CACHES = new WeakHashMap<>();
    private static final class TickCache {
        int tick;
        final Map<UUID, WeakReference<LivingEntity>> entities = new HashMap<>();
    }
    public static LivingEntity living(MinecraftServer server, UUID id) {
        TickCache cache = CACHES.computeIfAbsent(server, ignored -> new TickCache());
        if (cache.tick != server.getTickCount()) {
            cache.entities.clear();
            cache.tick = server.getTickCount();
        }
        WeakReference<LivingEntity> reference = cache.entities.get(id);
        LivingEntity found = reference == null ? null : reference.get();
        if (found != null && !found.isRemoved() && found.level() instanceof ServerLevel level
                && level.getServer() == server && level.getEntity(id) == found) return found;
        // Do not cache misses: a target can spawn or change dimension later in the same tick.
        for (ServerLevel level : server.getAllLevels()) {
            if (level.getEntity(id) instanceof LivingEntity living) {
                cache.entities.put(id, new WeakReference<>(living));
                return living;
            }
        }
        cache.entities.remove(id);
        return null;
    }
    private ServerEntityLookup() {}
}
