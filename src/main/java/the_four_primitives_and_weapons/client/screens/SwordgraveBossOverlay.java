package the_four_primitives_and_weapons.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Pixel-built stone frame: no texture allocation or world/entity scans per frame. */
@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons", value = Dist.CLIENT)
public final class SwordgraveBossOverlay {
    private static final String PREFIX = "boss.the_four_primitives_and_weapons.swordgrave.";
    private static final String ENTITY = "entity.the_four_primitives_and_weapons.swordgrave_warden";
    private SwordgraveBossOverlay() {}

    @SubscribeEvent
    public static void render(CustomizeGuiOverlayEvent.BossEventProgress event) {
        var boss = event.getBossEvent();
        if (!(boss.getName().getContents() instanceof TranslatableContents text)) return;
        String key = text.getKey();
        if (!key.startsWith(PREFIX) && !key.equals(ENTITY)) return;
        String state = key.startsWith(PREFIX) ? key.substring(PREFIX.length()) : "ready";
        boolean exposed = state.equals("open") || state.equals("broken");
        boolean warning = state.equals("thrust") || state.equals("sweep") || state.equals("fall");
        float health = Mth.clamp(boss.getProgress(), 0, 1);
        int fill = exposed ? 0xFF4EAEB7 : health <= 0.5f ? 0xFFAB4143 : 0xFF788A48;
        int accent = exposed ? 0xFF8AE1DC : warning ? 0xFFE7B765 : 0xFFB5C393;
        var gui = event.getGuiGraphics();
        var font = Minecraft.getInstance().font;
        int width = Math.min(182, Math.max(48, event.getWindow().getGuiScaledWidth() - 40));
        int x = (event.getWindow().getGuiScaledWidth() - width) / 2;
        int y = event.getY();
        event.setCanceled(true);
        event.setIncrement(30);

        // Five-pixel rail, matching vanilla thickness. Decoration lives at the
        // ends and outside the fill, keeping low health readable down to a pixel.
        gui.fill(x, y, x + width, y + 5, 0xFF222923);
        gui.fill(x + 1, y, x + width - 1, y + 1, 0xFF9BA692);
        gui.fill(x + 1, y + 4, x + width - 1, y + 5, 0xFF4A5943);
        int filled = Mth.ceil((width - 2) * health);
        if (filled > 0) {
            gui.fill(x + 1, y + 1, x + 1 + filled, y + 4, fill);
            gui.fill(x + 1, y + 1, x + 1 + filled, y + 2, accent);
        }
        // Chipped stone border and hanging moss: irregular, never crossing HP.
        for (int i = 0; i < 6; i++) {
            int patch = x + 12 + (width - 24) * i / 6;
            gui.fill(patch, y, patch + 2, y + 1, 0xFF35432F);
            gui.fill(patch + 3, y - 1, patch + 7, y, 0xFF688349);
            gui.fill(patch + 5, y + 5, patch + 8, y + 6, 0xFF435D36);
            if (i % 2 == 0) gui.fill(patch + 6, y + 6, patch + 7, y + 7, 0xFF688349);
        }
        // Symmetric stepped stone wings and inlaid runes at both ends.
        for (int side : new int[] {-1, 1}) {
            int edge = side < 0 ? x : x + width;
            for (int step = 0; step < 3; step++) {
                int start = side < 0 ? edge - 3 - step * 3 : edge + step * 3;
                gui.fill(start, y - 2 + step, start + 3, y + 7 - step, 0xFF28322B);
                gui.fill(start, y - 2 + step, start + 3, y - 1 + step, 0xFF9BA692);
                gui.fill(start + 1, y + 1, start + 2, y + 4, 0xFF617B47);
            }
        }
        sword(gui, x - 7, y);
        int jewel = x + width + 4;
        gui.fill(jewel, y - 1, jewel + 1, y + 6, 0xFF293B2D);
        gui.fill(jewel - 2, y + 2, jewel + 3, y + 3, 0xFF293B2D);
        gui.fill(jewel - 1, y + 1, jewel + 2, y + 4, accent);
        gui.fill(jewel, y + 1, jewel + 1, y + 2, 0xFFEDF0CF);

        Component name = text.getArgs().length > 0 && text.getArgs()[0] instanceof Component supplied
                ? supplied : Component.translatable(ENTITY);
        String percent = Math.round(health * 100) + "%";
        gui.drawString(font, font.plainSubstrByWidth(name.getString(), width - font.width(percent) - 8), x, y - 12, 0xFFE0E3D9);
        gui.drawString(font, percent, x + width - font.width(percent), y - 12, accent);
        String hint = Component.translatable("hud.the_four_primitives_and_weapons.swordgrave." + state).getString();
        gui.drawCenteredString(font, font.plainSubstrByWidth(hint, width + 16), x + width / 2, y + 11, accent);
    }
    private static void sword(GuiGraphics gui, int x, int y) {
        gui.fill(x, y - 5, x + 3, y + 8, 0xFF252B29);
        gui.fill(x, y + 2, x + 3, y + 7, 0xFFADB5B1);
        gui.fill(x + 1, y + 3, x + 2, y + 8, 0xFFE0E5DA);
        gui.fill(x - 2, y, x + 5, y + 2, 0xFF697569);
        gui.fill(x, y - 6, x + 3, y - 4, 0xFF74A341);
    }
}
