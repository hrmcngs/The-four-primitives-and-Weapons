
package minecraftarmorweapon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import minecraftarmorweapon.entity.TyokusenArrowEntity;

import minecraftarmorweapon.client.model.Modeltyokusenarrowonverted;

public class TyokusenArrowRenderer extends MobRenderer<TyokusenArrowEntity, Modeltyokusenarrowonverted<TyokusenArrowEntity>> {
	public TyokusenArrowRenderer(EntityRendererProvider.Context context) {
		super(context, new Modeltyokusenarrowonverted(context.bakeLayer(Modeltyokusenarrowonverted.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(TyokusenArrowEntity entity) {
		return new ResourceLocation("minecraft_armor_weapon:textures/entities/arrow.png");
	}
}
