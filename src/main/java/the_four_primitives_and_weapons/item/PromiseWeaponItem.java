package the_four_primitives_and_weapons.item;

import java.util.List;
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
}
