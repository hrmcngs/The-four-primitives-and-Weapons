package the_four_primitives_and_weapons.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.event.ShieldBashHandler;
import the_four_primitives_and_weapons.item.GreatshieldItem;
import the_four_primitives_and_weapons.network.ShieldBashInputPacket;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, value = Dist.CLIENT)
public final class ShieldBashInputHandler {
    private static boolean previous;
    private static int heartbeat;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        var mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null || mc.getConnection() == null) { previous = false; heartbeat = 0; return; }
        boolean shield = ShieldBashHandler.isShield(player.getMainHandItem())
                && !GreatshieldItem.isGreatshield(player.getMainHandItem());
        // バニラのスプリント状態ではなく、割り当て済みの前進キーを読む。
        boolean forward = shield && mc.screen == null && mc.options.keyUp.isDown() && !mc.options.keyDown.isDown();
        ShieldBashHandler.updateForwardInput(player, forward);
        if (forward != previous || (shield && ++heartbeat >= 5)) {
            TheFourPrimitivesAndWeaponsMod.PACKET_HANDLER.sendToServer(new ShieldBashInputPacket(forward));
            heartbeat = 0;
        }
        previous = forward;
    }
}
