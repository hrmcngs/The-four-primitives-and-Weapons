package the_four_primitives_and_weapons.util;

import net.minecraft.world.item.ItemStack;

import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;

import java.util.ArrayList;
import java.util.List;

/**
 * 拵え台 ( KoshiraeBench ) の候補生成。 入力アイテムに応じて「選べる見た目」の一覧を作る。
 *
 * <ul>
 *   <li>刀 ( IRON_KATANA ): 柄巻きデザイン ( {@link KatanaFittings#WRAPS} ) ＋ 既定</li>
 *   <li>鞘 ( saya ): 仕立て ( 木目/着せ/刻/石目/鮫/漆各種 ) ＋ 既定</li>
 * </ul>
 *
 * <p>各候補は 入力アイテムのコピーに NBT を付けたもの ( 素材消費なしの「見た目替え」)。
	 * 染料がある場合は、部位ごとの染色候補を生成する。</p>
 */
public final class KoshiraeFittings {

	private KoshiraeFittings() {}

	/** 鞘の仕立て候補 ( 漆/素地系 )。 木目は別途 全木材を追加。 */
	private static final String[] SAYA_STYLES = {
			"kise", "kizami", "ishime", "same", "kuroro", "roiro", "shunuri", "tame", "gunto"
	};

	public static boolean isSupported(ItemStack in) {
		if (in.isEmpty()) return false;
		return KatanaFittings.isFittingWeapon(in) || KatanaFittings.isHabakiDyeable(in) || SayaDesign.isSaya(in)
            || NinjatoVault.hasDyeableCord(in);
	}

	/** 入力に対する見た目候補 ( 先頭は「既定」)。 対象外なら空。 */
	public static List<ItemStack> candidatesFor(ItemStack in) {
		return candidatesFor(in, ItemStack.EMPTY);
	}

	/**
	 * 入力(+染料)に対する候補。
	 * <ul>
	 *   <li>染料あり: <b>部位ごとの色変更</b> ( 刀=柄/鍔/頭/はばき を個別に染料色へ、 鞘=地色 )。</li>
	 *   <li>染料なし: 見た目 ( 刀=柄巻きデザイン、 鞘=仕立て )。</li>
	 * </ul>
	 * 大釜は「一気に全部同色」なのに対し、 拵え台は「部位ごとに部分的に」変えられる。
	 */
	public static List<ItemStack> candidatesFor(ItemStack in, ItemStack dye) {
		List<ItemStack> out = new ArrayList<>();
		if (in.isEmpty()) return out;
		boolean hasDye = dye != null && dye.getItem() instanceof net.minecraft.world.item.DyeItem;
        if (hasDye && NinjatoVault.hasDyeableCord(in)) {
            ItemStack cord = in.copy();
            cord.setCount(1);
            cord.getOrCreateTag().putInt(NinjatoVault.CORD_COLOR,
                KatanaFittings.dyeRgb(((net.minecraft.world.item.DyeItem)dye.getItem()).getDyeColor()));
            cord.setHoverName(net.minecraft.network.chat.Component.translatable(
                "gui.the_four_primitives_and_weapons.koshirae.dye_cord"));
            out.add(cord);
        }

		if (KatanaFittings.isFittingWeapon(in) || KatanaFittings.isHabakiDyeable(in)) {
			if (hasDye) {
				int rgb = KatanaFittings.dyeRgb(((net.minecraft.world.item.DyeItem) dye.getItem()).getDyeColor());
				if (KatanaFittings.isFittingWeapon(in)) {
					out.add(katanaColor(in, "tsuka", rgb, "dye_tsuka"));
					out.add(katanaColor(in, "tsuba", rgb, "dye_tsuba"));
					out.add(katanaColor(in, "kashira", rgb, "dye_kashira"));
				}
				if (KatanaFittings.isHabakiDyeable(in))
					out.add(katanaColor(in, "habaki", rgb, "dye_habaki"));
			} else if (!KatanaFittings.isFittingWeapon(in)) {
				return out;
			} else if (isRapier(in)) {
				// レイピア: 柄(grip)/鍔(guard)/頭(pommel) のデザインを部位ごとに選択
				out.add(rapierDesign(in, "grip", "",        "grip_default"));
				out.add(rapierDesign(in, "grip", "grip_b",  "grip_b"));
				out.add(rapierDesign(in, "guard", "",       "guard_default"));
				out.add(rapierDesign(in, "guard", "guard_b","guard_b"));
				out.add(rapierDesign(in, "pommel", "",      "pommel_default"));
				out.add(rapierDesign(in, "pommel", "pommel_b","pommel_b"));
			} else {
				out.add(katana(in, ""));                 // 既定 ( 元の柄 )
				for (String w : KatanaFittings.WRAPS) out.add(katana(in, w));
			}
		} else if (SayaDesign.isSaya(in)) {
			if (hasDye) {
				int rgb = SayaDesign.dyeRgb(((net.minecraft.world.item.DyeItem) dye.getItem()).getDyeColor());
				ItemStack s = in.copy(); s.setCount(1);
				SayaDesign.setBaseColorRgb(s, rgb);
				s.setHoverName(net.minecraft.network.chat.Component.translatable("gui.the_four_primitives_and_weapons.koshirae.dye_saya"));
				out.add(s);
			} else {
				out.add(saya(in, ""));                   // 既定 ( 塗鞘 )
				for (String st : SAYA_STYLES) out.add(saya(in, st));
				for (String wood : SayaStyles.WOODS) out.add(saya(in, "wood:minecraft:" + wood + "_planks"));
			}
		}
		return out;
	}

