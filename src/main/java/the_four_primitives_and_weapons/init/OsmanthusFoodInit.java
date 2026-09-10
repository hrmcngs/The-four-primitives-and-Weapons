package the_four_primitives_and_weapons.init;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.*;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="the_four_primitives_and_weapons",bus=Mod.EventBusSubscriber.Bus.MOD)
public final class OsmanthusFoodInit {
    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS,"the_four_primitives_and_weapons");
    public static final DeferredRegister<Block> BLOCKS=DeferredRegister.create(ForgeRegistries.BLOCKS,"the_four_primitives_and_weapons");
    public static final RegistryObject<Item> FLOWERS=ITEMS.register("kinmokusei_flowers",()->new Item(new Item.Properties()));
    public static final RegistryObject<Item> SYRUP=ITEMS.register("kinmokusei_syrup",()->new Item(new Item.Properties().stacksTo(16)
            .craftRemainder(Items.GLASS_BOTTLE).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2F).build())){
        @Override public UseAnim getUseAnimation(ItemStack stack){return UseAnim.DRINK;}
        @Override public ItemStack finishUsingItem(ItemStack stack,Level level,LivingEntity user){
            ItemStack result=super.finishUsingItem(stack,level,user);
            if(user instanceof Player player && player.getAbilities().instabuild)return result;
            if(result.isEmpty())return new ItemStack(Items.GLASS_BOTTLE);
            if(user instanceof Player player && !player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE)))
                player.drop(new ItemStack(Items.GLASS_BOTTLE),false);
            return result;
        }
    });
    public static final RegistryObject<Item> COOKIE=ITEMS.register("kinmokusei_cookie",()->new Item(new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(3).saturationMod(0.4F).build())));
    public static final RegistryObject<Block> CAKE_BLOCK=BLOCKS.register("kinmokusei_cake",()->new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
    public static final RegistryObject<Item> CAKE=ITEMS.register("kinmokusei_cake",()->new BlockItem(CAKE_BLOCK.get(),new Item.Properties().stacksTo(1)));
    public static void register(net.minecraftforge.eventbus.api.IEventBus bus){BLOCKS.register(bus);ITEMS.register(bus);}
    @SubscribeEvent public static void creative(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey()==CreativeModeTabs.FOOD_AND_DRINKS)ITEMS.getEntries().forEach(item->event.accept(item.get()));
    }
    private OsmanthusFoodInit(){}
}
