package the_four_primitives_and_weapons.item;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import the_four_primitives_and_weapons.util.LunaBehaviorScript;
import java.nio.charset.StandardCharsets;

@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons")
public final class LunaFormula extends SimplePreparableReloadListener<LunaBehaviorScript> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation PATH = new ResourceLocation("the_four_primitives_and_weapons", "luna/formula.lisp");
    private static volatile LunaBehaviorScript current = LunaBehaviorScript.defaults();

    public static LunaBehaviorScript get() { return current; }

    @SubscribeEvent
    public static void onReloadListener(AddReloadListenerEvent event) { event.addListener(new LunaFormula()); }

    @Override
    protected LunaBehaviorScript prepare(ResourceManager manager, ProfilerFiller profiler) {
        try {
            var resource = manager.getResource(PATH);
            if (resource.isEmpty()) return LunaBehaviorScript.defaults();
            try (var input = resource.get().open()) {
                return LunaBehaviorScript.parse(new String(input.readAllBytes(), StandardCharsets.UTF_8), LOGGER::warn);
            }
        } catch (Exception ex) {
            LOGGER.warn("Could not reload {}; keeping previous Luna behavior", PATH, ex);
            return current;
        }
    }

    @Override
    protected void apply(LunaBehaviorScript prepared, ResourceManager manager, ProfilerFiller profiler) {
        current = prepared;
    }
}
