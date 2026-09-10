package the_four_primitives_and_weapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import net.minecraft.client.renderer.texture.TextureAtlas;

import the_four_primitives_and_weapons.entity.GateProjectileEntity;

/**
 * Gate飛び道具レンダラー — 金の剣が切先を進行方向に向けて飛ぶ
 */
public class GateProjectileRenderer extends EntityRenderer<GateProjectileEntity> {

    private final ItemRenderer itemRenderer;

    // @RotationParams(Gate直刀, cmd=/test gaterot {YAW_OFFSET} {PITCH_OFFSET} {ROLL_OFFSET} {SCALE_X} {SCALE_Y} {SCALE_Z})
    public static float YAW_OFFSET = 0f; // Y軸微調整
    public static float PITCH_OFFSET = 0f; // X軸微調整
    public static float ROLL_OFFSET = 0f; // Z軸微調整
    public static float SCALE_X = 1.0f; // Xサイズ
    public static float SCALE_Y = 1.0f; // Yサイズ
    public static float SCALE_Z = 1.0f; // Zサイズ
    // @EndRotationParams
    private static ItemStack sharedStack;
    

    public GateProjectileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(GateProjectileEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 投げナイフと同じく entity の YRot/XRot (モーションから projectile 基底が
        // 毎tick更新) を使って 3D で整列する。これで上下方向にも切先が向く。
        float yaw   = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());

        // 進行方向に整列 (矢方式): Y 軸で左右, Z 軸で上下
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 90f + YAW_OFFSET));
        poseStack.mulPose(Axis.ZP.rotationDegrees(pitch + PITCH_OFFSET));
        poseStack.mulPose(Axis.XP.rotationDegrees(ROLL_OFFSET));
        // generated の金の剣は右上 (+X,+Y) に刃が伸びるので +X へ揃える。
        poseStack.mulPose(Axis.ZP.rotationDegrees(-45f));

        poseStack.scale(SCALE_X, SCALE_Y, SCALE_Z);

        // 毎フレームの new ItemStack を避けて使い回す ( 表示専用で内容は不変 )
        if (sharedStack == null) {
            sharedStack = entity.getItem();
        }
        ItemStack displayStack = sharedStack;
        itemRenderer.renderStatic(
                displayStack, ItemDisplayContext.NONE,
                packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GateProjectileEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
