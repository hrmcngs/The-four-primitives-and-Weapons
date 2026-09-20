package the_four_primitives_and_weapons.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import the_four_primitives_and_weapons.weather.*;

/** Sky-pass dome: visible without shaders; VanillaLite preserves the same vertex colors. */
public final class RegionalWeatherSky {
    private static final the_four_primitives_and_weapons.performance.WeatherSkyMesh MESH = new the_four_primitives_and_weapons.performance.WeatherSkyMesh();
    public static void render(ClientLevel level, PoseStack pose, float partialTick) {
        var mc = Minecraft.getInstance();
        var camera = mc.gameRenderer.getMainCamera();
        var kind = RegionalWeather.at(level, camera.getBlockPosition());
        var appearance = WeatherSky.appearance(kind);
        boolean clouds = mc.options.getCloudsType() != CloudStatus.OFF;
        if (!clouds && appearance.haze() == 0) return;
        float daylight = Math.max(0F, (float) Math.cos(level.getTimeOfDay(partialTick) * Math.PI * 2));
        float shade = switch (kind) {
            case HEAVY_RAIN, THUNDERSTORM, HAIL -> .45F;
            case RAIN, DRIZZLE -> .65F;
            default -> 1F;
        };
        int red = Math.round(28 + 185 * daylight * shade);
        // Exact byte differences identify our geometry in VanillaLite's sky program.
        int green = appearance.sand() ? red - 20 : red + 1;
        int blue = appearance.sand() ? Math.max(0, red - 45) : red + 2;
        if (appearance.sand()) { red = Math.max(red, 55); green = red - 20; blue = red - 45; }
        boolean cull = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        var previousShader = RenderSystem.getShader();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        try {
            var buffer = Tesselator.getInstance().getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            Matrix4f matrix = pose.last().pose();
            double time = level.getGameTime() + partialTick;
            MESH.update(camera.getPosition().x, camera.getPosition().z, time, appearance, clouds);
            for (int ring = 0; ring < 20; ring++) {
                for (int segment = 0; segment < 96; segment++) {
                    vertex(buffer, matrix, ring, segment, red, green, blue);
                    vertex(buffer, matrix, ring + 1, segment, red, green, blue);
                    vertex(buffer, matrix, ring + 1, segment + 1, red, green, blue);
                    vertex(buffer, matrix, ring, segment + 1, red, green, blue);
                }
            }
            BufferUploader.drawWithShader(buffer.end());
        } finally {
            if (cull) RenderSystem.enableCull();
            if (depth) RenderSystem.enableDepthTest();
            if (!blend) RenderSystem.disableBlend();
            RenderSystem.setShader(() -> previousShader);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }
    private static void vertex(BufferBuilder buffer, Matrix4f matrix, int ring, int segment, int r, int g, int b) {
        int i = the_four_primitives_and_weapons.performance.WeatherSkyMesh.index(ring, segment);
        buffer.vertex(matrix, MESH.x(i) * 100, MESH.y(i) * 100, MESH.z(i) * 100)
            .color(r, g, b, MESH.alpha(i)).endVertex();
    }
    private RegionalWeatherSky() { }
}
