package the_four_primitives_and_weapons.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

/** 武器を呼び出すための指輪。Curiosのringスロット専用。 */
public final class RingItem extends Item implements ICurioItem {
    public RingItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public boolean canEquip(SlotContext context, ItemStack stack) {
        return "ring".equals(context.identifier());
    }

    @Override
    public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
        return true;
    }
}
