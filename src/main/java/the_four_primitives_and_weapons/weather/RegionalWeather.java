package the_four_primitives_and_weapons.weather;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;

public final class RegionalWeather {
    public static final TagKey<Biome> NO_RAIN = tag("no_rain");
    public static final TagKey<Biome> NO_SNOW = tag("no_snow");
    public static final TagKey<Biome> NO_THUNDER = tag("no_thunder");
    public static final TagKey<Biome> NO_FOG = tag("no_fog");
    public static final TagKey<Biome> SANDSTORMS = tag("sandstorms");
    private static TagKey<Biome> tag(String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(TheFourPrimitivesAndWeaponsMod.MODID, "weather/" + name));
    }
    public static WeatherKind at(Level level, BlockPos pos) { return at(level, pos, level.getBiome(pos).value()); }
    public static WeatherKind at(Level level, BlockPos pos, Biome biome) {
        if (!Level.OVERWORLD.equals(level.dimension())) return WeatherKind.CLEAR;
        var holder = level.getBiome(pos);
        boolean desert = holder.is(Biomes.DESERT);
        var climate = new WeatherRules.Climate(desert, !biome.hasPrecipitation(),
            biome.getPrecipitationAt(pos) == Biome.Precipitation.SNOW,
            biome.getModifiedClimateSettings().downfall() >= 0.7F,
            holder.is(SANDSTORMS), holder.is(NO_RAIN), holder.is(NO_SNOW), holder.is(NO_THUNDER), holder.is(NO_FOG));
        WeatherKind forced = level instanceof ServerLevel server ? RegionalWeatherData.get(server).forced : RegionalWeatherData.clientForced;
        return WeatherRules.resolve(climate, level.getDayTime(), level.getGameTime(), level.isRaining(), level.isThundering(), forced);
    }
    public static Biome.Precipitation precipitation(Level level, BlockPos pos, Biome biome) {
        if (!Level.OVERWORLD.equals(level.dimension())) return biome.getPrecipitationAt(pos);
        WeatherKind kind = at(level, pos, biome);
        if (WeatherRules.intensity(kind, level.getGameTime()) == 0F) return Biome.Precipitation.NONE;
        return switch (kind.precipitation) {
            case 1 -> Biome.Precipitation.RAIN;
            case 2 -> Biome.Precipitation.SNOW;
            default -> Biome.Precipitation.NONE;
        };
    }
    private RegionalWeather() { }
}
