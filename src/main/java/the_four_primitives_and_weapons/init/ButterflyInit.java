package the_four_primitives_and_weapons.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.entity.ButterflyEntity;

@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ButterflyInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            ForgeRegistries.ENTITY_TYPES, TheFourPrimitivesAndWeaponsMod.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS, TheFourPrimitivesAndWeaponsMod.MODID);
    public static final RegistryObject<EntityType<ButterflyEntity>> BUTTERFLY = ENTITIES.register("butterfly",
            () -> EntityType.Builder.of(ButterflyEntity::new, MobCategory.AMBIENT)
                    .sized(0.35F, 0.25F).clientTrackingRange(8).updateInterval(3).build("butterfly"));
    public static final RegistryObject<Item> SPAWN_EGG = ITEMS.register("butterfly_spawn_egg",
            () -> new ForgeSpawnEggItem(BUTTERFLY, 0x294365, 0x83CDEB, new Item.Properties()));

    @SubscribeEvent
    public static void attributes(EntityAttributeCreationEvent event) {
        event.put(BUTTERFLY.get(), ButterflyEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) event.accept(SPAWN_EGG.get());
    }
}
