# はばきの色指定

柄・鍔・頭が独立している `katana_a_parent`、`katana_c_parent`、
`reitou3d`、`ninzyatou`、`tyokuto_b_parent`、
Magical Katana の `katanairon3dmagicanimetion` のはばきに対応します。
一体型モデルは対象外です。

## 個別に色を変える

NBT の `HabakiColor` に `#RRGGBB` を指定します。

```mcfunction
/give @p the_four_primitives_and_weapons:iron_katana{HabakiColor:"#D4AF37"}
```

`"0xD4AF37"`、`"D4AF37"`、整数 RGB も使えます。
柄の `TsukaColor`、鍔の `TsubaColor`、頭の `KashiraColor` も同じ文字列形式に対応します。
6桁以外や不正な文字列は無視し、通常の未指定時の色を使います。

はばきの色は `HabakiColor` → `display.color` → 武器別の初期色の順で決まります。
`HabakiColor` を削除すると個別指定を解除できます。

## 武器別の初期色を編集する

`src/main/java/the_four_primitives_and_weapons/util/KatanaFittings.java` の
`defaultHabakiRgb` にある `0xRRGGBB` を編集して再ビルドします。
例: `iron_katana` の `0xFEF364` を `0xD4AF37` に変更。

モデルのはばきは `#habaki` と `tintindex: 5` を使い、既存の無彩色テクスチャ
`katana_fitting/kasira/kasira_gray` の明暗を含む不透明部分（UV: `[0, 0, 3, 1]`）を参照します。
単一ピクセルの引き伸ばしを避け、明度205〜225の控えめな金属の筋を表示します。黒い帯になる暗部は使いません。
柄・頭の色やデザイン変更とは独立して着色されます。
無彩色の地の明るさとゲーム内の照明によって、表示色には陰影が付きます。
