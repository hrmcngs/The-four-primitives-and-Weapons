# 蝶の色と模様

蝶 `the_four_primitives_and_weapons:butterfly` は10種類の配色、6系統の模様を持ちます。
スポーンエッグや種類を指定しない召喚ではランダムに決まり、保存・再読込後も維持されます。
種類はクライアントへ同期されます。以前の種類データがない蝶には初回読込時に種類が付きます。

| Variant | 配色 | 模様・形 |
| --- | --- | --- |
| 0 | モルフォ風の青 | 暗い縁と細い筋 |
| 1 | オオカバマダラ風の橙 | 黒い翅脈、縁の白い斑点 |
| 2 | アゲハ風の淡黄 | 黒い縞、後翅の斑点、尾状突起 |
| 3 | モンシロチョウ風の白 | 暗い翅先と斑点 |
| 4 | キチョウ風の黄 | 翅先と小さな斑点 |
| 5 | フクロウチョウ風の茶 | 同心状の目玉模様 |
| 6 | ポストマン風の黒と赤 | 赤い帯 |
| 7 | 青緑のアゲハ風 | 縞、斑点、尾状突起 |
| 8 | 桜色（創作） | クリーム色の帯 |
| 9 | 薄紫（創作） | 淡色の目玉模様 |

種の厳密な再現ではなく、Minecraft向けに単純化した模様です。
構造色・表裏の模様の違い・雌雄差は再現していません。

桜色を召喚：

```mcfunction
/summon the_four_primitives_and_weapons:butterfly ~ ~1 ~ {Variant:8}
```

橙色の種類を出すスポーンエッグ：

```mcfunction
/give @p the_four_primitives_and_weapons:butterfly_spawn_egg{EntityTag:{Variant:1}} 1
```

近くの蝶1匹を青に変更：

```mcfunction
/data merge entity @e[type=the_four_primitives_and_weapons:butterfly,distance=..8,sort=nearest,limit=1] {Variant:0}
```

`Variant` は整数0〜9です。範囲外の整数は0として扱います。
Javaコードの変更なので、反映にはMODの再ビルドとゲームの再起動が必要です。

参考にした資料：モルフォの青は [Natural History Museum](https://www.nhm.ac.uk/discover/spotlight-blue-morpho.html)、
オオカバマダラの黒い縁と白斑は [Monarch Joint Venture](https://monarchjointventure.org/faq/monarch-biology/monarch-mimics)、
目玉模様は [Natural History Museumのフクロウチョウ紹介](https://www.nhm.ac.uk/discover/spotlight-owl-butterfly.html)、
赤い帯は [蝶の羽の色の解説](https://www.nhm.ac.uk/discover/butterfly-wings-science-behind-the-colour.html)を参照しています。
画像を転載せず、色分けしたモデルのパーツで模様を描画しています。
