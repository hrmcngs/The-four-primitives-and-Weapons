
package minecraftarmorweapon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import minecraftarmorweapon.entity.LunaEntityEntity;

import minecraftarmorweapon.client.model.Modelluna_Converted;

public class LunaEntityRenderer extends MobRenderer<LunaEntityEntity, Modelluna_Converted<LunaEntityEntity>> {
	public LunaEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelluna_Converted(context.bakeLayer(Modelluna_Converted.LAYER_LOCATION)), 0f);
	}

	@Override
	public ResourceLocation getTextureLocation(LunaEntityEntity entity) {
		return new ResourceLocation("minecraft_armor_weapon:textures/entities/luna_converted.png");
	}
}
