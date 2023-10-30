
package minecraftarmorweapon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import minecraftarmorweapon.entity.HrmcngsEntity;

import minecraftarmorweapon.client.model.Modelplayer_slim__Converted;

public class HrmcngsRenderer extends MobRenderer<HrmcngsEntity, Modelplayer_slim__Converted<HrmcngsEntity>> {
	public HrmcngsRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelplayer_slim__Converted(context.bakeLayer(Modelplayer_slim__Converted.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(HrmcngsEntity entity) {
		return new ResourceLocation("minecraft_armor_weapon:textures/entities/bab6244e6b6cc95d.png");
	}
}
