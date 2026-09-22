package the_four_primitives_and_weapons.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/** A named katana; Feyn and elemental enhancements use the shared weapon systems. */
public final class KeitoKatanaItem extends SwordItem {
    public KeitoKatanaItem() {
        super(Tiers.DIAMOND, 3, -2.4F, new Item.Properties());
    }

    /** Feyn curses are not vanilla enchantments, but should also show the blue-white glint. */
    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack)
            || (stack.hasTag() && "cursed".equals(stack.getTag().getString("Feyn")));
    }

    /** Covers direct ItemStack construction, including give and forge results. */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag capabilityData) {
        KeitoFeyn.applyDefault(stack.getOrCreateTag());
        return super.initCapabilities(stack, capabilityData);
    }

    /** Also handles existing saves and commands supplying unrelated NBT. */
    @Override
    public void verifyTagAfterLoad(CompoundTag tag) {
        super.verifyTagAfterLoad(tag);
        KeitoFeyn.applyDefault(tag);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.the_four_primitives_and_weapons.keito_katana")
            .withStyle(ChatFormatting.GRAY));
    }
}
