package the_four_primitives_and_weapons.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.item.TicexWeaponRecipe;

@Mod.EventBusSubscriber(modid=TheFourPrimitivesAndWeaponsMod.MODID,value=Dist.CLIENT)
public final class TicexMaterialTooltip {
    @SubscribeEvent public static void tooltip(ItemTooltipEvent event) {
        var stack=event.getItemStack();
        if(!stack.hasTag()||!stack.getTag().contains(TicexWeaponRecipe.MATERIAL_TAG,10))return;
        var tag=stack.getTag().getCompound(TicexWeaponRecipe.MATERIAL_TAG);
        var id=ResourceLocation.tryParse(tag.getString("Id")); if(id==null)return;
        event.getToolTip().add(Component.translatable("tooltip.the_four_primitives_and_weapons.ticex_material",
                Component.translatable("material."+id.getNamespace()+"."+id.getPath()))
                .withStyle(ChatFormatting.AQUA));
    }
}
