package the_four_primitives_and_weapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import the_four_primitives_and_weapons.entity.PlacedGreatshieldEntity;

public class PlacedGreatshieldRenderer extends EntityRenderer<PlacedGreatshieldEntity> {
    private final net.minecraft.client.renderer.entity.ItemRenderer items;
    public PlacedGreatshieldRenderer(EntityRendererProvider.Context context) {
        super(context);
        items = context.getItemRenderer();
    }
    @Override public void render(PlacedGreatshieldEntity entity, float yaw, float partialTick,
                                 PoseStack pose, MultiBufferSource buffers, int light) {
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
        // JSONモデルの板面を地面・当たり判定へ合わせる（高さ30/16ブロック）。
        pose.translate(0, 0.75, 0.15625);
        items.renderStatic(entity.getShield(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY,
                pose, buffers, entity.level(), entity.getId());
        pose.popPose();
        super.render(entity, yaw, partialTick, pose, buffers, light);
    }
    @Override public ResourceLocation getTextureLocation(PlacedGreatshieldEntity entity) { return TextureAtlas.LOCATION_BLOCKS; }
}
