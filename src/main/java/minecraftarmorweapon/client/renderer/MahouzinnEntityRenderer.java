
package minecraftarmorweapon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import minecraftarmorweapon.entity.MahouzinnEntityEntity;

import minecraftarmorweapon.client.model.Modelmahouzinn;

public class MahouzinnEntityRenderer extends MobRenderer<MahouzinnEntityEntity, Modelmahouzinn<MahouzinnEntityEntity>> {
	public MahouzinnEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelmahouzinn(context.bakeLayer(Modelmahouzinn.LAYER_LOCATION)), 0f);
	}

	@Override
	public ResourceLocation getTextureLocation(MahouzinnEntityEntity entity) {
		return new ResourceLocation("minecraft_armor_weapon:textures/entities/alchemymod.png");
	}
}
