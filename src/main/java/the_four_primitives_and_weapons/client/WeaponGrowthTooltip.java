package the_four_primitives_and_weapons.client;

import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.skill.*;

@Mod.EventBusSubscriber(modid=TheFourPrimitivesAndWeaponsMod.MODID,value=Dist.CLIENT)
public final class WeaponGrowthTooltip {
    @SubscribeEvent public static void tooltip(ItemTooltipEvent event) {
        var stack=event.getItemStack();
        if(stack.is(the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems.TEMPERING_SHARD.get())) {
            event.getToolTip().add(Component.translatable("tooltip.the_four_primitives_and_weapons.tempering_shard").withStyle(ChatFormatting.GRAY));return;
        }
        if(!WeaponGrowth.supported(stack))return;
        event.getToolTip().add(Component.translatable("gui.the_four_primitives_and_weapons.upgrade.status",WeaponGrowth.level(stack),40,WeaponGrowth.power(stack),WeaponGrowth.freePoints(stack)).withStyle(ChatFormatting.AQUA));
        if(WeaponGrowth.level(stack)<40)event.getToolTip().add(Component.translatable("gui.the_four_primitives_and_weapons.upgrade.xp",WeaponGrowth.xp(stack)-WeaponGrowthRules.xpForLevel(WeaponGrowth.level(stack)),WeaponGrowthRules.xpForLevel(WeaponGrowth.level(stack)+1)-WeaponGrowthRules.xpForLevel(WeaponGrowth.level(stack))).withStyle(ChatFormatting.GRAY));
        if(WeaponGrowth.power(stack)>0)event.getToolTip().add(Component.translatable("tooltip.the_four_primitives_and_weapons.growth_power",WeaponGrowth.power(stack)*4).withStyle(ChatFormatting.DARK_GREEN));
        for(int i=0;i<3;i++){var perk=WeaponGrowth.slotPerk(stack,i);if(perk!=null)event.getToolTip().add(Component.translatable("gui.the_four_primitives_and_weapons.upgrade.slot",i+1,WeaponUpgradeScreen.perkName(perk),WeaponGrowth.slotRank(stack,i)).withStyle(ChatFormatting.LIGHT_PURPLE));}
        event.getToolTip().add(Component.translatable("tooltip.the_four_primitives_and_weapons.growth_hint").withStyle(ChatFormatting.GRAY));
    }
}
