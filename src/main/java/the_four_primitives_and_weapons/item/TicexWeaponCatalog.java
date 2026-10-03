package the_four_primitives_and_weapons.item;

import java.util.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.skill.WeaponTypeRegistry;

public final class TicexWeaponCatalog {
    private TicexWeaponCatalog() {}
    public static boolean supported(Item item) {
        ResourceLocation id=ForgeRegistries.ITEMS.getKey(item);
        if(id==null || !(id.getNamespace().equals(TheFourPrimitivesAndWeaponsMod.MODID)||id.getNamespace().equals("minecraft")))return false;
        return item instanceof SwordItem || item instanceof ShieldItem || item instanceof BowItem
                || item instanceof CrossbowItem || item instanceof TridentItem || item instanceof ThrowingKnifeItem
                || WeaponTypeRegistry.getTypeForItem(new ItemStack(item))!=null;
    }
    public static List<Item> all() {
        return ForgeRegistries.ITEMS.getValues().stream().filter(TicexWeaponCatalog::supported)
            .sorted(Comparator.comparing(i->ForgeRegistries.ITEMS.getKey(i).toString())).toList();
    }
    public static net.minecraft.network.chat.Component label(Item item) {
        var stack=new ItemStack(item);
        var label=stack.getHoverName().copy();
        if(item instanceof PromiseWeaponItem) {
            var type=WeaponTypeRegistry.getTypeForItem(stack);
            if(type!=null)label.append("（").append(net.minecraft.network.chat.Component.translatable(
                    "weapon_type.the_four_primitives_and_weapons."+type.getId())).append("）");
        }
        return label;
    }
    public static Item selected(ItemStack pattern) {
        ResourceLocation id=pattern.hasTag()?ResourceLocation.tryParse(pattern.getTag().getString("Weapon")):null;
        Item item=id==null?null:ForgeRegistries.ITEMS.getValue(id);
        return item!=null&&supported(item)?item:the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems.IRON_KATANA.get();
    }
}
