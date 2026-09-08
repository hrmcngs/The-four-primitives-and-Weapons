package the_four_primitives_and_weapons.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import the_four_primitives_and_weapons.entity.StabbedWeaponEntity;

/** 紐には武器の複製を保存せず、刺さった実体のUUIDだけを保持する。 */
public class NinjatoRecallCordItem extends Item {
    public NinjatoRecallCordItem() { super(new Item.Properties().stacksTo(1)); }

    @Override
    public int getUseDuration(ItemStack stack) { return 72000; }

    @Override
    public void releaseUsing(ItemStack stack, Level level, net.minecraft.world.entity.LivingEntity living, int remaining) {
        if (!level.isClientSide && stack.hasTag()) stack.getOrCreateTag().putBoolean("RecallArmed", true);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack cord = player.getItemInHand(hand);
        // 跳躍に使った右クリックを離すまで回収を受け付けない。
        if (cord.hasTag() && !cord.getTag().getBoolean("RecallArmed")) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(cord);
        }
        if (player instanceof ServerPlayer serverPlayer && cord.hasTag()
                && cord.getTag().hasUUID("PlantedWeapon") && cord.getTag().hasUUID("CordOwner")
                && player.getUUID().equals(cord.getTag().getUUID("CordOwner"))) {
            for (ServerLevel world : serverPlayer.server.getAllLevels()) {
                Entity entity = world.getEntity(cord.getTag().getUUID("PlantedWeapon"));
                if (entity instanceof StabbedWeaponEntity planted && planted.recallVault(player)) break;
            }
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }
}
