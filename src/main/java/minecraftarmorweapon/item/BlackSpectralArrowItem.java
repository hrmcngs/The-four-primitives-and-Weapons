
package minecraftarmorweapon.item;

//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
//import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.server.level.ServerPlayer;

import minecraftarmorweapon.init.MinecraftArmorWeaponModTabs;

import minecraftarmorweapon.entity.BlackSpectralArrowEntity;

public class BlackSpectralArrowItem extends ArrowItem {
    public BlackSpectralArrowItem () {
        super(new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_WEAPON).stacksTo(64));
    }

    public AbstractArrow createArrow(Level p40513, ItemStack p40514, LivingEntity p40515) {
        Arrow arrow = new Arrow(p40513, p40515);
        arrow.setEffectsFromItem(p40514);
        return arrow;
    }
    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.world.entity.player.Player player) {
        int enchant = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.INFINITY_ARROWS, bow);
        return enchant <= 0 ? false : this.getClass() == BlackSpectralArrowItem.class;
    }
}
//public class BlackSpectralArrowItem extends Item {
//	public BlackSpectralArrowItem() {
//		super(new Item.Properties().tab(MinecraftArmorWeaponModTabs.TAB_WEAPON).stacksTo(64));
//	}
//
//	@Override
//	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
//		entity.startUsingItem(hand);
//		return new InteractionResultHolder(InteractionResult.SUCCESS, entity.getItemInHand(hand));
//	}
//
//	@Override
//	public UseAnim getUseAnimation(ItemStack itemstack) {
//		return UseAnim.SPEAR;
//	}
//
//	@Override
//	public int getUseDuration(ItemStack itemstack) {
//		return 72000;
//	}
//
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public boolean isFoil(ItemStack itemstack) {
//		return true;
//	}
//
//	@Override
//	public void releaseUsing(ItemStack itemstack, Level world, LivingEntity entityLiving, int timeLeft) {
//		if (!world.isClientSide() && entityLiving instanceof ServerPlayer entity) {
//			double x = entity.getX();
//			double y = entity.getY();
//			double z = entity.getZ();
//			if (true) {
//				BlackSpectralArrowEntity entityarrow = BlackSpectralArrowEntity.shoot(world, entity, world.getRandom(), 1f, 5, 0);
//				itemstack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
//				entityarrow.pickup = AbstractArrow.Pickup.DISALLOWED;
//			}
//		}
//	}
//}
