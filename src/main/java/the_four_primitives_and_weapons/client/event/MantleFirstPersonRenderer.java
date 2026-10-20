package the_four_primitives_and_weapons.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.ClipContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.client.MantleAnimationState;
import the_four_primitives_and_weapons.client.model.MantleActionModels;
import the_four_primitives_and_weapons.network.MantleActionPacket;
import the_four_primitives_and_weapons.util.MantleCombatRules;

/** Camera-space cloth, separate from Curios' third-person body layer. */
@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, value = Dist.CLIENT)
public final class MantleFirstPersonRenderer {
    private static ModelPart wrap, blade, fan;
    @SubscribeEvent public static void render(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null || player.isSpectator() || player.isInvisible() || mc.getCameraEntity() != player
                || !mc.options.getCameraType().isFirstPerson() || MantleActionPacket.equipped(player).isEmpty()) return;
        var state = MantleAnimationState.get(player);
        if (state == null) return;
        float age = player.level().getGameTime() - state.start() + event.getPartialTick();
        float envelope = state.action() == MantleActionPacket.Action.GUARD
            ? MantleCombatRules.guardGesture(age) : MantleCombatRules.envelope(age, state.duration());
        if (state.action() == MantleActionPacket.Action.GUARD && envelope <= .001f) return;
        boolean thrust = state.action() == MantleActionPacket.Action.THRUST && player.getItemInHand(state.hand()).isEmpty();
        if (thrust && event.getHand() == state.hand()) event.setCanceled(true);
        // Use the visible item hand as the anchor even when using an off-hand bow.
        // The chosen empty arm is drawn explicitly and need not have a vanilla hand pass.
        InteractionHand anchor = player.isUsingItem() ? player.getUsedItemHand() : InteractionHand.MAIN_HAND;
        if (event.getHand() != anchor) return;
        if (wrap == null) {
            wrap = MantleActionModels.wrap(); blade = MantleActionModels.blade(); fan = MantleActionModels.fan();
        }
        PoseStack poses = event.getPoseStack();
        var buffers = event.getMultiBufferSource();
        var out = buffers.getBuffer(RenderType.entityCutoutNoCull(MantleActionModels.TEXTURE));
        int light = event.getPackedLight();
        if (state.action() == MantleActionPacket.Action.GUARD) {
            float response = MantleAnimationState.deflection(player, event.getPartialTick());
            float wave = MantleCombatRules.guardWave(age);
            poses.pushPose();
            int sign = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            float flick = MantleCombatRules.guardForward(age);
            // Follow the cape around the outside of the dominant arm.
            poses.translate(sign * (.5 + (1 - envelope) * .7 - flick * .28),
                -.22 + (1 - envelope) * .28 + flick * .2, -.48 + (1 - envelope) * .95);
            poses.mulPose(Axis.YP.rotationDegrees(sign * (65 - flick * 40)));
            if (player.getMainHandItem().isEmpty()) {
                poses.pushPose();
                poses.mulPose(Axis.XP.rotationDegrees(-55 + flick * 18));
                if (mc.getEntityRenderDispatcher().getRenderer(player) instanceof PlayerRenderer renderer) {
                    if (sign > 0) renderer.renderRightHand(poses, buffers, light, player);
                    else renderer.renderLeftHand(poses, buffers, light, player);
                }
                poses.popPose();
                if (event.getHand() == InteractionHand.MAIN_HAND) event.setCanceled(true);
            }
            out = buffers.getBuffer(RenderType.entityCutoutNoCull(MantleActionModels.TEXTURE));
            poses.mulPose(Axis.YP.rotationDegrees(wave * 8 + response * 24));
            poses.mulPose(Axis.ZP.rotationDegrees(wave * 5 + response * 12));
            poses.scale(1f, -1.25f, .7f);
            for (int i = -3; i <= 3; i++) {
                ModelPart panel = fan.getChild("panel" + i);
                float flutter = (float)Math.sin(age * .22f + i * .45f);
                panel.y = flutter * .9f;
                panel.xRot = -.25f + flutter * .2f + Math.abs(response) * .35f;
                panel.yRot = i * .025f + flutter * .1f;
                panel.getChild("hem").xRot = .3f + flutter * .3f;
            }
            fan.render(poses, out, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            poses.popPose();
            if (event.getHand() == InteractionHand.MAIN_HAND && !player.getMainHandItem().isEmpty()) {
                poses.translate(sign * flick * .16, flick * .12, 0);
                poses.mulPose(Axis.ZP.rotationDegrees(sign * (12 + flick * 24) * envelope));
                poses.mulPose(Axis.YP.rotationDegrees(sign * flick * 20 * envelope));
            }
        } else if (thrust) {
            HumanoidArm side = state.hand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            int sign = side == HumanoidArm.RIGHT ? 1 : -1;
            float extension = MantleCombatRules.thrustExtension(age);
            poses.pushPose();
            poses.translate(sign * .5, -.5, -.65 - extension * .45);
            poses.mulPose(Axis.XP.rotationDegrees(-72));
            // Keep the normal skin hand visible under the winding cloth, including the off hand.
            if (mc.getEntityRenderDispatcher().getRenderer(player) instanceof PlayerRenderer renderer) {
                poses.pushPose(); poses.translate(sign * 6 / 16f, 0, 0);
                if (side == HumanoidArm.RIGHT) renderer.renderRightHand(poses, buffers, light, player);
                else renderer.renderLeftHand(poses, buffers, light, player);
                poses.popPose();
            }
            for (int i = 0; i < 4; i++) {
                ModelPart coil = wrap.getChild("coil" + i);
                float turn = (1 - envelope) * (float)Math.PI * 1.5f + i * .6f;
                coil.x = (float)Math.sin(turn) * (1 - envelope) * 3;
                coil.z = (float)Math.cos(turn) * (1 - envelope) * 3;
                coil.yRot = i * .22f + (1 - envelope) * (float)Math.PI;
            }
            // Rendering the skin can flush/switch BufferSource's shared builder.
            out = buffers.getBuffer(RenderType.entityCutoutNoCull(MantleActionModels.TEXTURE));
            wrap.render(poses, out, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            poses.popPose();
            if (extension > 0) {
                var origin = player.getEyePosition().add(0, -.35, 0);
                var end = origin.add(player.getLookAngle().scale(7));
                var wall = player.level().clip(new ClipContext(origin, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation();
                poses.pushPose();
                poses.translate(sign * .5, -.31, -1.25 - extension * .45);
                poses.mulPose(Axis.YP.rotationDegrees(sign * 4));
                poses.scale(1, 1, (float)Math.min(7, origin.distanceTo(wall)) * extension);
                blade.render(poses, out, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                poses.popPose();
            }
        }
    }
}
