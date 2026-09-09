package the_four_primitives_and_weapons.event;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.init.UrushiWoodInit;

@Mod.EventBusSubscriber(modid = "the_four_primitives_and_weapons")
public final class UrushiWoodStripHandler {
    @SubscribeEvent
    public static void onStrip(BlockEvent.BlockToolModificationEvent event) {
        if (event.getToolAction() != ToolActions.AXE_STRIP
            || !event.getHeldItemStack().canPerformAction(ToolActions.AXE_STRIP)) return;
        Block input = event.getState().getBlock();
        Block output;
        if (input == UrushiWoodInit.URUSHI_LOG.get()) output = UrushiWoodInit.STRIPPED_URUSHI_LOG.get();
        else if (input == UrushiWoodInit.URUSHI_WOOD.get()) output = UrushiWoodInit.STRIPPED_URUSHI_WOOD.get();
        else if (input == UrushiWoodInit.BLACK_URUSHI_LOG.get()) output = UrushiWoodInit.STRIPPED_BLACK_URUSHI_LOG.get();
        else if (input == UrushiWoodInit.BLACK_URUSHI_WOOD.get()) output = UrushiWoodInit.STRIPPED_BLACK_URUSHI_WOOD.get();
        else if (input == UrushiWoodInit.RED_URUSHI_LOG.get()) output = UrushiWoodInit.STRIPPED_RED_URUSHI_LOG.get();
        else if (input == UrushiWoodInit.RED_URUSHI_WOOD.get()) output = UrushiWoodInit.STRIPPED_RED_URUSHI_WOOD.get();
        else return;
        event.setFinalState(output.defaultBlockState().setValue(RotatedPillarBlock.AXIS,
            event.getState().getValue(RotatedPillarBlock.AXIS)));
    }
}
