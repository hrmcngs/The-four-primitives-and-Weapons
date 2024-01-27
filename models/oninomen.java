// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class oninomen<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "oninomen"), "main");
	private final ModelPart head;

	public oninomen(ModelPart root) {
		this.head = root.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
		.texOffs(32, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition bone2 = head.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(64, 31).addBox(-1.75F, -32.25F, -7.0F, 0.0F, 1.0F, 0.0F, new CubeDeformation(0.75F))
		.texOffs(64, 31).addBox(-1.75F, -31.0F, -5.5F, 0.0F, 1.0F, 0.0F, new CubeDeformation(0.75F))
		.texOffs(64, 31).addBox(-4.75F, -32.25F, -7.0F, 0.0F, 1.0F, 0.0F, new CubeDeformation(0.75F))
		.texOffs(64, 31).addBox(-4.75F, -31.0F, -5.5F, 0.0F, 1.0F, 0.0F, new CubeDeformation(0.75F)), PartPose.offset(3.25F, 23.75F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}