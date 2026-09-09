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
import the_four_primitives_and_weapons.entity.ButterflyVariant;
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
    protected void scale(ButterflyEntity entity, PoseStack pose, float partialTick) {
        float size=entity.getButterflySize();
        pose.scale(size,size,size);
    }

    @Override
    public ResourceLocation getTextureLocation(ButterflyEntity entity) { return TEXTURE; }

    public static class Model extends EntityModel<ButterflyEntity> {
        private final ModelPart body, left, right, leftMark, rightMark;
        private final ModelPart[] tails = new ModelPart[2];
        private final ModelPart[][][] markings = new ModelPart[ButterflyVariant.PATTERN_COUNT][2][3];
        private int pattern, wingColor, edgeColor, accentColor, bodyColor;
        private boolean hasTails;
        private float wingWidth=1, wingLength=1;

        private static void mark(CubeListBuilder cubes, int side, float x, float z, float width, float depth, float layer) {
            cubes.texOffs(0,0).addBox(side < 0 ? -x-width : x, -layer, z, width, 0.15F+2*layer, depth);
        }

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
                root.addOrReplaceChild(name+"Tail", CubeListBuilder.create().texOffs(0,0)
                        .addBox(side < 0 ? -2.8F : 2.2F,0,2.7F,0.6F,0.15F,1.8F),PartPose.offset(0,22,0));
                for (int pattern=0; pattern<ButterflyVariant.PATTERN_COUNT; pattern++) {
                    CubeListBuilder dark=CubeListBuilder.create(), accent=CubeListBuilder.create(), pupil=CubeListBuilder.create();
                    switch (pattern) {
                        case 0 -> { // Subtle blue wing ribs.
                            for (float z=-2; z<=2; z+=1.3F) mark(dark,side,0.8F,z,z<0?3.2F:1.7F,0.10F,0.08F);
                        }
                        case 1 -> { // Orange cells, dark veins, pale spots along the border.
                            for (float x=1.5F; x<4.3F; x+=1.1F) mark(dark,side,x,-2.5F,0.14F,2.5F,0.08F);
                            mark(dark,side,0.7F,-1.1F,3.6F,0.13F,0.08F);
                            mark(dark,side,1.6F,1,0.15F,1.6F,0.08F);
                            for (float x=0.8F; x<4.6F; x+=0.85F) mark(accent,side,x,-2.92F,0.24F,0.22F,0.11F);
                            for (float z=-2; z<0; z+=0.65F) mark(accent,side,4.6F,z,0.22F,0.22F,0.11F);
                        }
                        case 2 -> { // Swallowtail stripes and warm hindwing spots.
                            for (float x=1.4F; x<4; x+=1.15F) mark(dark,side,x,-3,0.35F,2.5F-(x-1.4F)*0.35F,0.08F);
                            mark(dark,side,1,1.6F,2.2F,0.28F,0.08F);
                            mark(accent,side,2.2F,2.2F,0.5F,0.5F,0.11F);
                        }
                        case 3 -> { // Whites/sulphurs: dark tips and a pair of spots.
                            mark(dark,side,3.6F,-2.7F,1,0.65F,0.08F);
                            mark(dark,side,2.5F,-1,0.5F,0.5F,0.08F);
                            mark(dark,side,1.8F,1.4F,0.35F,0.4F,0.08F);
                        }
                        case 4 -> { // Pixelated concentric eyespots on both pairs of wings.
                            for (float z : new float[]{-1.9F,1.1F}) {
                                float x=z<0?2.5F:1.2F;
                                mark(dark,side,x,z,1.45F,1.4F,0.08F);
                                mark(accent,side,x+0.2F,z+0.2F,1.05F,1,0.11F);
                                mark(pupil,side,x+0.43F,z+0.43F,0.59F,0.54F,0.14F);
                            }
                        }
                        case 5 -> { // Broad contrasting bands.
                            mark(accent,side,2.3F,-2.65F,0.95F,2.8F,0.11F);
                            mark(accent,side,0.9F,1.5F,2.2F,0.5F,0.11F);
                        }
                        case 6 -> { // Rows of small pearl spots.
                            for (float x=1; x<4.5F; x+=1) for (float z=-2.3F; z<0; z+=1)
                                mark(accent,side,x,z,0.35F,0.35F,0.11F);
                            for (float x=1; x<3; x+=0.8F) mark(accent,side,x,1.6F,0.3F,0.3F,0.11F);
                        }
                        case 7 -> { // Stepped zigzag ribbon.
                            for (int i=0;i<6;i++) mark(accent,side,0.8F+i*0.55F,
                                    -1.8F+(i%2)*0.5F,0.55F,0.35F,0.11F);
                            for (int i=0;i<4;i++) mark(accent,side,0.8F+i*0.5F,
                                    1.2F+(i%2)*0.4F,0.5F,0.3F,0.11F);
                        }
                        case 8 -> { // Bright panels surrounded by a contrasting outline.
                            mark(accent,side,1,-2.2F,2.8F,1.7F,0.11F);
                            mark(pupil,side,1.3F,-1.9F,2.2F,1.1F,0.14F);
                            mark(accent,side,1,1.2F,1.5F,1.1F,0.11F);
                            mark(pupil,side,1.25F,1.45F,1,0.6F,0.14F);
                        }
                        case 9 -> { // Small alternating checkers.
                            for (int i=0;i<5;i++) for (int j=0;j<3;j++)
                                if ((i+j)%2==0) mark(dark,side,0.8F+i*0.65F,-2.4F+j*0.7F,0.55F,0.55F,0.08F);
                            mark(accent,side,1,1.7F,1.7F,0.25F,0.11F);
                        }
                    }
                    CubeListBuilder[] layers={dark,accent,pupil};
                    for (int layer=0;layer<3;layer++) root.addOrReplaceChild(name+"Pattern"+pattern+"_"+layer,
                            layers[layer],PartPose.offset(0,22,0));
                }
            }
            ModelPart baked = LayerDefinition.create(mesh,16,16).bakeRoot();
            body=baked.getChild("body");left=baked.getChild("left");right=baked.getChild("right");
            leftMark=baked.getChild("leftMark");rightMark=baked.getChild("rightMark");
            for (int side=0;side<2;side++) {
                String name=side==0?"left":"right";
                tails[side]=baked.getChild(name+"Tail");
                for (int pattern=0;pattern<ButterflyVariant.PATTERN_COUNT;pattern++) for (int layer=0;layer<3;layer++)
                    markings[pattern][side][layer]=baked.getChild(name+"Pattern"+pattern+"_"+layer);
            }
        }

        @Override
        public void setupAnim(ButterflyEntity entity,float limbSwing,float limbAmount,float age,float yaw,float pitch) {
            float flap = 0.25F + Mth.sin(age*1.3F*entity.getFlapSpeed() + entity.getId())*entity.getFlapAmount();
            left.zRot=leftMark.zRot=flap;
            right.zRot=rightMark.zRot=-flap;
            pattern=entity.getPattern();
            wingColor=entity.getWingColor(); edgeColor=entity.getEdgeColor();
            accentColor=entity.getAccentColor(); bodyColor=entity.getBodyColor();
            hasTails=entity.hasTails(); wingWidth=entity.getWingWidth(); wingLength=entity.getWingLength();
            for (int side=0;side<2;side++) {
                float angle=side==0?flap:-flap;
                tails[side].zRot=angle;
                for (int layer=0;layer<3;layer++) markings[pattern][side][layer].zRot=angle;
            }
        }

        private void draw(ModelPart part, int color, PoseStack pose, VertexConsumer buffer, int light, int overlay, float alpha) {
            part.render(pose,buffer,light,overlay,((color>>16)&255)/255F,((color>>8)&255)/255F,(color&255)/255F,alpha);
        }

        @Override
        public void renderToBuffer(PoseStack pose,VertexConsumer buffer,int light,int overlay,float r,float g,float b,float alpha) {
            draw(body,bodyColor,pose,buffer,light,overlay,alpha);
            pose.pushPose();
            pose.scale(wingWidth,1,wingLength);
            draw(left,edgeColor,pose,buffer,light,overlay,alpha);
            draw(right,edgeColor,pose,buffer,light,overlay,alpha);
            draw(leftMark,wingColor,pose,buffer,light,overlay,alpha);
            draw(rightMark,wingColor,pose,buffer,light,overlay,alpha);
            for (int side=0;side<2;side++) {
                if (hasTails) draw(tails[side],edgeColor,pose,buffer,light,overlay,alpha);
                for (int layer=0;layer<3;layer++) draw(markings[pattern][side][layer],
                        layer==1?accentColor:edgeColor,pose,buffer,light,overlay,alpha);
            }
            pose.popPose();
        }
    }
}
