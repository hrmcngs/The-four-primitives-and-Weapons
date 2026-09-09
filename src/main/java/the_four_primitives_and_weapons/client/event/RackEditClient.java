package the_four_primitives_and_weapons.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.client.screens.RackEditScreen;
import the_four_primitives_and_weapons.entity.WeaponRackEntity;

@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons", value = Dist.CLIENT)
public final class RackEditClient {
    @SubscribeEvent
    public static void interact(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!event.getLevel().isClientSide || !(event.getTarget() instanceof WeaponRackEntity rack)
            || !event.getEntity().isShiftKeyDown() || !event.getItemStack().isEmpty()) return;
        Minecraft.getInstance().setScreen(new RackEditScreen(rack.getId()));
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
