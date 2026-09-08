import the_four_primitives_and_weapons.util.NinjatoVaultGesture;

/** Run with scripts/test-ninjato-vault.sh; no Minecraft client required. */
public final class NinjatoVaultGestureTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        NinjatoVaultGesture gesture = new NinjatoVaultGesture();
        check(!gesture.update(0, 80), "Looking down without raising must not jump");
        check(!gesture.update(1, -70), "Raising alone must not jump");
        check(!gesture.update(3, 0), "Crossing the horizon must not jump");
        check(gesture.update(6, 70), "A fast up-to-down stroke must jump");
        check(!gesture.update(7, 80), "Holding down must not jump twice");

        gesture.update(10, -60);
        check(!gesture.update(21, 80), "A slow stroke must not jump");
        gesture.update(30, -60);
        check(gesture.update(40, 55), "The maximum allowed stroke duration should work");

        gesture.update(50, -60);
        gesture.update(150, -60);
        check(gesture.update(155, 80), "Holding up before a quick stroke must work");
        gesture.update(160, -60);
        check(!gesture.update(161, Float.NaN), "Invalid pitch must not trigger");
        check(!gesture.update(162, Float.POSITIVE_INFINITY), "Infinite pitch must not trigger");
        check(!new NinjatoVaultGesture().update(163, 80), "Releasing and restarting must reset the stroke");
        System.out.println("Ninjato vault gesture checks passed");
    }
}
