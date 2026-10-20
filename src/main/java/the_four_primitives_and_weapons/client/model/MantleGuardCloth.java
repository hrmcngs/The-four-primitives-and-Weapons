package the_four_primitives_and_weapons.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Vector3f;

/** Continuous cloth from the fixed back seam to the actual animated hand. */
public final class MantleGuardCloth {
    private MantleGuardCloth() {}
    public static void render(PoseStack poses, VertexConsumer out, int light,
            ModelPart body, ModelPart arm, int side, float blend, float age) {
        PoseStack local = new PoseStack();
        body.translateAndRotate(local);
        var inverseBody = new org.joml.Matrix4f(local.last().pose()).invert();
        local = new PoseStack();
        arm.translateAndRotate(local);
        Vector3f hand = new Vector3f(0, .57f, -.025f);
        local.last().pose().transformPosition(hand);
        inverseBody.transformPosition(hand);
        for (int strip = 0; strip < 7; strip++) {
            for (int row = 0; row < 8; row++) {
                float u0 = strip / 7f, u1 = (strip + 1) / 7f;
                float v0 = row / 8f, v1 = (row + 1) / 8f;
                Vector3f a = point(u0, v0, hand, side, blend, age);
                Vector3f b = point(u0, v1, hand, side, blend, age);
                Vector3f c = point(u1, v1, hand, side, blend, age);
                Vector3f d = point(u1, v0, hand, side, blend, age);
                Vector3f normal = new Vector3f(b).sub(a).cross(new Vector3f(d).sub(a)).normalize();
                vertex(poses, out, light, a, normal, u0, v0);
                vertex(poses, out, light, b, normal, u0, v1);
                vertex(poses, out, light, c, normal, u1, v1);
                vertex(poses, out, light, d, normal, u1, v0);
            }
        }
    }
    private static Vector3f point(float u, float v, Vector3f hand, int side, float blend, float age) {
        float width = (u - .5f) * .98f;
        Vector3f rest = new Vector3f(width, v * 1.44f, .22f);
        float t = Math.min(1, v / .625f), q = 1 - t;
        Vector3f pulled = new Vector3f(
            q*q*q * width + 3*q*q*t * side*.65f + 3*q*t*t * (hand.x + side*.1f) + t*t*t * hand.x,
            q*q*q * 0 + 3*q*q*t * .12f + 3*q*t*t * hand.y + t*t*t * hand.y,
            q*q*q * .22f + 3*q*q*t * .32f + 3*q*t*t * .32f + t*t*t * hand.z);
        // Spread the drape over the wrist and forearm; its centre stays on the hand.
        pulled.x += (u - .5f) * .75f * t;
        pulled.z += (u - .5f) * side * .18f * t;
        pulled.y -= (float)Math.sin(u * Math.PI) * .05f * t;
        if (v > .625f) {
            float hem = (v - .625f) / .375f;
            pulled.y += hem * .95f;
            pulled.x += side * hem * .04f;
            pulled.z += hem * (float)Math.sin(age * .45f + u * 3) * .06f;
        }
        return rest.lerp(pulled, blend);
    }
    private static void vertex(PoseStack poses, VertexConsumer out, int light, Vector3f p, Vector3f normal, float u, float v) {
        out.vertex(poses.last().pose(), p.x, p.y, p.z).color(255,255,255,255)
            .uv(u,v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
            .normal(poses.last().normal(), normal.x, normal.y, normal.z).endVertex();
    }
}
