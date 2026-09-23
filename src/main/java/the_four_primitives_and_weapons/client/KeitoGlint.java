package the_four_primitives_and_weapons.client;

import java.io.IOException;
import java.util.Map;
import java.util.EnumMap;
import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.damage.ElementType;
import the_four_primitives_and_weapons.damage.ElementalParticles;

/** Batched attribute-colored glints; NONE retains Keito's pale blue palette. */
@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class KeitoGlint extends RenderType {
    private static ShaderInstance shader;
    private static final Map<ElementType, RenderType[]> PALETTES = createPalettes();

    private static Map<ElementType, RenderType[]> createPalettes() {
        Map<ElementType, RenderType[]> palettes = new EnumMap<>(ElementType.class);
        for (ElementType element : ElementType.values()) {
            Vector3f color = ElementalParticles.colorOf(element);
            if (color == null) color = new Vector3f(0.65f, 0.86f, 1.0f);
            // Lift dark attribute colors without changing their hue or hiding the pattern.
            float peak = Math.max(color.x, Math.max(color.y, color.z));
            if (peak > 0 && peak < 0.75f) color.mul(0.75f / peak);
            String name = "element_glint_" + element.getName();
            palettes.put(element, new RenderType[]{
                make(name, false, false, color),
                make(name + "_translucent", false, true, color),
                make(name + "_entity", true, true, color),
                make(name + "_entity_direct", true, false, color)
            });
        }
        return palettes;
    }

    private KeitoGlint() {
        super("keito_unused", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, () -> {}, () -> {});
    }

    private static RenderType make(String name, boolean entity, boolean separateTarget, Vector3f color) {
        var state = CompositeState.builder()
            .setShaderState(new ShaderStateShard(() -> {
                if (shader == null) return GameRenderer.getRendertypeGlintShader();
                // Set color when this batch is drawn, not when an item queues vertices.
                // Different attributes can coexist in an inventory or world frame.
                var uniform = shader.getUniform("GlintColor");
                if (uniform != null) uniform.set(color.x, color.y, color.z);
                return shader;
            }))
            .setTextureState(new TextureStateShard(entity ? ItemRenderer.ENCHANTED_GLINT_ENTITY : ItemRenderer.ENCHANTED_GLINT_ITEM, true, false))
            .setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL)
            .setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY)
            .setTexturingState(entity ? ENTITY_GLINT_TEXTURING : GLINT_TEXTURING);
        if (separateTarget) state.setOutputState(ITEM_ENTITY_TARGET);
        return create(name, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, state.createCompositeState(false));
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(),
            new ResourceLocation(TheFourPrimitivesAndWeaponsMod.MODID, "keito_glint"), DefaultVertexFormat.POSITION_TEX),
            loaded -> shader = loaded);
    }

    public static RenderType recolor(RenderType type, ElementType element) {
        RenderType[] palette = PALETTES.get(element);
        if (type == glint() || type == glintDirect()) return palette[0];
        if (type == glintTranslucent()) return palette[1];
        if (type == entityGlint()) return palette[2];
        if (type == entityGlintDirect()) return palette[3];
        return type;
    }

    public static void addBuffers(Map<RenderType, BufferBuilder> buffers) {
        // Foil and base vertices are emitted together: each requires its own builder.
        for (RenderType[] palette : PALETTES.values()) {
            for (RenderType type : palette) {
                buffers.put(type, new BufferBuilder(type.bufferSize()));
            }
        }
    }
}
