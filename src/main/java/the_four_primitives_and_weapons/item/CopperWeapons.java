package the_four_primitives_and_weapons.item;

import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;

/** Copper sits between stone and iron; copper weapons repair with copper ingots. */
public final class CopperWeapons {
    private CopperWeapons() {}
    public static final Tier TIER = new Tier() {
        public int getUses() { return 180; }
        public float getSpeed() { return 5; }
        public float getAttackDamageBonus() { return 1; }
        public int getLevel() { return 1; }
        public int getEnchantmentValue() { return 10; }
        public Ingredient getRepairIngredient() { return Ingredient.of(Items.COPPER_INGOT); }
    };
    public static Item katana() { return new SwordItem(TIER, 3, -2.4f, new Item.Properties()); }
    public static Item tyokuto() { return new SwordItem(TIER, 3, -2.4f, new Item.Properties()); }
    public static Item sword() { return new SwordItem(TIER, 3, -2.4f, new Item.Properties()); }
    public static Item rapier() { return new AbstractTieredRapierItem(TIER, 2) {}; }
    public static Item dagger() { return new AbstractDaggerItem(TIER, 2, -1.8f, new Item.Properties()) {}; }
}
