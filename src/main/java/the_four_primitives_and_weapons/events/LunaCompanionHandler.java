package the_four_primitives_and_weapons.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import the_four_primitives_and_weapons.entity.LunaCompanionEntity;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModEntities;
import the_four_primitives_and_weapons.item.LunaItem;

import javax.annotation.Nullable;
import java.util.UUID;

/** Called after the shared UUID/provenance check, never from an arbitrary ItemTossEvent. */
public final class LunaCompanionHandler {
    private LunaCompanionHandler() {}

    @Nullable
    static UUID trySummon(Player player, ItemEntity dropped) {
        if (!(player.level() instanceof ServerLevel level)
                || !(dropped.getItem().getItem() instanceof LunaItem)) return null;
        LunaCompanionEntity luna = TheFourPrimitivesAndWeaponsModEntities.LUNA_COMPANION.get().create(level);
        if (luna == null) return null;
        ItemStack item = dropped.getItem();
        luna.bind(player, item);
        luna.moveTo(dropped.getX(), dropped.getY(), dropped.getZ(), player.getYRot(), 0.0F);
        // If another mod rejects the companion spawn, the original item remains on the ground.
        if (!level.addFreshEntity(luna) || luna.isRemoved()) return null;
        // bind stores one item including its NBT. Do not delete excess items from a command-created stack.
        ItemStack remaining = item.copy();
        remaining.shrink(1);
        if (remaining.isEmpty()) {
            dropped.setItem(ItemStack.EMPTY);
            dropped.discard();
        } else {
            dropped.setItem(remaining);
        }
        return luna.getUUID();
    }
}
