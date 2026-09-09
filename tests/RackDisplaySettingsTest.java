import java.io.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import the_four_primitives_and_weapons.util.RackDisplaySettings;

public class RackDisplaySettingsTest {
    private static void near(float actual, float expected) {
        if (Math.abs(actual - expected) > 0.0001f)
            throw new AssertionError("Expected " + expected + ", got " + actual);
    }

    public static void main(String[] args) throws Exception {
        CompoundTag left = new CompoundTag(), right = new CompoundTag();
        near(RackDisplaySettings.value(left, 6), 1);
        RackDisplaySettings.adjust(left, 0, 1, true);
        RackDisplaySettings.adjust(left, 4, -1, false);
        RackDisplaySettings.adjust(right, 1, -1, true);
        RackDisplaySettings.adjust(right, 6, 1, false);
        near(RackDisplaySettings.value(left, 0), 0.01f);
        near(RackDisplaySettings.value(left, 1), 0);
        near(RackDisplaySettings.value(right, 0), 0);
        near(RackDisplaySettings.value(right, 1), -0.01f);

        CompoundTag slots = new CompoundTag();
        slots.put("Slot0", left.copy()); slots.put("Slot1", right.copy());
        CompoundTag rack = new CompoundTag(); rack.put("SlotDisplaySettings", slots);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        NbtIo.write(rack, new DataOutputStream(bytes));
        CompoundTag restored = NbtIo.read(new DataInputStream(new ByteArrayInputStream(bytes.toByteArray())));
        CompoundTag loaded = restored.getCompound("SlotDisplaySettings");
        near(RackDisplaySettings.value(loaded.getCompound("Slot0"), 0), 0.01f);
        near(RackDisplaySettings.value(loaded.getCompound("Slot0"), 4), -5);
        near(RackDisplaySettings.value(loaded.getCompound("Slot1"), 1), -0.01f);
        near(RackDisplaySettings.value(loaded.getCompound("Slot1"), 6), 1.05f);
        RackDisplaySettings.adjust(left, 0, 1, false);
        near(RackDisplaySettings.value(loaded.getCompound("Slot0"), 0), 0.01f);

        for (int i = 0; i < 500; i++) {
            RackDisplaySettings.adjust(left, 0, 1, false);
            RackDisplaySettings.adjust(right, 6, -1, false);
        }
        near(RackDisplaySettings.value(left, 0), 2);
        near(RackDisplaySettings.value(right, 6), 0.1f);
        left.putFloat("RotX", 179);
        RackDisplaySettings.adjust(left, 3, 1, false);
        near(RackDisplaySettings.value(left, 3), -176);
        left.putFloat("Scale", Float.NaN);
        near(RackDisplaySettings.value(left, 6), 1);
        RackDisplaySettings.adjust(right, 6, 10, true);
        near(RackDisplaySettings.value(right, 6), 0.1f);
        System.out.println("Rack slot independence, NBT round-trip, fine steps, and bounds checks passed");
    }
}
