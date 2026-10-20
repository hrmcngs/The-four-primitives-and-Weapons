package the_four_primitives_and_weapons.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import java.util.List;
import javax.annotation.Nullable;

public class FantasyMantleItem extends Item implements ICurioItem {
    public FantasyMantleItem() { super(new Properties().stacksTo(1).rarity(Rarity.RARE)); }
    @Override public boolean canEquip(SlotContext context, ItemStack stack) {
        return context.identifier().equals("mantle");
    }
    @Override public boolean canEquipFromUse(SlotContext context, ItemStack stack) { return true; }
    @Override public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag) {
        lines.add(Component.translatable("tooltip.the_four_primitives_and_weapons.fantasy_mantle"));
        lines.add(Component.translatable("tooltip.the_four_primitives_and_weapons.fantasy_mantle.guard"));
    }
}
