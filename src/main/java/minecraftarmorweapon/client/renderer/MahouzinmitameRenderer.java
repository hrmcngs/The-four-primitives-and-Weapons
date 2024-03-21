package minecraftarmorweapon.client.renderer;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import minecraftarmorweapon.entity.MahouzinmitameEntity;

import minecraftarmorweapon.client.model.Modelkagamiyotei;

import com.mojang.math.Vector3f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class MahouzinmitameRenderer extends EntityRenderer<MahouzinmitameEntity> {
	private static final ResourceLocation texture = new ResourceLocation("minecraft_armor_weapon:textures/entities/kagaminoyotei.png");
	private final Modelkagamiyotei model;

	public MahouzinmitameRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new Modelkagamiyotei(context.bakeLayer(Modelkagamiyotei.LAYER_LOCATION));
	}

	@Override
	public void render(MahouzinmitameEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		VertexConsumer vb = bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
		poseStack.pushPose();
		poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(90 + Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
		model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.0625f);
		poseStack.popPose();
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(MahouzinmitameEntity entity) {
		return texture;
	}
}
