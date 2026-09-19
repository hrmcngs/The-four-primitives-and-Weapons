import the_four_primitives_and_weapons.world.AstronomicalEvents;
import the_four_primitives_and_weapons.world.AstronomicalEvents.MoonTint;

public class AstronomicalEventsTest {
    public static void main(String[] args) {
        for (int day = 0; day < 128; day++) {
            long time = day * 24000L + 18000L;
            MoonTint tint = AstronomicalEvents.moonTint(time, day % 8);
            check(day % 8 == 0 || tint == MoonTint.NORMAL, "Only full moons may be colored");
            check(tint == AstronomicalEvents.moonTint(time + 64 * 24000L, day % 8), "Calendar repeats");
        }
        check(AstronomicalEvents.moonTint(210000L, 0) == MoonTint.BLUE, "Blue moon example");
        check(AstronomicalEvents.moonTint(594000L, 0) == MoonTint.BLOOD, "Blood moon example");
        check(AstronomicalEvents.moonTint(594000L, 1) == MoonTint.NORMAL, "Phase gate");
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
