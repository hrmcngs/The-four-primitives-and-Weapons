package the_four_primitives_and_weapons.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_four_primitives_and_weapons.client.tooltip.TooltipScrollController;

@Mixin(MouseHandler.class)
public abstract class TooltipMouseScrollMixin {
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void maw$scrollTooltip(long window, double horizontal, double vertical, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (window != client.getWindow().getWindow() || client.getOverlay() != null) return;
        if (TooltipScrollController.onNativeScroll(horizontal, vertical)) ci.cancel();
    }
}
