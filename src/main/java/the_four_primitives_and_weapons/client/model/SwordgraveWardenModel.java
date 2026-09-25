package the_four_primitives_and_weapons.client.model;

import java.util.List;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.IronGolem;

/** Weathered stone guardian with articulated moss and a single embedded sword. */
public class SwordgraveWardenModel extends IronGolemModel<IronGolem> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            new ResourceLocation("the_four_primitives_and_weapons", "swordgrave_warden"), "main");
    public final ModelPart metal, hilt, gem, eyes, headPart;
    public final List<ModelPart> mossParents;
    public SwordgraveWardenModel(ModelPart root) {
        super(root);
        metal = root.getChild("metal");
        hilt = root.getChild("hilt");
        gem = root.getChild("gem");
        headPart = root.getChild("head");
        eyes = headPart.getChild("eyes");
        mossParents = List.of(headPart, root.getChild("body"), root.getChild("right_arm"),
                root.getChild("left_arm"), root.getChild("right_leg"), root.getChild("left_leg"));
        metal.visible = hilt.visible = gem.visible = eyes.visible = false;
        for (ModelPart parent : mossParents) parent.getChild("moss").visible = false;
    }
    private static CubeListBuilder box(float x, float y, float z, float w, float h, float d) {
        return CubeListBuilder.create().texOffs(0, 0).addBox(x, y, z, w, h, d);
    }
    private static void moss(PartDefinition parent, CubeListBuilder cubes) {
        parent.addOrReplaceChild("moss", cubes, PartPose.ZERO);
    }
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("head", box(-3.5f, -10, -4, 7, 9, 7)
                .addBox(-4, -10.5f, -4.5f, 8, 2, 8)
                .addBox(-0.75f, -6, -4.4f, 1.5f, 3, 0.5f), PartPose.offset(0, -7, -2));
        head.addOrReplaceChild("eyes", box(-2.7f, -7, -4.2f, 1.5f, 1.5f, 0.3f)
                .addBox(1.2f, -7, -4.2f, 1.5f, 1.5f, 0.3f), PartPose.ZERO);
        moss(head, box(-4.1f, -11.2f, -4.6f, 8.2f, 0.8f, 8.2f)
                .addBox(-3.6f, -8.5f, -4.1f, 1.5f, 3, 0.3f));
        PartDefinition body = root.addOrReplaceChild("body", box(-9, -2, -5, 18, 11, 10)
                .addBox(-7, 9, -4, 14, 2, 8)
                .addBox(-4, 11, -3, 8, 6, 6), PartPose.offset(0, -7, 0));
        moss(body, box(-9.1f, -2.8f, -5.1f, 18.2f, 0.9f, 10.2f)
                .addBox(-8, -2, -5.2f, 3, 4, 0.4f)
                .addBox(-6, 1, -5.2f, 2, 4, 0.4f)
                .addBox(-4, 4, -5.2f, 2, 3, 0.4f)
                .addBox(5, -2, -5.2f, 2, 2, 0.4f)
                .addBox(2, 12, -3.2f, 1.5f, 4, 0.4f));
        for (int side : new int[] {-1, 1}) {
            boolean left = side == 1;
            float armX = left ? 9 : -15;
            PartDefinition arm = root.addOrReplaceChild(left ? "left_arm" : "right_arm",
                    box(armX, -3, -4, 6, 9, 8)
                    .addBox(armX + 1, 6, -3, 4, 8, 6)
                    .addBox(armX, 14, -4, 6, 11, 8), PartPose.offset(0, -7, 0));
            moss(arm, box(armX - 0.1f, -3.8f, -4.1f, 6.2f, 0.9f, 8.2f)
                    .addBox(armX + (left ? 4 : 0), -3, -4.2f, 2, left ? 3 : 6, 0.4f)
                    .addBox(armX + 1, 18, -4.2f, 2, 4, 0.4f));
            PartDefinition leg = root.addOrReplaceChild(left ? "left_leg" : "right_leg",
                    box(-3, 0, -3, 6, 13, 6)
                    .addBox(-3.5f, 9, -5, 7, 4, 9), PartPose.offset(side * 4, 11, 0));
            moss(leg, box(-3.6f, 8.5f, -5.1f, 7.2f, 0.6f, 9.2f)
                    .addBox(left ? 1 : -3, 3, -3.2f, 2, 5, 0.4f));
        }
        // The blade enters the upper back. Separate materials keep the hilt dark.
        root.addOrReplaceChild("metal", box(-1.5f, -14, -0.6f, 3, 16, 1.2f),
                PartPose.offsetAndRotation(3, -9, 4, 0.08f, 0, 0.06f));
        root.addOrReplaceChild("hilt", box(-4, -15.5f, -1.2f, 8, 1.5f, 2.4f)
                .addBox(-0.8f, -24, -0.8f, 1.6f, 8.5f, 1.6f)
                .addBox(-1.5f, -25, -1.5f, 3, 1.5f, 3),
                PartPose.offsetAndRotation(3, -9, 4, 0.08f, 0, 0.06f));
        root.addOrReplaceChild("gem", box(-0.8f, -26, -0.8f, 1.6f, 1.4f, 1.6f),
                PartPose.offsetAndRotation(3, -9, 4, 0.08f, 0, 0.06f));
        return LayerDefinition.create(mesh, 16, 16);
    }
}
