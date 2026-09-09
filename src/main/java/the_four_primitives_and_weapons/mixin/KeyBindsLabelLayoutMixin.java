package the_four_primitives_and_weapons.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.locale.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** 長いMODキー名がキー設定一覧の全ラベルを画面外へ押し出すのを防ぐ。 */
@Mixin(KeyBindsList.KeyEntry.class)
public abstract class KeyBindsLabelLayoutMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target =
            "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I"))
    private int maw_drawVisibleKeyName(GuiGraphics graphics, Font font, Component name,
                                      int x, int y, int color, boolean shadow,
                                      GuiGraphics rowGraphics, int index, int rowTop, int rowLeft,
                                      int rowWidth, int rowHeight, int mouseX, int mouseY,
                                      boolean hovered, float partialTick) {
        int left = Math.max(8, x);
        // Forgeの変更ボタンはrowLeft+105、競合マーカーはその6px左にある。
        int available = Math.max(0, rowLeft + 95 - left);
        if (font.width(name) <= available) {
            return graphics.drawString(font, name, left, y, color, shadow);
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null && mouseX >= left && mouseX < left + available
                && mouseY >= rowTop && mouseY < rowTop + rowHeight) {
            // 一覧のscissor外で描くため、画面の最後にツールチップを描画する。
            mc.screen.setTooltipForNextRenderPass(font.split(name, Math.max(100, mc.screen.width - 20)));
        }
        String ellipsis = "...";
        int suffixWidth = font.width(ellipsis);
        if (available < suffixWidth) return left;
        int end = graphics.drawString(font, Language.getInstance().getVisualOrder(font.substrByWidth(name, available - suffixWidth)),
                left, y, color, shadow);
        return graphics.drawString(font, ellipsis, end, y, color, shadow);
    }
}
