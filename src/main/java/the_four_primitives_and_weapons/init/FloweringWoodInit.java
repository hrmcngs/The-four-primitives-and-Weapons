package the_four_primitives_and_weapons.init;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraftforge.registries.*;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import the_four_primitives_and_weapons.world.tree.FloweringTreeFeature;

@Mod.EventBusSubscriber(modid="the_four_primitives_and_weapons",bus=Mod.EventBusSubscriber.Bus.MOD)
public final class FloweringWoodInit {
    private static final String MODID="the_four_primitives_and_weapons";
    public static final DeferredRegister<Block> BLOCKS=DeferredRegister.create(ForgeRegistries.BLOCKS,MODID);
    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS,MODID);
    public static final DeferredRegister<Feature<?>> FEATURES=DeferredRegister.create(ForgeRegistries.FEATURES,MODID);
    public record Wood(RegistryObject<Block> log,RegistryObject<Block> stripped,RegistryObject<Block> planks,
                       RegistryObject<Block> leaves,RegistryObject<Block> flowers,RegistryObject<Block> sapling){}
    public static final Map<String,Wood> WOODS=new LinkedHashMap<>();
    static {for(String name:new String[]{"kinmokusei","ginmokusei","shidare_ume","tsubaki"}){
        FEATURES.register(name+"_tree",()->new FloweringTreeFeature(name));
        var log=block(name+"_log",()->new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
        var stripped=block("stripped_"+name+"_log",()->new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
        var planks=block(name+"_planks",()->new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
        var leaves=block(name+"_leaves",()->new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.AZALEA_LEAVES)));
        var flowers=block("flowering_"+name+"_leaves",()->new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.FLOWERING_AZALEA_LEAVES)));
        var sapling=block(name+"_sapling",()->new SaplingBlock(new AbstractTreeGrower(){
            @Override protected ResourceKey<ConfiguredFeature<?,?>> getConfiguredFeature(RandomSource random,boolean flowers){
                return ResourceKey.create(Registries.CONFIGURED_FEATURE,new ResourceLocation(MODID,name+"_tree"));
            }
        },BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
        WOODS.put(name,new Wood(log,stripped,planks,leaves,flowers,sapling));
    }}
    private static RegistryObject<Block> block(String name,java.util.function.Supplier<Block> factory){
        var block=BLOCKS.register(name,factory);ITEMS.register(name,()->new BlockItem(block.get(),new Item.Properties()));return block;
    }
    public static void register(net.minecraftforge.eventbus.api.IEventBus bus){BLOCKS.register(bus);ITEMS.register(bus);FEATURES.register(bus);}
    @SubscribeEvent public static void creative(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey()==CreativeModeTabs.NATURAL_BLOCKS||event.getTabKey()==CreativeModeTabs.BUILDING_BLOCKS)
            ITEMS.getEntries().forEach(item->event.accept(item.get()));
    }
    private FloweringWoodInit(){}
}
