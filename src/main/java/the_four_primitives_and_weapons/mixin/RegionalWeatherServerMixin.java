package the_four_primitives_and_weapons.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import the_four_primitives_and_weapons.weather.RegionalWeather;

@Mixin(ServerLevel.class)
public abstract class RegionalWeatherServerMixin {
    @Redirect(method = "tickChunk", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"), require = 1)
    private Biome.Precipitation regionalWeather$blockPrecipitation(Biome biome, BlockPos pos) {
        return RegionalWeather.precipitation((ServerLevel) (Object) this, pos, biome);
    }
    @Redirect(method = "tickChunk", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/biome/Biome;shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"), require = 1)
    private boolean regionalWeather$snowAccumulation(Biome biome, LevelReader reader, BlockPos pos) {
        return RegionalWeather.precipitation((ServerLevel) (Object) this, pos, biome) == Biome.Precipitation.SNOW
            && biome.shouldSnow(reader, pos);
    }
    @Redirect(method = "tickChunk", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/server/level/ServerLevel;isRainingAt(Lnet/minecraft/core/BlockPos;)Z"), require = 1)
    private boolean regionalWeather$lightning(ServerLevel level, BlockPos pos) {
        return level.isRainingAt(pos) && (!Level.OVERWORLD.equals(level.dimension()) || RegionalWeather.at(level, pos).thunder);
    }
}
