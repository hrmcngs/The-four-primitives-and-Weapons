
package the_four_primitives_and_weapons.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;

import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.SlotContext;

import the_four_primitives_and_weapons.procedures.SayaRightclickedProcedure;

import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModTabs;

import java.util.List;

public class SayaItem extends Item implements ICurioItem {
	public SayaItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND
                && the_four_primitives_and_weapons.util.NinjatoVault.isLoaded(entity.getItemInHand(hand))) {
            entity.startUsingItem(hand);
            the_four_primitives_and_weapons.util.NinjatoVault.begin(entity);
            return InteractionResultHolder.consume(entity.getItemInHand(hand));
        }
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		ItemStack itemstack = ar.getObject();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();

		SayaRightclickedProcedure.execute(world, entity, itemstack, hand);
		return ar;
	}

    @Override
    public int getUseDuration(ItemStack stack) { return 72000; }

    @Override
    public void onUseTick(Level level, net.minecraft.world.entity.LivingEntity living, ItemStack stack, int remaining) {
        if (living instanceof Player player) the_four_primitives_and_weapons.util.NinjatoVault.tick(player, stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, net.minecraft.world.entity.LivingEntity living, int remaining) {
        if (living instanceof Player player && !level.isClientSide)
            the_four_primitives_and_weapons.util.NinjatoVault.end(player);
    }

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, net.minecraft.world.item.TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
        if (stack.hasTag() && stack.getTag().getBoolean(the_four_primitives_and_weapons.util.NinjatoVault.TETHERED))
            list.add(Component.translatable("tooltip.the_four_primitives_and_weapons.ninjato_tethered"));
        if (the_four_primitives_and_weapons.util.NinjatoVault.isLoaded(stack))
            list.add(Component.translatable("tooltip.the_four_primitives_and_weapons.ninjato_vault"));

		String sayaHex = the_four_primitives_and_weapons.util.SayaDesign.getBaseHex(stack);
		if (sayaHex != null) {
			list.add(Component.translatable("tooltip.the_four_primitives_and_weapons.saya.base", sayaHex));
		}
		Component sayaFinish = the_four_primitives_and_weapons.util.SayaStyles.finishName(
				the_four_primitives_and_weapons.util.SayaDesign.getStyle(stack),
				the_four_primitives_and_weapons.util.SayaDesign.getLacquer(stack));
		if (sayaFinish != null)
			list.add(Component.translatable("tooltip.the_four_primitives_and_weapons.saya.style", sayaFinish));

		if (stack.hasTag() && stack.getTag().contains("StoredKatana")) {
			ItemStack storedKatana = ItemStack.of(stack.getTag().getCompound("StoredKatana"));
			list.add(Component.translatable("tooltip.the_four_primitives_and_weapons.saya.sheathed", storedKatana.getHoverName()));
		} else {
			list.add(Component.translatable("tooltip.the_four_primitives_and_weapons.saya.empty"));
			list.add(Component.translatable("tooltip.the_four_primitives_and_weapons.saya.hint"));
		}
	}

	@Override
	public boolean canEquip(SlotContext slotContext, ItemStack stack) {
		String id = slotContext.identifier();
		return id.equals("belt") || id.equals("back");
	}

	@Override
	public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
		return false;
	}
}
