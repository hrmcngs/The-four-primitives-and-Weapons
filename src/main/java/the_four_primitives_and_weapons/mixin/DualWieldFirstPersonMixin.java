package the_four_primitives_and_weapons.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import the_four_primitives_and_weapons.client.DualWieldSwingAnimation;

@Mixin(ItemInHandRenderer.class)
public abstract class DualWieldFirstPersonMixin {
    @ModifyVariable(method = "renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("HEAD"), argsOnly = true, ordinal = 2)
    private float tfpaw$handSwing(float original, AbstractClientPlayer player, float partial, float pitch,
                                 InteractionHand hand, float swing, ItemStack item, float equip,
                                 PoseStack pose, MultiBufferSource buffers, int light) {
        float progress = DualWieldSwingAnimation.progress(player, hand, partial);
        return progress >= 0 ? progress : original;
    }
}
