package the_four_primitives_and_weapons.weather;

/** Pure biome policy shared by rendering, gameplay and tests. */
public final class WeatherRules {
    public record Climate(boolean desert, boolean dry, boolean cold, boolean humid, boolean sand,
                          boolean noRain, boolean noSnow, boolean noThunder, boolean noFog) { }
    private WeatherRules() { }
    public static boolean night(long dayTime) {
        long time = Math.floorMod(dayTime, 24000L);
        return time >= 13000L && time < 23000L;
    }
    public static boolean allowed(WeatherKind weather, Climate c, long dayTime) {
        boolean liquid = !c.noRain && !c.cold && (!c.dry || (c.desert && night(dayTime)));
        return switch (weather) {
            case CLEAR -> true;
            case FOG -> c.humid && !c.noFog;
            case DRIZZLE, RAIN, HEAVY_RAIN, SHOWERS -> liquid;
            case THUNDERSTORM -> liquid && !c.noThunder;
            case HAIL -> liquid && !c.noThunder && !c.desert;
            case SNOW, HEAVY_SNOW, BLOWING_SNOW -> c.cold && !c.noSnow;
            case SANDSTORM -> c.sand && c.dry;
        };
    }
    public static WeatherKind resolve(Climate c, long dayTime, long gameTime,
                                      boolean raining, boolean thundering, WeatherKind forced) {
        if (forced != null) {
            // Vanilla precipitation timers can expire while a type override is retained.
            if (forced.precipitation != 0 && !raining) return WeatherKind.CLEAR;
            return allowed(forced, c, dayTime) ? forced : WeatherKind.CLEAR;
        }
        int pattern = (int) Math.floorMod(Math.floorDiv(gameTime, 6000L), 6L);
        WeatherKind candidate;
        if (!raining) {
            long hour = Math.floorMod(dayTime, 24000L);
            candidate = c.humid && hour < 2500L && pattern % 2 == 0 ? WeatherKind.FOG : WeatherKind.CLEAR;
        } else if (c.dry && !(c.desert && night(dayTime))) {
            candidate = WeatherKind.SANDSTORM;
        } else if (c.cold) {
            candidate = switch (pattern % 3) {
                case 0 -> WeatherKind.SNOW;
                case 1 -> WeatherKind.HEAVY_SNOW;
                default -> WeatherKind.BLOWING_SNOW;
            };
        } else if (thundering && !c.noThunder) {
            candidate = !c.desert && pattern == 5 ? WeatherKind.HAIL : WeatherKind.THUNDERSTORM;
        } else {
            candidate = switch (pattern % 4) {
                case 0 -> WeatherKind.DRIZZLE;
                case 1 -> WeatherKind.RAIN;
                case 2 -> WeatherKind.HEAVY_RAIN;
                default -> WeatherKind.SHOWERS;
            };
        }
        return allowed(candidate, c, dayTime) ? candidate : WeatherKind.CLEAR;
    }
    public static float intensity(WeatherKind kind, long time) {
        // Showers have intermittent wet and dry spells, unlike continuous rain.
        if (kind == WeatherKind.SHOWERS && Math.floorMod(time, 600L) >= 360L) return 0F;
        return kind.intensity;
    }
}
