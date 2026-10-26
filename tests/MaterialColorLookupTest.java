import java.util.Map;
import the_four_primitives_and_weapons.client.MaterialColorLookup;

public class MaterialColorLookupTest {
    public static void main(String[] args) {
        Map<String, Integer> colors = Map.of(
            MaterialColorLookup.resourceKey("tconstruct", "wood"), 0xFF876627,
            MaterialColorLookup.resourceKey("tconstruct", "wood/crimson"), 0xFF4B2737,
            MaterialColorLookup.resourceKey("tconstruct", "wood/warped"), 0xFF1F5752,
            MaterialColorLookup.resourceKey("ticex", "test_metal"), 0xFF4080C0);
        check(colors, "tconstruct:wood", 0xFF876627);
        check(colors, "tconstruct:wood#crimson", 0xFF4B2737);
        check(colors, "tconstruct:wood#warped", 0xFF1F5752);
        check(colors, "tconstruct:wood#unknown", 0xFF876627);
        check(colors, "ticex:test_metal", 0xFF4080C0);
        check(colors, "missing:material", 0xFFFFFFFF);
        System.out.println("Material colors: wood variants, addon metal and fallback passed");
    }

    private static void check(Map<String, Integer> colors, String material, int expected) {
        if (MaterialColorLookup.color(colors, material) != expected) throw new AssertionError(material);
    }
}
