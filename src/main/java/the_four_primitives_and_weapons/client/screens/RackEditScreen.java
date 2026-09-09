package the_four_primitives_and_weapons.client.screens;

import java.util.Locale;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.entity.WeaponRackEntity;
import the_four_primitives_and_weapons.network.RackEditMessage;
import the_four_primitives_and_weapons.util.RackDisplaySettings;

/** ワールドを見ながら編集し、変更ごとに設置済みラックへ保存する。 */
public final class RackEditScreen extends Screen {
    private final int entityId;
    private int slot;
    private int top;
    private boolean fine = true;
    private static final String[] LABELS = {"x", "y", "z", "rx", "ry", "rz", "scale"};

    public RackEditScreen(int entityId) {
        super(text("title"));
        this.entityId = entityId;
    }
    private static Component text(String key, Object... args) {
        return Component.translatable("gui.the_four_primitives_and_weapons.rack." + key, args);
    }
    private WeaponRackEntity rack() {
        return minecraft != null && minecraft.level != null
            && minecraft.level.getEntity(entityId) instanceof WeaponRackEntity rack ? rack : null;
    }
    @Override
    protected void init() {
        top = Math.max(4, (height - 238) / 2);
        var rack = rack();
        int count = rack != null && rack.supportsTwoSlots() ? 2 : 1;
        for (int i = 0; i < count; i++) {
            final int selected = i;
            addRenderableWidget(Button.builder(text("slot", i + 1), b -> slot = selected)
                .bounds(12 + i * 103, top + 16, 100, 18).build());
        }
        for (int mode = 0; mode < LABELS.length; mode++) {
            final int value = mode;
            int y = top + 58 + mode * 20;
            addRenderableWidget(Button.builder(Component.literal("−"), b -> send(value, -1)).bounds(155, y, 26, 18).build());
            addRenderableWidget(Button.builder(Component.literal("+"), b -> send(value, 1)).bounds(187, y, 26, 18).build());
        }
        addRenderableWidget(Button.builder(text("pose"), b -> send(7, 1)).bounds(12, top + 200, 65, 18).build());
        addRenderableWidget(Button.builder(text("fine"), b -> {
            fine = !fine; b.setMessage(text(fine ? "fine" : "coarse"));
        }).bounds(80, top + 200, 65, 18).build());
        addRenderableWidget(Button.builder(text("reset"), b -> send(8, 1)).bounds(148, top + 200, 65, 18).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> onClose()).bounds(12, top + 220, 201, 18).build());
    }
    private void send(int mode, int direction) {
        var rack = rack();
        if (rack == null || (slot == 0 ? rack.getItem() : rack.getItem2()).isEmpty()) return;
        TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.sendToServer(new RackEditMessage(entityId, slot, mode, direction, fine));
    }
    @Override public boolean isPauseScreen() { return false; }
    @Override public void tick() {
        var rack = rack();
        if (rack == null || minecraft.player == null || rack.distanceToSqr(minecraft.player) > 36) onClose();
    }
    @Override public void render(GuiGraphics g, int mx, int my, float partial) {
        g.fill(6, top - 4, 219, top + 242, 0xCF161616);
        g.drawString(font, title, 12, top + 2, 0xFFFFFF);
        var rack = rack();
        if (rack != null) {
            var item = slot == 0 ? rack.getItem() : rack.getItem2();
            Component label = text("selected", slot + 1, item.isEmpty() ? text("empty") : item.getHoverName());
            g.drawString(font, font.plainSubstrByWidth(label.getString(), 201), 12, top + 39, 0xFFE080);
            var settings = rack.getSlotSettings(slot);
            for (int i = 0; i < LABELS.length; i++) {
                String value = String.format(Locale.ROOT, "%.2f", RackDisplaySettings.value(settings, i));
                g.drawString(font, text(LABELS[i]), 12, top + 63 + i * 20, 0xFFFFFF);
                g.drawString(font, value, 107, top + 63 + i * 20, 0xBBBBBB);
            }
        }
        super.render(g, mx, my, partial);
    }
}
