package the_four_primitives_and_weapons.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import the_four_primitives_and_weapons.events.GateDropHandler;

@Mixin(ServerPlayer.class)
public abstract class GateDropKeyMixin {
    @Redirect(method = "drop(Z)Z", at = @At(value = "INVOKE", remap = false,
            target = "Lnet/minecraftforge/common/ForgeHooks;onPlayerTossEvent(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemEntity tfpw$gateDropKey(Player player, ItemStack stack, boolean includeName) {
        return GateDropHandler.dropFromKey(player, stack, includeName);
    }
}
