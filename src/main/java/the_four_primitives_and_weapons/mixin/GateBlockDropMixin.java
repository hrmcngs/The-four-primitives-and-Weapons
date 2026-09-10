package the_four_primitives_and_weapons.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import the_four_primitives_and_weapons.events.GateDropHandler;
import the_four_primitives_and_weapons.util.GateDropContext.Reason;

@Mixin(Block.class)
public abstract class GateBlockDropMixin {
    @Redirect(method = "popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private static boolean tfpw$gateDropReason(Level level, Entity entity) {
        return GateDropHandler.addWorldDrop(level, entity, Reason.BLOCK);
    }
}
