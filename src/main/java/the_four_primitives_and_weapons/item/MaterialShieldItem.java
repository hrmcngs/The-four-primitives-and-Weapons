package the_four_primitives_and_weapons.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Tiers;

/** 素材ごとの耐久値・修理素材・エンチャント適性を持つ通常の盾。 */
public class MaterialShieldItem extends ShieldItem {
    private final Tiers tier;

    public MaterialShieldItem(Tiers tier, int durability) {
        super(properties(tier, durability));
        this.tier = tier;
    }

    private static Item.Properties properties(Tiers tier, int durability) {
        Item.Properties properties = new Item.Properties().durability(durability);
        return tier == Tiers.NETHERITE ? properties.fireResistant() : properties;
    }

    @Override
    public int getEnchantmentValue() {
        return tier.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        return tier.getRepairIngredient().test(ingredient);
    }
}