	/** レイピア判定 ( アイテム名に rapier を含む )。 */
	private static boolean isRapier(ItemStack in) {
		net.minecraft.resources.ResourceLocation id =
				net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(in.getItem());
		return id != null && id.getPath().contains("rapier");
	}

	/** レイピアの部位デザインを設定 ( grip→柄wrap / guard→鍔style / pommel→頭style )。 空=既定。 */
	private static ItemStack rapierDesign(ItemStack in, String part, String design, String label) {
		ItemStack s = in.copy();
		s.setCount(1);
		switch (part) {
			case "guard":  KatanaFittings.setTsubaStyle(s, design); break;
			case "pommel": KatanaFittings.setKashiraStyle(s, design); break;
			default:       KatanaFittings.setTsukaWrap(s, design);
		}
		s.setHoverName(net.minecraft.network.chat.Component.translatable("gui.the_four_primitives_and_weapons.koshirae." + label));
		return s;
	}

	private static ItemStack katanaColor(ItemStack in, String part, int rgb, String label) {
		ItemStack s = in.copy();
		s.setCount(1);
		switch (part) {
			case "tsuba":   KatanaFittings.setTsuba(s, rgb); break;
			case "fuchi":   KatanaFittings.setFuchi(s, rgb); break;
			case "kashira": KatanaFittings.setKashira(s, rgb); break;
			case "habaki":  KatanaFittings.setHabaki(s, rgb); break;
			default:        KatanaFittings.setTsuka(s, rgb);
		}
		s.setHoverName(net.minecraft.network.chat.Component.translatable("gui.the_four_primitives_and_weapons.koshirae." + label));
		return s;
	}

	private static ItemStack katana(ItemStack in, String wrap) {
		ItemStack s = in.copy();
		s.setCount(1);
		KatanaFittings.setTsukaWrap(s, wrap);
		s.setHoverName(net.minecraft.network.chat.Component.translatable(
				"gui.the_four_primitives_and_weapons.koshirae.wrap",
				net.minecraft.network.chat.Component.translatable(
						"gui.the_four_primitives_and_weapons.koshirae.wrap." + (wrap.isEmpty() ? "default" : wrap))));
		return s;
	}

	private static ItemStack saya(ItemStack in, String style) {
		ItemStack s = in.copy();
		s.setCount(1);
		SayaDesign.setStyle(s, style);
		s.setHoverName(net.minecraft.network.chat.Component.translatable(
				"gui.the_four_primitives_and_weapons.koshirae.finish", SayaStyles.styleName(style)));
		return s;
	}
}
