# 花木

金木犀・銀木犀・枝垂れ梅・椿を追加しています。各樹種には原木・樹皮を剥いだ原木・板材・花なし葉・花付き葉・苗木があります。クリエイティブの「自然のブロック」「建築ブロック」から入手できます。

| 名前 | IDの接頭辞 | 樹形・花 |
| --- | --- | --- |
| 金木犀 | `kinmokusei` | 密な丸い樹冠、橙の小花 |
| 銀木犀 | `ginmokusei` | 密な丸い樹冠、白〜クリームの小花 |
| 枝垂れ梅 | `shidare_ume` | 広がって下がる枝、薄桃色の花 |
| 椿 | `tsubaki` | 低めの密な樹冠、深緑の葉と赤い花 |

苗木を土や草ブロックへ植えると育ち、骨粉でも成長を促せます。周囲に建物や未読み込みチャンクがある場合は生成せず、苗木を残します。自然バイオームへの自動追加はありません。新規の撮影用ワールドには桜と混ぜて配置します。

```mcfunction
/give @s the_four_primitives_and_weapons:kinmokusei_sapling
/give @s the_four_primitives_and_weapons:kinmokusei_leaves
/give @s the_four_primitives_and_weapons:flowering_kinmokusei_leaves
/place feature the_four_primitives_and_weapons:kinmokusei_tree ~ ~ ~
```

`place feature`は足元が土・草で、木を置く空間がある場所で実行します。原木は斧で皮を剥げ、原木1個から板材4個をクラフトできます。葉はハサミ・シルクタッチで回収し、通常破壊では苗木が確率で落ちます。花あり・花なしは別の葉ブロックで、季節による自動切替はありません。

枝は幹から切れずにつながる構造です。葉は実際の原木からの支持距離を計算して配置し、支えを失えば通常の葉と同様に消えます。形はブロック表現向けに簡略化しています。
金木犀・銀木犀の丸く密な常緑樹形は[Oregon State UniversityのOsmanthus解説](https://landscapeplants.oregonstate.edu/plants/osmanthus-fragrans)、椿の直立した低木形は[RHSのCamellia解説](https://rhs.crocdn.co.uk/plants/_/camellia-japonica-nuccios-pearl/classid.2000047821/)を参考にしています。

テクスチャはMinecraft Java 1.20.1のオークとツツジを元に、花と葉の領域を分けて配色を調整しています。元の透過・解像度・木目・葉の形を維持します。素材再生成の使い方は `bash sh/generate/generate-flowering-woods.sh help` で確認できます。

## 金木犀の料理

花付きの金木犀の葉を空手で右クリックすると、花を1〜3個収穫できます。葉は花なしに変わりますが、骨粉を1個使うと再び花付きになります。

| 作るもの | 不定形レシピ | 出来上がり |
| --- | --- | --- |
| 金木犀シロップ | 金木犀の花×3・砂糖×2・ガラス瓶・水入りバケツ | 1本、空バケツ返却 |
| 金木犀ケーキ | バニラのケーキ・金木犀シロップ | 1個、空き瓶返却 |
| 金木犀クッキー | 小麦×2・金木犀シロップ | 8個、空き瓶返却 |

ケーキはバニラと同様に置いて7回に分けて食べられます。シロップを直接飲むと満腹度2、クッキーを食べると満腹度3を回復します。シロップを飲んでも空き瓶が戻ります。レシピブックは花やシロップを入手すると解放されます。

アイテムIDは `kinmokusei_flowers`、`kinmokusei_syrup`、`kinmokusei_cake`、`kinmokusei_cookie`（名前空間は `the_four_primitives_and_weapons`）です。料理素材の再生成方法は `bash sh/generate/generate-osmanthus-food.sh help` で確認できます。

### JEI

JEI導入時は、金木犀シロップ・ケーキ・クッキーと各板材のクラフトを通常のレシピ画面で確認できます。花・葉・苗木・樹皮を剥いだ原木には、収穫、再開花、成長、採取方法の説明ページも表示されます。アイテムにカーソルを合わせてJEIのレシピ表示キー（標準では R）を押してください。

刀の染色、鞘の染色・仕立て、魔法刀の解放、忍者刀の紐付けも特殊クラフトの表示に対応しています。NBT変更レシピの表示は完成品の一例です。実際の加工で保持される情報や、加工できない条件は説明ページを確認してください。
