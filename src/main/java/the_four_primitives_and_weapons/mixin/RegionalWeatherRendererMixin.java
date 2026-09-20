package the_four_primitives_and_weapons.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import the_four_primitives_and_weapons.weather.*;

@Mixin(LevelRenderer.class)
public abstract class RegionalWeatherRendererMixin {
    @Redirect(method = "renderSky", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"))
    private net.minecraft.world.phys.Vec3 eclipse$skyColor(ClientLevel client, net.minecraft.world.phys.Vec3 pos, float partialTick) {
        return client.getSkyColor(pos, partialTick).scale(the_four_primitives_and_weapons.client.SolarEclipseLighting.brightness());
    }

    @Shadow private ClientLevel level;
    @Unique private float regionalWeather$columnIntensity = 1F;

    @org.spongepowered.asm.mixin.injection.Inject(method = "renderSky", at = @At(value = "INVOKE",
        target = "Lcom/mojang/blaze3d/systems/RenderSystem;depthMask(Z)V", ordinal = 1))
    private void regionalWeather$sky(com.mojang.blaze3d.vertex.PoseStack pose, org.joml.Matrix4f projection,
            float partialTick, net.minecraft.client.Camera camera, boolean foggy, Runnable setupFog,
            org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        if (Level.OVERWORLD.equals(level.dimension()))
            the_four_primitives_and_weapons.client.RegionalWeatherSky.render(level, pose, partialTick);
    }

    @org.spongepowered.asm.mixin.injection.Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void regionalWeather$replaceClouds(com.mojang.blaze3d.vertex.PoseStack pose, org.joml.Matrix4f projection,
            float partialTick, double x, double y, double z, org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci) {
        if (Level.OVERWORLD.equals(level.dimension())) ci.cancel();
    }

    @Redirect(method = "renderSnowAndRain", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/biome/Biome;hasPrecipitation()Z"), require = 1)
    private boolean regionalWeather$allowColumns(Biome biome) {
        return Level.OVERWORLD.equals(level.dimension()) || biome.hasPrecipitation();
    }
    @Redirect(method = "renderSnowAndRain", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"), require = 1)
    private Biome.Precipitation regionalWeather$column(Biome biome, BlockPos pos) {
        if (!Level.OVERWORLD.equals(level.dimension())) {
            regionalWeather$columnIntensity = 1F;
            return biome.getPrecipitationAt(pos);
        }
        var kind = RegionalWeather.at(level, pos, biome);
        regionalWeather$columnIntensity = WeatherRules.opacity(kind, level.getGameTime());
        return RegionalWeather.precipitation(kind, level.getGameTime());
    }
    @ModifyArg(method = "renderSnowAndRain", at = @At(value = "INVOKE",
        target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), index = 3, require = 1)
    private float regionalWeather$rainDensity(float alpha) { return alpha * regionalWeather$columnIntensity; }

    @Redirect(method = "tickRain", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/biome/Biome$Precipitation;"), require = 1)
    private Biome.Precipitation regionalWeather$rainSound(Biome biome, BlockPos pos) {
        return RegionalWeather.precipitation(level, pos, biome);
    }
    @Redirect(method = "tickRain", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"), require = 1)
    private float regionalWeather$splashDensity(ClientLevel client, float partialTick) {
        if (!Level.OVERWORLD.equals(client.dimension())) return client.getRainLevel(partialTick);
        BlockPos pos = Minecraft.getInstance().gameRenderer.getMainCamera().getBlockPosition();
        return client.getRainLevel(partialTick) * WeatherRules.intensity(RegionalWeather.at(client, pos), client.getGameTime());
    }
}
