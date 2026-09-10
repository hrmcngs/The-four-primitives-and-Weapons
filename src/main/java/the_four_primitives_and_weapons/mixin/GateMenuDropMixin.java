package the_four_primitives_and_weapons.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import the_four_primitives_and_weapons.events.GateDropHandler;
import the_four_primitives_and_weapons.util.GateDropContext.Reason;

@Mixin(AbstractContainerMenu.class)
public abstract class GateMenuDropMixin {
    @Redirect(method = "doClick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemEntity tfpw$gateInventoryDrop(Player player, ItemStack stack, boolean includeName) {
        return GateDropHandler.dropFromMenu(player, stack, includeName, Reason.INVENTORY);
    }

    @Redirect(method = {"removed", "clearContainer"}, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemEntity tfpw$gateMenuCleanup(Player player, ItemStack stack, boolean includeName) {
        return GateDropHandler.dropFromMenu(player, stack, includeName, Reason.MENU_CLEANUP);
    }
}
