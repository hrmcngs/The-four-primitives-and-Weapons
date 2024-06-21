
package minecraftarmorweapon.client.renderer;

import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import minecraftarmorweapon.entity.model.BlackholeModel;
import minecraftarmorweapon.entity.BlackholeEntity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class BlackholeRenderer extends GeoEntityRenderer<BlackholeEntity> {
	public BlackholeRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new BlackholeModel());
		this.shadowRadius = 0f;
	}

	@Override
	public RenderType getRenderType(BlackholeEntity entity, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		stack.scale(1f, 1f, 1f);
		return RenderType.entityTranslucent(getTextureLocation(entity));
	}
}
