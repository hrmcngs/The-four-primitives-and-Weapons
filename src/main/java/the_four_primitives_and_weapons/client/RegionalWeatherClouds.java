package the_four_primitives_and_weapons.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.weather.RegionalWeather;
import the_four_primitives_and_weapons.weather.WeatherKind;

/** Reuses the normal cloud pass and the active resource pack's cloud pattern. */
@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RegionalWeatherClouds {
    private static final ResourceLocation SOURCE = new ResourceLocation("textures/environment/clouds.png");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[5];
    private static boolean loaded;
    private RegionalWeatherClouds() { }

    @SubscribeEvent
    public static void registerReload(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) manager -> {
            // Reload listeners may run off the render thread; release GPU resources there.
            Minecraft.getInstance().execute(RegionalWeatherClouds::reset);
        });
    }
    private static void reset() {
        var manager = Minecraft.getInstance().getTextureManager();
        for (int i = 0; i < TEXTURES.length; i++) {
            if (TEXTURES[i] != null) manager.release(TEXTURES[i]);
            TEXTURES[i] = null;
        }
        loaded = false;
    }
    private static WeatherKind kind() {
        var mc = Minecraft.getInstance();
        return RegionalWeather.at(mc.level, mc.gameRenderer.getMainCamera().getBlockPosition());
    }
    private static boolean enabled() {
        var level = Minecraft.getInstance().level;
        return level != null && Level.OVERWORLD.equals(level.dimension());
    }
    public static Vec3 color(Vec3 vanilla) {
        if (!enabled()) return vanilla;
        // Multiply vanilla lighting so sunset, night and weather dimming remain intact.
        return switch (kind()) {
            case CLEAR -> vanilla;
            case FOG -> vanilla.multiply(.87, .9, .92);
            case DRIZZLE -> vanilla.multiply(.8, .84, .88);
            case RAIN, SHOWERS -> vanilla.multiply(.67, .72, .78);
            case HEAVY_RAIN -> vanilla.multiply(.53, .58, .65);
            case THUNDERSTORM, HAIL -> vanilla.multiply(.43, .47, .55);
            case SNOW, HEAVY_SNOW, BLOWING_SNOW -> vanilla.multiply(.91, .95, 1);
            case SANDSTORM -> vanilla.multiply(.85, .7, .46);
        };
    }
    public static ResourceLocation texture(ResourceLocation original) {
        if (!enabled()) return original;
        if (!loaded) load();
        int index = switch (kind()) {
            case CLEAR -> 0;
            case SHOWERS, SANDSTORM -> 1;
            case FOG, DRIZZLE, SNOW, BLOWING_SNOW -> 2;
            case RAIN -> 3;
            case HEAVY_RAIN, HEAVY_SNOW, THUNDERSTORM, HAIL -> 4;
        };
        return TEXTURES[index] == null ? original : TEXTURES[index];
    }
    private static void load() {
        loaded = true;
        var mc = Minecraft.getInstance();
        try (var stream = mc.getResourceManager().open(SOURCE); var source = NativeImage.read(stream)) {
            int width = source.getWidth(), height = source.getHeight();
            for (int variant = 0; variant < TEXTURES.length; variant++) {
                NativeImage image = new NativeImage(width, height, false);
                // Erode clear skies; dilate wet-weather patterns. Wrap at texture edges.
                int radius = variant == 0 ? 1 : variant - 1;
                for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
                    int alpha = variant == 0 ? 255 : 0;
                    for (int dy = -radius; dy <= radius; dy++) for (int dx = -radius; dx <= radius; dx++) {
                        int sample = source.getPixelRGBA(Math.floorMod(x + dx, width), Math.floorMod(y + dy, height)) >>> 24;
                        alpha = variant == 0 ? Math.min(alpha, sample) : Math.max(alpha, sample);
                    }
                    image.setPixelRGBA(x, y, (alpha << 24) | 0xFFFFFF);
                }
                TEXTURES[variant] = mc.getTextureManager().register("regional_clouds_" + variant, new DynamicTexture(image));
            }
        } catch (java.io.IOException exception) {
            com.mojang.logging.LogUtils.getLogger().warn("Could not prepare regional cloud textures; using original clouds", exception);
        }
    }
}
