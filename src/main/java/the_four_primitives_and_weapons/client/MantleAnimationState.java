package the_four_primitives_and_weapons.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import the_four_primitives_and_weapons.network.MantleActionPacket;
import the_four_primitives_and_weapons.util.MantleCombatRules;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.network.MantleAnimationPacket;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, value = Dist.CLIENT)
public final class MantleAnimationState {
    public record Pose(MantleActionPacket.Action action, InteractionHand hand, long start, int duration, ClientLevel world) {}
    private static final Map<UUID, Pose> STATES = new HashMap<>();
    private record Deflect(long start, int side, ClientLevel world) {}
    private static final Map<UUID, Deflect> DEFLECTIONS = new HashMap<>();
    public static void deflect(UUID player, int side) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        DEFLECTIONS.values().removeIf(d -> d.world != level || level.getGameTime() - d.start >= 10);
        DEFLECTIONS.put(player, new Deflect(level.getGameTime(), side < 0 ? -1 : 1, level));
    }
    public static float deflection(LivingEntity entity, float partial) {
        var d = DEFLECTIONS.get(entity.getUUID());
        if (d == null) return 0;
        if (d.world != entity.level() || entity.level().getGameTime() - d.start >= 10) {
            DEFLECTIONS.remove(entity.getUUID()); return 0;
        }
        return d.side * MantleCombatRules.deflection(entity.level().getGameTime() - d.start + partial);
    }
    public static void accept(MantleAnimationPacket packet) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        STATES.values().removeIf(s -> s.world() != level || level.getGameTime() - s.start() >= s.duration());
        if (packet.duration() <= 0) STATES.remove(packet.player());
        else STATES.put(packet.player(), new Pose(packet.action(), packet.hand(),
            MantleCombatRules.localStart(level.getGameTime(), packet.elapsed(), packet.duration()), packet.duration(), level));
    }
    public static Pose get(LivingEntity entity) {
        var state = STATES.get(entity.getUUID());
        if (state != null && (entity.level() != state.world()
            || entity.level().getGameTime() - state.start() >= state.duration())) {
            STATES.remove(entity.getUUID()); return null;
        }
        return state;
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event) { STATES.clear(); DEFLECTIONS.clear(); }
}
