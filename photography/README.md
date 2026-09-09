# 刀の庭園 — 紹介画像用

Minecraft Java 1.20.1 / Forge、本MODを入れた環境用の撮影ワールドです。
シングルプレイから「刀の庭園 — 紹介画像用」を開くと、初回に庭園を生成します。
生成には数秒かかります。初期状態はクリエイティブ・ピースフル・夕暮れ固定です。
既存の「武器紹介撮影ワールド」とは別のセーブです。

水鏡を渡る石橋、その先の刀の祭壇、背景の崩れた回廊とアーチ、右側の刀の丘、
水辺のシアン・紫の侵食結晶を配置しています。装飾刀は回収すると消えます。

追加の4エリアは庭園の東側にあります。既存ワールドでは `/reload` で未生成の追加エリアを順に生成します。
各エリアの完成メッセージが出てから移動してください。

| 移動コマンド | 景観 |
| --- | --- |
| `/function blade_gallery:camera/rubble` | 128×160の水没廃墟。奥と左右に崩壊ビル10棟、倒れた柱と瓦礫 |
| `/function blade_gallery:camera/burned` | 焼け落ちた家、煙、小さな炎、灰に横たわる刀 |
| `/function blade_gallery:camera/battlefield` | 砲撃跡、黒旗、鉄の剣・直刀・刀37本。一部は倒れた状態 |
| `/function blade_gallery:camera/flowers` | 白・桃・青・黄の花畑、桜、苔むした台座のmagical_katana。はばきは桜色（#E8B4C8） |

花畑は `/function blade_gallery:light/day`、焼け跡と戦場は `light/sunset` または `light/night` で撮影できます。
時刻変更は全エリアに反映されます。炎は延焼しない設定です。
追加エリアを再生成する場合は `/function blade_gallery:scenes/setup` を実行してください。
瓦礫の再生成範囲はX 64〜191、Z -112〜47、Y 60〜148です。
他の追加エリアはY 60〜100、Z -32〜31、Xは焼け跡224〜287・戦場352〜415・花畑480〜543です。
その範囲の建築は置き換わります。既存の中央庭園はこのコマンドでは再生成しません。

`/reload` で未適用の更新があるエリアを再生成します（各更新は一度だけ）。
現在の装飾更新は中央庭園と4つの追加エリアを再生成します。自分で加えた建築がある場合は再生成前にバックアップしてください。
花畑は高さ約8ブロックの丘、戦場は約9ブロックの起伏と深さ2ブロックの曲がった塹壕、
焼け跡は段差のある斜面、瓦礫は水没地を囲む崩落した山と土手になっています。
花・刀・桜・撮影位置も地形に追従し、水面とビル・家の基礎は水平にしています。

花畑は桜を計9本に増やし、花の咲く低木、落ちた花びら、苔岩、木の腰掛け、石灯りを加えています。
桜は中央庭園も含め、横に広がる枝と傘状の樹冠から花葉が3〜5ブロックほど垂れる枝垂れ桜風です。
垂れた葉の先端は地形に合わせて高さを調整しています。
枝垂れ桜への更新は `/reload` で中央庭園と花畑を再生成します。
中央庭園にも低木・桜・水面の葉・苔岩を配置しました。
瓦礫には鉄筋や排水管の残骸と水面の葉、焼け跡には焦げた木や樽・板材、
戦場には枯れ木、物資の残骸、塹壕の足場や崩れた土留めを追加しています。
新しい装飾は地形の高さに合わせて配置されます。
個別にやり直す場合は `/function blade_gallery:scenes/rubble/setup` または
`/function blade_gallery:scenes/battlefield/setup` を使ってください。
瓦礫の撮影には描画距離12チャンク程度を目安に、背景のビルが見える距離へ調整してください。
戦場の刀・直刀は `TsukaColor`・`TsubaColor`・`KashiraColor`・`HabakiColor` で灰茶・灰緑の拵えにしています。
刀身は元の鉄系テクスチャです。通常の鉄の剣はバニラの銀灰色の刀身と茶色の柄を使います。

| コマンド | 用途 |
| --- | --- |
| `/function blade_gallery:camera/main` | 全景。祭壇と廃神殿を水辺から撮影 |
| `/function blade_gallery:camera/water` | 水面に近い低い視点 |
| `/function blade_gallery:camera/detail` | 主役の刀を接写 |
| `/function blade_gallery:camera/hill` | 刀が並ぶ丘 |
| `/function blade_gallery:light/sunset` | 夕暮れ |
| `/function blade_gallery:light/day` | 日中。刀の色や形の確認 |
| `/function blade_gallery:light/night` | 夜。結晶と灯りの撮影 |
| `/function blade_gallery:reset_weapons` | 撮影用の刀だけ再配置。近くで実行 |
| `/function blade_gallery:setup` | 建築を含めて庭園を再生成 |

F1でHUDを隠し、F2で撮影します。FOVは50〜65程度から調整すると刀を見せやすいです。
シェーダーはゲーム内の設定から選択してください。このワールドはシェーダー設定を変更しません。
刀を自分の武器に替える場合は装飾刀を回収し、battle_stakeで設置します。
`/weaponedit` で設置した刀を編集できます。

**setupはオーバーワールドのX/Z -48〜47、Y 60〜100を作り直します。**
付属の専用ワールドで使ってください。自分で配置した刀や建築の保持が必要な場合は再生成前にバックアップしてください。
導入したプレイヤーは初回にクリエイティブへ切り替わり、撮影位置へ移動します。

配布用 `blade-gallery-world.zip` は展開して中のワールドフォルダをMinecraftの `saves` に入れます。
`blade_gallery/` が編集可能なデータパックです。変更後は対象ワールドのdatapacksにもコピーし、
`/reload` → `/function blade_gallery:setup` を実行してください。
書き出しは `bash sh/generate/generate-photo-world.sh --world <新しい保存先>` です。詳しい使い方はsh内にあります。
既存の保存先は上書きしません。Common Lisp（SBCL）とgzip・zipで動作します。景観の編集元は `blade_gallery/` 内のmcfunctionで、Pythonからの再生成は不要です。
