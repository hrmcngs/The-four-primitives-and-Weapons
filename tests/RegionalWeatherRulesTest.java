import the_four_primitives_and_weapons.weather.*;
import the_four_primitives_and_weapons.weather.WeatherRules.Climate;

public class RegionalWeatherRulesTest {
    private static void check(boolean value, String message) { if (!value) throw new AssertionError(message); }
    public static void main(String[] args) {
        var desert = new Climate(true, true, false, false, true, false, true, true, true);
        var badlands = new Climate(false, true, false, false, true, true, true, true, true);
        var savanna = new Climate(false, true, false, false, false, true, true, true, true);
        var forest = new Climate(false, false, false, true, false, false, false, false, false);
        var snow = new Climate(false, false, true, false, false, false, false, false, false);
        for (long time : new long[]{0, 6000, 12999, 23000, 23999})
            check(!WeatherRules.allowed(WeatherKind.RAIN, desert, time), "Desert dry by day");
        for (long time : new long[]{13000, 18000, 22999, 37000})
            check(WeatherRules.allowed(WeatherKind.RAIN, desert, time), "Desert rain at night");
        for (long tick = 0; tick < 72000; tick += 100) {
            var weather = WeatherRules.resolve(desert, 18000, tick, true, false, null);
            check(weather.precipitation == 1, "Natural desert night rain");
            check(WeatherRules.resolve(desert, 6000, tick, true, false, null) == WeatherKind.SANDSTORM, "Desert day sandstorm");
            check(WeatherRules.resolve(savanna, 18000, tick, true, true, null) == WeatherKind.CLEAR, "No rain/sandstorm in savanna");
            check(WeatherRules.resolve(desert, 18000, tick, false, false, null) == WeatherKind.CLEAR, "Night does not force daily rain");
        }
        for (WeatherKind kind : WeatherKind.values()) {
            if (kind.precipitation == 1) check(!WeatherRules.allowed(kind, badlands, 18000), "No liquid in badlands");
            if (kind.precipitation == 2 || kind == WeatherKind.BLOWING_SNOW)
                check(!WeatherRules.allowed(kind, forest, 18000), "No snow in warm forest");
            check(WeatherRules.resolve(desert, 18000, 0, true, true, kind)
                == (WeatherRules.allowed(kind, desert, 18000) ? kind : WeatherKind.CLEAR), "Manual weather respects restrictions");
        }
        check(WeatherRules.allowed(WeatherKind.SNOW, snow, 6000), "Cold snow");
        check(!WeatherRules.allowed(WeatherKind.RAIN, snow, 18000), "Cold biome uses snow");
        check(WeatherRules.allowed(WeatherKind.FOG, forest, 6000), "Humid fog");
        check(!WeatherRules.allowed(WeatherKind.SANDSTORM, forest, 6000), "Sandstorm biome restriction");
        check(!WeatherRules.allowed(WeatherKind.RAIN, new Climate(true,true,false,false,true,true,true,true,true),18000), "Data pack rain ban wins over desert exception");
        check(WeatherRules.intensity(WeatherKind.SHOWERS, 100) > 0 && WeatherRules.intensity(WeatherKind.SHOWERS, 400) == 0, "Showers intermittent");
        check(WeatherRules.resolve(forest, 18000, 0, false, false, WeatherKind.RAIN) == WeatherKind.CLEAR, "Vanilla clear ends precipitation");
        check(WeatherKind.RAIN.wmoCode == 63 && WeatherKind.CLEAR.wmoCode == -1, "Codes: fair weather is not mislabelled WMO 00");
        for (var kind : WeatherKind.values()) {
            var sky = the_four_primitives_and_weapons.weather.WeatherSky.appearance(kind);
            for (int i = -100; i <= 100; i++) {
                float density = the_four_primitives_and_weapons.weather.WeatherSky.density(i * .13, i * -.21, 9000, sky);
                check(Float.isFinite(density) && density >= 0 && density <= 1, "Bounded sky opacity");
            }
        }
        var clearSky = the_four_primitives_and_weapons.weather.WeatherSky.appearance(WeatherKind.CLEAR);
        var stormSky = the_four_primitives_and_weapons.weather.WeatherSky.appearance(WeatherKind.THUNDERSTORM);
        check(stormSky.coverage() > clearSky.coverage(), "Storm clouds cover more sky");
        check(clearSky.haze() == 0, "Clear sky has no weather haze");
        check(the_four_primitives_and_weapons.weather.WeatherSky.appearance(WeatherKind.SANDSTORM).sand(), "Sand sky palette");
        for (var kind : WeatherKind.values()) {
            var ambience = the_four_primitives_and_weapons.weather.WeatherAtmosphere.profile(kind, 0);
            check(ambience.volume() >= 0 && ambience.volume() <= 1, "Weather sound volume range");
            check(ambience.particles() >= 0 && ambience.particles() <= 14, "Particle budget");
            check(ambience.sound().isEmpty() == (ambience.volume() == 0), "Silent weather has no loop");
        }
        check(the_four_primitives_and_weapons.weather.WeatherAtmosphere.profile(WeatherKind.CLEAR, 0).particles() == 0, "Clear has no added particles");
        check(the_four_primitives_and_weapons.weather.WeatherAtmosphere.profile(WeatherKind.FOG, 0).sound().isEmpty(), "Fog remains silent");
        check(the_four_primitives_and_weapons.weather.WeatherAtmosphere.profile(WeatherKind.RAIN, 0).sound().isEmpty(), "Do not duplicate vanilla rain sound");
        check(the_four_primitives_and_weapons.weather.WeatherAtmosphere.profile(WeatherKind.SHOWERS, 400).particles() == 0, "No particles during shower dry spell");
        System.out.println("Regional weather: biome exclusions, desert night boundaries, dry spells and forced modes passed.");
    }
}
