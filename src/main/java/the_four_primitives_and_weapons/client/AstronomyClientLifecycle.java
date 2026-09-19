package the_four_primitives_and_weapons.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.world.AstronomyData;
import the_four_primitives_and_weapons.world.AstronomySettings;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public final class AstronomyClientLifecycle {
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event) {
        AstronomyData.clientSettings = AstronomySettings.DEFAULT;
    }
}
