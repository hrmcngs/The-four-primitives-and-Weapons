package the_four_primitives_and_weapons.item;

import net.minecraft.nbt.CompoundTag;

/** Default only: preserve an explicit Feyn and all other saved item data. */
public final class KeitoFeyn {
    private KeitoFeyn() {}

    public static void applyDefault(CompoundTag tag) {
        if (!tag.contains("Feyn")) tag.putString("Feyn", "cursed");
    }
}
