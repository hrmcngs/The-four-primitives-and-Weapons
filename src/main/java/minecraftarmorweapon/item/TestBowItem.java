
package minecraftarmorweapon.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

import minecraftarmorweapon.procedures.TestBowYoukuritukusitatokiProcedure;

import minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;

public class TestBowItem extends Item {
	public TestBowItem() {
		super(new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_WEAPON).stacksTo(1).rarity(Rarity.EPIC));
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.BOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		ItemStack itemstack = ar.getObject();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		TestBowYoukuritukusitatokiProcedure.execute(world, entity);
		return ar;
	}
}
