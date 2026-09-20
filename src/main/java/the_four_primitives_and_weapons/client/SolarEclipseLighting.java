package the_four_primitives_and_weapons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.world.AstronomyData;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public final class SolarEclipseLighting {
    private static float previous, current;
    private static Level lastLevel;
    public static float strength() {
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.level != lastLevel || !Level.OVERWORLD.equals(mc.level.dimension())) return 0;
        return previous + (current - previous) * mc.getFrameTime();
    }
    public static float brightness() { return 1 - .85F * strength(); }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        var mc = Minecraft.getInstance();
        if (mc.level != lastLevel) { previous = current = 0; lastLevel = mc.level; }
        if (mc.level == null || mc.isPaused()) return;
        float target = 0;
        if (Level.OVERWORLD.equals(mc.level.dimension())) {
            float progress = AstronomyData.settings(mc.level).solarProgress(mc.level.getDayTime());
            float daylight = Math.max(0F, Math.min(1F, (float) Math.cos(mc.level.getTimeOfDay(1) * Math.PI * 2) * 4));
            target = the_four_primitives_and_weapons.world.EclipseLightCurve.strength(progress) * daylight;
        }
        previous = current;
        current += Math.max(-.01F, Math.min(.01F, target - current));
    }
    @SubscribeEvent public static void fog(ViewportEvent.ComputeFogColor event) {
        float factor = brightness();
        event.setRed(event.getRed() * factor);
        event.setGreen(event.getGreen() * factor);
        event.setBlue(event.getBlue() * factor);
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event) { previous = current = 0; lastLevel = null; }
    private SolarEclipseLighting() { }
}
