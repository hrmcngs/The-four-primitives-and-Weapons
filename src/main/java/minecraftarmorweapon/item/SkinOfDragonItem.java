
package minecraftarmorweapon.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.entity.player.Player;

import minecraftarmorweapon.procedures.SkinOfDragonpureiyaniyotuteaitemugadorotupusaretatokiProcedure;

public class SkinOfDragonItem extends Item {
	public SkinOfDragonItem() {
		super(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS).stacksTo(64).fireResistant().rarity(Rarity.EPIC));
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack itemstack, Player entity) {
		SkinOfDragonpureiyaniyotuteaitemugadorotupusaretatokiProcedure.execute(entity);
		return true;
	}
}
