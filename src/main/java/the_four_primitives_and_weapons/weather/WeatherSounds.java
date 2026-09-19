package the_four_primitives_and_weapons.weather;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons", bus = Mod.EventBusSubscriber.Bus.MOD)
public final class WeatherSounds {
    public static final Map<String, SoundEvent> SOUNDS = Map.of("wind", sound("wind"), "sand", sound("sand"), "hail", sound("hail"));
    private static SoundEvent sound(String name) {
        return SoundEvent.createVariableRangeEvent(new ResourceLocation("the_four_primitives_and_weapons", "weather." + name));
    }
    @SubscribeEvent public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.SOUND_EVENTS, helper -> SOUNDS.values().forEach(sound -> helper.register(sound.getLocation(), sound)));
    }
    private WeatherSounds() { }
}
