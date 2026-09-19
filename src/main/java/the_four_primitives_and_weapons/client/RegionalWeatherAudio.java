package the_four_primitives_and_weapons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FogType;
import the_four_primitives_and_weapons.weather.*;

/** One extra ambience layer, with short fades on shelter and weather changes. */
public final class RegionalWeatherAudio {
    private static Loop current;
    private static int retry;
    public static void reset() {
        if (current != null) Minecraft.getInstance().getSoundManager().stop(current);
        current = null;
        retry = 0;
    }
    private static WeatherAtmosphere.Profile sample() {
        var mc = Minecraft.getInstance();
        var camera = mc.gameRenderer.getMainCamera();
        if (mc.level == null || mc.player == null || !Level.OVERWORLD.equals(mc.level.dimension())
            || camera.getFluidInCamera() != FogType.NONE) return WeatherAtmosphere.profile(WeatherKind.CLEAR, 0);
        var pos = camera.getBlockPosition();
        if (mc.level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ()) - pos.getY() > 12)
            return WeatherAtmosphere.profile(WeatherKind.CLEAR, 0);
        var profile = WeatherAtmosphere.profile(RegionalWeather.at(mc.level, pos), mc.level.getGameTime());
        return new WeatherAtmosphere.Profile(profile.sound(), profile.volume() * (mc.level.canSeeSky(pos) ? 1F : .18F), profile.particles());
    }
    public static void tick() {
        var mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) { reset(); return; }
        if (mc.isPaused()) return;
        var profile = sample();
        if (current != null && (!current.key.equals(profile.sound()) || current.level != mc.level)) {
            current.retiring = true;
            current = null;
            retry = 0;
        }
        if (retry > 0) retry--;
        if (!profile.sound().isEmpty() && retry == 0 && (current == null || !mc.getSoundManager().isActive(current))) {
            current = new Loop(profile.sound(), mc.level);
            mc.getSoundManager().play(current);
            retry = 20;
        }
    }
    private static final class Loop extends AbstractTickableSoundInstance {
        private final String key;
        private final ClientLevel level;
        private boolean retiring;
        Loop(String key, ClientLevel level) {
            super(WeatherSounds.SOUNDS.get(key), SoundSource.WEATHER, RandomSource.create());
            this.key = key; this.level = level;
            looping = true;
            relative = true;
            attenuation = Attenuation.NONE;
            volume = .001F;
        }
        @Override public boolean canStartSilent() { return true; }
        @Override public void tick() {
            var mc = Minecraft.getInstance();
            if (mc.level != level || mc.player == null) { stop(); return; }
            var profile = sample();
            float target = !retiring && key.equals(profile.sound()) ? profile.volume() : 0;
            volume += Math.max(-.025F, Math.min(.025F, target - volume));
            if (target == 0 && volume <= .001F) stop();
        }
    }
    private RegionalWeatherAudio() { }
}
