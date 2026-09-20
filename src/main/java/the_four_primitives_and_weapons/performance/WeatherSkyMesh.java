package the_four_primitives_and_weapons.performance;

import the_four_primitives_and_weapons.weather.WeatherSky;

/** Fixed sphere geometry and reusable opacity storage. Owned by the render thread. */
public final class WeatherSkyMesh {
    public static final int RINGS = 20, SEGMENTS = 96;
    private final float[] xs = new float[(RINGS + 1) * SEGMENTS];
    private final float[] ys = new float[xs.length], zs = new float[xs.length];
    private final int[] alpha = new int[xs.length];
    public WeatherSkyMesh() {
        for (int ring = 0; ring <= RINGS; ring++) {
            double elevation = -.08 + (Math.PI / 2 + .08) * ring / RINGS;
            for (int segment = 0; segment < SEGMENTS; segment++) {
                double angle = Math.PI * 2 * segment / SEGMENTS;
                int i = index(ring, segment);
                xs[i] = (float) (Math.cos(angle) * Math.cos(elevation));
                ys[i] = (float) Math.sin(elevation);
                zs[i] = (float) (Math.sin(angle) * Math.cos(elevation));
            }
        }
    }
    public static int index(int ring, int segment) { return ring * SEGMENTS + segment % SEGMENTS; }
    public float x(int i) { return xs[i]; }
    public float y(int i) { return ys[i]; }
    public float z(int i) { return zs[i]; }
    public int alpha(int i) { return alpha[i]; }
    public boolean visible(int ring, int segment) {
        return alpha[index(ring, segment)] != 0 || alpha[index(ring + 1, segment)] != 0
            || alpha[index(ring + 1, segment + 1)] != 0 || alpha[index(ring, segment + 1)] != 0;
    }
    public void update(double cameraX, double cameraZ, double time, WeatherSky.Appearance a, boolean clouds) {
        for (int i = 0; i < xs.length; i++) {
            float y = ys[i];
            float cloud = clouds ? WeatherSky.density(xs[i] * 9 + cameraX * .0008, zs[i] * 9 + cameraZ * .0008, time, a) : 0;
            cloud *= Math.min(1F, Math.max(0F, y * 7));
            float haze = a.haze() * (1 - Math.max(0F, y) * .65F);
            float opacity = Math.max(cloud, haze);
            if (y < 0) opacity *= Math.max(0F, 1 + y / .08F);
            alpha[i] = Math.round(opacity * 255);
        }
    }
}
