package the_four_primitives_and_weapons.integration.jei;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.ForgeRegistries;
import the_four_primitives_and_weapons.init.FloweringWoodInit;
import the_four_primitives_and_weapons.init.OsmanthusFoodInit;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;
import the_four_primitives_and_weapons.item.SayaStyleRecipe;
import the_four_primitives_and_weapons.util.KatanaFittings;
import the_four_primitives_and_weapons.util.SayaDesign;

/** JEI-only examples for dynamic recipes. Normal recipe JSONs are handled by JEI itself. */
final class SpecialCraftingJeiRecipes {
    private static final String MOD = "the_four_primitives_and_weapons";

    static void register(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) return;
        List<CraftingRecipe> displays = new ArrayList<>();
        List<Item> styles = new ArrayList<>(List.of(Items.LEATHER, Items.FLINT, Items.IRON_INGOT,
                Items.HONEYCOMB, TheFourPrimitivesAndWeaponsModItems.RAW_URUSHI.get(),
                TheFourPrimitivesAndWeaponsModItems.URUSHI_BLACK.get(),
                TheFourPrimitivesAndWeaponsModItems.URUSHI_RED.get()));
        for (Item item : ForgeRegistries.ITEMS) {
            ItemStack stack = item.getDefaultInstance();
            if ((stack.is(ItemTags.PLANKS) || stack.is(SayaStyleRecipe.RAYSKIN)) && !styles.contains(item))
                styles.add(item);
        }
        for (Item item : ForgeRegistries.ITEMS) {
            ItemStack base = item.getDefaultInstance();
            if (SayaDesign.isSaya(base)) {
                for (DyeColor color : DyeColor.values())
                    add(displays, "saya_dye", base, DyeItem.byColor(color).getDefaultInstance());
                for (Item material : styles) add(displays, "saya_style", base, material.getDefaultInstance());
                add(displays, "saya_style", base, new ItemStack(TheFourPrimitivesAndWeaponsModItems.URUSHI_BLACK.get()), new ItemStack(Items.GLOWSTONE_DUST));
                add(displays, "saya_style", base, new ItemStack(TheFourPrimitivesAndWeaponsModItems.RAW_URUSHI.get()), new ItemStack(Items.GRAVEL));
                add(displays, "saya_style", base, new ItemStack(TheFourPrimitivesAndWeaponsModItems.URUSHI_RED.get()), new ItemStack(Items.GRAVEL));
            }
            if (KatanaFittings.isFittingWeapon(base)) {
                for (DyeColor color : DyeColor.values()) {
                    ItemStack dye = new ItemStack(DyeItem.byColor(color));
                    add(displays, "katana_fitting", base, dye);
                    for (Item marker : List.of(Items.GOLD_NUGGET, Items.IRON_NUGGET, Items.COPPER_INGOT))
                        add(displays, "katana_fitting", base, dye, new ItemStack(marker));
                }
                add(displays, "katana_fitting", base, new ItemStack(Items.STRING));
                add(displays, "katana_fitting", base, new ItemStack(Items.IRON_INGOT));
            }
        }
        add(displays, "magical_katana_unlock", new ItemStack(TheFourPrimitivesAndWeaponsModItems.MAGICAL_KATANA.get()),
                new ItemStack(TheFourPrimitivesAndWeaponsModItems.CORROSION_BOOK.get()));
        for (Item cord : List.of(Items.STRING, Items.CHAIN))
            add(displays, "ninjato_cord", new ItemStack(TheFourPrimitivesAndWeaponsModItems.NINJATO_SAYA.get()), new ItemStack(cord));
        registration.addRecipes(RecipeTypes.CRAFTING, displays);
    }

    /** Match and assemble with the loaded recipe, so datapack removal and output NBT are respected. */
    private static void add(List<CraftingRecipe> displays, String name, ItemStack... inputs) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        var loaded = level.getRecipeManager().byKey(new ResourceLocation(MOD, name)).orElse(null);
        if (!(loaded instanceof CustomRecipe recipe)) return;
        var menu = new AbstractContainerMenu(null, -1) {
            @Override public ItemStack quickMoveStack(Player player, int slot) { return ItemStack.EMPTY; }
            @Override public boolean stillValid(Player player) { return false; }
        };
        var grid = new TransientCraftingContainer(menu, 3, 3);
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (int i = 0; i < inputs.length; i++) {
            grid.setItem(i, inputs[i].copy());
            ingredients.add(Ingredient.of(inputs[i].copy()));
        }
        if (!recipe.matches(grid, level)) return;
        ItemStack result = recipe.assemble(grid, level.registryAccess());
        if (result.isEmpty()) return;
        displays.add(new ShapelessRecipe(new ResourceLocation(MOD, "jei/" + name + "/" + displays.size()),
                "", CraftingBookCategory.MISC, result, ingredients));
    }

    static void registerHints(IRecipeRegistration registration) {
        for (Item item : ForgeRegistries.ITEMS) {
            ItemStack stack = item.getDefaultInstance();
            if (SayaDesign.isSaya(stack)) info(registration, stack, "saya_customization");
            if (KatanaFittings.isFittingWeapon(stack)) info(registration, stack, "fittings");
        }
        info(registration, new ItemStack(TheFourPrimitivesAndWeaponsModItems.MAGICAL_KATANA.get()), "magical_unlock");
        info(registration, new ItemStack(TheFourPrimitivesAndWeaponsModItems.CORROSION_BOOK.get()), "magical_unlock");
        info(registration, new ItemStack(TheFourPrimitivesAndWeaponsModItems.NINJATO_SAYA.get()), "ninjato_cord");
        info(registration, new ItemStack(OsmanthusFoodInit.FLOWERS.get()), "kinmokusei_harvest");
        info(registration, new ItemStack(OsmanthusFoodInit.SYRUP.get()), "kinmokusei_syrup");
        info(registration, new ItemStack(OsmanthusFoodInit.CAKE.get()), "kinmokusei_cake");
        info(registration, new ItemStack(OsmanthusFoodInit.COOKIE.get()), "kinmokusei_cookie");
        FloweringWoodInit.WOODS.forEach((name, wood) -> {
            info(registration, new ItemStack(wood.sapling().get()), "flowering_tree");
            info(registration, new ItemStack(wood.stripped().get()), "stripped_wood");
            info(registration, new ItemStack(wood.leaves().get()), "flowering_leaves");
            info(registration, new ItemStack(wood.flowers().get()), "flowering_leaves");
            if (name.equals("kinmokusei")) {
                info(registration, new ItemStack(wood.leaves().get()), "kinmokusei_harvest");
                info(registration, new ItemStack(wood.flowers().get()), "kinmokusei_harvest");
            }
        });
    }

    private static void info(IRecipeRegistration registration, ItemStack stack, String key) {
        registration.addIngredientInfo(stack, VanillaTypes.ITEM_STACK, Component.translatable("jei." + MOD + ".info." + key));
    }
    private SpecialCraftingJeiRecipes() {}
}
