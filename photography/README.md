# 刀の庭園 — 紹介画像用

Minecraft Java 1.20.1 / Forge、本MODを入れた環境用の撮影ワールドです。
シングルプレイから「刀の庭園 — 紹介画像用」を開くと、初回に庭園を生成します。
生成には数秒かかります。初期状態はクリエイティブ・ピースフル・夕暮れ固定です。
既存の「武器紹介撮影ワールド」とは別のセーブです。

水鏡を渡る石橋、その先の刀の祭壇、背景の崩れた回廊とアーチ、右側の刀の丘、
水辺のシアン・紫の侵食結晶を配置しています。装飾刀は回収すると消えます。

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
再生成用スクリプトは `python3 tools/create_photo_world.py --world <新しい保存先>` です。
既存の保存先は上書きしません。Python標準ライブラリだけで動作します。
