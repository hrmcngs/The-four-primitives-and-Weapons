package the_four_primitives_and_weapons.mixin;

import java.util.Optional;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import the_four_primitives_and_weapons.client.tooltip.PageScrollAccumulator;

/** Only gates JEI's hovered paged grid; recipe scrolling and other GUIs are unaffected. */
@Pseudo
@Mixin(targets = "mezz.jei.gui.overlay.IngredientGridWithNavigation$UserInputHandler", remap = false)
public abstract class JeiPageScrollMixin {
    @Unique private final PageScrollAccumulator maw$pageScroll = new PageScrollAccumulator();

    // paged is read only after JEI has confirmed that this grid is under the cursor.
    @Inject(method = "handleMouseScrolled", at = @At(value = "FIELD",
        target = "Lmezz/jei/gui/overlay/IngredientGridWithNavigation$UserInputHandler;paged:Lmezz/jei/gui/overlay/IngredientGridWithNavigation$IngredientGridPaged;"),
        cancellable = true, require = 0, remap = false)
    private void maw$accumulatePageScroll(double mouseX, double mouseY, double delta,
            CallbackInfoReturnable<Optional<?>> cir) {
        if (!maw$pageScroll.shouldTurn(delta, Util.getMillis(), Minecraft.getInstance().screen))
            cir.setReturnValue(Optional.of(this));
    }
}
