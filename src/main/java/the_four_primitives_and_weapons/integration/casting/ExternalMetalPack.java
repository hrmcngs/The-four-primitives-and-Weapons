package the_four_primitives_and_weapons.integration.casting;

import com.google.gson.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.resource.PathPackResources;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;

/** Optional, additive compatibility pack. No dependency on Tinkers classes at load time. */
public final class ExternalMetalPack {
    private static Path generated;
    private ExternalMetalPack() {}

    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (!ModList.get().isLoaded("tconstruct")) return;
        event.addRepositorySource(consumer -> {
            try {
                Path root = prepare().resolve(event.getPackType() == PackType.SERVER_DATA ? "server" : "client");
                Pack pack = Pack.readMetaAndCreate("the_four_primitives_and_weapons:external_metals",
                    Component.literal("External metal casting compatibility"), true,
                    id -> new PathPackResources(id, true, root), event.getPackType(), Pack.Position.BOTTOM, PackSource.BUILT_IN);
                if (pack != null) consumer.accept(pack);
            } catch (IOException | RuntimeException error) {
                TheFourPrimitivesAndWeaponsMod.LOGGER.error("Cannot generate external metal casting compatibility", error);
            }
        });
    }

    private static synchronized Path prepare() throws IOException {
        if (generated != null) return generated;
        List<JsonObject> recipes = new ArrayList<>();
        for (var mod : ModList.get().getModFiles()) {
            Path data = mod.getFile().findResource("data");
            if (!Files.isDirectory(data)) continue;
            try (var namespaces = Files.list(data)) {
                for (Path namespace : namespaces.toList()) {
                    Path directory = namespace.resolve("recipes");
                    if (!Files.isDirectory(directory)) continue;
                    try (var files = Files.walk(directory)) {
                        for (Path file : files.filter(p -> p.toString().endsWith(".json")).toList()) {
                            try {
                                String text = Files.readString(file);
                                if (!text.contains("tconstruct:casting_table") && !text.contains("tconstruct:material_fluid")) continue;
                                JsonElement parsed = JsonParser.parseString(text);
                                if (parsed.isJsonObject()) recipes.add(parsed.getAsJsonObject());
                            } catch (IOException | RuntimeException error) {
                                TheFourPrimitivesAndWeaponsMod.LOGGER.warn("Cannot inspect external casting recipe {}", file, error);
                            }
                        }
                    }
                }
            }
        }
        Path config = FMLPaths.CONFIGDIR.get().resolve("the_four_primitives_and_weapons/external_metals.json");
        JsonArray configured = new JsonArray();
        if (Files.exists(config)) configured = JsonParser.parseString(Files.readString(config)).getAsJsonArray();
        Map<String, JsonObject> resources = ExternalMetalData.generate(recipes, configured);
        Path root = Files.createTempDirectory("maw-external-metals-");
        for (String side : List.of("server", "client")) {
            Path directory = root.resolve(side);
            Files.createDirectories(directory);
            Files.writeString(directory.resolve("pack.mcmeta"), "{\"pack\":{\"pack_format\":"
                + net.minecraft.SharedConstants.getCurrentVersion().getPackVersion(
                    side.equals("server") ? PackType.SERVER_DATA : PackType.CLIENT_RESOURCES)
                + ",\"description\":\"External metal casting compatibility\"}}");
        }
        for (var resource : resources.entrySet()) {
            String side = resource.getKey().startsWith("data/") ? "server" : "client";
            Path file = root.resolve(side).resolve(resource.getKey());
            Files.createDirectories(file.getParent());
            Files.writeString(file, resource.getValue().toString());
        }
        try (var files = Files.walk(root)) {
            files.sorted(Comparator.comparingInt(Path::getNameCount)).forEach(p -> p.toFile().deleteOnExit());
        }
        generated = root;
        TheFourPrimitivesAndWeaponsMod.LOGGER.info("Generated {} external metal compatibility resources from {} casting recipes", resources.size(), recipes.size());
        return root;
    }
}
