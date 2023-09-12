package minecraftarmorweapon.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelluna_Converted<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("minecraft_armor_weapon", "modelluna_converted"), "main");
	public final ModelPart bone;

	public Modelluna_Converted(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition bone = partdefinition.addOrReplaceChild("bone",
				CubeListBuilder.create().texOffs(11, 25).addBox(-0.75F, -1.4F, 5.95F, 1.5F, 2.1F, 0.3F, new CubeDeformation(0.0F)).texOffs(6, 18).addBox(-0.35F, -0.75F, 6.25F, 0.75F, 0.8F, 5.75F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.25F, 0.0F));
		PartDefinition bone2 = bone.addOrReplaceChild("bone2",
				CubeListBuilder.create().texOffs(7, 7).addBox(-0.1F, -0.6F, -7.25F, 0.2F, 0.25F, 0.75F, new CubeDeformation(0.0F)).texOffs(7, 7).addBox(-0.1F, -0.5F, -7.8F, 0.2F, 0.3F, 0.55F, new CubeDeformation(0.0F)).texOffs(7, 7)
						.addBox(-0.1F, -0.3F, -8.3F, 0.2F, 0.3F, 0.5F, new CubeDeformation(0.0F)).texOffs(29, 20).addBox(-0.2F, -0.2F, -7.8F, 0.4F, 0.2F, 0.55F, new CubeDeformation(0.0F)).texOffs(29, 20)
						.addBox(-0.2F, -0.4F, -7.25F, 0.4F, 0.4F, 0.75F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, -0.25F));
		PartDefinition bone3 = bone2.addOrReplaceChild("bone3",
				CubeListBuilder.create().texOffs(-2, -2).addBox(-0.1F, -0.75F, -6.5F, 0.2F, 0.25F, 12.75F, new CubeDeformation(0.0F)).texOffs(6, 18).addBox(-0.2F, -0.5F, -6.5F, 0.4F, 0.5F, 12.75F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.bone.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.bone.xRot = headPitch / (180F / (float) Math.PI);
	}
}
