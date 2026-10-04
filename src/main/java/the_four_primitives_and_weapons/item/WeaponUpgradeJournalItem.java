package the_four_primitives_and_weapons.item;

import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import the_four_primitives_and_weapons.skill.WeaponGrowth;

public final class WeaponUpgradeJournalItem extends Item {
    public WeaponUpgradeJournalItem(){super(new Properties().stacksTo(1));}
    @Override public void appendHoverText(ItemStack stack,Level level,java.util.List<net.minecraft.network.chat.Component> tooltip,TooltipFlag flag) {
        tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip.the_four_primitives_and_weapons.upgrade_journal"));
    }
    @Override public InteractionResultHolder<ItemStack> use(Level level,Player player,InteractionHand hand){
        if(!level.isClientSide) {
            for(int i=0;i<player.getInventory().getContainerSize();i++) {
                var stack=player.getInventory().getItem(i);if(WeaponGrowth.supported(stack))WeaponGrowth.ensureId(stack);
            }
            player.inventoryMenu.broadcastChanges();
        } else DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->the_four_primitives_and_weapons.client.WeaponUpgradeScreen.open());
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand),level.isClientSide);
    }
}
