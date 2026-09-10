package the_four_primitives_and_weapons.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.init.FloweringWoodInit;
import the_four_primitives_and_weapons.init.OsmanthusFoodInit;

@Mod.EventBusSubscriber(modid="the_four_primitives_and_weapons")
public final class OsmanthusHarvestHandler {
    @SubscribeEvent public static void interact(PlayerInteractEvent.RightClickBlock event){
        var level=event.getLevel();var state=level.getBlockState(event.getPos());
        var wood=FloweringWoodInit.WOODS.get("kinmokusei");var held=event.getItemStack();
        boolean harvest=state.is(wood.flowers().get())&&held.isEmpty();
        boolean bloom=state.is(wood.leaves().get())&&held.is(Items.BONE_MEAL);
        if(!harvest&&!bloom)return;
        if(!level.isClientSide){
            var replacement=(harvest?wood.leaves():wood.flowers()).get().defaultBlockState()
                .setValue(LeavesBlock.DISTANCE,state.getValue(LeavesBlock.DISTANCE))
                .setValue(LeavesBlock.PERSISTENT,state.getValue(LeavesBlock.PERSISTENT))
                .setValue(LeavesBlock.WATERLOGGED,state.getValue(LeavesBlock.WATERLOGGED));
            if(level.setBlock(event.getPos(),replacement,3)){
                if(harvest)Block.popResource(level,event.getPos(),new ItemStack(OsmanthusFoodInit.FLOWERS.get(),1+level.random.nextInt(3)));
                else {if(!event.getEntity().getAbilities().instabuild)held.shrink(1);level.levelEvent(1505,event.getPos(),0);}
            }
        }
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));event.setCanceled(true);
    }
    private OsmanthusHarvestHandler(){}
}
