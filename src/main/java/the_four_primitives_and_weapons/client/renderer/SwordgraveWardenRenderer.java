package the_four_primitives_and_weapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.IronGolem;
import the_four_primitives_and_weapons.client.model.SwordgraveWardenModel;

public class SwordgraveWardenRenderer extends MobRenderer<IronGolem, SwordgraveWardenModel> {
    private static final ResourceLocation ARMOUR = new ResourceLocation("textures/block/stone.png");
    private static final ResourceLocation STEEL = new ResourceLocation("textures/block/iron_block.png");
    private static final ResourceLocation EYES = new ResourceLocation("textures/block/red_concrete.png");
    private static final ResourceLocation MOSS = new ResourceLocation("textures/block/moss_block.png");
    private static final ResourceLocation HILT = new ResourceLocation("textures/block/polished_deepslate.png");
    private static final ResourceLocation GEM = new ResourceLocation("textures/block/lime_concrete.png");
    public SwordgraveWardenRenderer(EntityRendererProvider.Context context) {
        super(context, new SwordgraveWardenModel(context.bakeLayer(SwordgraveWardenModel.LAYER)), 0.8f);
        addLayer(new RenderLayer<IronGolem, SwordgraveWardenModel>(this) {
            @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, IronGolem entity,
                    float limbSwing, float limbAmount, float partialTick, float age, float headYaw, float headPitch) {
                if (entity.isInvisible()) return;
                var model = getParentModel();
                draw(model.hilt, pose, buffers, HILT, light);
                draw(model.gem, pose, buffers, GEM, 0xF000F0);
                for (var parent : model.mossParents) {
                    pose.pushPose();
                    parent.translateAndRotate(pose);
                    draw(parent.getChild("moss"), pose, buffers, MOSS, light);
                    pose.popPose();
                }
                model.metal.visible = true;
                model.metal.render(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(STEEL)), light, OverlayTexture.NO_OVERLAY,
                        0.65f, 0.7f, 0.73f, 1);
                model.metal.visible = false;
                pose.pushPose();
                model.headPart.translateAndRotate(pose);
                model.eyes.visible = true;
                model.eyes.render(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(EYES)), 0xF000F0, OverlayTexture.NO_OVERLAY);
                model.eyes.visible = false;
                pose.popPose();
            }
        });
    }
    private static void draw(net.minecraft.client.model.geom.ModelPart part, PoseStack pose,
            MultiBufferSource buffers, ResourceLocation texture, int light) {
        part.visible = true;
        try {
            part.render(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(texture)), light, OverlayTexture.NO_OVERLAY);
        } finally {
            part.visible = false;
        }
    }
    @Override public ResourceLocation getTextureLocation(IronGolem entity) { return ARMOUR; }
}
