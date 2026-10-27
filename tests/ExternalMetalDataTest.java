import com.google.gson.*;
import java.util.*;
import the_four_primitives_and_weapons.integration.casting.ExternalMetalData;

public class ExternalMetalDataTest {
    static JsonObject obj(String json) { return JsonParser.parseString(json).getAsJsonObject(); }
    static void check(boolean valid, String message) { if (!valid) throw new AssertionError(message); }
    public static void main(String[] args) {
        var alloy = obj("{\"type\":\"tconstruct:casting_table\",\"cast\":{\"tag\":\"tconstruct:casts/multi_use/ingot\"},\"fluid\":{\"tag\":\"forge:molten_external_alloy\",\"amount\":144},\"result\":{\"tag\":\"forge:ingots/external_alloy\"},\"conditions\":[{\"type\":\"forge:mod_loaded\",\"modid\":\"example\"}]}");
        var nativeCasting = alloy.deepCopy();
        nativeCasting.getAsJsonObject("fluid").addProperty("tag", "forge:molten_iron");
        var nativeMaterial = obj("{\"type\":\"tconstruct:material_fluid\",\"fluid\":{\"tag\":\"forge:molten_iron\",\"amount\":90},\"output\":\"tconstruct:iron\"}");
        var output = ExternalMetalData.generate(List.of(alloy, alloy.deepCopy(), nativeCasting, nativeMaterial), new JsonArray());
        var fluids = output.entrySet().stream().filter(e -> e.getKey().endsWith("_fluid.json")).map(Map.Entry::getValue).toList();
        check(fluids.size() == 1, "must deduplicate ingot casts and preserve native materials");
        check(fluids.get(0).getAsJsonObject("fluid").get("amount").getAsInt() == 144, "must preserve external ingot volume");
        check(fluids.get(0).getAsJsonArray("conditions").size() == 2, "must preserve source conditions");
        check(output.keySet().stream().anyMatch(n -> n.contains("/materials/stats/")), "missing usable material stats");
        check(output.keySet().stream().anyMatch(n -> n.startsWith("assets/") && n.contains("/materials/")), "missing client color");
        var entries = JsonParser.parseString("[{\"name\":\"custom:metal\",\"fluid\":{\"name\":\"other:liquid\",\"amount\":90},\"ingot\":{\"item\":\"custom:metal_ingot\"},\"durability\":777,\"attack\":4,\"color\":\"FF123456\"}]").getAsJsonArray();
        var configured = ExternalMetalData.generate(List.of(), entries);
        var stats = configured.entrySet().stream().filter(e -> e.getKey().contains("/materials/stats/")).findFirst().orElseThrow().getValue();
        check(stats.getAsJsonObject("stats").getAsJsonObject("tconstruct:head").get("durability").getAsInt() == 777, "configured performance ignored");
        var byItem = alloy.deepCopy();
        byItem.add("result", obj("{\"item\":\"example:exotic_ingot\"}"));
        check(!ExternalMetalData.generate(List.of(byItem), new JsonArray()).isEmpty(), "direct item ingots must work");
        byItem.getAsJsonObject("result").addProperty("count", 2);
        check(ExternalMetalData.generate(List.of(byItem), new JsonArray()).isEmpty(), "multi-ingot volume cannot be guessed");
        var notMetal = alloy.deepCopy();
        notMetal.add("result", obj("{\"item\":\"minecraft:apple\"}"));
        check(ExternalMetalData.generate(List.of(notMetal), new JsonArray()).isEmpty(), "must not turn arbitrary fluids into metal");
        entries.get(0).getAsJsonObject().getAsJsonObject("fluid").addProperty("amount", 0);
        try { ExternalMetalData.generate(List.of(), entries); throw new AssertionError("zero amount accepted"); }
        catch (IllegalArgumentException expected) { }
        System.out.println("External metals: discovery, native preservation, volume, conditions, config and invalid inputs passed");
    }
}
