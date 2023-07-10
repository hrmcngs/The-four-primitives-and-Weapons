
package minecraftarmorweapon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;

public class CompressedElectricityItem extends Item {
	public CompressedElectricityItem() {
		super(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS).stacksTo(64).rarity(Rarity.UNCOMMON));
	}
}
