package the_four_primitives_and_weapons.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_four_primitives_and_weapons.client.MantleAnimationState;
import the_four_primitives_and_weapons.network.MantleActionPacket;
import the_four_primitives_and_weapons.util.MantleCombatRules;

@Mixin(HumanoidModel.class)
public abstract class MantleGuardArmMixin {
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void mantleGuard(LivingEntity entity, float swing, float amount, float age, float yaw, float pitch, CallbackInfo ci) {
        if (!(entity instanceof Player player)) return;
        var state = MantleAnimationState.get(player);
        if (state == null || state.action() != MantleActionPacket.Action.GUARD) return;
        float partial = Math.max(0, Math.min(1, age - player.tickCount));
        float elapsed = player.level().getGameTime() - state.start() + partial;
        float blend = MantleCombatRules.guardGesture(elapsed);
        float flick = MantleCombatRules.guardForward(elapsed);
        int sign = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        ModelPart arm = sign > 0 ? rightArm : leftArm;
        arm.xRot += (-.4f - flick * .95f - arm.xRot) * blend;
        arm.yRot += (sign * (-.15f - flick * .5f) - arm.yRot) * blend;
        arm.zRot += (sign * (.5f - flick * .3f) - arm.zRot) * blend;
    }
}
