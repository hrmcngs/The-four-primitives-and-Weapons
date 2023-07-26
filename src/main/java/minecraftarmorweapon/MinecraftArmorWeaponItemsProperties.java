
package minecraftarmorweapon;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.minecraft_armor_weapon.init.MinecraftArmorWeaponItems

import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation
import net.minecraft.client.renderer.item.ItemProperties

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinecraftArmorWeaponItemsProperties {
	public MinecraftArmorWeaponItemsProperties() {
			public static void addCustomItemProperties() {
			makeBow(MinecraftArmorWeaponItems.BLACK_BOW.get());1
	}
	public static void makeBow(Item item) {
		ItemModelsProperties.registerProperty(item, new ResourceLocation("pull"), (p_343014_0_, p_343014_1_, p_343014_2_) -> {
			if (p_343014_2_ == null) {
				return 0.0F;
			} else {
				return p_343014_2_.getActiveItemStack() != p_343014_0_ ? 0.0F : (float) (p_343014_0_.getUseDuration() - p_343014_2_.getItemInUseCount()) / 20.0F;
			}
		});
		ItemModelsProperties.registerProperty(item, new ResourceLocation("pulling"), (p_343015_0_, p_343015_1_, p_343015_2_) -> {
			return p_343015_2_ != null && p_343015_2_.isHandActive() && p_343015_2_.getActiveItemStack() == p_343015_0_ ? 1.0F : 0.0F;
		});
	}
} }
