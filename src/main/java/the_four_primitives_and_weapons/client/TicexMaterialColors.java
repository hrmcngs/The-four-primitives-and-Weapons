package the_four_primitives_and_weapons.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.init.TicexCastingRegistrar;

/** Uses Tinkers' resource-pack material colors without requiring its Java API. */
@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class TicexMaterialColors {
    private static volatile Map<String, Integer> colors = Map.of();

    @SubscribeEvent
    public static void reload(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new SimplePreparableReloadListener<Map<String, Integer>>() {
            @Override
            protected Map<String, Integer> prepare(ResourceManager manager, ProfilerFiller profiler) {
                Map<String, Integer> loaded = new HashMap<>();
                manager.listResources("tinkering/materials", path -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
                    try (Reader reader = resource.openAsReader()) {
                        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                        if (json.has("color")) {
                            String hex = json.get("color").getAsString().replace("#", "");
                            int color = (int) Long.parseLong(hex, 16) | 0xFF000000;
                            String path = id.getPath();
                            loaded.put(id.getNamespace() + ":" + path.substring("tinkering/materials/".length(), path.length() - 5), color);
                        }
                    } catch (java.io.IOException | RuntimeException exception) {
                        org.slf4j.LoggerFactory.getLogger(TicexMaterialColors.class).warn("Cannot read material color {}", id, exception);
                    }
                });
                return Map.copyOf(loaded);
            }
            @Override
            protected void apply(Map<String, Integer> prepared, ResourceManager manager, ProfilerFiller profiler) {
                colors = prepared;
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void register(RegisterColorHandlersEvent.Item event) {
        if (!TicexCastingRegistrar.KATANA.isPresent()) return;
        event.register(TicexMaterialColors::partColor, TicexCastingRegistrar.KATANA_BLADE.get(),
            TicexCastingRegistrar.TSUKA.get(), TicexCastingRegistrar.TSUBA.get(), TicexCastingRegistrar.KASHIRA.get());
        event.register(TicexMaterialColors::toolColor, TicexCastingRegistrar.KATANA.get());
    }

    public static int partColor(ItemStack stack, int tint) {
        CompoundTag tag = stack.getTag();
        return tint == 0 && tag != null ? materialColor(tag.getString("Material")) : 0xFFFFFFFF;
    }

    public static int toolColor(ItemStack stack, int tint) {
        CompoundTag tag = stack.getTag();
        if (tag == null || tint < 0 || tint > 3) return 0xFFFFFFFF;
        return materialColor(tag.getList("tic_materials", Tag.TAG_STRING).getString(tint));
    }

    private static int materialColor(String material) {
        Integer exact = colors.get(material);
        if (exact != null) return exact;
        int variant = material.indexOf('#');
        return colors.getOrDefault(variant < 0 ? material : material.substring(0, variant), 0xFFFFFFFF);
    }
}
