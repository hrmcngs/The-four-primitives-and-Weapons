
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

import minecraftarmorweapon.entity.SkeltonMobEntity;
import minecraftarmorweapon.entity.MahoutaneEntity;
import minecraftarmorweapon.entity.M16Entity;
import minecraftarmorweapon.entity.BlackSpectralArrowEntity;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinecraftArmorWeaponModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<EntityType<MahoutaneEntity>> MAHOUTANE = register("projectile_mahoutane",
			EntityType.Builder.<MahoutaneEntity>of(MahoutaneEntity::new, MobCategory.MISC).setCustomClientFactory(MahoutaneEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<BlackSpectralArrowEntity>> BLACK_SPECTRAL_ARROW = register("projectile_black_spectral_arrow", EntityType.Builder.<BlackSpectralArrowEntity>of(BlackSpectralArrowEntity::new, MobCategory.MISC)
			.setCustomClientFactory(BlackSpectralArrowEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<SkeltonMobEntity>> SKELTON_MOB = register("skelton_mob",
			EntityType.Builder.<SkeltonMobEntity>of(SkeltonMobEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(SkeltonMobEntity::new)

					.sized(1f, 1.5f));
	public static final RegistryObject<EntityType<M16Entity>> M_16 = register("projectile_m_16",
			EntityType.Builder.<M16Entity>of(M16Entity::new, MobCategory.MISC).setCustomClientFactory(M16Entity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SkeltonMobEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(SKELTON_MOB.get(), SkeltonMobEntity.createAttributes().build());
	}
}
