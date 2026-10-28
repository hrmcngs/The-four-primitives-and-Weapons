package the_four_primitives_and_weapons.client.tooltip;

/** One page per wheel unit, rather than one page per fractional trackpad event. */
public final class PageScrollAccumulator {
    private double remainder;
    private long lastInput;
    private Object screen;

    public boolean shouldTurn(double delta, long now, Object currentScreen) {
        if (!Double.isFinite(delta) || delta == 0) return false;
        if (screen != currentScreen || now - lastInput > 250 || now < lastInput
                || Math.signum(remainder) != Math.signum(delta)) remainder = 0;
        screen = currentScreen;
        lastInput = now;
        remainder += delta;
        if (Math.abs(remainder) < 1 - 1.0e-9) return false;
        remainder -= Math.copySign(Math.floor(Math.abs(remainder) + 1.0e-9), remainder);
        return true;
    }
}
