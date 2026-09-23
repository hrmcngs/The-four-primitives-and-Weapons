package the_four_primitives_and_weapons.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_four_primitives_and_weapons.client.DualWieldSwingAnimation;

@Mixin(HumanoidModel.class)
public abstract class DualWieldThirdPersonMixin {
    @Shadow @Final public ModelPart body;
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;

    @Inject(method = "setupAttackAnimation(Lnet/minecraft/world/entity/LivingEntity;F)V", at = @At("HEAD"), cancellable = true)
    private void tfpaw$bothArms(LivingEntity entity, float ageInTicks, CallbackInfo ci) {
        if (!(entity instanceof Player player)) return;
        float partial = Mth.clamp(ageInTicks - player.tickCount, 0, 1);
        float main = DualWieldSwingAnimation.progress(player, InteractionHand.MAIN_HAND, partial);
        float off = DualWieldSwingAnimation.progress(player, InteractionHand.OFF_HAND, partial);
        if (main < 0 && off < 0) return;
        float right = player.getMainArm() == HumanoidArm.RIGHT ? main : off;
        float left = player.getMainArm() == HumanoidArm.LEFT ? main : off;
        body.yRot = tfpaw$twist(right) - tfpaw$twist(left);
        rightArm.z = Mth.sin(body.yRot) * 5;
        rightArm.x = -Mth.cos(body.yRot) * 5;
        leftArm.z = -Mth.sin(body.yRot) * 5;
        leftArm.x = Mth.cos(body.yRot) * 5;
        rightArm.yRot += body.yRot;
        leftArm.yRot += body.yRot;
        tfpaw$animate(rightArm, right, 1);
        tfpaw$animate(leftArm, left, -1);
        ci.cancel();
    }
    @Unique private static float tfpaw$twist(float progress) {
        return progress < 0 ? 0 : Mth.sin(Mth.sqrt(progress) * Mth.TWO_PI) * 0.2F;
    }
    @Unique private void tfpaw$animate(ModelPart arm, float progress, int side) {
        if (progress < 0) return;
        float ease = 1 - progress;
        ease = 1 - ease * ease * ease * ease;
        arm.xRot -= Mth.sin(ease * Mth.PI) * 1.2F
            + Mth.sin(progress * Mth.PI) * -(head.xRot - 0.7F) * 0.75F;
        arm.yRot += tfpaw$twist(progress) * 2 * side;
        arm.zRot += Mth.sin(progress * Mth.PI) * -0.4F * side;
    }
}
