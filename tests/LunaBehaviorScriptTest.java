import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import the_four_primitives_and_weapons.util.LunaBehaviorScript;
import static the_four_primitives_and_weapons.util.LunaBehaviorScript.Setting.*;

public final class LunaBehaviorScriptTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    private static Map<String, Object> sensors() {
        Map<String, Object> values = new HashMap<>();
        values.put("owner-health-ratio", 1.0);
        values.put("owner-attack-delay", 12.5);
        values.put("target-distance", 10.0);
        values.put("owner-attacked-target", false);
        values.put("target-attacked-owner", false);
        values.put("luna-element-level", 5);
        values.put("book-element-level", 3);
        return values;
    }
    private static void rejects(String script) {
        try {
            LunaBehaviorScript.parse(script, message -> {});
            throw new AssertionError("Invalid script was accepted: " + script);
        } catch (IllegalArgumentException expected) { }
    }
    public static void main(String[] args) throws Exception {
        String source = Files.readString(Path.of("src/main/resources/data/the_four_primitives_and_weapons/luna/formula.lisp"));
        LunaBehaviorScript defaults = LunaBehaviorScript.parse(source, message -> { throw new AssertionError(message); });
        for (var setting : LunaBehaviorScript.Setting.values()) {
            check(defaults.value(setting) == setting.fallback, "Default changed: " + setting);
        }
        Map<String, Object> state = sensors();
        check(defaults.damage(state) == 8, "Original damage");
        check(defaults.cooldown(state) == 18, "Original fractional attack-delay rounding");
        state.put("owner-attack-delay", 2.0);
        check(defaults.cooldown(state) == 10, "Original minimum cooldown");
        check(defaults.targetPriority(state) == 1, "Background threat priority");
        state.put("target-attacked-owner", true);
        check(defaults.targetPriority(state) == 2, "Retaliation priority");
        state.put("owner-attacked-target", true);
        check(defaults.targetPriority(state) == 3, "Owner attack has highest priority");
        check(defaults.attackEnabled(state) && defaults.preferLunaElement(state), "Original decisions");
        for (int luna = 0; luna <= 20; luna++) for (int book = 0; book <= 20; book++) {
            state.put("luna-element-level", luna);
            state.put("book-element-level", book);
            int expected = Math.max(luna, book) + Math.max(Math.min(luna, book) / 2, 1);
            check(defaults.combinedElementLevel(state, luna, book) == expected, "Element merge changed");
        }
        String custom = """
                (define anchor-side -2)
                (define particle-interval 0)
                (rule target-priority (if (< owner-health-ratio 0.3)
                    (if target-attacked-owner 9 1) (if owner-attacked-target 3 1)))
                (rule attack-enabled (< target-distance 6))
                (rule damage (* laser-damage 2))
                (rule cooldown 25)
                (rule prefer-luna-element false)
                (rule combined-element-level (+ luna-element-level book-element-level))
                """;
        LunaBehaviorScript edited = LunaBehaviorScript.parse(custom, message -> { throw new AssertionError(message); });
        state = sensors();
        state.put("owner-health-ratio", 0.2);
        state.put("target-attacked-owner", true);
        check(edited.targetPriority(state) == 9, "Low-health defense rule");
        check(!edited.attackEnabled(state), "Distance-based hold fire");
        state.put("target-distance", 4.0);
        check(edited.attackEnabled(state), "Distance-based enable fire");
        check(edited.damage(state) == 16 && edited.cooldown(state) == 25, "Changed damage/cooldown");
        check(!edited.preferLunaElement(state), "Book element preference");
        check(edited.combinedElementLevel(state, 5, 3) == 8, "Changed element formula");
        check(edited.value(ANCHOR_SIDE) == -2 && edited.ticks(PARTICLE_INTERVAL) == 0, "Position and effects controls");
        check(state.size() == 7, "Evaluation must not write constants into caller state");
        LunaBehaviorScript fresh = LunaBehaviorScript.parse("", message -> {});
        check(fresh.value(ANCHOR_SIDE) == 1.5 && fresh.damage(state) == 8, "Removed overrides must not leak across reloads");
        check(fresh.attackEnabled(state), "Removed rules must not leak across reloads");
        for (String invalid : List.of("(define scan-interval 0)", "(define scan-interval 1.5)",
                "(define laser-step 0)", "(define rotation-blend 2)", "(define idle-speed -1)",
                "(define typo 1)", "(rule damage missing-sensor)", "(rule damage (unknown 1))",
                "(rule damage (+ 1))", "(define scan-range 20", ")", "(rule damage 8))",
                "(define scan-range true)", "(rule damage 8) (rule damage 9)", "(rule damage \"bad\")")) rejects(invalid);
        List<String> warnings = new ArrayList<>();
        LunaBehaviorScript badResults = LunaBehaviorScript.parse("""
                (rule damage (* 1.0e308 1.0e308))
                (rule cooldown 0)
                (rule attack-enabled 1)
                (rule combined-element-level -1)
                """, warnings::add);
        check(badResults.damage(state) == 8 && badResults.damage(state) == 8, "Nonfinite damage fallback");
        check(warnings.size() == 1, "Invalid rule must not flood logs every tick");
        check(badResults.cooldown(state) == 18, "Zero cooldown fallback");
        check(badResults.attackEnabled(state), "Wrong boolean type fallback");
        check(badResults.combinedElementLevel(state, 5, 3) == 6, "Negative level fallback");
        check(defaults.damage(state) == 8, "Another configuration must not affect defaults");
        System.out.println("Luna Lisp: defaults, 441 element cases, conditional rules, reload isolation and invalid-input checks passed");
    }
}
