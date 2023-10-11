
package minecraftarmorweapon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import minecraftarmorweapon.entity.OtiruyoEntity;

import minecraftarmorweapon.client.model.Modelluna_Converted;

public class OtiruyoRenderer extends MobRenderer<OtiruyoEntity, Modelluna_Converted<OtiruyoEntity>> {
	public OtiruyoRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelluna_Converted(context.bakeLayer(Modelluna_Converted.LAYER_LOCATION)), 0f);
	}

	@Override
	public ResourceLocation getTextureLocation(OtiruyoEntity entity) {
		return new ResourceLocation("minecraft_armor_weapon:textures/entities/nonono.png");
	}
}
