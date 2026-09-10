package the_four_primitives_and_weapons.item;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import the_four_primitives_and_weapons.util.KatanaFittings;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/** ストーリーモード用の独立した武器ID。進行条件・固有能力は今後ここへ接続する。 */
public final class PromiseWeaponItem extends SwordItem {
    private final String form;

    public PromiseWeaponItem(String form) {
        super(Tiers.DIAMOND, 3, -2.4F, new Properties().rarity(Rarity.EPIC));
        this.form = form;
    }

    @Override
    public String getDescriptionId() {
        return "item.the_four_primitives_and_weapons.promise_weapon";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("gui.the_four_primitives_and_weapons.ring.form." + form));
        tooltip.add(Component.translatable("tooltip.the_four_primitives_and_weapons.promise_weapon"));
    }
    // Palette generated with the addon ./maw create-weapon command.
    private static void applyDefaultColors(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(KatanaFittings.BLADE_KEY)) {
            KatanaFittings.setBlade(stack, 0xCAD2D9);
        }
        if (!tag.contains(KatanaFittings.TSUKA_KEY)) {
            KatanaFittings.setTsuka(stack, 0x171B22);
        }
        if (!tag.contains(KatanaFittings.TSUBA_KEY)) {
            KatanaFittings.setTsuba(stack, 0xAEBAC5);
        }
        if (!tag.contains(KatanaFittings.KASHIRA_KEY)) {
            KatanaFittings.setKashira(stack, 0xAEBAC5);
        }
        if (!tag.contains(KatanaFittings.HABAKI_KEY)) {
            KatanaFittings.setHabaki(stack, 0xDFE6EB);
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        applyDefaultColors(stack);
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        applyDefaultColors(stack);
        super.inventoryTick(stack, level, entity, slot, selected);
    }
}
