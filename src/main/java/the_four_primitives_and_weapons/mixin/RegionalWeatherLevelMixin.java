package the_four_primitives_and_weapons.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import the_four_primitives_and_weapons.weather.RegionalWeather;

@Mixin(Level.class)
public abstract class RegionalWeatherLevelMixin {
    @Redirect(method = "isRainingAt", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"), require = 1)
    private Biome.Precipitation regionalWeather$wetness(Biome biome, BlockPos pos) {
        return RegionalWeather.precipitation((Level) (Object) this, pos, biome);
    }
}
