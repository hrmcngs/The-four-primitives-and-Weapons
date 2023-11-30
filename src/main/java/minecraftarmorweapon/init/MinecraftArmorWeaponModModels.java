
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import minecraftarmorweapon.client.model.Modelswordbblock_Converted;
import minecraftarmorweapon.client.model.Modelstray_outer_Converted_hat;
import minecraftarmorweapon.client.model.Modelstray_outer_Converted;
import minecraftarmorweapon.client.model.Modelskeleton_Converted;
import minecraftarmorweapon.client.model.Modelplayer_slim__Converted;
import minecraftarmorweapon.client.model.Modelpillager_Converted;
import minecraftarmorweapon.client.model.Modelpiglin_brute_Converted;
import minecraftarmorweapon.client.model.Modelnetherite_arrow_armor_layer_1_Converted;
import minecraftarmorweapon.client.model.Modelmahouzinn;
import minecraftarmorweapon.client.model.Modelluna_Converted2;
import minecraftarmorweapon.client.model.Modelluna_Converted;
import minecraftarmorweapon.client.model.Modelillusioner_armor_layer_3_Converted;
import minecraftarmorweapon.client.model.Modelhusk_Converted;
import minecraftarmorweapon.client.model.Modelelytra_Converted;
import minecraftarmorweapon.client.model.Modeldoragon_leprica_armor_layer_3;
import minecraftarmorweapon.client.model.Modelblack_spectral_arrow_Converted;
import minecraftarmorweapon.client.model.Modelbanner_Converted;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class MinecraftArmorWeaponModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelskeleton_Converted.LAYER_LOCATION, Modelskeleton_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelnetherite_arrow_armor_layer_1_Converted.LAYER_LOCATION, Modelnetherite_arrow_armor_layer_1_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelpillager_Converted.LAYER_LOCATION, Modelpillager_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelmahouzinn.LAYER_LOCATION, Modelmahouzinn::createBodyLayer);
		event.registerLayerDefinition(Modelplayer_slim__Converted.LAYER_LOCATION, Modelplayer_slim__Converted::createBodyLayer);
		event.registerLayerDefinition(Modelstray_outer_Converted.LAYER_LOCATION, Modelstray_outer_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelluna_Converted2.LAYER_LOCATION, Modelluna_Converted2::createBodyLayer);
		event.registerLayerDefinition(Modelhusk_Converted.LAYER_LOCATION, Modelhusk_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelillusioner_armor_layer_3_Converted.LAYER_LOCATION, Modelillusioner_armor_layer_3_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelpiglin_brute_Converted.LAYER_LOCATION, Modelpiglin_brute_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelblack_spectral_arrow_Converted.LAYER_LOCATION, Modelblack_spectral_arrow_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelswordbblock_Converted.LAYER_LOCATION, Modelswordbblock_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelbanner_Converted.LAYER_LOCATION, Modelbanner_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelluna_Converted.LAYER_LOCATION, Modelluna_Converted::createBodyLayer);
		event.registerLayerDefinition(Modelstray_outer_Converted_hat.LAYER_LOCATION, Modelstray_outer_Converted_hat::createBodyLayer);
		event.registerLayerDefinition(Modeldoragon_leprica_armor_layer_3.LAYER_LOCATION, Modeldoragon_leprica_armor_layer_3::createBodyLayer);
		event.registerLayerDefinition(Modelelytra_Converted.LAYER_LOCATION, Modelelytra_Converted::createBodyLayer);
	}
}
