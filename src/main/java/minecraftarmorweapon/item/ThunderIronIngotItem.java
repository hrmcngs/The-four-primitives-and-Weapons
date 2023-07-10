
package minecraftarmorweapon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;

public class ThunderIronIngotItem extends Item {
	public ThunderIronIngotItem() {
		super(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS).stacksTo(64).rarity(Rarity.RARE));
	}
}
