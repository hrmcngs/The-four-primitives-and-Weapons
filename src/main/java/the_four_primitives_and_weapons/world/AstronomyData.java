package the_four_primitives_and_weapons.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public final class AstronomyData extends SavedData {
    private AstronomySettings settings = AstronomySettings.DEFAULT;
    // Written only by the S2C packet; server state always comes from SavedData.
    public static AstronomySettings clientSettings = AstronomySettings.DEFAULT;

    public static AstronomyData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(
            AstronomyData::load, AstronomyData::new, "tfpaw_astronomy");
    }
    public static AstronomySettings settings(Level level) {
        if (!Level.OVERWORLD.equals(level.dimension())) return AstronomySettings.DEFAULT;
        return level instanceof ServerLevel server ? get(server).settings : clientSettings;
    }
    public AstronomySettings settings() { return settings; }
    public void update(AstronomySettings value) { settings = value; setDirty(); }
    public static AstronomySettings decode(CompoundTag tag) {
        return new AstronomySettings(tag.contains("Size") ? tag.getFloat("Size") : -1F,
            tag.contains("Phase") ? tag.getInt("Phase") : -1,
            tag.contains("Color") ? tag.getInt("Color") : -1,
            tag.contains("Meteors") ? tag.getInt("Meteors") : -1,
            !tag.contains("Effects") || tag.getBoolean("Effects"),
            tag.contains("Solar") ? tag.getFloat("Solar") : -1F,
            tag.contains("Lunar") ? tag.getFloat("Lunar") : -1F);
    }
    public static CompoundTag encode(AstronomySettings value) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Size", value.size()); tag.putInt("Phase", value.phase());
        tag.putInt("Color", value.color()); tag.putInt("Meteors", value.meteors());
        tag.putBoolean("Effects", value.effects());
        tag.putFloat("Solar", value.solar()); tag.putFloat("Lunar", value.lunar());
        return tag;
    }
    public static AstronomyData load(CompoundTag tag) {
        AstronomyData data = new AstronomyData(); data.settings = decode(tag); return data;
    }
    @Override public CompoundTag save(CompoundTag tag) { tag.merge(encode(settings)); return tag; }
}
