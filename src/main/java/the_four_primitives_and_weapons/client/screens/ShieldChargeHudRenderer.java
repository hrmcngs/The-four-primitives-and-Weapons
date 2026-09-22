package the_four_primitives_and_weapons.client.screens;

import the_four_primitives_and_weapons.event.ShieldBashHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 利き手の盾をチャージ中、画面下部にバニラのジャンプチャージバーを表示する。 */
@Mod.EventBusSubscriber({Dist.CLIENT})
public class ShieldChargeHudRenderer {
    private static final ResourceLocation GUI_ICONS = new ResourceLocation("minecraft", "textures/gui/icons.png");
    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 5;
    private static final int HUD_GAP = 6;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui || player.isSpectator()) return;
        if (!player.isUsingItem() || player.getUsedItemHand() != InteractionHand.MAIN_HAND) return;
        if (!ShieldBashHandler.isShield(player.getUseItem())) return;

        int held = player.getUseItem().getUseDuration() - player.getUseItemRemainingTicks();
        float charge = Mth.clamp((float) held / ShieldBashHandler.MAX_CHARGE_TICKS, 0.0f, 1.0f);
        int filledWidth = (int) (BAR_WIDTH * charge);

        // ホットバー上に配置。体力・防具・空気・騎乗体力の段数に応じて重なりを避ける。
        int hudHeight = 39;
        if (mc.gui instanceof ForgeGui forgeGui) {
            hudHeight = Math.max(forgeGui.leftHeight, forgeGui.rightHeight);
        }
        int barX = (event.getWindow().getGuiScaledWidth() - BAR_WIDTH) / 2;
        int barY = Math.max(0, event.getWindow().getGuiScaledHeight() - hudHeight - HUD_GAP);
        var gui = event.getGuiGraphics();

        // 標準の枠と青い充填テクスチャをそのまま使用。リソースパックにも追従する。
        gui.blit(GUI_ICONS, barX, barY, 0, 84, BAR_WIDTH, BAR_HEIGHT);
        if (filledWidth > 0) {
            gui.blit(GUI_ICONS, barX, barY, 0, 89, filledWidth, BAR_HEIGHT);
        }
        String label = ShieldBashHandler.isDashBashCharging(player)
                ? "hud.the_four_primitives_and_weapons.shield_dash_bash_damage"
                : "hud.the_four_primitives_and_weapons.shield_bash_damage";
        Component damage = Component.translatable(label,
                net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(
                        ShieldBashHandler.getDisplayDamage(player.getUseItem(), held)));
        gui.drawCenteredString(mc.font, damage, barX + BAR_WIDTH / 2, barY - 11, 0xFFFFFF);
    }
}
