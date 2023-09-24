
package minecraftarmorweapon;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import minecraftarmorweapon.item.AchromaticShieldItem;


public class MinecraftArmorWeaponItemProrties {
	public static void addCustomItemProperties(){
	makeShield(MinecraftArmorWeaponItems.ACHROMATIC_SHIELD.get());
	}

	private static void makeShield(Item item){
	ItemProperties.register(item, new ResourceLocation("blocking"), (p_343014_, p_014343_, p_034314_, p_343140_) -> {
		return p_034314_ !=null && p_034314_.isUsingItem() && p_034314_.getUseItem() == p_343014_ ? 1.0F : 0.0F;
		});
		}
		}
