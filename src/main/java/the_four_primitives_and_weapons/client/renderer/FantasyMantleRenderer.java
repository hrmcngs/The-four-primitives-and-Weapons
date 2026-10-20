package the_four_primitives_and_weapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import the_four_primitives_and_weapons.client.model.FantasyPatternQuad;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.ClipContext;
import com.mojang.math.Axis;
import the_four_primitives_and_weapons.client.MantleAnimationState;
import the_four_primitives_and_weapons.network.MantleActionPacket;
import the_four_primitives_and_weapons.util.MantleCombatRules;

/** Unadorned black mantle: folded shoulder yoke, overlapping cloth strips and ankle-length hem. */
public class FantasyMantleRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(
        "the_four_primitives_and_weapons", "textures/models/armor/fantasy_mantle.png");
    private final ModelPart cloth;
    private final ModelPart wrap;
    private final ModelPart blade;
    public FantasyMantleRenderer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("yoke", CubeListBuilder.create()
            .addBox(-5, -1.2f, 2.4f, 10, 1.8f, 1.2f), PartPose.ZERO);
        for (int sign : new int[]{-1, 1}) {
            root.addOrReplaceChild("shoulder" + sign, CubeListBuilder.create()
                .addBox(-2, -.6f, -2.4f, 4, 1.2f, 5.8f),
                PartPose.offsetAndRotation(sign * 4.6f, -.4f, 0, .06f, 0, sign * .16f));
            root.addOrReplaceChild("lapel" + sign, CubeListBuilder.create()
                .addBox(-.65f, -1.3f, -.45f, 1.3f, 3.2f, .9f),
                PartPose.offsetAndRotation(sign * 3.7f, .2f, -2.3f, -.15f, 0, -sign * .18f));
        }
        for (int i = -3; i <= 3; i++) {
            PartDefinition panel = root.addOrReplaceChild("panel" + i, CubeListBuilder.create()
                .addBox(-1.3f, 0, 0, 2.6f, 9, .7f),
                PartPose.offsetAndRotation(i * 2.2f, 0, 3.2f + Math.abs(i) * .05f,
                    .07f, -i * .025f, -i * .012f));
            panel.addOrReplaceChild("hem", CubeListBuilder.create()
                .addBox(-1.6f, 0, 0, 3.2f, 14 - Math.abs(i) * .4f, .7f),
                PartPose.offsetAndRotation(0, 9, 0, .035f, 0, -i * .012f));
        }
        cloth = LayerDefinition.create(mesh, 64, 64).bakeRoot();
        wrap = the_four_primitives_and_weapons.client.model.MantleActionModels.wrap();
        blade = the_four_primitives_and_weapons.client.model.MantleActionModels.blade();
    }
    @Override public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext context, PoseStack poses, RenderLayerParent<T, M> parent,
            MultiBufferSource buffers, int light, float limbSwing, float limbAmount,
            float partialTicks, float age, float yaw, float pitch) {
        if (!(parent.getModel() instanceof HumanoidModel<?> humanoid) || context.entity().isInvisible()) return;
        var state = MantleAnimationState.get(context.entity());
        float ageTicks = state == null ? 0 : context.entity().level().getGameTime() - state.start() + partialTicks;
        float deformation = state == null ? 0 : MantleCombatRules.envelope(ageTicks, state.duration());
        boolean guarding = state != null && state.action() == MantleActionPacket.Action.GUARD;
        boolean thrusting = state != null && state.action() == MantleActionPacket.Action.THRUST
            && context.entity().getItemInHand(state.hand()).isEmpty();
        float guard = guarding ? deformation : 0;
        float winding = thrusting ? deformation : 0;
        for (int i = -3; i <= 3; i++) {
            ModelPart panel = cloth.getChild("panel" + i);
            float wave = (float)Math.sin(ageTicks * .22f + i * .4f);
            float response = MantleAnimationState.deflection(context.entity(), partialTicks);
            // Lift and sweep the cloth away from an impact; keep it open, never form a cocoon.
            panel.x = i * 2.2f + guard * (i * .7f + response * 5);
            panel.y = -guard * (2.5f + wave * .7f + Math.abs(response));
            panel.z = 3.2f + Math.abs(i) * .05f - guard * (2.8f + Math.abs(response) * 2);
            panel.xRot = .07f + Math.min(limbAmount, 1) * .22f
                + (float)Math.sin(age * .07f + i * .35f) * .018f
                - guard * (.8f + wave * .18f + Math.abs(response) * .3f);
            panel.yRot = -i * .025f + guard * (wave * .25f + response * .7f);
            panel.zRot = -i * .012f + guard * (wave * .15f + response * .25f);
            panel.getChild("hem").xRot = .035f + guard * (.35f + wave * .3f)
                + (float)Math.sin(age * .07f + i * .35f - .5f) * .025f;
        }
        poses.pushPose();
        humanoid.body.translateAndRotate(poses);
        poses.scale(1 - winding * .2f, 1 - winding * .55f, 1);
        VertexConsumer out = buffers.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        cloth.render(poses, out, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        for (int i = -3; i <= 3; i++) {
            ModelPart panel = cloth.getChild("panel" + i);
            float u0 = (i + 3) / 7f;
            float u1 = (i + 4) / 7f;
            float split = 9 / 23f;
            poses.pushPose(); panel.translateAndRotate(poses);
            FantasyPatternQuad.draw(poses, out, light, OverlayTexture.NO_OVERLAY,
                -1.3f, 0, 1.3f, 9, .72f, u0, 0, u1, split, true, 1, 1, 1, 1);
            poses.pushPose(); panel.getChild("hem").translateAndRotate(poses);
            FantasyPatternQuad.draw(poses, out, light, OverlayTexture.NO_OVERLAY,
                -1.6f, 0, 1.6f, 14 - Math.abs(i) * .4f, .72f,
                u0, split, u1, 1, true, 1, 1, 1, 1);
            poses.popPose(); poses.popPose();
        }
        poses.popPose();
        if (thrusting && context.entity() instanceof Player player) {
            HumanoidArm side = state.hand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            ModelPart arm = side == HumanoidArm.RIGHT ? humanoid.rightArm : humanoid.leftArm;
            poses.pushPose();
            arm.translateAndRotate(poses);
            poses.translate((side == HumanoidArm.RIGHT ? -1 : 1) / 16f, 0, 0);
            poses.scale(1, .65f + winding * .35f, 1);
            for (int i = 0; i < 4; i++) {
                ModelPart coil = wrap.getChild("coil" + i);
                float turn = (1 - winding) * (float)Math.PI * 1.5f + i * .6f;
                coil.x = (float)Math.sin(turn) * (1 - winding) * 3;
                coil.z = (float)Math.cos(turn) * (1 - winding) * 3;
                coil.yRot = i * .22f + (1 - winding) * (float)Math.PI;
            }
            wrap.render(poses, out, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            float extension = MantleCombatRules.thrustExtension(ageTicks);
            if (extension > 0) {
                poses.translate(0, 9 / 16f, -2 / 16f);
                poses.mulPose(Axis.XP.rotationDegrees(player.getXRot()));
                var origin = player.getEyePosition().add(0, -.35, 0);
                var end = origin.add(player.getLookAngle().scale(7));
                var wall = player.level().clip(new ClipContext(origin, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation();
                poses.scale(1, 1, (float)Math.min(7, origin.distanceTo(wall)) * extension);
                blade.render(poses, out, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
            poses.popPose();
        }
    }
}
