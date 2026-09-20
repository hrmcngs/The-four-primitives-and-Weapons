package the_four_primitives_and_weapons.weather;

/** Extra ambience; vanilla supplies rain and real lightning sounds. */
public final class WeatherAtmosphere {
    public record Profile(String sound, float volume, int particles) { }
    private static final Profile[] PROFILES = new Profile[WeatherKind.values().length];
    static { for (var kind : WeatherKind.values()) PROFILES[kind.ordinal()] = create(kind); }
    private static final Profile QUIET = new Profile("", 0, 0);
    public static Profile profile(WeatherKind kind, long time) {
        if (kind == WeatherKind.SHOWERS && WeatherRules.intensity(kind, time) == 0) return QUIET;
        return PROFILES[kind.ordinal()];
    }
    private static Profile create(WeatherKind kind) {
        return switch (kind) {
            case CLEAR -> new Profile("", 0, 0);
            case FOG -> new Profile("", 0, 2);
            case DRIZZLE -> new Profile("", 0, 2);
            case RAIN, SHOWERS -> new Profile("", 0, 5);
            case HEAVY_RAIN -> new Profile("wind", .16F, 10);
            case THUNDERSTORM -> new Profile("wind", .32F, 12);
            case SNOW -> new Profile("", 0, 4);
            case HEAVY_SNOW -> new Profile("wind", .22F, 10);
            case HAIL -> new Profile("hail", .45F, 12);
            case SANDSTORM -> new Profile("sand", .4F, 14);
            case BLOWING_SNOW -> new Profile("wind", .4F, 14);
        };
    }
    private WeatherAtmosphere() { }
}
