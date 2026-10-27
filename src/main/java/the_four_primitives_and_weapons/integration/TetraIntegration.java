package the_four_primitives_and_weapons.integration;

import java.util.Set;
import java.util.function.BiFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TetraIntegration {
    private static final Set<String> PRESERVED = Set.of("display", "ElementType", "ElementLevel", "ElementType2",
        "ElementLevel2", "ElementDamageKind", "TFPWWeaponGrowth", "Unbreakable");
    private TetraIntegration() {}

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        if (!ModList.get().isLoaded("tetra")) return;
        event.enqueueWork(() -> {
            try {
                Class<?> registryClass = Class.forName("se.mickelus.tetra.module.ItemUpgradeRegistry");
                Object registry = registryClass.getField("instance").get(null);
                registryClass.getMethod("registerReplacementHook", BiFunction.class).invoke(registry,
                    (BiFunction<ItemStack, ItemStack, ItemStack>) TetraIntegration::preserveWeaponData);
            } catch (ReflectiveOperationException | LinkageError error) {
                TheFourPrimitivesAndWeaponsMod.LOGGER.error("Tetra weapon conversion hook is unavailable", error);
            }
        });
    }

    public static ItemStack preserveWeaponData(ItemStack original, ItemStack converted) {
        var id = ForgeRegistries.ITEMS.getKey(original.getItem());
        if (id == null || !id.getNamespace().equals(TheFourPrimitivesAndWeaponsMod.MODID)) return converted;
        CompoundTag output = converted.getOrCreateTag();
        output.putString("TFPWTetraOrigin", id.toString());
        output.putString("TFPWTetraType", "katana");
        CompoundTag input = original.getTag();
        if (input != null) for (String key : PRESERVED) {
            if (input.contains(key)) output.put(key, input.get(key).copy());
        }
        // Tetra owns modular stats and enchantment transfer. Do not copy fixed material/attribute overrides.
        int oldMax = original.getMaxDamage();
        int newMax = converted.getMaxDamage();
        if (oldMax > 0 && newMax > 0 && original.getDamageValue() > 0)
            converted.setDamageValue((int) Math.min(newMax, ((long)original.getDamageValue() * newMax + oldMax - 1) / oldMax));
        return converted;
    }
}
