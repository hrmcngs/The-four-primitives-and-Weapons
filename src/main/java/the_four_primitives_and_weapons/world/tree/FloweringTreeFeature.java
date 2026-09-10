package the_four_primitives_and_weapons.world.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import the_four_primitives_and_weapons.init.FloweringWoodInit;

public final class FloweringTreeFeature extends Feature<NoneFeatureConfiguration> {
    private final String species;
    public FloweringTreeFeature(String species){super(NoneFeatureConfiguration.CODEC);this.species=species;}
    @Override public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context){
        var level=context.level();var origin=context.origin();
        if(!level.getBlockState(origin.below()).is(BlockTags.DIRT))return false;
        var shape=FloweringTreeShape.create(species,context.random().nextLong());
        // Check the complete footprint before modifying anything; never overwrite buildings.
        var positions=new java.util.HashSet<>(shape.logs());positions.addAll(shape.leaves().keySet());
        for(var p:positions){BlockPos pos=origin.offset(p.x(),p.y(),p.z());
            if(level.isOutsideBuildHeight(pos)||!level.hasChunkAt(pos))return false;
            var state=level.getBlockState(pos);
            if(!state.isAir()&&!state.is(BlockTags.LEAVES)&&!state.is(BlockTags.REPLACEABLE_BY_TREES))return false;
        }
        var wood=FloweringWoodInit.WOODS.get(species);
        for(var p:shape.logs())level.setBlock(origin.offset(p.x(),p.y(),p.z()),wood.log().get().defaultBlockState(),3);
        shape.leaves().forEach((p,d)->level.setBlock(origin.offset(p.x(),p.y(),p.z()),
            (context.random().nextInt(4)==0?wood.leaves():wood.flowers()).get().defaultBlockState()
                .setValue(LeavesBlock.DISTANCE,d).setValue(LeavesBlock.PERSISTENT,false),3));
        return true;
    }
}
