package the_four_primitives_and_weapons.weather;

/** Shared, deterministic sky appearance. Coverage is independent of global rain strength. */
public final class WeatherSky {
    public record Appearance(float coverage, float opacity, float haze, float speed, boolean sand) { }
    private static final Appearance[] PROFILES = new Appearance[WeatherKind.values().length];
    static { for (var kind : WeatherKind.values()) PROFILES[kind.ordinal()] = create(kind); }
    public static Appearance appearance(WeatherKind kind) { return PROFILES[kind.ordinal()]; }
    private static Appearance create(WeatherKind kind) {
        return switch (kind) {
            case CLEAR -> new Appearance(.32F, .78F, 0F, .4F, false);
            case FOG -> new Appearance(.72F, .6F, .8F, .18F, false);
            case DRIZZLE -> new Appearance(.82F, .78F, .18F, .5F, false);
            case RAIN -> new Appearance(.9F, .9F, .22F, .8F, false);
            case HEAVY_RAIN -> new Appearance(1F, .98F, .4F, 1.2F, false);
            case SNOW -> new Appearance(.82F, .86F, .3F, .35F, false);
            case HEAVY_SNOW -> new Appearance(1F, .94F, .55F, .6F, false);
            case SHOWERS -> new Appearance(.64F, .9F, .13F, 1.4F, false);
            case THUNDERSTORM, HAIL -> new Appearance(1F, 1F, .42F, 1.6F, false);
            case SANDSTORM -> new Appearance(.88F, .9F, .9F, 2.5F, true);
            case BLOWING_SNOW -> new Appearance(.92F, .9F, .8F, 2F, false);
        };
    }
    public static float density(double x, double z, double time, Appearance a) {
        double u = x + time * .0006 * a.speed;
        double v = z + time * .0002 * a.speed;
        double noise = .5 + .22 * Math.sin(u * 1.7 + Math.sin(v))
            + .18 * Math.cos(v * 2.1 - u * .4) + .1 * Math.sin(u * 4.3 + v * 3.7);
        double t = Math.max(0, Math.min(1, (noise - (1 - a.coverage)) * 4));
        return (float) (t * t * (3 - 2 * t) * a.opacity);
    }
    private WeatherSky() { }
}
