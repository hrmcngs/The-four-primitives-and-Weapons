package the_four_primitives_and_weapons.world;

/** Independent overrides. Eclipse values: -1 = calendar, -2 = off, 0..1 = fixed. */
public record AstronomySettings(float size, int phase, int color, int meteors, boolean effects,
                                float solar, float lunar) {
    public static final AstronomySettings DEFAULT = new AstronomySettings(-1F, -1, -1, -1, true);

    /** Existing saves and callers start with both eclipses on their natural calendars. */
    public AstronomySettings(float size, int phase, int color, int meteors, boolean effects) {
        this(size, phase, color, meteors, effects, -1F, -1F);
    }
    public AstronomySettings {
        if (size != -1F && (!Float.isFinite(size) || size < 0.25F || size > 4F)) size = -1F;
        if (phase < -1 || phase > 7) phase = -1;
        if (color < -1 || color >= AstronomicalEvents.MoonTint.values().length) color = -1;
        if (meteors < -1 || meteors > 1) meteors = -1;
        solar = validEclipse(solar);
        lunar = validEclipse(lunar);
    }
    private static float validEclipse(float value) {
        return value == -1F || value == -2F || (Float.isFinite(value) && value >= 0F && value <= 1F) ? value : -1F;
    }
    public AstronomySettings withSize(float value) { return new AstronomySettings(value, phase, color, meteors, effects, solar, lunar); }
    public AstronomySettings withPhase(int value) { return new AstronomySettings(size, value, color, meteors, effects, solar, lunar); }
    public AstronomySettings withColor(int value) { return new AstronomySettings(size, phase, value, meteors, effects, solar, lunar); }
    public AstronomySettings withMeteors(int value) { return new AstronomySettings(size, phase, color, value, effects, solar, lunar); }
    public AstronomySettings withEffects(boolean value) { return new AstronomySettings(size, phase, color, meteors, value, solar, lunar); }
    public AstronomySettings withSolar(float value) { return new AstronomySettings(size, phase, color, meteors, effects, value, lunar); }
    public AstronomySettings withLunar(float value) { return new AstronomySettings(size, phase, color, meteors, effects, solar, value); }
    public float moonSize(long time) { return size < 0F ? LunarCycle.sizeMultiplier(time) : size; }
    public AstronomySettings vanillaMoon(int moonPhase) {
        return new AstronomySettings(1F, moonPhase, AstronomicalEvents.MoonTint.NORMAL.ordinal(), meteors, effects, solar, lunar);
    }
    public int moonPhase(int naturalPhase) { return phase < 0 ? naturalPhase : phase; }
    public AstronomicalEvents.MoonTint moonColor(long time, int naturalPhase) {
        return color < 0 ? AstronomicalEvents.moonTint(time, moonPhase(naturalPhase))
            : AstronomicalEvents.MoonTint.values()[color];
    }
    public boolean meteorShower(long time) {
        return meteors < 0 ? AstronomicalEvents.isMeteorShower(time) : meteors == 1;
    }
    public float solarProgress(long time) {
        return solar == -1F ? AstronomicalEvents.solarEclipseProgress(time) : solar == -2F ? -1F : solar;
    }
    public float lunarStrength(long time, int naturalPhase) {
        if (lunar == -2F) return 0F;
        if (lunar >= 0F) return lunar;
        return moonPhase(naturalPhase) == 0 ? AstronomicalEvents.lunarEclipseStrength(time) : 0F;
    }
    public boolean lunarNight(long time, int naturalPhase) {
        return lunar > 0F || (lunar == -1F && moonPhase(naturalPhase) == 0 && AstronomicalEvents.isLunarEclipseDay(time));
    }
}
