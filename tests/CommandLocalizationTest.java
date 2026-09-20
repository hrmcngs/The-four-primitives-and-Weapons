import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

/** Render real translatable components in both languages, including nested arguments. */
public class CommandLocalizationTest {
    private static Path root = Path.of(".");
    private static final String PREFIX = "command.the_four_primitives_and_weapons.";
    private static final Pattern FORMAT = Pattern.compile("%(?:(\\d+)\\$)?s|%%");
    private static Map<String, String> read(String locale) throws Exception {
        var json = JsonParser.parseString(Files.readString(root.resolve(
            "src/main/resources/assets/the_four_primitives_and_weapons/lang/" + locale + ".json"))).getAsJsonObject();
        Map<String, String> result = new HashMap<>();
        json.entrySet().forEach(e -> result.put(e.getKey(), e.getValue().getAsString()));
        return result;
    }
    private static void use(Map<String, String> translations) {
        Language.inject(new Language() {
            public String getOrDefault(String key, String fallback) { return translations.getOrDefault(key, fallback); }
            public boolean has(String key) { return translations.containsKey(key); }
            public boolean isDefaultRightToLeft() { return false; }
            public FormattedCharSequence getVisualOrder(FormattedText text) { return FormattedCharSequence.EMPTY; }
        });
    }
    private static Set<Integer> arguments(String template) {
        Set<Integer> indices = new HashSet<>();
        var matcher = FORMAT.matcher(template);
        int sequential = 0;
        while (matcher.find()) {
            if (matcher.group().equals("%%")) continue;
            indices.add(matcher.group(1) == null ? sequential++ : Integer.parseInt(matcher.group(1)) - 1);
        }
        return indices;
    }
    private static boolean included(String key) {
        return key.startsWith(PREFIX) || key.startsWith("weather.the_four_primitives_and_weapons.")
            || key.startsWith("trait.the_four_primitives_and_weapons.");
    }
    public static void main(String[] args) throws Exception {
        if (args.length > 0) root = Path.of(args[0]);
        var japanese = read("ja_jp");
        var english = read("en_us");
        var before = Language.getInstance();
        int checked = 0;
        try {
            for (var entry : japanese.entrySet()) {
                String key = entry.getKey();
                if (!included(key)) continue;
                if (!english.containsKey(key)) throw new AssertionError("Missing English: " + key);
                var indices = arguments(entry.getValue());
                if (!indices.equals(arguments(english.get(key)))) throw new AssertionError("Argument mismatch: " + key);
                if (english.get(key).codePoints().anyMatch(c -> c >= 0x3000 && c <= 0x9fff))
                    throw new AssertionError("Japanese in English translation: " + key);
                Object[] values = new Object[indices.stream().mapToInt(i -> i + 1).max().orElse(0)];
                for (int i = 0; i < values.length; i++) values[i] = Component.literal("ARG_" + i);
                Component message = Component.translatable(key, values);
                for (var language : java.util.List.of(japanese, english)) {
                    use(language);
                    String rendered = message.getString();
                    if (rendered.contains(key) || FORMAT.matcher(rendered).find())
                        throw new AssertionError("Unresolved translation: " + key + " -> " + rendered);
                    for (int index : indices) if (!rendered.contains("ARG_" + index))
                        throw new AssertionError("Lost argument: " + key);
                }
                checked++;
            }
            // The same component must be rendered per recipient, not frozen on the server.
            Component weather = Component.translatable(PREFIX + "weather.status",
                Component.translatable("weather.the_four_primitives_and_weapons.drizzle"));
            use(english);
            if (!weather.getString().equals("Local weather: Drizzle")) throw new AssertionError(weather.getString());
            use(japanese);
            if (!weather.getString().equals("現在地: 霧雨")) throw new AssertionError(weather.getString());
            use(english);
            String damage = Component.translatable(PREFIX + "elementdamagecommand.2", 3, "ICE", 2, "12.5").getString();
            if (!damage.contains("12.5§a damage to 3 entities") || !damage.contains("ICE§a Lv.2"))
                throw new AssertionError("English argument order: " + damage);
            try (var files = Files.list(root.resolve("src/main/java/the_four_primitives_and_weapons/command"))) {
                var keys = Pattern.compile("Component\\.translatable\\(\"([^\"]+)\"");
                for (Path file : files.filter(p -> p.toString().endsWith(".java")).toList()) {
                    var matcher = keys.matcher(Files.readString(file));
                    while (matcher.find()) {
                        String key = matcher.group(1);
                        if (key.endsWith(".")) continue; // dynamic enum keys covered by catalog checks
                        if (!japanese.containsKey(key) || !english.containsKey(key))
                            throw new AssertionError("Missing command key: " + key);
                    }
                }
            }
        } finally { Language.inject(before); }
        System.out.println("Command localization passed: " + checked + " bilingual entries, nested components and reordered arguments.");
    }
}
