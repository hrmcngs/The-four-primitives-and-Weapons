# 蝶の色と模様

蝶 `the_four_primitives_and_weapons:butterfly` は49種類のプリセット、11系統の模様を持ちます。
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

`Variant` は整数0〜48、または下記の名前を指定できます。範囲外の整数・未知の名前は0として扱います。
Javaコードの変更なので、反映にはMODの再ビルドとゲームの再起動が必要です。

参考にした資料：モルフォの青は [Natural History Museum](https://www.nhm.ac.uk/discover/spotlight-blue-morpho.html)、
オオカバマダラの黒い縁と白斑は [Monarch Joint Venture](https://monarchjointventure.org/faq/monarch-biology/monarch-mimics)、
目玉模様は [Natural History Museumのフクロウチョウ紹介](https://www.nhm.ac.uk/discover/spotlight-owl-butterfly.html)、
赤い帯は [蝶の羽の色の解説](https://www.nhm.ac.uk/discover/butterfly-wings-science-behind-the-colour.html)を参照しています。
画像を転載せず、色分けしたモデルのパーツで模様を描画しています。

## summonで細かく指定

プリセットを基準に、指定した項目だけを変更します。

```mcfunction
/summon the_four_primitives_and_weapons:butterfly ~ ~1 ~ {Variant:"sakura",WingColor:"#E8A6C4",EdgeColor:"#55384F",AccentColor:"#FFF2CE",BodyColor:"#382D35",Pattern:6,Tails:1b,Size:1.5f,WingWidth:1.2f,WingLength:1.1f,FlapSpeed:0.7f,FlapAmount:0.8f,FlightSpeed:0.6f}
```

| NBT | 範囲／形式 | 用途 |
| --- | --- | --- |
| `Variant` | 0〜48 または名前 | 色・模様・尾のプリセット |
| `WingColor` | `"#RRGGBB"` または整数RGB | 羽の地色 |
| `EdgeColor` | 同上 | 羽の縁と模様の暗い部分 |
| `AccentColor` | 同上 | 斑点・帯・模様の明るい部分 |
| `BodyColor` | 同上 | 胴体と触角 |
| `Pattern` | 0〜10 | 模様を独立して選択 |
| `AntennaLength` | 0.25〜3 | 頭を根元にした触角の長さ（既定1） |
| `AntennaSpread` | 0〜80 | 触角の左右の開き角、度（既定25） |
| `AntennaTilt` | 0〜80 | 触角を上へ向ける角度、度（既定20） |
| `BodyWidth` | 0.5〜2 | 胴体・頭の幅（既定1） |
| `BodyLength` | 0.5〜2 | 胴体・頭の長さ（既定1） |
| `FlapRestAngle` | 0〜80 | 羽ばたきの中心角、度（既定約14.324） |
| `Tails` | `0b` / `1b` | 尾状突起を非表示／表示 |
| `Size` | 0.25〜4、標準1 | 全体の大きさ。当たり判定も拡縮 |
| `WingWidth` | 0.5〜2、標準1 | 羽だけの横幅 |
| `WingLength` | 0.5〜2、標準1 | 羽だけの前後方向の長さ |
| `FlapSpeed` | 0〜4、標準1 | 羽ばたき速度。0で動きを止める |
| `FlapAmount` | 0〜1.4、標準0.9 | 羽ばたきの振幅 |
| `FlightSpeed` | 0〜3、標準1 | 自律飛行の速度倍率 |

色は `"0xE8A6C4"` や `"E8A6C4"` でも指定できます。保存時には整数RGBへ変換します。
羽の縦横比は見た目のみの設定です。プリセット名は大文字・小文字どちらでも指定できます。
既存の蝶の `Variant` を変えても、明示した個別設定は優先されます。
サイズなど範囲外の正数は上限・下限に収めます。負数や不正な値はその項目の初期設定へ戻します。
`Pattern` の範囲外と `Tails:-1` はプリセット設定に戻します。

近くの蝶の模様と羽の色を変更：

```mcfunction
/data merge entity @e[type=the_four_primitives_and_weapons:butterfly,distance=..8,sort=nearest,limit=1] {Pattern:4,WingColor:"#B997E2"}
```

羽の色だけプリセットに戻す：

```mcfunction
/data remove entity @e[type=the_four_primitives_and_weapons:butterfly,distance=..8,sort=nearest,limit=1] WingColor
```

撮影用にその場へ止める（羽ばたきは続く）：

```mcfunction
/summon the_four_primitives_and_weapons:butterfly ~ ~1 ~ {Variant:"moonlight",Size:2f,NoAI:1b,NoGravity:1b,Invulnerable:1b,PersistenceRequired:1b}
```

羽ばたきも止めたい場合は `FlapSpeed:0f` を追加します。
光るような名前のプリセットも、発光やパーティクルを自動で付けるものではありません。
自然生成や撮影ワールドへの自動配置はありません。

## 模様の番号

| Pattern | 模様 |
| --- | --- |
| 0 | 細い翅脈 |
| 1 | 黒い翅脈と縁の白斑 |
| 2 | アゲハ風の縞 |
| 3 | 翅先と小斑点 |
| 4 | 目玉模様 |
| 5 | 太い帯 |
| 6 | 真珠状の点列 |
| 7 | ジグザグ帯 |
| 8 | 輪郭付きパネル |
| 9 | 市松状の斑紋 |
| 10 | 夢見鳥風の雫模様・専用の羽形状 |

## 全プリセット

最初の10種類は従来のIDを維持しています。ID 10〜47は色をテーマにした創作プリセット、ID 48は夢見鳥風のdreamwingです。既存のIDは維持しています。

| ID | 名前 | 羽の色 | 模様 | 尾 |
| --- | --- | --- | --- | --- |
| 0 | `morpho` | `#278EDE` | 0 | なし |
| 1 | `monarch` | `#E88B29` | 1 | なし |
| 2 | `swallowtail` | `#EADC83` | 2 | あり |
| 3 | `cabbage_white` | `#F0EFD9` | 3 | なし |
| 4 | `sulphur` | `#E7D94E` | 3 | なし |
| 5 | `owl` | `#997650` | 4 | なし |
| 6 | `postman` | `#302C35` | 5 | なし |
| 7 | `blue_swallowtail` | `#345F72` | 2 | あり |
| 8 | `sakura` | `#E5A8BE` | 5 | なし |
| 9 | `lavender` | `#AD98D7` | 4 | なし |
| 10 | `pearl` | `#EDE6DC` | 6 | なし |
| 11 | `sunset` | `#EE805E` | 7 | なし |
| 12 | `emerald` | `#39AF86` | 8 | あり |
| 13 | `obsidian` | `#32313F` | 6 | あり |
| 14 | `snow` | `#FFFFFF` | 0 | なし |
| 15 | `copper` | `#BF7242` | 1 | なし |
| 16 | `ruby` | `#BF4166` | 4 | なし |
| 17 | `sapphire` | `#4559CE` | 8 | あり |
| 18 | `amber` | `#DDAF38` | 9 | なし |
| 19 | `mint` | `#9ED9B5` | 3 | なし |
| 20 | `coral` | `#EC9192` | 6 | なし |
| 21 | `indigo` | `#595293` | 5 | なし |
| 22 | `lime` | `#B7CC52` | 2 | あり |
| 23 | `teal` | `#329FA7` | 7 | なし |
| 24 | `rose` | `#D181A7` | 8 | なし |
| 25 | `gold` | `#EBC55D` | 6 | あり |
| 26 | `silver` | `#AFBFCC` | 9 | なし |
| 27 | `chocolate` | `#78533E` | 4 | なし |
| 28 | `cream` | `#E9D8AB` | 3 | なし |
| 29 | `violet` | `#9565BD` | 2 | あり |
| 30 | `aqua` | `#6BDDE0` | 5 | なし |
| 31 | `scarlet` | `#D5523E` | 1 | なし |
| 32 | `forest` | `#526E46` | 9 | なし |
| 33 | `dusk` | `#9B83A5` | 7 | なし |
| 34 | `dawn` | `#F4C0A2` | 0 | なし |
| 35 | `ice` | `#AED9EE` | 6 | あり |
| 36 | `plum` | `#854A74` | 8 | なし |
| 37 | `olive` | `#959357` | 2 | なし |
| 38 | `peach` | `#F1B79B` | 3 | なし |
| 39 | `ocean` | `#326BAC` | 4 | なし |
| 40 | `magenta` | `#CA6BA9` | 7 | あり |
| 41 | `sand` | `#CBBB95` | 9 | なし |
| 42 | `charcoal` | `#56585D` | 5 | なし |
| 43 | `lilac` | `#D2B7E4` | 6 | なし |
| 44 | `cyan` | `#38BEE0` | 8 | なし |
| 45 | `crimson` | `#8E3043` | 2 | あり |
| 46 | `firefly_gold` | `#DBC057` | 5 | なし |
| 47 | `moonlight` | `#D6DCEB` | 4 | あり |
| 48 | `dreamwing` | `#F06428` | 10 | なし |

## 夢見鳥風の蝶

赤橙の羽・深紅の縁・金色の雫模様を持つプリセットです。Pattern 10で専用の羽形状になります。色の上書きも可能です。

```mcfunction
summon the_four_primitives_and_weapons:butterfly ~ ~1 ~ {Variant:"dreamwing"}
```

撮影用に位置を固定してゆっくり羽ばたかせる例：

```mcfunction
summon the_four_primitives_and_weapons:butterfly ~ ~1 ~ {Variant:"dreamwing",NoAI:1b,PersistenceRequired:1b,Size:1.5f,FlapSpeed:0.4f}
```

`bash sh/generate/generate_commands.sh` でもdreamwingを選択できます。通常の蝶として飛び、吸収・変身・戦闘能力は持ちません。
