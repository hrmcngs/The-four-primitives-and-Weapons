package the_four_primitives_and_weapons.client.model;

import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

/** Shared cloth geometry for the arm and the extended thrust in both camera views. */
public final class MantleActionModels {
    public static final ResourceLocation TEXTURE = new ResourceLocation("the_four_primitives_and_weapons", "textures/models/armor/fantasy_mantle.png");
    private MantleActionModels() {}
    public static ModelPart wrap() {
        MeshDefinition wrapped = new MeshDefinition();
        for (int i = 0; i < 4; i++) {
            wrapped.getRoot().addOrReplaceChild("coil" + i, CubeListBuilder.create()
                .addBox(-2.8f, 3.5f + i * 1.8f, -2.8f, 5.6f, 1.4f, .65f)
                .addBox(-2.8f, 3.5f + i * 1.8f, 2.15f, 5.6f, 1.4f, .65f)
                .addBox(-2.8f, 3.5f + i * 1.8f, -2.15f, .65f, 1.4f, 4.3f)
                .addBox(2.15f, 3.5f + i * 1.8f, -2.15f, .65f, 1.4f, 4.3f),
                PartPose.offsetAndRotation(0, 0, 0, 0, i * .22f, 0));
        }
        return LayerDefinition.create(wrapped, 64, 64).bakeRoot();

    }
    public static ModelPart blade() {
        MeshDefinition spear = new MeshDefinition();
        spear.getRoot().addOrReplaceChild("tip", CubeListBuilder.create()
            .addBox(-1.2f, -1.2f, -4, 2.4f, 2.4f, 4)
            .addBox(-.75f, -.75f, -10, 1.5f, 1.5f, 6)
            .addBox(-.3f, -.3f, -16, .6f, .6f, 6), PartPose.ZERO);
        return LayerDefinition.create(spear, 64, 64).bakeRoot();
    }
    public static ModelPart fan() {
        MeshDefinition mesh = new MeshDefinition();
        for (int i = -3; i <= 3; i++) {
            PartDefinition panel = mesh.getRoot().addOrReplaceChild("panel" + i,
                CubeListBuilder.create().addBox(-1.7f, 0, 0, 3.4f, 5, .25f), PartPose.offset(i * 3, 0, 0));
            panel.addOrReplaceChild("hem", CubeListBuilder.create().addBox(-1.9f, 0, 0, 3.8f, 4, .25f), PartPose.offset(0, 5, 0));
        }
        return LayerDefinition.create(mesh, 64, 64).bakeRoot();
    }
}
