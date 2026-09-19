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

/** Called inside vanilla's sky pass, after the moon and before stars. */
public final class AstronomicalSkyRenderer {
    private AstronomicalSkyRenderer() { }

    /** Vanilla sky overlay; VanillaLite recognizes this signature and uses its own mask. */
    public static void renderSolarEclipse(ClientLevel level, PoseStack pose, float partialTick) {
        float progress = AstronomicalEvents.solarEclipseProgress(level.getDayTime());
        if (progress < 0F) return;
        float alpha = (float) Math.sin(Math.PI * progress) * (1F - level.getRainLevel(partialTick));
        if (alpha <= 0.001F) return;
        float radius = 32F;
        float centerX = (progress * 4.8F - 2.4F) * radius;
        Matrix4f matrix = pose.last().pose();
        boolean culling = org.lwjgl.opengl.GL11.glIsEnabled(org.lwjgl.opengl.GL11.GL_CULL_FACE);
        RenderSystem.disableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        try {
            BufferBuilder buffer = Tesselator.getInstance().getBuilder();
            buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            eclipseVertex(buffer, matrix, centerX, 0F, alpha);
            for (int segment = 0; segment <= 64; segment++) {
                double angle = segment * Math.PI * 2.0 / 64.0;
                eclipseVertex(buffer, matrix, centerX + radius * (float) Math.cos(angle),
                    radius * (float) Math.sin(angle), alpha);
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
        buffer.vertex(matrix, x, 99F, z).color(3, 4, 5, Math.round(alpha * 255F)).endVertex();
    }

    public static void renderMeteors(ClientLevel level, PoseStack pose, float partialTick) {
        var meteor = AstronomicalEvents.meteor(level.getDayTime());
        if (meteor == null) return;
        float alpha = meteor.opacity() * Math.min(1F, level.getStarBrightness(partialTick) * 2F)
            * (1F - level.getRainLevel(partialTick)) * (1F - level.getThunderLevel(partialTick));
        if (alpha <= 0.001F) return;

        // Undo the celestial rotation while retaining the camera's sky view matrix.
        Matrix4f matrix = new Matrix4f(pose.last().pose())
            .rotateX(-level.getTimeOfDay(partialTick) * (float) (Math.PI * 2.0))
            .rotateY((float) (Math.PI / 2.0));
        Vec3 head = new Vec3(meteor.x(), 75.0, meteor.z());
        Vec3 direction = new Vec3(meteor.dx(), 0.0, meteor.dz());
        Vec3 tail = head.subtract(direction.scale(19.0));
        Vec3 width = direction.cross(head).normalize().scale(0.22);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        boolean culling = org.lwjgl.opengl.GL11.glIsEnabled(org.lwjgl.opengl.GL11.GL_CULL_FACE);
        RenderSystem.disableCull();
        try {
            BufferBuilder buffer = Tesselator.getInstance().getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            vertex(buffer, matrix, head.add(width), alpha);
            vertex(buffer, matrix, head.subtract(width), alpha);
            vertex(buffer, matrix, tail.subtract(width.scale(0.08)), 0F);
            vertex(buffer, matrix, tail.add(width.scale(0.08)), 0F);
            BufferUploader.drawWithShader(buffer.end());
        } finally {
            if (culling) RenderSystem.enableCull();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        }
    }

    private static void vertex(BufferBuilder buffer, Matrix4f matrix, Vec3 position, float alpha) {
        buffer.vertex(matrix, (float) position.x, (float) position.y, (float) position.z)
            .color(0.8F, 0.9F, 1F, alpha).endVertex();
    }
}
