package the_four_primitives_and_weapons.item;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.*;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;
import java.lang.reflect.Method;
import java.util.*;

/** Optional bridge: uses the active Tinkers material recipes and synchronized material registry. */
public final class TicexWeaponRecipe extends CustomRecipe {
    public static final String MATERIAL_TAG = "TFPW_TicexMaterial";
    private record Preview(List<ItemStack> inputs, ItemStack output) {}
    private final Map<CraftingContainer, Preview> previews = new WeakHashMap<>();
    private boolean warned;
    public TicexWeaponRecipe(ResourceLocation id,CraftingBookCategory category){super(id,category);}
    private static boolean weapon(ItemStack stack) {
        return TicexWeaponCatalog.supported(stack.getItem());
    }
    @Override public boolean matches(CraftingContainer inv,Level level) {
        previews.remove(inv);
        if(!ModList.get().isLoaded("ticex") || !ModList.get().isLoaded("tconstruct"))return false;
        ItemStack template=ItemStack.EMPTY;
        ItemStack pattern=ItemStack.EMPTY;
        boolean handle=false;
        List<ItemStack> inputs=new ArrayList<>();
        for(int i=0;i<inv.getContainerSize();i++) {
            ItemStack stack=inv.getItem(i); if(stack.isEmpty())continue;
            if(stack.getItem() instanceof TicexWeaponPatternItem) {
                if(!pattern.isEmpty())return false; pattern=stack;
            } else if(stack.is(Items.STICK)) {
                if(handle)return false;handle=true;
            } else if(weapon(stack)) {
                if(!template.isEmpty())return false; template=stack;
            } else inputs.add(stack);
        }
        if(inputs.size()!=3)return false;
        if(!pattern.isEmpty()) {
            if(!template.isEmpty()||!handle)return false;
            template=new ItemStack(TicexWeaponCatalog.selected(pattern));
        } else if(template.isEmpty()) {
            if(!handle)return false;
            template=new ItemStack(TheFourPrimitivesAndWeaponsModItems.IRON_KATANA.get());
        } else if(handle)return false;
        try {
            Class<?> recipeClass=Class.forName("slimeknights.tconstruct.library.recipe.material.MaterialRecipe");
            Class<?> registryClass=Class.forName("slimeknights.tconstruct.library.materials.MaterialRegistry");
            Class<?> registryApi=Class.forName("slimeknights.tconstruct.library.materials.IMaterialRegistry");
            Class<?> materialId=Class.forName("slimeknights.tconstruct.library.materials.definition.MaterialId");
            Class<?> statsId=Class.forName("slimeknights.tconstruct.library.materials.stats.MaterialStatsId");
            Object headId=Class.forName("slimeknights.tconstruct.tools.stats.HeadMaterialStats").getField("ID").get(null);
            Object registry=registryClass.getMethod("getInstance").invoke(null);
            Method statsMethod=registryApi.getMethod("getMaterialStats",materialId,statsId);
            Method ingredientMethod=recipeClass.getMethod("getIngredient");
            for(Recipe<?> recipe:level.getRecipeManager().getRecipes()) {
                if(!recipeClass.isInstance(recipe))continue;
                // One consumed item per slot: only full, single-unit ingots/gems qualify.
                if(((Number)recipeClass.getMethod("getValue").invoke(recipe)).intValue()!=1
                        ||((Number)recipeClass.getMethod("getNeeded").invoke(recipe)).intValue()!=1)continue;
                Ingredient ingredient=(Ingredient)ingredientMethod.invoke(recipe);
                if(inputs.stream().anyMatch(s->!ingredient.test(s)))continue;
                Object variant=recipeClass.getMethod("getMaterial").invoke(recipe);
                if((boolean)variant.getClass().getMethod("isUnknown").invoke(variant))continue;
                Object id=variant.getClass().getMethod("getId").invoke(variant);
                if(template.hasTag() && id.toString().equals(template.getTag().getCompound(MATERIAL_TAG).getString("Id")))continue;
                Optional<?> headStats=(Optional<?>)statsMethod.invoke(registry,id,headId);
                String statType = template.getItem() instanceof ShieldItem ? "tconstruct:plating_shield"
                        : template.getItem() instanceof BowItem || template.getItem() instanceof CrossbowItem ? "tconstruct:limb" : "tconstruct:head";
                Object preferredId=statsId.getConstructor(String.class).newInstance(statType);
                Optional<?> stats=(Optional<?>)statsMethod.invoke(registry,id,preferredId);
                if(stats.isEmpty())stats=headStats;
                if(stats.isEmpty())continue;
                Object body=stats.get();
                int durability=((Number)body.getClass().getMethod("durability").invoke(body)).intValue();
                float attack=headStats.isPresent()?((Number)headStats.get().getClass().getMethod("attack").invoke(headStats.get())).floatValue():2;
                if(durability<=0||!Float.isFinite(attack)||attack<0)continue;
                ItemStack out=template.copy();
                out.setCount(1);
                int oldMax=out.getMaxDamage(), oldDamage=out.getDamageValue();
                var tag=out.getOrCreateTag().getCompound(MATERIAL_TAG);
                tag.putString("Id",id.toString()); tag.putInt("Durability",durability); tag.putFloat("Attack",attack);
                out.getOrCreateTag().put(MATERIAL_TAG,tag);
                // Reforging preserves damage; it cannot be used as a free repair.
                if(oldDamage>0 && oldMax>0)
                    out.setDamageValue((int)Math.min(durability-1,((long)oldDamage*durability+oldMax-1)/oldMax));
                List<ItemStack> snapshot=new ArrayList<>();
                for(int i=0;i<inv.getContainerSize();i++)snapshot.add(inv.getItem(i).copy());
                previews.put(inv,new Preview(snapshot,out)); return true;
            }
        } catch(ReflectiveOperationException|LinkageError e) {
            // External API may differ: optional compatibility must never break crafting.
            if(!warned) { warned=true; TheFourPrimitivesAndWeaponsMod.LOGGER.warn("TicEX material weapon integration could not read the Tinkers material API",e); }
        }
        return false;
    }
    @Override public ItemStack assemble(CraftingContainer inv,RegistryAccess access) {
        Preview preview=previews.get(inv);
        if(preview==null||preview.inputs.size()!=inv.getContainerSize())return ItemStack.EMPTY;
        for(int i=0;i<inv.getContainerSize();i++)
            if(!ItemStack.matches(preview.inputs.get(i),inv.getItem(i)))return ItemStack.EMPTY;
        return preview.output.copy();
    }
    @Override public boolean canCraftInDimensions(int w,int h){return w*h>=4;}
    @Override public net.minecraft.core.NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        var remaining=super.getRemainingItems(inv);
        for(int i=0;i<inv.getContainerSize();i++)if(inv.getItem(i).getItem() instanceof TicexWeaponPatternItem)
            remaining.set(i,inv.getItem(i).copy());
        return remaining;
    }
    @Override public RecipeSerializer<?> getSerializer(){return Registrar.SERIALIZER.get();}
    public static final class Registrar {
        public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS=DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS,TheFourPrimitivesAndWeaponsMod.MODID);
        public static final RegistryObject<RecipeSerializer<TicexWeaponRecipe>> SERIALIZER=SERIALIZERS.register("ticex_weapon",()->new SimpleCraftingRecipeSerializer<>(TicexWeaponRecipe::new));
    }
}
