package the_four_primitives_and_weapons.util;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/** Sheath names stay generic; the compatible weapon kind belongs in the details. */
public final class SayaTooltip {
    private SayaTooltip() {}
    public static void appendType(ItemStack stack, List<Component> tooltip) {
        var id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id == null || !id.getNamespace().equals("the_four_primitives_and_weapons")) return;
        String type = switch (id.getPath()) {
            case "saya" -> "katana";
            case "ninjato_saya" -> "ninjato";
            case "tyokuto_saya" -> "straight_sword";
            case "sword_saya" -> "sword";
            case "rapier_saya" -> "rapier";
            case "dagger_saya" -> "dagger";
            default -> null;
        };
        if (type != null) tooltip.add(Component.translatable("tooltip.the_four_primitives_and_weapons.saya.type",
            Component.translatable("tooltip.the_four_primitives_and_weapons.saya.type." + type)));
    }
}
