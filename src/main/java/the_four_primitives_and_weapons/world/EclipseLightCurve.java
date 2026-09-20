package the_four_primitives_and_weapons.world;

public final class EclipseLightCurve {
    public static float strength(float progress) {
        if (!Float.isFinite(progress) || progress <= 0 || progress >= 1) return 0;
        double sine = Math.sin(Math.PI * progress);
        return (float) (sine * sine);
    }
    private EclipseLightCurve() { }
}
