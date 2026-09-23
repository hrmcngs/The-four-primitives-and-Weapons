package the_four_primitives_and_weapons.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import the_four_primitives_and_weapons.damage.ElementalDamageUtils;

/** An elemental enhancement has its own visible glint, even without enchantments. */
@Mixin(ItemStack.class)
public abstract class ElementGlintFoilMixin {
    @Inject(method = "hasFoil", at = @At("RETURN"), cancellable = true)
    private void tfpaw$elementGlint(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && ElementalDamageUtils.hasElement((ItemStack) (Object) this)) {
            cir.setReturnValue(true);
        }
    }
}
