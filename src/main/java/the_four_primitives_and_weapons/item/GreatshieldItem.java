package the_four_primitives_and_weapons.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import the_four_primitives_and_weapons.entity.PlacedGreatshieldEntity;
import the_four_primitives_and_weapons.init.CustomEntityInit;

public class GreatshieldItem extends MaterialShieldItem {
    public GreatshieldItem(Tiers tier, int durability) {
        super(tier, durability);
    }

    public static boolean isGreatshield(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof GreatshieldItem;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!player.isShiftKeyDown()) return super.use(level, player, hand);
        return new InteractionResultHolder<>(placeInFront(player, hand), player.getItemInHand(hand));
    }

    private static InteractionResult placementFailure(Player player, String reason) {
        if (!player.level().isClientSide) {
            player.displayClientMessage(Component.translatable(
                    "message.the_four_primitives_and_weapons." + reason), true);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        if (player == null || !player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }
        return placeInFront(player, context.getHand());
    }

    public InteractionResult placeInFront(Player player, InteractionHand hand) {
        var level = player.level();
        ItemStack stack = player.getItemInHand(hand);
        if (player.isSpectator() || !isGreatshield(stack)) return InteractionResult.FAIL;
        // 視線の上下やクリック対象によらず、水平な前方1ブロックに固定する。
        double yaw = Math.toRadians(player.getYRot());
        double x = player.getX() - Math.sin(yaw);
        double z = player.getZ() + Math.cos(yaw);
        double y = player.getY();
        BlockPos pos = BlockPos.containing(x, y, z);
        if (!level.mayInteract(player, pos) || !player.mayUseItemAt(pos, Direction.UP, stack)) {
            return placementFailure(player, "greatshield_place_denied");
        }
        var shield = new PlacedGreatshieldEntity(CustomEntityInit.PLACED_GREATSHIELD.get(), level);
        shield.setPos(x, y, z);
        shield.setYRot(player.getDirection().getOpposite().toYRot());
        shield.updateShieldBounds();
        if (!level.isClientSide) {
            ItemStack stored = stack.copy();
            stored.setCount(1);
            shield.setShield(stored);
            if (!level.addFreshEntity(shield)) return placementFailure(player, "greatshield_place_denied");
            if (!player.getAbilities().instabuild) stack.shrink(1);
            shield.playSound(net.minecraft.sounds.SoundEvents.SHIELD_BLOCK, 0.8f, 0.8f);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
