package the_four_primitives_and_weapons.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import the_four_primitives_and_weapons.events.GateDropHandler;
import the_four_primitives_and_weapons.util.GateDropContext.Reason;

@Mixin(Inventory.class)
public abstract class GateInventoryOverflowMixin {
    @Redirect(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemEntity tfpw$gateOverflow(Player player, ItemStack stack, boolean includeName) {
        return GateDropHandler.dropFromMenu(player, stack, includeName, Reason.INVENTORY_OVERFLOW);
    }
}
