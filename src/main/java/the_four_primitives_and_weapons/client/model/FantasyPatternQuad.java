package the_four_primitives_and_weapons.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

/** Explicit face UVs keep heraldry upright instead of repeating it on every cube face. */
public final class FantasyPatternQuad {
    private FantasyPatternQuad() {}
    public static void draw(PoseStack poses, VertexConsumer out, int light, int overlay,
            float x0, float y0, float x1, float y1, float z,
            float u0, float v0, float u1, float v1, boolean back,
            float r, float g, float b, float a) {
        vertex(poses, out, light, overlay, x0, y0, z, u0, v0, back, r, g, b, a);
        vertex(poses, out, light, overlay, x0, y1, z, u0, v1, back, r, g, b, a);
        vertex(poses, out, light, overlay, x1, y1, z, u1, v1, back, r, g, b, a);
        vertex(poses, out, light, overlay, x1, y0, z, u1, v0, back, r, g, b, a);
    }
    private static void vertex(PoseStack poses, VertexConsumer out, int light, int overlay,
            float x, float y, float z, float u, float v, boolean back, float r, float g, float b, float a) {
        out.vertex(poses.last().pose(), x / 16, y / 16, z / 16).color(r, g, b, a)
            .uv(u, v).overlayCoords(overlay).uv2(light)
            .normal(poses.last().normal(), 0, 0, back ? 1 : -1).endVertex();
    }
}
