package the_four_primitives_and_weapons.integration.casting;

import com.google.gson.*;
import java.util.*;

/** Builds additive Tinkers data from actual ingot casting recipes, never fluid-name guesses. */
public final class ExternalMetalData {
    private static final String NS = "the_four_primitives_and_weapons";
    private ExternalMetalData() {}

    public static Map<String, JsonObject> generate(Collection<JsonObject> recipes, JsonArray configured) {
        Set<String> nativeFluids = new HashSet<>();
        for (JsonObject recipe : recipes) {
            if ("tconstruct:material_fluid".equals(string(recipe, "type")) && recipe.has("fluid") && recipe.get("fluid").isJsonObject())
                nativeFluids.add(fluidKey(recipe.getAsJsonObject("fluid")));
        }
        Map<String, JsonObject> output = new TreeMap<>();
        JsonObject names = new JsonObject();
        Set<String> generatedFluids = new HashSet<>();
        List<JsonObject> entries = new ArrayList<>();
        // Explicit entries win over automatic discovery, but not existing Tinkers materials.
        for (JsonElement entry : configured) entries.add(entry.getAsJsonObject());
        for (JsonObject recipe : recipes) {
            if (!"tconstruct:casting_table".equals(string(recipe, "type"))
                    || !recipe.has("fluid") || !recipe.has("result") || !recipe.has("cast")) continue;
            if (!recipe.get("fluid").isJsonObject()) continue;
            JsonObject sourceFluid = recipe.getAsJsonObject("fluid");
            if (!sourceFluid.has("amount") || fluidKey(sourceFluid).isEmpty()) continue;
            int sourceAmount = sourceFluid.get("amount").getAsInt();
            if (sourceAmount <= 0 || sourceAmount > Integer.MAX_VALUE / 3) continue;
            JsonElement cast = recipe.get("cast");
            if (!cast.isJsonObject() || !(string(cast.getAsJsonObject(), "tag").equals("tconstruct:casts/multi_use/ingot")
                    || string(cast.getAsJsonObject(), "item").equals("tconstruct:ingot_cast"))) continue;
            JsonElement result = recipe.get("result");
            if (!result.isJsonPrimitive() && !result.isJsonObject()) continue;
            if (result.isJsonObject() && integer(result.getAsJsonObject(), "count", 1) != 1) continue;
            JsonObject ingredient = new JsonObject();
            String name;
            if (result.isJsonObject() && string(result.getAsJsonObject(), "tag").startsWith("forge:ingots/")) {
                name = "forge:" + string(result.getAsJsonObject(), "tag").substring("forge:ingots/".length());
                ingredient.addProperty("tag", string(result.getAsJsonObject(), "tag"));
            } else {
                String item = result.isJsonPrimitive() ? result.getAsString() : string(result.getAsJsonObject(), "item");
                if (!item.matches("[a-z0-9_.-]+:[a-z0-9_./-]+") || !item.endsWith("_ingot")) continue;
                name = item.substring(0, item.length() - "_ingot".length());
                ingredient.addProperty("item", item);
            }
            JsonObject entry = new JsonObject();
            entry.addProperty("name", name);
            entry.add("ingot", ingredient);
            entry.add("fluid", recipe.get("fluid").deepCopy());
            if (recipe.has("conditions")) entry.add("conditions", recipe.get("conditions").deepCopy());
            entries.add(entry);
        }
        for (JsonObject entry : entries) {
            String name = string(entry, "name");
            if (!name.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")) throw new IllegalArgumentException("Invalid metal name: " + name);
            JsonObject fluid = entry.getAsJsonObject("fluid");
            int amount = fluid.get("amount").getAsInt();
            if (amount <= 0 || amount > Integer.MAX_VALUE / 3 || fluidKey(fluid).isEmpty())
                throw new IllegalArgumentException("Invalid ingot fluid: " + name);
            String key = fluidKey(fluid);
            if (nativeFluids.contains(key) || !generatedFluids.add(key)) continue;
            // Hex encoding avoids ID collisions, including slash/underscore and namespace collisions.
            String path = "external_" + java.util.HexFormat.of().formatHex(name.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            String material = NS + ":" + path;
            names.addProperty("material." + NS + "." + path,
                entry.has("display_name") ? entry.get("display_name").getAsString() : name);
            JsonObject definition = json("{\"craftable\":false,\"hidden\":false,\"tier\":2,\"sortOrder\":100}");
            definition.addProperty("tier", integer(entry, "tier", 2));
            output.put("data/"+NS+"/tinkering/materials/definition/"+path+".json", definition);
            int durability = integer(entry, "durability", 250);
            float attack = number(entry, "attack", 2);
            if (durability <= 0 || !Float.isFinite(attack) || attack < 0) throw new IllegalArgumentException("Invalid stats: " + name);
            JsonObject stats = json("{\"stats\":{\"tconstruct:head\":{\"durability\":250,\"melee_attack\":2,\"mining_speed\":6,\"mining_tier\":\"minecraft:iron\"},\"tconstruct:handle\":{\"durability\":0,\"melee_damage\":0,\"melee_speed\":0,\"mining_speed\":0},\"tconstruct:binding\":{}}}");
            JsonObject head = stats.getAsJsonObject("stats").getAsJsonObject("tconstruct:head");
            head.addProperty("durability", durability);
            head.addProperty("melee_attack", attack);
            output.put("data/"+NS+"/tinkering/materials/stats/"+path+".json", stats);
            output.put("data/"+NS+"/tinkering/materials/traits/"+path+".json", json("{\"default\":[]}"));
            JsonObject casting = new JsonObject();
            casting.addProperty("type", "tconstruct:material_fluid");
            casting.add("fluid", fluid.deepCopy());
            casting.addProperty("output", material);
            casting.addProperty("temperature", integer(entry, "temperature", 800));
            JsonArray conditions = entry.has("conditions") ? entry.getAsJsonArray("conditions").deepCopy() : new JsonArray();
            conditions.add(json("{\"type\":\"forge:mod_loaded\",\"modid\":\"tconstruct\"}"));
            casting.add("conditions", conditions);
            output.put("data/"+NS+"/recipes/external_metals/"+path+"_fluid.json", casting);
            JsonObject ingot = new JsonObject();
            ingot.addProperty("type", "tconstruct:material");
            ingot.add("ingredient", entry.get("ingot").deepCopy());
            ingot.addProperty("material", material);
            ingot.addProperty("needed", 1);
            ingot.addProperty("value", 1);
            ingot.add("conditions", conditions.deepCopy());
            output.put("data/"+NS+"/recipes/external_metals/"+path+"_ingot.json", ingot);
            JsonObject color = new JsonObject();
            color.addProperty("color", entry.has("color") ? entry.get("color").getAsString() : "FFD8D8D8");
            color.add("fallbacks", JsonParser.parseString("[\"metal\"]"));
            output.put("assets/"+NS+"/tinkering/materials/"+path+".json", color);
        }
        if (!names.entrySet().isEmpty()) {
            output.put("assets/"+NS+"/lang/en_us.json", names);
            output.put("assets/"+NS+"/lang/ja_jp.json", names.deepCopy());
        }
        return output;
    }

    private static String fluidKey(JsonObject fluid) {
        return fluid.has("tag") ? "tag:"+fluid.get("tag").getAsString()
            : fluid.has("name") ? "name:"+fluid.get("name").getAsString() : "";
    }
    private static String string(JsonObject obj, String field) { return obj.has(field) ? obj.get(field).getAsString() : ""; }
    private static int integer(JsonObject obj, String field, int fallback) { return obj.has(field) ? obj.get(field).getAsInt() : fallback; }
    private static float number(JsonObject obj, String field, float fallback) { return obj.has(field) ? obj.get(field).getAsFloat() : fallback; }
    private static JsonObject json(String value) { return JsonParser.parseString(value).getAsJsonObject(); }
}
