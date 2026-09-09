package the_four_primitives_and_weapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.entity.ButterflyEntity;
import the_four_primitives_and_weapons.init.ButterflyInit;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ButterflyRenderer extends MobRenderer<ButterflyEntity, ButterflyRenderer.Model> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/block/white_concrete.png");

    public ButterflyRenderer(EntityRendererProvider.Context context) {
        super(context, new Model(), 0.12F);
    }

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ButterflyInit.BUTTERFLY.get(), ButterflyRenderer::new);
    }

    @Override
    public ResourceLocation getTextureLocation(ButterflyEntity entity) { return TEXTURE; }

    public static class Model extends EntityModel<ButterflyEntity> {
        private final ModelPart body, left, right, leftMark, rightMark;

        public Model() {
            MeshDefinition mesh = new MeshDefinition();
            var root = mesh.getRoot();
            root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0,0)
                    .addBox(-0.45F,-0.5F,-2.5F,0.9F,1,5)
                    .addBox(-1,-0.7F,-3.4F,0.25F,0.3F,1.4F)
                    .addBox(0.75F,-0.7F,-3.4F,0.25F,0.3F,1.4F), PartPose.offset(0,22,0));
            for (int side : new int[]{-1,1}) {
                String name = side < 0 ? "left" : "right";
                root.addOrReplaceChild(name, CubeListBuilder.create().texOffs(0,0)
                        .addBox(side < 0 ? -5 : 0,0,-3,5,0.15F,3.5F)
                        .addBox(side < 0 ? -3.5F : 0,0,0.5F,3.5F,0.15F,2.7F),PartPose.offset(0,22,0));
                root.addOrReplaceChild(name+"Mark", CubeListBuilder.create().texOffs(0,0)
                        .addBox(side < 0 ? -4.3F : 0.7F,-0.05F,-2.5F,3.6F,0.25F,2.4F)
                        .addBox(side < 0 ? -2.8F : 0.7F,-0.05F,1,2.1F,0.25F,1.6F),PartPose.offset(0,22,0));
            }
            ModelPart baked = LayerDefinition.create(mesh,16,16).bakeRoot();
            body=baked.getChild("body");left=baked.getChild("left");right=baked.getChild("right");
            leftMark=baked.getChild("leftMark");rightMark=baked.getChild("rightMark");
        }

        @Override
        public void setupAnim(ButterflyEntity entity,float limbSwing,float limbAmount,float age,float yaw,float pitch) {
            float flap = 0.25F + Mth.sin(age*1.3F + entity.getId())*0.9F;
            left.zRot=leftMark.zRot=flap;
            right.zRot=rightMark.zRot=-flap;
        }

        @Override
        public void renderToBuffer(PoseStack pose,VertexConsumer buffer,int light,int overlay,float r,float g,float b,float alpha) {
            body.render(pose,buffer,light,overlay,0.15F,0.12F,0.12F,alpha);
            left.render(pose,buffer,light,overlay,0.12F,0.22F,0.35F,alpha);
            right.render(pose,buffer,light,overlay,0.12F,0.22F,0.35F,alpha);
            leftMark.render(pose,buffer,light,overlay,0.45F,0.78F,0.92F,alpha);
            rightMark.render(pose,buffer,light,overlay,0.45F,0.78F,0.92F,alpha);
        }
    }
}
