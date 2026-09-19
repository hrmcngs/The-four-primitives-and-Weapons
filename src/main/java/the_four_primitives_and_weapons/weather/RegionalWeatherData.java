package the_four_primitives_and_weapons.weather;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public final class RegionalWeatherData extends SavedData {
    public WeatherKind forced;
    public static WeatherKind clientForced;
    public static RegionalWeatherData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(
            RegionalWeatherData::load, RegionalWeatherData::new, "tfpaw_regional_weather");
    }
    public static RegionalWeatherData load(CompoundTag tag) {
        var data = new RegionalWeatherData();
        try { data.forced = WeatherKind.valueOf(tag.getString("Weather")); }
        catch (IllegalArgumentException ignored) { data.forced = null; }
        return data;
    }
    @Override public CompoundTag save(CompoundTag tag) {
        tag.putString("Weather", forced == null ? "auto" : forced.name()); return tag;
    }
}
