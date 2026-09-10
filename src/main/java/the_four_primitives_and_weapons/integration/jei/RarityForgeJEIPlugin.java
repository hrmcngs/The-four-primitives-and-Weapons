package the_four_primitives_and_weapons.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import the_four_primitives_and_weapons.init.CustomEntityInit;
import the_four_primitives_and_weapons.init.TheFourPrimitivesAndWeaponsModItems;
import the_four_primitives_and_weapons.init.RarityForgeRegistration;
import the_four_primitives_and_weapons.item.rarity.RarityForgeNewRecipes;
import the_four_primitives_and_weapons.item.rarity.RarityForgeRecipes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


@JeiPlugin
public class RarityForgeJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("the_four_primitives_and_weapons", "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new RarityForgeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(
                new RarityForgeLegacyRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(
                new UrushiTapCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RarityForgeRecipeCategory.RECIPE_TYPE, RarityForgeNewRecipes.build());
        // 旧 rarity_forge_recipes/*.json を JEI に表示
        registration.addRecipes(RarityForgeLegacyRecipeCategory.RECIPE_TYPE, RarityForgeRecipes.getAll());

        // 鞘クラフトレシピをJEIに登録
        SpecialCraftingJeiRecipes.register(registration);

        // 漆の採取 ( 原木 + 火打石/空きビン ) を 3D 表示するカテゴリに登録
        registration.addRecipes(UrushiTapCategory.RECIPE_TYPE, java.util.List.of(
                new UrushiTapRecipe(
                        java.util.List.of(
                                new ItemStack(Items.FLINT),
                                new ItemStack(Items.GLASS_BOTTLE)),
                        new ItemStack(TheFourPrimitivesAndWeaponsModItems.RAW_URUSHI.get()),
                        net.minecraft.world.level.block.Blocks.OAK_LOG.defaultBlockState())));

        // 漆・鞘の仕立て ( スタイル ) をJEIに登録
        SpecialCraftingJeiRecipes.registerHints(registration);

        // JEI 情報パネル: アイテム選択時に操作/効果を説明表示
        registerKnifeItemInfo(registration);
        registerUrushiItemInfo(registration);
    }

    /** 漆系アイテムの入手/使い方を JEI の情報パネルに表示。 */
    private void registerUrushiItemInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(
            new ItemStack(TheFourPrimitivesAndWeaponsModItems.RAW_URUSHI.get()),
            VanillaTypes.ITEM_STACK,
            Component.translatable("jei.the_four_primitives_and_weapons.info.raw_urushi"));
        registration.addIngredientInfo(
            new ItemStack(TheFourPrimitivesAndWeaponsModItems.URUSHI_BLACK.get()),
            VanillaTypes.ITEM_STACK,
            Component.translatable("jei.the_four_primitives_and_weapons.info.urushi_black"));
        registration.addIngredientInfo(
            new ItemStack(TheFourPrimitivesAndWeaponsModItems.URUSHI_RED.get()),
            VanillaTypes.ITEM_STACK,
            Component.translatable("jei.the_four_primitives_and_weapons.info.urushi_red"));
        registration.addIngredientInfo(
            new ItemStack(TheFourPrimitivesAndWeaponsModItems.SAYA.get()),
            VanillaTypes.ITEM_STACK,
            Component.translatable("jei.the_four_primitives_and_weapons.info.saya"));
    }

    // ナイフ系・ガイドブックに "info" パネルを付与。
    // vanilla の crafting レシピ (data配下のrecipesフォルダに置いた json) は
    // JEI が自動で拾うのでレシピ登録は不要。ここでは使用方法の説明文のみ。
    private void registerKnifeItemInfo(IRecipeRegistration registration) {
        registration.addIngredientInfo(
            new ItemStack(CustomEntityInit.KNIFE_LAUNCHER.get()),
            VanillaTypes.ITEM_STACK,
            Component.literal("§6ナイフホルダー"),
            Component.literal("右クリックで扇形に投擲。"),
            Component.literal("Shift+左クリックで技選択画面を開く。"),
            Component.literal("モードと本数 (1〜10) を設定できる。"),
            Component.literal("§c本数が多いほどブレが大きくなる。"),
            Component.literal("弾は各種ナイフをインベントリから消費。")
        );
        registration.addIngredientInfo(
            new ItemStack(CustomEntityInit.GUIDE_BOOK.get()),
            VanillaTypes.ITEM_STACK,
            Component.literal("§6始まりのガイドブック"),
            Component.literal("右クリックでガイド画面を開く。"),
            Component.literal("操作方法・ナイフの技・Mob の情報を"),
            Component.literal("画像付きで確認できる。"),
            Component.literal("ワールド初回参加時に自動支給。")
        );
        registration.addIngredientInfo(
            new ItemStack(CustomEntityInit.THROWING_KNIFE.get()),
            VanillaTypes.ITEM_STACK,
            Component.literal("§f通常ナイフ"),
            Component.literal("右クリックで投擲、左クリックで近接攻撃。"),
            Component.literal("敵に当てれば消滅、ブロックに刺さる。"),
            Component.literal("MP 消費なし / クールダウン 8t。")
        );
        registration.addIngredientInfo(
            new ItemStack(CustomEntityInit.STUN_KNIFE.get()),
            VanillaTypes.ITEM_STACK,
            Component.literal("§eスタンナイフ"),
            Component.literal("命中で感電: 移動低下 + 弱体化。"),
            Component.literal("MP 25/本 / クールダウン 16t。")
        );
        registration.addIngredientInfo(
            new ItemStack(CustomEntityInit.SCREW_KNIFE.get()),
            VanillaTypes.ITEM_STACK,
            Component.literal("§bスクリューナイフ"),
            Component.literal("木材/葉を破壊できる。"),
            Component.literal("葉は素手扱い → 苗木/棒が低確率。"),
            Component.literal("木に当たると消滅、葉は貫通継続。"),
            Component.literal("MP 10/本 / クールダウン 10t。")
        );

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(RarityForgeRegistration.getBlock()),
                RarityForgeRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(
                new ItemStack(RarityForgeRegistration.getBlock()),
                RarityForgeLegacyRecipeCategory.RECIPE_TYPE);
        // バニラ crafting カテゴリでも RarityForge ブロックを触媒として表示
        // ( = レアリティ作業台でもバニラ crafting レシピが作れるとユーザに伝える )
        registration.addRecipeCatalyst(
                new ItemStack(RarityForgeRegistration.getBlock()),
                mezz.jei.api.constants.RecipeTypes.CRAFTING);
    }

    @Override
    public void registerRecipeTransferHandlers(
            mezz.jei.api.registration.IRecipeTransferRegistration registration) {
        // JEI レシピの「+」 ボタンで グリッドに素材を自動転送
        registration.addRecipeTransferHandler(
                new RarityForgeRecipeTransferInfo());
    }

    @Override
    public void registerGuiHandlers(mezz.jei.api.registration.IGuiHandlerRegistration registration) {
        // 結晶ポーチのフローティングパネル領域を JEI の占有域として登録
        // ( パネル越しに背後のアイテム名が出るのを防ぐ )
        registration.addGlobalGuiHandler(new PouchPanelGuiHandler());
    }
}
