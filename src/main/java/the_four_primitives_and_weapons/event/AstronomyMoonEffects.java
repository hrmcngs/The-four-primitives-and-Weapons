package the_four_primitives_and_weapons.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.world.*;

@Mod.EventBusSubscriber
public final class AstronomyMoonEffects {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void miningExperience(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || !Level.OVERWORLD.equals(level.dimension())
                || event.getPlayer().isCreative() || event.getPlayer().isSpectator()) return;
        event.setExpToDrop(AstronomyData.settings(level).miningExperience(
            level.getDayTime(), level.getMoonPhase(), event.getExpToDrop()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void cropGrowth(BlockEvent.CropGrowEvent.Pre event) {
        if (event.getResult() != Event.Result.DEFAULT || !(event.getLevel() instanceof ServerLevel level)
                || !Level.OVERWORLD.equals(level.dimension())) return;
        var settings = AstronomyData.settings(level);
        // Use existing random growth attempts: no scanning or forced chunk loading.
        if (settings.activeEffectColor(level.getDayTime(), level.getMoonPhase()) != AstronomicalEvents.MoonTint.JADE) return;
        if (settings.boostCropGrowth(level.getDayTime(), level.getMoonPhase(), level.random.nextFloat()))
            event.setResult(Event.Result.ALLOW);
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
