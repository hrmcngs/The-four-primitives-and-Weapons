package the_four_primitives_and_weapons.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import the_four_primitives_and_weapons.client.KeitoGlint;
import the_four_primitives_and_weapons.item.KeitoKatanaItem;

@Mixin(ItemRenderer.class)
public abstract class KeitoGlintMixin {
    @ModifyVariable(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
        at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private MultiBufferSource tfpaw$keitoGlint(MultiBufferSource original, ItemStack stack,
            ItemDisplayContext context, boolean leftHand, PoseStack pose, MultiBufferSource buffers,
            int light, int overlay, BakedModel model) {
        if (!(stack.getItem() instanceof KeitoKatanaItem) || !stack.hasFoil()) return original;
        return type -> original.getBuffer(KeitoGlint.recolor(type));
    }
}
