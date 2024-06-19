package minecraftarmorweapon.entity.model;

import software.bernie.geckolib3.model.AnimatedGeoModel;

import net.minecraft.resources.ResourceLocation;

import minecraftarmorweapon.entity.BlackholeEntity;

public class BlackholeModel extends AnimatedGeoModel<BlackholeEntity> {
	@Override
	public ResourceLocation getAnimationResource(BlackholeEntity entity) {
		return new ResourceLocation("minecraft_armor_weapon", "animations/blackhole.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(BlackholeEntity entity) {
		return new ResourceLocation("minecraft_armor_weapon", "geo/blackhole.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BlackholeEntity entity) {
		return new ResourceLocation("minecraft_armor_weapon", "textures/entities/" + entity.getTexture() + ".png");
	}

}
