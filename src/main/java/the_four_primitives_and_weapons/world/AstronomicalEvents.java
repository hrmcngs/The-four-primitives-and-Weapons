package the_four_primitives_and_weapons.world;

import java.util.Random;

/** Fictional, deterministic sky events driven by the world's calendar. */
public final class AstronomicalEvents {
    public enum MoonTint {
        NORMAL("", 1F, 1F, 1F),
        BLUE("ブルームーン", 0.35F, 0.65F, 1F),
        BLOOD("ブラッドムーン", 1F, 0.22F, 0.12F);

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

    public record Meteor(double x, double z, double dx, double dz, float opacity) { }

    /** One brief streak per time slot; no entities, packets or persistent state. */
    public static Meteor meteor(long dayTime) {
        long timeOfDay = Math.floorMod(dayTime, 24000L);
        if (timeOfDay < 13000L || timeOfDay >= 23000L) return null;
        int interval = isMeteorShower(dayTime) ? 40 : 320;
        long slot = Math.floorDiv(dayTime, interval);
        int age = (int) Math.floorMod(dayTime, interval);
        if (age >= 24) return null;
        Random random = new Random(slot * 0x9E3779B97F4A7C15L);
        double angle = random.nextDouble() * Math.PI * 2.0;
        double direction = angle + 0.6 + random.nextDouble();
        double radius = 25.0 + random.nextDouble() * 65.0;
        double dx = Math.cos(direction), dz = Math.sin(direction);
        double travel = (age / 23.0 - 0.5) * 45.0;
        return new Meteor(Math.cos(angle) * radius + dx * travel,
            Math.sin(angle) * radius + dz * travel, dx, dz,
            (float) Math.sin(Math.PI * age / 23.0));
    }
}
