
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package minecraftarmorweapon.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import minecraftarmorweapon.client.renderer.SkeltonMobRenderer;
import minecraftarmorweapon.client.renderer.Ruami284Renderer;
import minecraftarmorweapon.client.renderer.OtiruyoRenderer;
import minecraftarmorweapon.client.renderer.KillotiruRenderer;
import minecraftarmorweapon.client.renderer.KatanaTobuRenderer;
import minecraftarmorweapon.client.renderer.HrmcngsRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MinecraftArmorWeaponModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(MinecraftArmorWeaponModEntities.SKELTON_MOB.get(), SkeltonMobRenderer::new);
		event.registerEntityRenderer(MinecraftArmorWeaponModEntities.OTIRUYO.get(), OtiruyoRenderer::new);
		event.registerEntityRenderer(MinecraftArmorWeaponModEntities.HRMCNGS.get(), HrmcngsRenderer::new);
		event.registerEntityRenderer(MinecraftArmorWeaponModEntities.RUAMI_284.get(), Ruami284Renderer::new);
		event.registerEntityRenderer(MinecraftArmorWeaponModEntities.KILLOTIRU.get(), KillotiruRenderer::new);
		event.registerEntityRenderer(MinecraftArmorWeaponModEntities.KATANA_TOBU.get(), KatanaTobuRenderer::new);
		event.registerEntityRenderer(MinecraftArmorWeaponModEntities.SYABONDAMA.get(), ThrownItemRenderer::new);
	}
}
