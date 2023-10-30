
package minecraftarmorweapon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import minecraftarmorweapon.entity.Ruami284Entity;

import minecraftarmorweapon.client.model.Modelplayer_slim__Converted;

public class Ruami284Renderer extends MobRenderer<Ruami284Entity, Modelplayer_slim__Converted<Ruami284Entity>> {
	public Ruami284Renderer(EntityRendererProvider.Context context) {
		super(context, new Modelplayer_slim__Converted(context.bakeLayer(Modelplayer_slim__Converted.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(Ruami284Entity entity) {
		return new ResourceLocation("minecraft_armor_weapon:textures/entities/5cd8782a5d07987a.png");
	}
}
