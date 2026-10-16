package the_four_primitives_and_weapons.client.model;

import net.minecraft.client.model.HumanoidModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

/** Original knight silhouette: split crest, visor, layered pauldrons and blue crystal. */
public class FantasyKnightArmorModel extends HumanoidModel<LivingEntity> {
    public FantasyKnightArmorModel() { super(create().bakeRoot()); }
    private static CubeListBuilder dark() { return CubeListBuilder.create().texOffs(0, 0); }
    private static CubeListBuilder silver() { return CubeListBuilder.create().texOffs(64, 0); }
    private static CubeListBuilder crystal() { return CubeListBuilder.create().texOffs(64, 64); }
    private static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", dark().addBox(-4.4f, -8.5f, -4.4f, 8.8f, 8.8f, 8.8f), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("brow", silver().addBox(-4.6f, -7, -4.8f, 9.2f, 1.3f, 1), PartPose.ZERO);
        head.addOrReplaceChild("visor", crystal().addBox(-3.4f, -5.6f, -4.9f, 6.8f, .7f, .5f), PartPose.ZERO);
        head.addOrReplaceChild("face", silver().addBox(-1, -4.6f, -5, 2, 4.5f, 1)
            .addBox(-4.5f, -4.4f, -4.7f, 1.4f, 4.5f, 2)
            .addBox(3.1f, -4.4f, -4.7f, 1.4f, 4.5f, 2), PartPose.ZERO);
        for (int sign : new int[]{-1, 1}) {
            head.addOrReplaceChild("crest" + sign, silver().addBox(-.6f, -5, -.6f, 1.2f, 5, 1.2f),
                PartPose.offsetAndRotation(sign * 2.6f, -8, 0, -.15f, 0, sign * .35f));
        }
        PartDefinition body = root.addOrReplaceChild("body", dark().addBox(-4.3f, -.2f, -2.4f, 8.6f, 12.4f, 4.8f), PartPose.ZERO);
        body.addOrReplaceChild("breastplate", silver().addBox(-4.5f, .4f, -3.1f, 9, 6.2f, 1.2f)
            .addBox(-4.4f, 7.5f, -2.9f, 8.8f, 1.2f, .8f)
            .addBox(-4.4f, 9.6f, -2.9f, 8.8f, 1.2f, .8f), PartPose.ZERO);
        body.addOrReplaceChild("heart", crystal().addBox(-1, -1, -.6f, 2, 2, 1.2f),
            PartPose.offsetAndRotation(0, 3.4f, -3.7f, 0, 0, .7854f));
        body.addOrReplaceChild("belt", silver().addBox(-4.6f, 11, -2.7f, 9.2f, 1.3f, 5.4f), PartPose.ZERO);
        for (int sign : new int[]{-1, 1}) {
            String side = sign == -1 ? "right" : "left";
            PartDefinition arm = root.addOrReplaceChild(side + "_arm", dark()
                .addBox(sign == -1 ? -3.3f : -1.3f, -2.2f, -2.3f, 4.6f, 12.4f, 4.6f),
                PartPose.offset(sign * 5, 2, 0));
            arm.addOrReplaceChild("pauldron", silver()
                .addBox(-3.4f, -3.2f, -3, 6.8f, 3.5f, 6), PartPose.offset(sign, 0, 0));
            arm.addOrReplaceChild("bracer", silver()
                .addBox(sign == -1 ? -3.6f : -1.6f, 5.5f, -2.7f, 5.2f, 4.7f, 5.4f), PartPose.ZERO);
            PartDefinition leg = root.addOrReplaceChild(side + "_leg", CubeListBuilder.create(), PartPose.offset(sign * 1.9f, 12, 0));
            leg.addOrReplaceChild("greave", dark().addBox(-2.2f, -.2f, -2.3f, 4.4f, 9.3f, 4.6f), PartPose.ZERO);
            leg.addOrReplaceChild("tasset", silver().addBox(-2.3f, 0, -2.9f, 4.6f, 4.5f, .8f)
                .addBox(-2.3f, 5.2f, -3, 4.6f, 2.3f, 1), PartPose.ZERO);
            leg.addOrReplaceChild("boot", silver().addBox(-2.4f, 8, -2.6f, 4.8f, 4.3f, 5.2f)
                .addBox(-2.4f, 10.1f, -3.7f, 4.8f, 2.2f, 1.1f), PartPose.ZERO);
        }
        return LayerDefinition.create(mesh, 128, 128);
    }
    @Override public void renderToBuffer(PoseStack poses, VertexConsumer out, int light, int overlay,
            float r, float g, float b, float a) {
        super.renderToBuffer(poses, out, light, overlay, r, g, b, a);
        poses.pushPose();
        if (young) { poses.scale(.75f, .75f, .75f); poses.translate(0, 1, 0); }
        if (head.visible) {
            plate(head, poses, out, light, overlay, -4.5f, -7, 4.5f, -5.7f, -4.83f, .5f, 0, 1, .12f, r, g, b, a);
        }
        poses.popPose();
        poses.pushPose();
        if (young) { poses.scale(.5f, .5f, .5f); poses.translate(0, 1.5f, 0); }
        if (body.visible) {
            plate(body, poses, out, light, overlay, -4.5f, .4f, 4.5f, 6.6f, -3.12f, .5f, 0, 1, .5f, r, g, b, a);
        }
        for (ModelPart arm : new ModelPart[]{leftArm, rightArm}) {
            if (!arm.visible) continue;
            poses.pushPose(); arm.translateAndRotate(poses);
            plate(arm.getChild("pauldron"), poses, out, light, overlay,
                -3.4f, -3.2f, 3.4f, .3f, -3.02f, .5f, .05f, 1, .3f, r, g, b, a);
            float x = arm == rightArm ? -3.6f : -1.6f;
            plate(arm.getChild("bracer"), poses, out, light, overlay,
                x, 5.5f, x + 5.2f, 10.2f, -2.72f, .5f, .17f, 1, .5f, r, g, b, a);
            poses.popPose();
        }
        for (ModelPart leg : new ModelPart[]{leftLeg, rightLeg}) {
            if (!leg.visible) continue;
            poses.pushPose(); leg.translateAndRotate(poses);
            if (leg.getChild("tasset").visible) {
                plate(leg.getChild("tasset"), poses, out, light, overlay,
                    -2.3f, 0, 2.3f, 4.5f, -2.92f, .5f, .1f, 1, .5f, r, g, b, a);
            }
            if (leg.getChild("boot").visible) {
                plate(leg.getChild("boot"), poses, out, light, overlay,
                    -2.4f, 8, 2.4f, 10.1f, -2.62f, .5f, .15f, 1, .5f, r, g, b, a);
            }
            poses.popPose();
        }
        poses.popPose();
    }
    private static void plate(ModelPart part, PoseStack poses, VertexConsumer out, int light, int overlay,
            float x0, float y0, float x1, float y1, float z, float u0, float v0, float u1, float v1,
            float r, float g, float b, float a) {
        poses.pushPose(); part.translateAndRotate(poses);
        FantasyPatternQuad.draw(poses, out, light, overlay, x0, y0, x1, y1, z,
            u0, v0, u1, v1, false, r, g, b, a);
        poses.popPose();
    }
    public void showLegParts(EquipmentSlot slot) {
        for (ModelPart leg : new ModelPart[]{leftLeg, rightLeg}) {
            leg.getChild("greave").visible = slot == EquipmentSlot.LEGS;
            leg.getChild("tasset").visible = slot == EquipmentSlot.LEGS;
            leg.getChild("boot").visible = slot == EquipmentSlot.FEET;
        }
    }
}
