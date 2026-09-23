package the_four_primitives_and_weapons.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import the_four_primitives_and_weapons.skill.AttackHandContext;

@Mixin(LivingEntity.class)
public abstract class AttackHandMixin {
    @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", at = @At("HEAD"))
    private void tfpaw$syncBothSwings(InteractionHand hand, boolean updateSelf,
            org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        var context = AttackHandContext.forEntity((LivingEntity) (Object) this);
        if (context != null && context.player() instanceof net.minecraft.server.level.ServerPlayer player) {
            the_four_primitives_and_weapons.network.DualWieldSwingPacket.send(player, context.hand());
        }
    }
    @Inject(method = "getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D", at = @At("HEAD"), cancellable = true)
    private void tfpaw$weaponAttributes(net.minecraft.world.entity.ai.attributes.Attribute attribute,
                                       CallbackInfoReturnable<Double> cir) {
        var context = AttackHandContext.forEntity((LivingEntity) (Object) this);
        if (context != null && context.hand() == InteractionHand.OFF_HAND
                && (attribute == net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE
                    || attribute == net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED)) {
            cir.setReturnValue(AttackHandContext.attributeValue(context, attribute));
        }
    }
    @Inject(method = "getMainHandItem", at = @At("HEAD"), cancellable = true)
    private void tfpaw$attackWeapon(CallbackInfoReturnable<ItemStack> cir) {
        var context = AttackHandContext.forEntity((LivingEntity) (Object) this);
        if (context != null) cir.setReturnValue(context.weapon());
    }
    @Inject(method = "getItemInHand", at = @At("HEAD"), cancellable = true)
    private void tfpaw$attackHand(InteractionHand hand, CallbackInfoReturnable<ItemStack> cir) {
        var context = AttackHandContext.forEntity((LivingEntity) (Object) this);
        if (context != null && hand == InteractionHand.MAIN_HAND) cir.setReturnValue(context.weapon());
    }
}
