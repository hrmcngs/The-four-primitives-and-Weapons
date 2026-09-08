package the_four_primitives_and_weapons.item;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;
import the_four_primitives_and_weapons.util.NinjatoVault;

/** 忍者刀の鞘 + 糸または鎖1個。納刀中の刀・染色・名前を含むNBTを保持する。 */
public class NinjatoCordRecipe extends CustomRecipe {
    public NinjatoCordRecipe(ResourceLocation id, CraftingBookCategory category) { super(id, category); }

    private ItemStack findSaya(CraftingContainer inv) {
        ItemStack saya = ItemStack.EMPTY;
        boolean string = false;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(TheFourPrimitivesAndWeaponsModItems.NINJATO_SAYA.get()) || NinjatoVault.isLoaded(stack)) {
                if (!saya.isEmpty() || (stack.hasTag() && stack.getTag().getBoolean(NinjatoVault.TETHERED)))
                    return ItemStack.EMPTY;
                saya = stack;
            } else if ((stack.is(Items.STRING) || stack.is(Items.CHAIN)) && !string) {
                string = true;
            } else return ItemStack.EMPTY;
        }
        return string ? saya : ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level level) { return !findSaya(inv).isEmpty(); }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        ItemStack saya = findSaya(inv);
        if (saya.isEmpty()) return ItemStack.EMPTY;
        ItemStack result = saya.copy();
        result.setCount(1);
        result.getOrCreateTag().putBoolean(NinjatoVault.TETHERED, true);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (inv.getItem(i).is(Items.CHAIN)) result.getOrCreateTag().putString(NinjatoVault.MATERIAL, "chain");
            else if (inv.getItem(i).is(Items.STRING)) result.getOrCreateTag().putString(NinjatoVault.MATERIAL, "string");
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) { return width * height >= 2; }
    @Override
    public RecipeSerializer<?> getSerializer() { return Registrar.SERIALIZER.get(); }

    public static final class Registrar {
        public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TheFourPrimitivesAndWeaponsMod.MODID);
        public static final RegistryObject<RecipeSerializer<NinjatoCordRecipe>> SERIALIZER =
            SERIALIZERS.register("ninjato_cord", () -> new SimpleCraftingRecipeSerializer<>(NinjatoCordRecipe::new));
    }
}
