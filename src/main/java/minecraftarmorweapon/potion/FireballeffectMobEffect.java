
package minecraftarmorweapon.potion;

import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;

import minecraftarmorweapon.procedures.FireballeffectehuekutogaYouXiaoShinoteitukuProcedure;

import com.mojang.blaze3d.vertex.PoseStack;

public class FireballeffectMobEffect extends MobEffect {
	public FireballeffectMobEffect() {
		super(MobEffectCategory.NEUTRAL, -5832704);
	}

	@Override
	public String getDescriptionId() {
		return "effect.minecraft_armor_weapon.fireballeffect";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		FireballeffectehuekutogaYouXiaoShinoteitukuProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void initializeClient(java.util.function.Consumer<IClientMobEffectExtensions> consumer) {
		consumer.accept(new IClientMobEffectExtensions() {
			@Override
			public boolean isVisibleInInventory(MobEffectInstance effect) {
				return false;
			}

			@Override
			public boolean renderInventoryText(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, PoseStack poseStack, int x, int y, int blitOffset) {
				return false;
			}

			@Override
			public boolean isVisibleInGui(MobEffectInstance effect) {
				return false;
			}
		});
	}
}
