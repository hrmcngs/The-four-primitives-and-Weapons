package the_four_primitives_and_weapons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;
import the_four_primitives_and_weapons.weather.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public final class RegionalWeatherEffects {
    private static final DustParticleOptions SAND = new DustParticleOptions(new Vector3f(0.76F, 0.62F, 0.38F), 1.5F);
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut e) { RegionalWeatherData.clientForced = null; RegionalWeatherAudio.reset(); }
    private static WeatherKind cameraWeather() {
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || !Level.OVERWORLD.equals(mc.level.dimension())) return WeatherKind.CLEAR;
        var camera = mc.gameRenderer.getMainCamera();
        if (camera.getFluidInCamera() != FogType.NONE || !mc.level.canSeeSky(camera.getBlockPosition())
            || mc.player.hasEffect(MobEffects.BLINDNESS) || mc.player.hasEffect(MobEffects.DARKNESS)) return WeatherKind.CLEAR;
        return RegionalWeather.at(mc.level, camera.getBlockPosition());
    }
    @SubscribeEvent public static void fog(ViewportEvent.RenderFog e) {
        if (e.getMode() != FogRenderer.FogMode.FOG_TERRAIN) return;
        float end = switch (cameraWeather()) {
            case FOG -> 40F;
            case SANDSTORM -> 28F;
            case BLOWING_SNOW -> 32F;
            default -> -1F;
        };
        if (end < 0F) return;
        e.setNearPlaneDistance(Math.min(e.getNearPlaneDistance(), 2F));
        e.setFarPlaneDistance(Math.min(e.getFarPlaneDistance(), end));
        e.setCanceled(true);
    }
    @SubscribeEvent public static void fogColor(ViewportEvent.ComputeFogColor e) {
        if (cameraWeather() == WeatherKind.SANDSTORM) {
            e.setRed(e.getRed() * 0.5F + 0.38F);
            e.setGreen(e.getGreen() * 0.5F + 0.31F);
            e.setBlue(e.getBlue() * 0.5F + 0.19F);
        }
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        RegionalWeatherAudio.tick();
        var mc = Minecraft.getInstance();
        if (mc.isPaused() || mc.level == null || mc.player == null) return;
        WeatherKind kind = cameraWeather();
        int baseCount = WeatherAtmosphere.profile(kind, mc.level.getGameTime()).particles();
        if (baseCount == 0) return;
        var random = mc.level.random;
        var camera = mc.gameRenderer.getMainCamera().getPosition();
        int count = switch (mc.options.particles().get()) {
            case MINIMAL -> Math.max(1, baseCount / 6);
            case DECREASED -> Math.max(1, baseCount / 2);
            default -> baseCount;
        };
        for (int i = 0; i < count; i++) {
            double x = camera.x + (random.nextDouble() - 0.5) * 32;
            double y = camera.y + (kind.precipitation != 0 ? 4 + random.nextDouble() * 6 : random.nextDouble() * 8 - 2);
            double z = camera.z + (random.nextDouble() - 0.5) * 32;
            var pos = BlockPos.containing(x, y, z);
            if (!mc.level.getBlockState(pos).isAir() || !mc.level.canSeeSky(pos) || RegionalWeather.at(mc.level, pos) != kind) continue;
            switch (kind) {
                case SANDSTORM -> mc.level.addParticle(SAND, x, y, z, 0.6, 0.02, 0.2);
                case HAIL -> mc.level.addParticle(ParticleTypes.ITEM_SNOWBALL, x, y, z, 0.05, -0.8, 0.02);
                case DRIZZLE, RAIN, HEAVY_RAIN, SHOWERS, THUNDERSTORM -> {
                    mc.level.addParticle(ParticleTypes.RAIN, x, y, z, 0, -.5, 0);
                    var ground = mc.level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, pos);
                    if (Math.abs(ground.getY() - camera.y) < 10 && mc.level.canSeeSky(ground)
                        && RegionalWeather.at(mc.level, ground) == kind)
                        mc.level.addParticle(ParticleTypes.SPLASH, x, ground.getY() + .05, z, 0, .02, 0);
                }
                case SNOW, HEAVY_SNOW -> mc.level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, .025, -.05, .01);
                case BLOWING_SNOW -> mc.level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, 0.4, -0.02, 0.15);
                case FOG -> mc.level.addParticle(ParticleTypes.CLOUD, x, y, z, 0.01, 0, 0.01);
                default -> { }
            }
        }
    }
}
