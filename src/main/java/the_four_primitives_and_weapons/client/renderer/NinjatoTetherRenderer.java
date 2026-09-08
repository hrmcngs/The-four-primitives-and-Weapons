package the_four_primitives_and_weapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import the_four_primitives_and_weapons.entity.StabbedWeaponEntity;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;

/**
 * Crossed textured chain planes and hand anchoring adapted from yori3o/Yori3osGrapplingHooks
 * (HookRenderer / HookCustomGeometryRenderer), commit 7e6ba92f6958a6dd49b96fe2d1ea716ba2a3973b.
 * Copyright (c) 2026 yori3o, MIT; see META-INF/licenses/Yori3osGrapplingHooks-MIT.txt.
 * Ported to Forge 1.20.1, with UUID-specific cord matching and a planted sheath anchor.
 */
public final class NinjatoTetherRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(
        "the_four_primitives_and_weapons", "textures/entity/ninjato_tether.png");

    private NinjatoTetherRenderer() {}

    public static void render(StabbedWeaponEntity entity, float partialTick, PoseStack pose,
                              MultiBufferSource buffers, EntityRenderDispatcher dispatcher) {
        if (entity.getTetherOwner().isEmpty()) return;
        Player owner = entity.level().getPlayerByUUID(entity.getTetherOwner().get());
        if (owner == null || !owner.isAlive()) return;
        boolean main = matches(owner.getMainHandItem(), entity);
        if (!main && !matches(owner.getOffhandItem(), entity)) return;
        Vec3 hand = handPosition(owner, main, partialTick, dispatcher);
        Vec3 anchor = entity.weaponSegment()[0];
        Vec3 cable = hand.subtract(anchor);
        float length = (float) cable.length();
        if (!Float.isFinite(length) || length < 0.001F || length > 16.0F) return;
        Vec3 direction = cable.scale(1.0 / length);
        float pitch = (float) Math.acos(Mth.clamp(direction.y, -1.0, 1.0));
        float yaw = (float) Math.atan2(direction.z, direction.x);
        Vec3 entityPos = new Vec3(Mth.lerp(partialTick, entity.xo, entity.getX()),
            Mth.lerp(partialTick, entity.yo, entity.getY()), Mth.lerp(partialTick, entity.zo, entity.getZ()));

        pose.pushPose();
        Vec3 offset = anchor.subtract(entityPos);
        pose.translate(offset.x, offset.y, offset.z);
        pose.mulPose(Axis.YP.rotation((float) (Math.PI / 2) - yaw));
        pose.mulPose(Axis.XP.rotation(pitch));
        if (!entity.isChainTether()) {
            renderString(pose, buffers, length, dispatcher.getPackedLightCoords(owner, partialTick));
            pose.popPose();
            return;
        }
        VertexConsumer vertices = buffers.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        int light = dispatcher.getPackedLightCoords(owner, partialTick);
        float vEnd = length * 2.5F - 1.0F;
        // The two halves of the texture form perpendicular strips, repeating per 0.4 blocks.
        vertex(vertices, pose, -0.1F, length, 0, 0.5F, vEnd, light);
        vertex(vertices, pose, -0.1F, 0, 0, 0.5F, -1, light);
        vertex(vertices, pose, 0.1F, 0, 0, 0, -1, light);
        vertex(vertices, pose, 0.1F, length, 0, 0, vEnd, light);
        vertex(vertices, pose, 0, length, 0.1F, 1, vEnd, light);
        vertex(vertices, pose, 0, 0, 0.1F, 1, -1, light);
        vertex(vertices, pose, 0, 0, -0.1F, 0.5F, -1, light);
        vertex(vertices, pose, 0, length, -0.1F, 0.5F, vEnd, light);
        pose.popPose();
    }

    /** 紐は鎖より細い、淡い茶色の交差面。 */
    private static void renderString(PoseStack pose, MultiBufferSource buffers, float length, int light) {
        VertexConsumer out = buffers.getBuffer(RenderType.leash());
        float w = 0.025F;
        for (float[] p : new float[][]{{-w,0,0},{w,0,0},{-w,length,0},{w,length,0},
                {0,length,-w},{0,length,w},{0,0,-w},{0,0,w}}) {
            out.vertex(pose.last().pose(), p[0], p[1], p[2]).color(190, 163, 112, 255).uv2(light).endVertex();
        }
    }

    private static boolean matches(ItemStack stack, StabbedWeaponEntity entity) {
        return the_four_primitives_and_weapons.util.NinjatoVault.isRecallItem(stack)
            && stack.hasTag() && stack.getTag().hasUUID("PlantedWeapon")
            && entity.getUUID().equals(stack.getTag().getUUID("PlantedWeapon"));
    }

    private static Vec3 handPosition(Player player, boolean main, float partialTick, EntityRenderDispatcher dispatcher) {
        int armSign = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        if (!main) armSign = -armSign;
        if (dispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
            double fovScale = 960.0 / dispatcher.options.fov().get();
            float swing = Mth.sin(Mth.sqrt(player.getAttackAnim(partialTick)) * (float) Math.PI);
            Vec3 offset = dispatcher.camera.getNearPlane().getPointOnPlane(armSign * 0.825F, -0.5F)
                .yRot(swing * 0.5F).xRot(-swing * 0.7F).scale(fovScale);
            return player.getEyePosition(partialTick).add(offset);
        }
        float yaw = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot) * Mth.DEG_TO_RAD;
        double sin = Mth.sin(yaw), cos = Mth.cos(yaw);
        double side = armSign * 0.35;
        return player.getEyePosition(partialTick).add(-cos * side - sin * 0.4,
            (player.isCrouching() ? -0.1875 : 0) - 0.55, -sin * side + cos * 0.4);
    }

    private static void vertex(VertexConsumer vertices, PoseStack pose, float x, float y, float z,
                               float u, float v, int light) {
        vertices.vertex(pose.last().pose(), x, y, z).color(255, 255, 255, 255)
            .uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
            .normal(pose.last().normal(), 0, 1, 0).endVertex();
    }
}
