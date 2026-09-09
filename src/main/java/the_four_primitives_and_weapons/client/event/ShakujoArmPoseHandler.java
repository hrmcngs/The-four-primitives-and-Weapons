package the_four_primitives_and_weapons.client.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 通常の錫杖と、納刀中の仕込み錫杖が共有する片手持ち。 */
@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons", value = Dist.CLIENT)
public final class ShakujoArmPoseHandler {
    private static final TagKey<Item> HELD = ItemTags.create(
        new ResourceLocation("the_four_primitives_and_weapons", "shakujo_hold"));
    private static final HumanoidModel.ArmPose POSE = HumanoidModel.ArmPose.create(
        "MAW_SHAKUJO_HOLD", false, (model, entity, arm) -> {
            var part = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
            part.xRot = (float)Math.toRadians(-35);
            part.yRot = 0;
            part.zRot = 0;
        });

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
        if (!(event.getEntity() instanceof AbstractClientPlayer player)
            || !(event.getRenderer().getModel() instanceof PlayerModel<?> model)) return;
        if (player.isUsingItem() || player.isFallFlying() || player.isSwimming()
            || model.rightArmPose.isTwoHanded() || model.leftArmPose.isTwoHanded()) return;
        for (HumanoidArm arm : HumanoidArm.values()) {
            var stack = arm == player.getMainArm() ? player.getMainHandItem() : player.getOffhandItem();
            var current = arm == HumanoidArm.RIGHT ? model.rightArmPose : model.leftArmPose;
            // ガード等の専用ポーズは優先し、通常の持ち方だけを差し替える。
            if (stack.is(HELD) && (current == HumanoidModel.ArmPose.ITEM || current == HumanoidModel.ArmPose.EMPTY)) {
                if (arm == HumanoidArm.RIGHT) model.rightArmPose = POSE;
                else model.leftArmPose = POSE;
            }
        }
    }
}
