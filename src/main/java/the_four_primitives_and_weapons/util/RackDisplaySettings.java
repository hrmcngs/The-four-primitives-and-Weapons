package the_four_primitives_and_weapons.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

/** 設置ラックに保存する各スロットの追加変換。距離はブロック、角度は度。 */
public final class RackDisplaySettings {
    private static final String[] KEYS = {"X", "Y", "Z", "RotX", "RotY", "RotZ", "Scale"};
    private RackDisplaySettings() {}

    public static float value(CompoundTag slot, int mode) {
        if (mode < 0 || mode >= KEYS.length) return 0;
        float fallback = mode == 6 ? 1 : 0;
        float value = slot.contains(KEYS[mode], 99) ? slot.getFloat(KEYS[mode]) : fallback;
        return Float.isFinite(value) ? value : fallback;
    }

    public static void adjust(CompoundTag slot, int mode, int direction, boolean fine) {
        if (mode < 0 || mode >= KEYS.length || (direction != -1 && direction != 1)) return;
        float step = mode < 3 ? (fine ? 0.01f : 0.05f) : mode < 6 ? (fine ? 1 : 5) : (fine ? 0.01f : 0.05f);
        float next = value(slot, mode) + direction * step;
        if (mode < 3) next = Mth.clamp(next, -2, 2);
        else if (mode < 6) next = Mth.wrapDegrees(next);
        else next = Mth.clamp(next, 0.1f, 3);
        slot.putFloat(KEYS[mode], next);
    }
}
