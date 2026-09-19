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
            for (int ring = 0; ring < 20; ring++) {
                for (int segment = 0; segment < 96; segment++) {
                    vertex(buffer, matrix, ring, segment, camera.getPosition().x, camera.getPosition().z, time, appearance, clouds, red, green, blue);
                    vertex(buffer, matrix, ring + 1, segment, camera.getPosition().x, camera.getPosition().z, time, appearance, clouds, red, green, blue);
                    vertex(buffer, matrix, ring + 1, segment + 1, camera.getPosition().x, camera.getPosition().z, time, appearance, clouds, red, green, blue);
                    vertex(buffer, matrix, ring, segment + 1, camera.getPosition().x, camera.getPosition().z, time, appearance, clouds, red, green, blue);
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
    private static void vertex(BufferBuilder buffer, Matrix4f matrix, int ring, int segment,
            double cameraX, double cameraZ, double time, WeatherSky.Appearance a, boolean clouds, int r, int g, int b) {
        double elevation = -.08 + (Math.PI / 2 + .08) * ring / 20;
        double angle = Math.PI * 2 * (segment % 96) / 96;
        float x = (float) (Math.cos(angle) * Math.cos(elevation));
        float y = (float) Math.sin(elevation);
        float z = (float) (Math.sin(angle) * Math.cos(elevation));
        float cloud = clouds ? WeatherSky.density(x * 9 + cameraX * .0008, z * 9 + cameraZ * .0008, time, a) : 0;
        cloud *= Math.min(1F, Math.max(0F, y * 7));
        float haze = a.haze() * (1 - Math.max(0F, y) * .65F);
        float alpha = Math.max(cloud, haze);
        // Fade the lower rim into the horizon without a hard dome boundary.
        if (y < 0) alpha *= Math.max(0F, 1 + y / .08F);
        buffer.vertex(matrix, x * 100, y * 100, z * 100).color(r, g, b, Math.round(alpha * 255)).endVertex();
    }
    private RegionalWeatherSky() { }
}
