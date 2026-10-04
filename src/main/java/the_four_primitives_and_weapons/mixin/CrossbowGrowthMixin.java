package the_four_primitives_and_weapons.mixin;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import the_four_primitives_and_weapons.skill.*;
@Mixin(CrossbowItem.class)
public abstract class CrossbowGrowthMixin {
    @Inject(method="getChargeDuration",at=@At("RETURN"),cancellable=true)
    private static void tfpw$growthCharge(ItemStack stack,CallbackInfoReturnable<Integer> cir){
        int rank=WeaponGrowth.rank(stack,WeaponGrowthRules.Perk.HASTE);
        if(rank>0)cir.setReturnValue(Math.max(1,(int)Math.ceil(cir.getReturnValue()/(1+.06*rank))));
    }
}
