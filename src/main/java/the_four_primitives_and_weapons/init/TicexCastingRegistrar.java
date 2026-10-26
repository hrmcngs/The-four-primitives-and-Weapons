package the_four_primitives_and_weapons.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.*;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;

/** Native Tinkers parts, loaded when Tinkers Construct is present; addon materials use its registry. */
public final class TicexCastingRegistrar {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TheFourPrimitivesAndWeaponsMod.MODID);
    public static final RegistryObject<Item> KATANA_BLADE = ITEMS.register("ticex_katana_blade", TicexCastingRegistrar::blade);
    public static final RegistryObject<Item> KATANA_BLADE_CAST = ITEMS.register("ticex_katana_blade_cast", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSUKA = ITEMS.register("ticex_tsuka", () -> part("handle"));
    public static final RegistryObject<Item> TSUBA = ITEMS.register("ticex_tsuba", () -> part("binding"));
    public static final RegistryObject<Item> KASHIRA = ITEMS.register("ticex_kashira", () -> part("binding"));
    public static final RegistryObject<Item> TSUKA_CAST = ITEMS.register("ticex_tsuka_cast", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TSUBA_CAST = ITEMS.register("ticex_tsuba_cast", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KASHIRA_CAST = ITEMS.register("ticex_kashira_cast", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KATANA = ITEMS.register("ticex_katana", TicexCastingRegistrar::katana);
    private TicexCastingRegistrar() {}
    public static void register(IEventBus bus) {
        if (ModList.get().isLoaded("tconstruct")) ITEMS.register(bus);
    }
    private static Item blade() { return part("head"); }
    private static Item part(String kind) {
        try {
            Class<?> stats = Class.forName("slimeknights.tconstruct.library.materials.stats.MaterialStatsId");
            Object stat;
            if (kind.equals("binding")) {
                Class<?> singleton = Class.forName("slimeknights.tconstruct.tools.stats.StatlessMaterialStats");
                Object binding = singleton.getField("BINDING").get(null);
                stat = Class.forName("slimeknights.tconstruct.library.materials.stats.IMaterialStats")
                    .getMethod("getIdentifier").invoke(binding);
            } else {
                String name = kind.equals("head") ? "HeadMaterialStats" : "HandleMaterialStats";
                stat = Class.forName("slimeknights.tconstruct.tools.stats." + name).getField("ID").get(null);
            }
            return (Item)Class.forName("slimeknights.tconstruct.library.tools.part.ToolPartItem")
                .getConstructor(Item.Properties.class, stats).newInstance(new Item.Properties(), stat);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("TiCEX katana parts require a compatible Tinkers material API", e);
        }
    }
    private static Item katana() {
        try {
            Class<?> definition = Class.forName("slimeknights.tconstruct.library.tools.definition.ToolDefinition");
            Object tool = definition.getMethod("create", net.minecraft.resources.ResourceLocation.class)
                .invoke(null, new net.minecraft.resources.ResourceLocation(TheFourPrimitivesAndWeaponsMod.MODID, "ticex_katana"));
            return (Item)Class.forName("slimeknights.tconstruct.tools.item.ModifiableSwordItem")
                .getConstructor(Item.Properties.class, definition).newInstance(new Item.Properties().stacksTo(1), tool);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot register the four-part Tinkers katana", e);
        }
    }
    public static net.minecraft.world.item.ItemStack displayBlade() {
        return displayPart(KATANA_BLADE.get());
    }
    public static net.minecraft.world.item.ItemStack displayPart(Item part) {
        try {
            Class<?> id = Class.forName("slimeknights.tconstruct.library.materials.definition.MaterialVariantId");
            Object iron = id.getMethod("parse", String.class).invoke(null, part == TSUKA.get() ? "tconstruct:wood" : "tconstruct:iron");
            return (net.minecraft.world.item.ItemStack)Class.forName("slimeknights.tconstruct.library.tools.part.IMaterialItem")
                .getMethod("withMaterial", net.minecraft.world.item.ItemStack.class, id)
                .invoke(null, new net.minecraft.world.item.ItemStack(part), iron);
        } catch (ReflectiveOperationException e) { return new net.minecraft.world.item.ItemStack(part); }
    }
}
