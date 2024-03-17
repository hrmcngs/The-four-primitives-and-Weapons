
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
import minecraftarmorweapon.entity.Ruami284Entity;
import minecraftarmorweapon.entity.OtiruyoEntity;
import minecraftarmorweapon.entity.MahouzinmitameEntity;
import minecraftarmorweapon.entity.KillotiruEntity;
import minecraftarmorweapon.entity.KatanaTobuEntity;
import minecraftarmorweapon.entity.HrmcngsEntity;

import minecraftarmorweapon.MinecraftArmorWeaponMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinecraftArmorWeaponModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MinecraftArmorWeaponMod.MODID);
	public static final RegistryObject<EntityType<SkeltonMobEntity>> SKELTON_MOB = register("skelton_mob",
			EntityType.Builder.<SkeltonMobEntity>of(SkeltonMobEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(SkeltonMobEntity::new)

					.sized(0.6f, 1.5f));
	public static final RegistryObject<EntityType<OtiruyoEntity>> OTIRUYO = register("otiruyo",
			EntityType.Builder.<OtiruyoEntity>of(OtiruyoEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(OtiruyoEntity::new).fireImmune().sized(1.2f, 3.6f));
	public static final RegistryObject<EntityType<HrmcngsEntity>> HRMCNGS = register("hrmcngs",
			EntityType.Builder.<HrmcngsEntity>of(HrmcngsEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(HrmcngsEntity::new)

					.sized(0.9f, 0.9f));
	public static final RegistryObject<EntityType<Ruami284Entity>> RUAMI_284 = register("ruami_284",
			EntityType.Builder.<Ruami284Entity>of(Ruami284Entity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Ruami284Entity::new)

					.sized(0.9f, 0.9f));
	public static final RegistryObject<EntityType<KillotiruEntity>> KILLOTIRU = register("killotiru", EntityType.Builder.<KillotiruEntity>of(KillotiruEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
			.setUpdateInterval(3).setCustomClientFactory(KillotiruEntity::new).fireImmune().sized(1.2f, 3.6f));
	public static final RegistryObject<EntityType<KatanaTobuEntity>> KATANA_TOBU = register("projectile_katana_tobu",
			EntityType.Builder.<KatanaTobuEntity>of(KatanaTobuEntity::new, MobCategory.MISC).setCustomClientFactory(KatanaTobuEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<MahouzinmitameEntity>> MAHOUZINMITAME = register("projectile_mahouzinmitame",
			EntityType.Builder.<MahouzinmitameEntity>of(MahouzinmitameEntity::new, MobCategory.MISC).setCustomClientFactory(MahouzinmitameEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SkeltonMobEntity.init();
			OtiruyoEntity.init();
			HrmcngsEntity.init();
			Ruami284Entity.init();
			KillotiruEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(SKELTON_MOB.get(), SkeltonMobEntity.createAttributes().build());
		event.put(OTIRUYO.get(), OtiruyoEntity.createAttributes().build());
		event.put(HRMCNGS.get(), HrmcngsEntity.createAttributes().build());
		event.put(RUAMI_284.get(), Ruami284Entity.createAttributes().build());
		event.put(KILLOTIRU.get(), KillotiruEntity.createAttributes().build());
	}
}
