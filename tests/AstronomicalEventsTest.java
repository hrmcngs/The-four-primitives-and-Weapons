import the_four_primitives_and_weapons.world.AstronomicalEvents;
import the_four_primitives_and_weapons.world.AstronomicalEvents.MoonTint;
import the_four_primitives_and_weapons.world.AstronomySettings;

public class AstronomicalEventsTest {
    public static void main(String[] args) {
        var settings = new AstronomySettings(2F, 5, MoonTint.JADE.ordinal(), 1, false);
        check(settings.moonSize(186000L) == 2F && settings.moonPhase(7) == 5, "Moon overrides on shower day");
        check(settings.moonColor(186000L, 7) == MoonTint.JADE, "Manual color on crescent");
        check(settings.meteorShower(18000L), "Shower independent of calendar");
        var combined = settings.withSolar(0.5F).withLunar(0.75F);
        check(combined.solarProgress(18000L) == 0.5F && combined.lunarStrength(6000L, 7) == 0.75F,
            "Both eclipses independent of calendar and phase");
        var edited = combined.withSize(1.2F).withPhase(0).withColor(MoonTint.BLUE.ordinal()).withMeteors(1).withEffects(true);
        check(edited.solar() == 0.5F && edited.lunar() == 0.75F && edited.meteorShower(6000L), "Other controls preserve both eclipses");
        check(combined.vanillaMoon(5).solar() == 0.5F && combined.vanillaMoon(5).lunar() == 0.75F, "Vanilla preset preserves eclipses");
        check(combined.withSolar(-2F).solarProgress(294000L) == -1F && combined.withSolar(-2F).lunar() == 0.75F, "Solar off independent");
        check(combined.withLunar(-2F).lunarStrength(1170000L, 0) == 0F && combined.withLunar(-2F).solar() == 0.5F, "Lunar off independent");
        check(combined.withSolar(-1F).solarProgress(294000L) == 0.5F, "Restore natural solar calendar");
        check(combined.withLunar(-1F).withPhase(-1).lunarStrength(1170000L, 0) == 1F, "Restore natural lunar calendar");
        check(combined.withSolar(Float.NaN).solar() == -1F && combined.withLunar(2F).lunar() == -1F, "Invalid eclipse values");
        check(AstronomicalEvents.meteor(6005L, true, 0, true) != null, "Forced shower during solar eclipse/daylight");
        check(AstronomicalEvents.meteor(6005L, true, 0, false) == null, "Automatic shower remains night only");
        for (int phase = 0; phase < 8; phase++) {
            var vanilla = settings.vanillaMoon(phase);
            check(vanilla.moonSize(186000L) == 1F && vanilla.moonPhase(7) == phase, "Vanilla size and phase");
            check(vanilla.moonColor(186000L, 7) == MoonTint.NORMAL, "Vanilla moon stays white");
            check(vanilla.meteors() == settings.meteors() && vanilla.effects() == settings.effects(), "Preserve shower and effect settings");
        }
        check(!new AstronomySettings(-1F, -1, -1, 0, true).meteorShower(186000L), "Shower disabled");
        check(new AstronomySettings(Float.NaN, 8, 100, 2, true).equals(AstronomySettings.DEFAULT), "Invalid settings normalized");
        check(AstronomySettings.DEFAULT.moonColor(210000L, 0) == MoonTint.BLUE, "Automatic color restored");
        boolean low = false, high = false;
        boolean[] quadrants = new boolean[4];
        for (long t = 181000L; t < 191000L; t++) {
            for (int lane = 0; lane < 3; lane++) {
                var meteor = AstronomicalEvents.meteor(t, true, lane);
                if (meteor == null) continue;
                double elevation = Math.toDegrees(Math.atan2(meteor.y(), Math.hypot(meteor.x(), meteor.z())));
                check(elevation > 0 && elevation < 90, "Above horizon");
                check(meteor.dy() < 0, "Meteor descends");
                low |= elevation < 25; high |= elevation > 65;
                quadrants[(meteor.x() < 0 ? 2 : 0) + (meteor.z() < 0 ? 1 : 0)] = true;
                check(meteor.equals(AstronomicalEvents.meteor(t, true, lane)), "Deterministic lane");
            }
        }
        check(low && high, "Meteors cover low and high skies");
        for (boolean quadrant : quadrants) check(quadrant, "Meteors cover every direction");
        for (int day = 0; day < 128; day++) {
            long time = day * 24000L + 18000L;
            MoonTint tint = AstronomicalEvents.moonTint(time, day % 8);
            check(day % 8 == 0 || tint == MoonTint.NORMAL, "Only full moons may be colored");
            check(tint == AstronomicalEvents.moonTint(time + 128 * 24000L, day % 8), "Calendar repeats");
        }
        check(AstronomicalEvents.moonTint(210000L, 0) == MoonTint.BLUE, "Blue moon example");
        check(AstronomicalEvents.moonTint(594000L, 0) == MoonTint.BLOOD, "Blood moon example");
        check(AstronomicalEvents.moonTint(594000L, 1) == MoonTint.NORMAL, "Phase gate");
        long[] coloredTimes = {402000L, 786000L, 1362000L, 2898000L};
        MoonTint[] newTints = {MoonTint.GOLD, MoonTint.JADE, MoonTint.VIOLET, MoonTint.ROSE};
        for (int i = 0; i < coloredTimes.length; i++) {
            check(AstronomicalEvents.moonTint(coloredTimes[i], 0) == newTints[i], "New color command");
            check(AstronomicalEvents.nightEventNames(coloredTimes[i], 0).contains(newTints[i].displayName), "Color notification");
            for (int phase = 1; phase < 8; phase++) {
                check(AstronomicalEvents.moonTint(coloredTimes[i], phase) == MoonTint.NORMAL, "New colors require full moon");
            }
        }
        check(AstronomicalEvents.moonTint(18000L, 0) == MoonTint.NORMAL, "First supermoon remains white");
        check(AstronomicalEvents.moonTint(1170000L, 0) == MoonTint.NORMAL, "Lunar eclipse has no competing color");
        check(AstronomicalEvents.isMeteorShower(186000L), "Shower example");
        check(AstronomicalEvents.nightEventNames(186000L, 7).contains("流星群"), "Shower notification");
        check(AstronomicalEvents.solarEclipseProgress(294000L) == 0.5F, "Solar maximum");
        check(AstronomicalEvents.solarEclipseProgress(291999L) == -1F, "Before solar eclipse");
        check(AstronomicalEvents.solarEclipseProgress(296001L) == -1F, "After solar eclipse");
        check(AstronomicalEvents.solarEclipseProgress(6000L) == -1F, "Ordinary noon");
        check(AstronomicalEvents.lunarEclipseStrength(1170000L) == 1F, "Lunar maximum");
        check(AstronomicalEvents.lunarEclipseStrength(1167999L) == 0F, "Before lunar eclipse");
        check(AstronomicalEvents.lunarEclipseStrength(1172001L) == 0F, "After lunar eclipse");
        check(AstronomicalEvents.lunarEclipseStrength(18000L) == 0F, "Ordinary full moon");
        check(AstronomicalEvents.nightEventNames(1165000L, 0).contains("月食"), "Lunar notification at nightfall");
        for (long t = 0; t < 64 * 24000L; t += 37) {
            float solar = AstronomicalEvents.solarEclipseProgress(t);
            float lunar = AstronomicalEvents.lunarEclipseStrength(t);
            check(solar == AstronomicalEvents.solarEclipseProgress(t + 64 * 24000L), "Solar periodicity");
            check(lunar == AstronomicalEvents.lunarEclipseStrength(t + 64 * 24000L), "Lunar periodicity");
            check(lunar >= 0F && lunar <= 1F, "Lunar intensity range");
            if (solar >= 0F) check((t / 24000L) % 8 == 4 && lunar == 0F, "Solar requires new moon");
            if (lunar > 0F) check((t / 24000L) % 8 == 0 && solar == -1F, "Lunar requires full moon");
        }
        int ordinary = countVisibleTicks(0), shower = countVisibleTicks(7);
        check(shower > ordinary * 6, "Showers must be noticeably more frequent");
        for (long t = 0; t < 24000L; t++) {
            var meteor = AstronomicalEvents.meteor(t);
            if (t < 13000 || t >= 23000) check(meteor == null, "No daytime streaks");
            if (meteor != null) {
                check(meteor.equals(AstronomicalEvents.meteor(t)), "Clients agree at same time");
                check(meteor.opacity() >= 0 && meteor.opacity() <= 1, "Bounded opacity");
                check(Double.isFinite(meteor.x()) && Double.isFinite(meteor.z()), "Finite position");
            }
        }
        System.out.println("Astronomical event calendar, phase gates, visibility and determinism passed.");
    }

    private static int countVisibleTicks(int day) {
        int count = 0;
        for (long t = day * 24000L; t < (day + 1L) * 24000L; t++) {
            if (AstronomicalEvents.meteor(t) != null) count++;
        }
        return count;
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
