package the_four_primitives_and_weapons.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import java.util.List;

public final class TicexWeaponPatternItem extends Item {
    public TicexWeaponPatternItem(){super(new Properties().stacksTo(1));}
    @Override public InteractionResultHolder<ItemStack> use(Level level,Player player,InteractionHand hand) {
        if(level.isClientSide)DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->
            the_four_primitives_and_weapons.client.TicexPatternScreen.open(hand));
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand),level.isClientSide);
    }
    @Override public void appendHoverText(ItemStack stack,Level level,List<Component> tooltip,TooltipFlag flag) {
        ItemStack weapon=new ItemStack(TicexWeaponCatalog.selected(stack));
        tooltip.add(Component.translatable("tooltip.the_four_primitives_and_weapons.ticex_pattern_weapon",TicexWeaponCatalog.label(weapon.getItem())).withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.translatable("tooltip.the_four_primitives_and_weapons.ticex_pattern_use").withStyle(ChatFormatting.GRAY));
    }
}
