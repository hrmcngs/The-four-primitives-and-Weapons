package the_four_primitives_and_weapons.client;

import the_four_primitives_and_weapons.TheFourPrimitivesAndWeaponsMod;
import the_four_primitives_and_weapons.util.KatanaFittings;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 柄(1)・鍔(2)・頭(3)は保存色、はばき(5)は保存色または武器別の初期色で着色する。
 */
@Mod.EventBusSubscriber(modid = TheFourPrimitivesAndWeaponsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KatanaColorClient {

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, tintIndex) -> {
			// はばきは無彩色の地に直接乗算する。暗色でも指定 RGB をそのまま使う。
			if (tintIndex == 5) return 0xFF000000 | KatanaFittings.habakiRgb(stack);
			if (!KatanaFittings.isFittingWeapon(stack)) return 0xFFFFFFFF;
			// 色を設定した部位は モデル側で グレー版(tint) か 暗版(模様入りの黒、tintなし) に差し替わる。
			// 暗版(ほぼ黒)のときは 乗算で潰れないよう tint を掛けない ( テクスチャに任せる )。
			switch (tintIndex) {
				case 1: return tint(KatanaFittings.tsukaRgb(stack));
				case 2: return tint(KatanaFittings.tsubaRgb(stack));
				case 3: return tint(KatanaFittings.kashiraRgb(stack));
				// 縁(fuchi=4)は無し
				default: return 0xFFFFFFFF;
			}
		}, fittingWeaponItems());
	}

	/** 本MODの 刀/直刀/レイピア系 + addon の SwordItem 系。 dyeable 可否は tag/NBT 評価時に判定する。 */
	static net.minecraft.world.item.Item[] fittingWeaponItems() {
		return ForgeRegistries.ITEMS.getValues().stream()
				.filter(KatanaFittings::isPotentialFittingWeaponItem)
				.toArray(net.minecraft.world.item.Item[]::new);
	}

	/** 色を tint 値(0xFFRRGGBB)へ。 未設定/ほぼ黒 は白 ( = 無着色。 黒は暗版テクスチャが担う )。 */
	private static int tint(int rgb) {
		if (rgb < 0 || KatanaFittings.isNearBlack(rgb)) return 0xFFFFFFFF;
		return 0xFF000000 | rgb;
	}
}
