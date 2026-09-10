package the_four_primitives_and_weapons.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

/** Validates saved/command appearance independently of entity and world initialization. */
public final class ButterflyAppearanceSettings {
    public static CompoundTag readAppearance(CompoundTag tag) {
        CompoundTag appearance = new CompoundTag();
        for (String key : new String[]{"WingColor", "EdgeColor", "AccentColor", "BodyColor"}) {
            if (tag.contains(key, 99)) {
                int value=tag.getInt(key);
                if (value>=0 && value<=0xFFFFFF) appearance.putInt(key,value);
            } else if (tag.contains(key, 8)) {
                String value=tag.getString(key);
                if (value.startsWith("#")) value=value.substring(1);
                else if (value.startsWith("0x") || value.startsWith("0X")) value=value.substring(2);
                if (value.matches("[0-9a-fA-F]{6}")) appearance.putInt(key,Integer.parseInt(value,16));
            }
        }
        if (tag.contains("Pattern",99) && tag.getInt("Pattern")>=0 && tag.getInt("Pattern")<ButterflyVariant.PATTERN_COUNT)
            appearance.putInt("Pattern",tag.getInt("Pattern"));
        if (tag.contains("Tails",99) && tag.getInt("Tails")>=0 && tag.getInt("Tails")<=1)
            appearance.putInt("Tails",tag.getInt("Tails"));
        readSetting(tag,appearance,"Size",0.25F,4);
        readSetting(tag,appearance,"FlapSpeed",0,4);
        readSetting(tag,appearance,"FlapAmount",0,1.4F);
        readSetting(tag,appearance,"FlightSpeed",0,3);
        readSetting(tag,appearance,"WingWidth",0.5F,2);
        readSetting(tag,appearance,"WingLength",0.5F,2);
        readSetting(tag,appearance,"AntennaLength",0.25F,3);
        readSetting(tag,appearance,"AntennaSpread",0,80);
        readSetting(tag,appearance,"AntennaTilt",0,80);
        readSetting(tag,appearance,"BodyWidth",0.5F,2);
        readSetting(tag,appearance,"BodyLength",0.5F,2);
        readSetting(tag,appearance,"FlapRestAngle",0,80);
        return appearance;
    }

    private static void readSetting(CompoundTag source, CompoundTag target, String key, float min, float max) {
        if (source.contains(key,99)) {
            float value=source.getFloat(key);
            if (Float.isFinite(value) && value>=0) target.putFloat(key,Mth.clamp(value,min,max));
        }
    }

    private ButterflyAppearanceSettings() {}
}
