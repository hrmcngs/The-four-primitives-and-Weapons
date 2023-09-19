
package minecraftarmorweapon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import minecraftarmorweapon.entity.SkeltonMobEntity;

import minecraftarmorweapon.client.model.Modelskeleton_Converted;

public class SkeltonMobRenderer extends MobRenderer<SkeltonMobEntity, Modelskeleton_Converted<SkeltonMobEntity>> {
	public SkeltonMobRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelskeleton_Converted(context.bakeLayer(Modelskeleton_Converted.LAYER_LOCATION)), 0f);
	}

	@Override
	public ResourceLocation getTextureLocation(SkeltonMobEntity entity) {
		return new ResourceLocation("minecraft_armor_weapon:textures/entities/skeleton.png");
	}
}
