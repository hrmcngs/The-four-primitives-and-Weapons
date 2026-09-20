import java.util.*;
import the_four_primitives_and_weapons.performance.*;
import the_four_primitives_and_weapons.weather.*;

public final class RuntimePerformanceTest {
    private static void check(boolean value, String message) { if (!value) throw new AssertionError(message); }
    public static void main(String[] args) throws Exception {
        TickPositionCache<String> cache = new TickPositionCache<>();
        Object world = new Object();
        cache.begin(world, 1, 13000, 0);
        cache.put(-1L, "rain");
        cache.begin(world, 1, 13000, 0);
        check("rain".equals(cache.get(-1L)), "Same-tick weather lookup reused");
        cache.begin(world, 1, 13000, 1); check(cache.get(-1L) == null, "Weather override invalidates immediately");
        cache.put(-1L, "rain"); cache.begin(world, 1, 23000, 1); check(cache.get(-1L) == null, "Time command invalidates immediately");
        cache.put(-1L, "rain"); cache.begin(world, 2, 23000, 1); check(cache.get(-1L) == null, "Tick boundary invalidates");
        cache.put(-1L, "rain"); cache.begin(new Object(), 2, 23000, 1); check(cache.get(-1L) == null, "World switch invalidates");
        for (long i = 0; i < 20000; i++) cache.put(i, Long.toString(i));
        for (long i = 0; i < 20000; i++) check(cache.get(i) == null || cache.get(i).equals(Long.toString(i)), "Hash collisions never return another position");
        TickWorkQueue queue = new TickWorkQueue();
        List<Integer> calls = new ArrayList<>();
        queue.add(3, () -> calls.add(3));
        queue.add(1, () -> { calls.add(1); queue.add(1, () -> calls.add(2)); });
        queue.add(3, () -> calls.add(4));
        queue.tick(); check(calls.equals(List.of(1)), "Delay 1 and reentrant work");
        queue.tick(); check(calls.equals(List.of(1, 2)), "Nested task runs next tick");
        queue.tick(); check(calls.equals(List.of(1, 2, 3, 4)), "Stable equal-deadline ordering");
        queue.add(0, () -> calls.add(5)); queue.tick(); check(calls.get(4) == 5, "Zero delay does not leak");
        queue.add(1, () -> calls.add(6)); queue.clear(); queue.tick(); check(calls.size() == 5, "Server shutdown clears tasks");
        int[] completed = {0};
        List<Thread> producers = new ArrayList<>();
        for (int t = 0; t < 4; t++) {
            Thread producer = new Thread(() -> { for (int i = 0; i < 1000; i++) queue.add(2, () -> completed[0]++); });
            producers.add(producer); producer.start();
        }
        for (Thread producer : producers) producer.join();
        queue.tick(); check(completed[0] == 0, "Concurrent tasks retain delay");
        queue.tick(); check(completed[0] == 4000, "Concurrent submissions run once");
        WeatherSkyMesh mesh = new WeatherSkyMesh();
        for (var kind : WeatherKind.values()) for (boolean clouds : new boolean[] {false,true}) {
            var appearance = WeatherSky.appearance(kind);
            check(appearance == WeatherSky.appearance(kind), "Immutable sky profile reused");
            check(WeatherAtmosphere.profile(kind, 0) == WeatherAtmosphere.profile(kind, 0), "Immutable ambience reused");
            mesh.update(1234.5, -789.1, 24000.25, appearance, clouds);
            for (int r = 0; r < 20; r++) for (int s = 0; s < 96; s++) {
                boolean reference = mesh.alpha(WeatherSkyMesh.index(r,s)) != 0 || mesh.alpha(WeatherSkyMesh.index(r+1,s)) != 0
                    || mesh.alpha(WeatherSkyMesh.index(r+1,s+1)) != 0 || mesh.alpha(WeatherSkyMesh.index(r,s+1)) != 0;
                check(mesh.visible(r,s) == reference, "Only fully transparent faces are omitted");
            }
            for (int r = 0; r <= 20; r++) for (int s = 0; s <= 96; s++) {
                int i = WeatherSkyMesh.index(r,s);
                double elevation = -.08 + (Math.PI / 2 + .08) * r / 20;
                double angle = Math.PI * 2 * (s % 96) / 96;
                float x = (float)(Math.cos(angle)*Math.cos(elevation));
                float y = (float)Math.sin(elevation);
                float z = (float)(Math.sin(angle)*Math.cos(elevation));
                check(x == mesh.x(i) && y == mesh.y(i) && z == mesh.z(i), "Geometry and seam unchanged");
                float cloud = clouds ? WeatherSky.density(x*9+1234.5*.0008,z*9-789.1*.0008,24000.25,appearance) : 0;
                cloud *= Math.min(1F,Math.max(0F,y*7));
                float alpha = Math.max(cloud,appearance.haze()*(1-Math.max(0F,y)*.65F));
                if(y<0) alpha*=Math.max(0F,1+y/.08F);
                check(Math.round(alpha*255)==mesh.alpha(i), "Cloud/haze alpha unchanged");
            }
        }
        System.out.println("Position cache invalidation/collisions, scheduler, transparent-face culling and weather mesh equivalence checks passed.");
        System.out.println("Sky: 7680 repeated vertex calculations -> 2016 unique vertices; geometry trigonometry precomputed once.");
    }
}
