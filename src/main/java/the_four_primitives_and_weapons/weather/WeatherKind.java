package the_four_primitives_and_weapons.weather;

/** Representative WMO 4677 codes; observational history is simplified for gameplay. */
public enum WeatherKind {
    CLEAR("晴れ", -1, 0F, 0, false),
    FOG("霧", 45, 0F, 0, false),
    DRIZZLE("霧雨", 51, 0.25F, 1, false),
    RAIN("雨", 63, 0.65F, 1, false),
    HEAVY_RAIN("大雨", 65, 1F, 1, false),
    SNOW("雪", 73, 0.55F, 2, false),
    HEAVY_SNOW("大雪", 75, 1F, 2, false),
    SHOWERS("にわか雨", 80, 0.7F, 1, false),
    THUNDERSTORM("雷雨", 95, 1F, 1, true),
    HAIL("雷雨を伴う雹", 96, 0.75F, 1, true),
    SANDSTORM("砂嵐", 31, 0F, 0, false),
    BLOWING_SNOW("地吹雪", 38, 0F, 0, false);

    public final String label;
    public final int wmoCode;
    public final float intensity;
    /** 0 none, 1 liquid, 2 snow. Hail adds ice particles to rain. */
    public final int precipitation;
    public final boolean thunder;
    WeatherKind(String label, int wmoCode, float intensity, int precipitation, boolean thunder) {
        this.label = label; this.wmoCode = wmoCode; this.intensity = intensity;
        this.precipitation = precipitation; this.thunder = thunder;
    }
}
