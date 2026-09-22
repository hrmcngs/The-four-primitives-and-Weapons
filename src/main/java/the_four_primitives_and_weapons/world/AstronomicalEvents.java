package the_four_primitives_and_weapons.world;

import java.util.Random;

/** Fictional, deterministic sky events driven by the world's calendar. */
public final class AstronomicalEvents {
    public enum MoonTint {
        NORMAL("", 1F, 1F, 1F),
        BLUE("ブルームーン", 0.35F, 0.65F, 1F),
        BLOOD("ブラッドムーン", 1F, 0.22F, 0.12F),
        GOLD("ゴールドムーン", 1F, 0.72F, 0.20F),
        JADE("ジェイドムーン", 0.25F, 1F, 0.55F),
        VIOLET("バイオレットムーン", 0.65F, 0.30F, 1F),
        ROSE("ローズムーン", 1F, 0.40F, 0.70F);

        public final String displayName;
        public final float red, green, blue;

        MoonTint(String displayName, float red, float green, float blue) {
            this.displayName = displayName;
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }

    private AstronomicalEvents() { }

    /** Game-calendar eclipses: a new-moon noon and a full-moon midnight. */
    public static boolean isSolarEclipseDay(long dayTime) {
        return Math.floorMod(Math.floorDiv(dayTime, 24000L), 64L) == 12L;
    }

    public static float solarEclipseProgress(long dayTime) {
        return isSolarEclipseDay(dayTime) ? eclipseProgress(dayTime, 4000L) : -1F;
    }

    public static boolean isLunarEclipseDay(long dayTime) {
        return Math.floorMod(Math.floorDiv(dayTime, 24000L), 64L) == 48L;
    }

    public static float lunarEclipseStrength(long dayTime) {
        float progress = isLunarEclipseDay(dayTime) ? eclipseProgress(dayTime, 16000L) : -1F;
        return progress < 0F ? 0F : (float) Math.pow(Math.sin(Math.PI * progress), 2.0);
    }

    private static float eclipseProgress(long dayTime, long start) {
        long elapsed = Math.floorMod(dayTime, 24000L) - start;
        return elapsed >= 0L && elapsed <= 4000L ? elapsed / 4000F : -1F;
    }

    public static MoonTint moonTint(long dayTime, int moonPhase) {
        if (moonPhase != 0) return MoonTint.NORMAL;
        long day = Math.floorDiv(dayTime, 24000L);
        if (Math.floorMod(day, 64L) == 24L) return MoonTint.BLOOD;
        if (Math.floorMod(day, 32L) == 8L) return MoonTint.BLUE;
        if (Math.floorMod(day, 64L) == 16L) return MoonTint.GOLD;
        if (Math.floorMod(day, 64L) == 32L) return MoonTint.JADE;
        if (Math.floorMod(day, 128L) == 56L) return MoonTint.VIOLET;
        if (Math.floorMod(day, 128L) == 120L) return MoonTint.ROSE;
        return MoonTint.NORMAL;
    }

    public static boolean isMeteorShower(long dayTime) {
        return Math.floorMod(Math.floorDiv(dayTime, 24000L), 16L) == 7L;
    }

    public static String nightEventNames(long dayTime, int moonPhase) {
        String names = LunarCycle.fullMoonEventName(dayTime, moonPhase);
        String tint = moonTint(dayTime, moonPhase).displayName;
        if (!tint.isEmpty()) names += (names.isEmpty() ? "" : "・") + tint;
        if (isMeteorShower(dayTime)) names += (names.isEmpty() ? "" : "・") + "流星群";
        if (moonPhase == 0 && isLunarEclipseDay(dayTime)) names += (names.isEmpty() ? "" : "・") + "月食";
        return names;
    }

    public enum MeteorKind { SHORT, STREAK, TRAIN, FIREBALL }
    public record Meteor(double x, double y, double z, double dx, double dy, double dz,
                         float opacity, MeteorKind kind, float headOpacity) { }

    /** Deterministic streaks and fading trains; no entities, packets or persistent state. */
    public static Meteor meteor(long dayTime) {
        return meteor(dayTime, isMeteorShower(dayTime), 0);
    }

    public static Meteor meteor(long dayTime, boolean shower, int lane) {
        return meteor(dayTime, shower, lane, false);
    }

    public static Meteor meteor(long dayTime, boolean shower, int lane, boolean allowDaytime) {
        long timeOfDay = Math.floorMod(dayTime, 24000L);
        if (!allowDaytime && (timeOfDay < 13000L || timeOfDay >= 23000L)) return null;
        int interval = shower ? 100 : 320;
        dayTime += lane * 33L;
        long slot = Math.floorDiv(dayTime, interval);
        int age = (int) Math.floorMod(dayTime, interval);
        Random random = new Random(slot * 0x9E3779B97F4A7C15L + lane * 7919L);
        // Ordinary nights get a 25% chance per 16-second slot, rather than a guaranteed streak.
        if (!shower && random.nextFloat() >= 0.25F) return null;
        float kindRoll = random.nextFloat();
        MeteorKind kind = kindRoll < 0.05F ? MeteorKind.FIREBALL : kindRoll < 0.35F ? MeteorKind.TRAIN
            : kindRoll < 0.65F ? MeteorKind.SHORT : MeteorKind.STREAK;
        int flight = kind == MeteorKind.SHORT ? 12 : 24;
        int duration = kind == MeteorKind.TRAIN || kind == MeteorKind.FIREBALL ? 90 : flight;
        if (age >= duration) return null;
        double angle = random.nextDouble() * Math.PI * 2.0;
        // Sample the visible hemisphere, including low skies in every direction.
        double elevation = Math.toRadians(12.0 + random.nextDouble() * 65.0);
        double horizontal = Math.cos(elevation), vertical = Math.sin(elevation);
        double side = random.nextBoolean() ? 0.8 : -0.8;
        double dx = 0.6 * vertical * Math.cos(angle) - side * Math.sin(angle);
        double dy = -0.6 * horizontal;
        double dz = 0.6 * vertical * Math.sin(angle) + side * Math.cos(angle);
        double travel = (Math.min(age, flight - 1) / (double)(flight - 1) - 0.5) * 32.0;
        float headOpacity = age < flight ? (float) Math.sin(Math.PI * age / (flight - 1)) : 0F;
        float opacity = duration == flight ? headOpacity : age < flight
            ? Math.min(1F, age / 6F) : (duration - age) / (float)(duration - flight);
        return new Meteor(100.0 * horizontal * Math.cos(angle) + dx * travel,
            100.0 * vertical + dy * travel,
            100.0 * horizontal * Math.sin(angle) + dz * travel, dx, dy, dz,
            opacity, kind, headOpacity);
    }
}
