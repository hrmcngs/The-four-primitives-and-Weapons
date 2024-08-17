package minecraftarmorweapon;

import net.minecraftforge.fml.common.Mod;
import minecraftarmorweapon.init.MinecraftArmorWeaponModItems;
import net.minecraft.world.item.Item;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
// public class TestBowItemsProperties {

// public TestBowItemsProperties() {
// addCustomItemProperties();
// }

public class TestBowItemsProperties {
	public static void addCustomItemProperties() {
		makeBow(MinecraftArmorWeaponModItems.TEST_BOW.get());
	}

	// 他のメソッドやコード

	private static void makeBow(Item item) {
		ItemProperties.register(item, new ResourceLocation("pull"), (p_343014_, p_343015_, p_343016_, p_343017_) -> {
			if (p_343016_ == null) {
				return 0.0F;
			} else {
				return p_343016_.getUseItem() != p_343014_ ? 0.0F
						: (float) (p_343014_.getUseDuration() -
								p_343016_.getUseItemRemainingTicks()) / 20.0F;
			}
		});
		ItemProperties.register(item, new ResourceLocation("pulling"), (p_343010_, p_343011_, p_343012_, p_343013_) -> {
			return p_343012_ != null && p_343012_.isUsingItem() && p_343012_.getUseItem() == p_343010_ ? 1.0F : 0.0F;
		});
	}
}
