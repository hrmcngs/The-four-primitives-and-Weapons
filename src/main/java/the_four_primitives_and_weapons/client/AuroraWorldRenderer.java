package the_four_primitives_and_weapons.client;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import the_four_primitives_and_weapons.world.*;

/** Depth-tested, world-anchored light curtains above the normal cloud layer. */
@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons", value = Dist.CLIENT)
public final class AuroraWorldRenderer {
    private AuroraWorldRenderer() {}
    @SubscribeEvent
    public static void render(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) return;
        var mc = Minecraft.getInstance();
        var level = mc.level;
        if (level == null || !Level.OVERWORLD.equals(level.dimension())) return;
        var settings = AstronomyData.settings(level);
        if (!settings.auroraActive(level.getDayTime())) return;
        var biome = level.getBiome(event.getCamera().getBlockPosition());
        var style = AuroraBiomeStyle.select(biome.value().getBaseTemperature(),
                biome.is(BiomeTags.IS_JUNGLE), biome.is(BiomeTags.IS_BADLANDS),
                biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_BEACH),
                biome.is(BiomeTags.IS_MOUNTAIN), biome.is(BiomeTags.IS_FOREST) || biome.is(BiomeTags.IS_TAIGA))
                .forForcedDisplay(settings.aurora() == 1);
        float partial = event.getPartialTick();
        float visibility = Math.min(1, level.getStarBrightness(partial) * 2) * style.strength
                * (1 - level.getRainLevel(partial)) * (1 - level.getThunderLevel(partial));
        if (visibility < 0.01F) return;
        double cloudHeight = level.effects().getCloudHeight();
        if (!Double.isFinite(cloudHeight)) cloudHeight = 192;
        Vec3 camera = event.getCamera().getPosition();
        int tileX = AuroraCurtainGeometry.tile(camera.x), tileZ = AuroraCurtainGeometry.tile(camera.z);
        double time = (Math.floorMod(level.getDayTime(), 24000L) + partial) * 0.003;
        var buffers = mc.renderBuffers().bufferSource();
        VertexConsumer buffer = buffers.getBuffer(CurtainType.TYPE);
        Matrix4f matrix = event.getPoseStack().last().pose();
        // At most nine tiles; edge fading removes tiles before the window shifts.
        // Three translucent sheets give each curtain thickness without particles.
        for (int tx = tileX - 1; tx <= tileX + 1; tx++) {
            for (int tz = tileZ - 1; tz <= tileZ + 1; tz++) {
                if (!event.getFrustum().isVisible(new AABB((double) tx * 256, cloudHeight, (double) tz * 256 - 48,
                        ((double) tx + 1) * 256, cloudHeight + 120, ((double) tz + 1) * 256 + 48))) continue;
                for (int ribbon = 0; ribbon < style.ribbons; ribbon++) {
                    for (int segment = 0; segment < AuroraCurtainGeometry.SEGMENTS; segment++) {
                        double a = segment / (double) AuroraCurtainGeometry.SEGMENTS;
                        double b = (segment + 1) / (double) AuroraCurtainGeometry.SEGMENTS;
                        double midX = AuroraCurtainGeometry.x(tx, (a + b) * 0.5);
                        double midZ = AuroraCurtainGeometry.z(tz, ribbon, midX, time, 0);
                        if ((midX - camera.x) * (midX - camera.x) + (midZ - camera.z) * (midZ - camera.z) > 260 * 260) continue;
                        for (int layer = -1; layer <= 1; layer++) for (int band = 0; band < 3; band++) {
                            vertex(buffer, matrix, camera, tx, tz, a, band, ribbon, layer, time, cloudHeight, visibility, style);
                            vertex(buffer, matrix, camera, tx, tz, b, band, ribbon, layer, time, cloudHeight, visibility, style);
                            vertex(buffer, matrix, camera, tx, tz, b, band + 1, ribbon, layer, time, cloudHeight, visibility, style);
                            vertex(buffer, matrix, camera, tx, tz, a, band + 1, ribbon, layer, time, cloudHeight, visibility, style);
                        }
                    }
                }
            }
        }
        buffers.endBatch(CurtainType.TYPE);
    }
    private static void vertex(VertexConsumer out, Matrix4f matrix, Vec3 camera, int tileX, int tileZ,
            double along, int band, int ribbon, int layer, double time, double cloudHeight,
            float visibility, AuroraBiomeStyle style) {
        double x = AuroraCurtainGeometry.x(tileX, along);
        double z = AuroraCurtainGeometry.z(tileZ, ribbon, x, time, layer);
        double fraction = band / 3.0;
        double y = AuroraCurtainGeometry.y(cloudHeight, x, ribbon, time, style.height, fraction);
        double dx = x - camera.x, dz = z - camera.z;
        float alpha = visibility * (float) Math.pow(Math.sin(along * Math.PI), 2)
                * AuroraCurtainGeometry.fade(Math.sqrt(dx * dx + dz * dz))
                * (band == 0 || band == 3 ? 0 : band == 1 ? 0.14F : 0.07F)
                * (layer == 0 ? 1 : 0.55F);
        float t = (float) fraction;
        out.vertex(matrix, (float) dx, (float) (y - camera.y), (float) dz)
                .color(style.red + (style.topRed - style.red) * t,
                       style.green + (style.topGreen - style.green) * t,
                       style.blue + (style.topBlue - style.blue) * t, alpha).endVertex();
    }
    private static final class CurtainType extends RenderType {
        private CurtainType() { super("aurora_unused", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS,
                256, false, false, () -> {}, () -> {}); }
        // Additive light is order independent. Keep terrain/cloud depth testing,
        // but never write depth for transparent light or hide other translucency.
        private static final RenderType TYPE = create("tfpaw_aurora", DefaultVertexFormat.POSITION_COLOR,
                VertexFormat.Mode.QUADS, 262144, false, false, CompositeState.builder()
                .setShaderState(RENDERTYPE_LIGHTNING_SHADER).setTransparencyState(LIGHTNING_TRANSPARENCY)
                .setCullState(NO_CULL).setDepthTestState(LEQUAL_DEPTH_TEST).setWriteMaskState(COLOR_WRITE)
                .setOutputState(WEATHER_TARGET).createCompositeState(false));
    }
}
