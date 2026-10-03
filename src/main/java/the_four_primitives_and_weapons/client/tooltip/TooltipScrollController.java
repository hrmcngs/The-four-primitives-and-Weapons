package the_four_primitives_and_weapons.client.tooltip;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.config.TooltipConfig;

/** Shift+scroll follows both native axes; Alt+wheel forces horizontal movement. */
@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons", value = Dist.CLIENT)
public final class TooltipScrollController {
    private static final int SCREEN_MARGIN = 4;
    private static final int SCROLL_STEP = 24;
    private static final long HOVER_TIMEOUT_MS = 1000L;

    private static ItemStack lastStack = ItemStack.EMPTY;
    private static ItemStack lastStackReference = ItemStack.EMPTY;
    private static Screen lastScreen;
    private static int offset;
    private static double scrollRemainder;
    private static int horizontalOffset;
    private static double horizontalRemainder;
    private static long lastRenderTime;

    private TooltipScrollController() {}

    @SubscribeEvent
    public static void onTooltipPre(RenderTooltipEvent.Pre event) {
        ItemStack stack = event.getItemStack();
        Screen screen = Minecraft.getInstance().screen;
        boolean resumedAfterPause = Util.getMillis() - lastRenderTime > HOVER_TIMEOUT_MS;
        // A hovered stack may update its NBT each tick. That must not reset its
        // scroll position while the cursor remains over the same stack object.
        boolean changedStack = lastStackReference != stack && !ItemStack.isSameItemSameTags(lastStack, stack);
        if (resumedAfterPause || screen != lastScreen || changedStack) {
            lastStack = stack.copy();
            offset = 0;
            scrollRemainder = 0.0;
            horizontalOffset = 0;
            horizontalRemainder = 0.0;
        }
        lastStackReference = stack;
        lastScreen = screen;
        lastRenderTime = Util.getMillis();
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (applyScroll(event.getScrollDelta())) event.setCanceled(true);
    }

    /** GUI screens use ScreenEvent instead of InputEvent.MouseScrollingEvent. */
    @SubscribeEvent
    public static void onScreenMouseScroll(ScreenEvent.MouseScrolled.Pre event) {
        if (applyScroll(event.getScrollDelta())) event.setCanceled(true);
    }

    private static boolean applyScroll(double delta) {
        if (!canScroll() || !Double.isFinite(delta) || delta == 0) return false;
        return addScroll(delta, Screen.hasAltDown());
    }

    private static boolean canScroll() {
        return !((!Screen.hasShiftDown() && !Screen.hasAltDown()
                && !(Minecraft.ON_OSX && Screen.hasControlDown()))
                || lastRenderTime == 0 || Minecraft.getInstance().screen != lastScreen
                || Util.getMillis() - lastRenderTime > HOVER_TIMEOUT_MS);
    }

    public static boolean onNativeScroll(double x, double y) {
        if (!canScroll()) return false;
        var movement = TooltipScrollInput.route(x, y, Screen.hasAltDown());
        boolean consumed = false;
        if (movement.horizontal() != 0) consumed |= addScroll(movement.horizontal(), true);
        if (movement.vertical() != 0) consumed |= addScroll(movement.vertical(), false);
        return consumed;
    }

    private static boolean addScroll(double delta, boolean horizontal) {

        // A mouse wheel usually reports +/-1, while macOS trackpads send many
        // small fractional deltas. Accumulating the fraction supports both.
        double direction = TooltipConfig.reverseScrollDirection ? -1.0 : 1.0;
        if (horizontal) {
            horizontalRemainder += delta * SCROLL_STEP * direction;
            int movement = (int) horizontalRemainder;
            horizontalRemainder -= movement;
            horizontalOffset += movement;
            return true;
        }
        scrollRemainder += delta * SCROLL_STEP * direction;
        int movement = (int) scrollRemainder;
        if (movement == 0) return true;

        scrollRemainder -= movement;
        int oldOffset = offset;
        offset += movement;
        return offset != oldOffset;
    }

    /** Uses the same edge behavior as vertical movement, including oversized tooltips. */
    public static int adjustedX(int vanillaX, int screenWidth, int tooltipWidth) {
        int minimumX = SCREEN_MARGIN - tooltipWidth;
        int maximumX = screenWidth - SCREEN_MARGIN;
        int adjusted = Math.max(minimumX, Math.min(maximumX, vanillaX + horizontalOffset));
        horizontalOffset = adjusted - vanillaX;
        return adjusted;
    }

    /** Called after vanilla has chosen the tooltip position. */
    public static int adjustedY(int vanillaY, int screenHeight, int tooltipHeight) {
        int minimumY = SCREEN_MARGIN - tooltipHeight;
        int maximumY = screenHeight - SCREEN_MARGIN;
        int adjusted = Math.max(minimumY, Math.min(maximumY, vanillaY + offset));
        // Keep the stored value synchronized with the clamped position so the
        // reverse direction responds immediately at either edge.
        offset = adjusted - vanillaY;
        return adjusted;
    }
}
