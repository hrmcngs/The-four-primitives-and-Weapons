package the_four_primitives_and_weapons.item;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import java.util.Optional;
import the_four_primitives_and_weapons.init.TicexCastingRegistrar;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;

/** A cast native blade plus a handle; keep the material on the finished katana. */
public final class TicexKatanaAssemblyRecipe extends CustomRecipe {
    public TicexKatanaAssemblyRecipe(ResourceLocation id, CraftingBookCategory category) { super(id, category); }
    private ItemStack blade(CraftingContainer inv) {
        if (!TicexCastingRegistrar.KATANA_BLADE.isPresent()) return ItemStack.EMPTY;
        ItemStack blade = ItemStack.EMPTY;
        boolean stick = false;
        for (int i=0;i<inv.getContainerSize();i++) {
            ItemStack stack=inv.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(TicexCastingRegistrar.KATANA_BLADE.get()) && blade.isEmpty()) blade=stack;
            else if (stack.is(Items.STICK) && !stick) stick=true;
            else return ItemStack.EMPTY;
        }
        return stick ? blade : ItemStack.EMPTY;
    }
    private ItemStack output(ItemStack blade) {
        if (blade.isEmpty()) return ItemStack.EMPTY;
        try {
            Class<?> materialId=Class.forName("slimeknights.tconstruct.library.materials.definition.MaterialId");
            Class<?> statsId=Class.forName("slimeknights.tconstruct.library.materials.stats.MaterialStatsId");
            Object material=Class.forName("slimeknights.tconstruct.library.tools.part.IMaterialItem")
                .getMethod("getMaterialFromStack",ItemStack.class).invoke(null,blade);
            Object id=material.getClass().getMethod("getId").invoke(material);
            Object registry=Class.forName("slimeknights.tconstruct.library.materials.MaterialRegistry").getMethod("getInstance").invoke(null);
            Object head=Class.forName("slimeknights.tconstruct.tools.stats.HeadMaterialStats").getField("ID").get(null);
            Optional<?> stats=(Optional<?>)Class.forName("slimeknights.tconstruct.library.materials.IMaterialRegistry")
                .getMethod("getMaterialStats",materialId,statsId).invoke(registry,id,head);
            if (stats.isEmpty()) return ItemStack.EMPTY;
            Object body=stats.get();
            int durability=((Number)body.getClass().getMethod("durability").invoke(body)).intValue();
            float attack=((Number)body.getClass().getMethod("attack").invoke(body)).floatValue();
            if (durability<=0 || !Float.isFinite(attack) || attack<0) return ItemStack.EMPTY;
            ItemStack output=new ItemStack(TheFourPrimitivesAndWeaponsModItems.IRON_KATANA.get());
            var tag=new net.minecraft.nbt.CompoundTag();
            tag.putString("Id",id.toString()); tag.putInt("Durability",durability); tag.putFloat("Attack",attack);
            output.getOrCreateTag().put(TicexWeaponRecipe.MATERIAL_TAG,tag);
            return output;
        } catch (ReflectiveOperationException | LinkageError e) { return ItemStack.EMPTY; }
    }
    @Override public boolean matches(CraftingContainer inv, Level level) { return !output(blade(inv)).isEmpty(); }
    @Override public ItemStack assemble(CraftingContainer inv, RegistryAccess access) { return output(blade(inv)); }
    @Override public boolean canCraftInDimensions(int width,int height) { return width*height>=2; }
    @Override public RecipeSerializer<?> getSerializer() { return TicexWeaponRecipe.Registrar.KATANA_ASSEMBLY.get(); }
}
