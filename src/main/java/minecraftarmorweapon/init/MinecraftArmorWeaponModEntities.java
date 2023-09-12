
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import minecraftarmorweapon.entity.MahouzinnEntityEntity;
import minecraftarmorweapon.entity.MahoutaneEntity;
import minecraftarmorweapon.entity.LunaEntityEntity;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinecraftArmorWeaponModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<EntityType<LunaEntityEntity>> LUNA_ENTITY = register("luna_entity",
			EntityType.Builder.<LunaEntityEntity>of(LunaEntityEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(LunaEntityEntity::new).fireImmune().sized(1f, 1f));
	public static final RegistryObject<EntityType<MahoutaneEntity>> MAHOUTANE = register("projectile_mahoutane",
			EntityType.Builder.<MahoutaneEntity>of(MahoutaneEntity::new, MobCategory.MISC).setCustomClientFactory(MahoutaneEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<MahouzinnEntityEntity>> MAHOUZINN_ENTITY = register("mahouzinn_entity", EntityType.Builder.<MahouzinnEntityEntity>of(MahouzinnEntityEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MahouzinnEntityEntity::new).fireImmune().sized(1f, 1f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			LunaEntityEntity.init();
			MahouzinnEntityEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(LUNA_ENTITY.get(), LunaEntityEntity.createAttributes().build());
		event.put(MAHOUZINN_ENTITY.get(), MahouzinnEntityEntity.createAttributes().build());
	}
}
