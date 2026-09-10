
package the_four_primitives_and_weapons.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.SlimeModel;

import the_four_primitives_and_weapons.entity.AlchemyCraftBlockEntityEntity;

public class AlchemyCraftBlockEntityRenderer extends MobRenderer<AlchemyCraftBlockEntityEntity, SlimeModel<AlchemyCraftBlockEntityEntity>> {
    private static final ResourceLocation FIXED_TEXTURE_ID = new ResourceLocation("the_four_primitives_and_weapons:textures/entities/toumei.png");
	public AlchemyCraftBlockEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new SlimeModel(context.bakeLayer(ModelLayers.SLIME)), 0.1f);
	}

	@Override
	public ResourceLocation getTextureLocation(AlchemyCraftBlockEntityEntity entity) {
		return FIXED_TEXTURE_ID;
	}
}
