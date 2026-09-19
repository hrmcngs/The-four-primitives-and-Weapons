package the_four_primitives_and_weapons.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.world.*;

@Mod.EventBusSubscriber
public final class AstronomyMoonEffects {
    public static MobEffect effect(AstronomicalEvents.MoonTint color) {
        return switch (color) {
            case BLUE -> MobEffects.NIGHT_VISION;
            case BLOOD -> MobEffects.DAMAGE_BOOST;
            case GOLD -> MobEffects.LUCK;
            case JADE -> MobEffects.REGENERATION;
            case VIOLET -> MobEffects.DAMAGE_RESISTANCE;
            case ROSE -> MobEffects.SLOW_FALLING;
            default -> null;
        };
    }
    @SubscribeEvent public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.getServer().getTickCount() % 20 != 0) return;
        ServerLevel level = event.getServer().overworld();
        var settings = AstronomyData.settings(level);
        if (!settings.effects() || !MoonPhaseDifficulty.isMoonActive(level)) return;
        // No light reaches the world during a new moon, even with an explicit color.
        if (settings.moonPhase(level.getMoonPhase()) == 4) return;
        MobEffect effect = effect(settings.moonColor(level.getDayTime(), level.getMoonPhase()));
        if (effect == null) return;
        int duration = effect == MobEffects.NIGHT_VISION ? 240 : 60;
        for (var player : level.players()) {
            if (!player.isAlive() || player.isSpectator()) continue;
            var existing = player.getEffect(effect);
            if (existing != null && (existing.getAmplifier() > 0 || existing.getDuration() > duration || existing.isInfiniteDuration())) continue;
            player.addEffect(new MobEffectInstance(effect, duration, 0, true, false, true));
        }
    }
    public static String nightNames(ServerLevel level) {
        var settings = AstronomyData.settings(level);
        int phase = settings.moonPhase(level.getMoonPhase());
        String size = phase == 0 ? (settings.moonSize(level.getDayTime()) >= 1.19F ? "スーパームーン"
            : settings.moonSize(level.getDayTime()) <= 0.81F ? "マイクロムーン" : "") : "";
        String color = settings.moonColor(level.getDayTime(), level.getMoonPhase()).displayName;
        String names = size + (size.isEmpty() || color.isEmpty() ? "" : "・") + color;
        if (settings.meteorShower(level.getDayTime())) names += (names.isEmpty() ? "" : "・") + "流星群";
        if (settings.lunarNight(level.getDayTime(), level.getMoonPhase())) names += (names.isEmpty() ? "" : "・") + "月食";
        return names;
    }
}
