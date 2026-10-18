package the_four_primitives_and_weapons.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import the_four_primitives_and_weapons.world.AstronomicalEvents;
import the_four_primitives_and_weapons.world.AstronomyData;

/** Called inside vanilla's sky pass, after the moon and before stars. */
public final class AstronomicalSkyRenderer {
    private AstronomicalSkyRenderer() { }

    /** Pixel-grid shadow; VanillaLite recognizes its signature and supplies a sky-toned tint. */
    public static void renderSolarEclipse(ClientLevel level, PoseStack pose, float partialTick) {
        float progress = AstronomyData.settings(level).solarProgress(level.getDayTime());
        if (progress < 0F) return;
        float alpha = (float) Math.sin(Math.PI * progress) * (1F - level.getRainLevel(partialTick));
        if (alpha <= 0.001F) return;
        float radius = 7.5F;
        float centerX = Math.round((progress * 4F - 2F) * 8F) * radius / 8F;
        Matrix4f matrix = pose.last().pose();
        boolean culling = org.lwjgl.opengl.GL11.glIsEnabled(org.lwjgl.opengl.GL11.GL_CULL_FACE);
        RenderSystem.disableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        try {
            BufferBuilder buffer = Tesselator.getInstance().getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            // Match the bright 8-pixel core of Minecraft's 32-pixel sun texture.
            // Keep a one-pixel rim at maximum coverage, with stepped corners.
            float pixel = radius / 4F;
            for (int z = -4; z < 4; z++) {
                for (int x = -4; x < 4; x++) {
                    float cx = (x + .5F) * pixel;
                    float cz = (z + .5F) * pixel;
                    if (Math.abs(cx - centerX) >= radius - pixel * .5F
                        || Math.abs(cz) >= radius - pixel * .5F) continue;
                    if (Math.abs(cx - centerX) > radius - pixel * 1.5F
                        && Math.abs(cz) > radius - pixel * 1.5F) continue;
                    eclipseVertex(buffer, matrix, x * pixel, z * pixel, alpha);
                    eclipseVertex(buffer, matrix, (x + 1) * pixel, z * pixel, alpha);
                    eclipseVertex(buffer, matrix, (x + 1) * pixel, (z + 1) * pixel, alpha);
                    eclipseVertex(buffer, matrix, x * pixel, (z + 1) * pixel, alpha);
                }
            }
            BufferUploader.drawWithShader(buffer.end());
        } finally {
            if (culling) RenderSystem.enableCull();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F - level.getRainLevel(partialTick));
        }
    }

    private static void eclipseVertex(BufferBuilder buffer, Matrix4f matrix, float x, float z, float alpha) {
        buffer.vertex(matrix, x, 99F, z).color(4, 7, 11, Math.round(alpha * 255F)).endVertex();
    }

    public static void renderMeteors(ClientLevel level, PoseStack pose, float partialTick) {
        var settings = AstronomyData.settings(level);
        if (settings.meteors() == 0) return;
        boolean shower = settings.meteorShower(level.getDayTime());
        for (int lane = 0; lane < (shower ? 3 : 1); lane++) {
            renderMeteor(level, pose, partialTick, AstronomicalEvents.meteor(level.getDayTime(), shower, lane, settings.meteors() == 1));
        }
    }

    private static void renderMeteor(ClientLevel level, PoseStack pose, float partialTick,
            AstronomicalEvents.Meteor meteor) {
        if (meteor == null) return;
        float visibility = AstronomyData.settings(level).meteors() == 1 ? 1F
            : Math.min(1F, level.getStarBrightness(partialTick) * 2F);
        float alpha = meteor.opacity() * visibility
            * (1F - level.getRainLevel(partialTick)) * (1F - level.getThunderLevel(partialTick));
        if (alpha <= 0.001F) return;

        // Undo the celestial rotation while retaining the camera's sky view matrix.
        Matrix4f matrix = new Matrix4f(pose.last().pose())
            .rotateX(-level.getTimeOfDay(partialTick) * (float) (Math.PI * 2.0))
            .rotateY((float) (Math.PI / 2.0));
        Vec3 head = new Vec3(meteor.x(), meteor.y(), meteor.z());
        Vec3 direction = new Vec3(meteor.dx(), meteor.dy(), meteor.dz());
        boolean fireball = meteor.kind() == AstronomicalEvents.MeteorKind.FIREBALL;
        double length = meteor.kind() == AstronomicalEvents.MeteorKind.SHORT ? 7.0 : fireball ? 28.0 : 19.0;
        Vec3 tail = head.subtract(direction.scale(length));
        Vec3 width = direction.cross(head).normalize().scale(fireball ? 0.6 : 0.22);
        float red = fireball ? 1F : 0.8F, green = fireball ? 0.65F : 0.9F, blue = fireball ? 0.3F : 1F;
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        boolean culling = org.lwjgl.opengl.GL11.glIsEnabled(org.lwjgl.opengl.GL11.GL_CULL_FACE);
        RenderSystem.disableCull();
        try {
            BufferBuilder buffer = Tesselator.getInstance().getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            vertex(buffer, matrix, head.add(width), alpha, red, green, blue);
            vertex(buffer, matrix, head.subtract(width), alpha, red, green, blue);
            vertex(buffer, matrix, tail.subtract(width.scale(0.08)), 0F, red, green, blue);
            vertex(buffer, matrix, tail.add(width.scale(0.08)), 0F, red, green, blue);
            if (fireball && meteor.headOpacity() > 0F) {
                // A bright, angular head rather than a smooth circular sprite.
                Vec3 along = direction.normalize().scale(1.1);
                Vec3 across = width.scale(1.8);
                float headAlpha = meteor.headOpacity() * visibility * (1F - level.getRainLevel(partialTick));
                vertex(buffer, matrix, head.add(along), headAlpha, red, green, blue);
                vertex(buffer, matrix, head.add(across), headAlpha, red, green, blue);
                vertex(buffer, matrix, head.subtract(along), headAlpha, red, green, blue);
                vertex(buffer, matrix, head.subtract(across), headAlpha, red, green, blue);
            }
            BufferUploader.drawWithShader(buffer.end());
        } finally {
            if (culling) RenderSystem.enableCull();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        }
    }

    private static void vertex(BufferBuilder buffer, Matrix4f matrix, Vec3 position, float alpha, float red, float green, float blue) {
        buffer.vertex(matrix, (float) position.x, (float) position.y, (float) position.z)
            .color(red, green, blue, alpha).endVertex();
    }
}
