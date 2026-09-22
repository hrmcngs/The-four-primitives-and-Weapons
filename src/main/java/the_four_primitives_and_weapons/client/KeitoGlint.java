package the_four_primitives_and_weapons.client;

import java.io.IOException;
import java.util.Map;
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

/** Keeps vanilla glint motion and intensity, with a pale blue palette for Keito. */
@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class KeitoGlint extends RenderType {
    private static ShaderInstance shader;
    private static final RenderType ITEM = make("keito_glint", false, false);
    private static final RenderType TRANSLUCENT = make("keito_glint_translucent", false, true);
    private static final RenderType ENTITY = make("keito_entity_glint", true, true);
    private static final RenderType ENTITY_DIRECT = make("keito_entity_glint_direct", true, false);

    private KeitoGlint() {
        super("keito_unused", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, () -> {}, () -> {});
    }

    private static RenderType make(String name, boolean entity, boolean separateTarget) {
        var state = CompositeState.builder()
            .setShaderState(new ShaderStateShard(() -> shader != null ? shader : GameRenderer.getRendertypeGlintShader()))
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

    public static RenderType recolor(RenderType type) {
        if (type == glint() || type == glintDirect()) return ITEM;
        if (type == glintTranslucent()) return TRANSLUCENT;
        if (type == entityGlint()) return ENTITY;
        if (type == entityGlintDirect()) return ENTITY_DIRECT;
        return type;
    }

    public static void addBuffers(Map<RenderType, BufferBuilder> buffers) {
        // Foil and base vertices are emitted together: each requires its own builder.
        for (RenderType type : new RenderType[]{ITEM, TRANSLUCENT, ENTITY, ENTITY_DIRECT}) {
            buffers.put(type, new BufferBuilder(type.bufferSize()));
        }
    }
}
