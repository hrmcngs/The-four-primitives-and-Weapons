# [![Typing SVG](https://readme-typing-svg.demolab.com?font=Jacquard+12&size=60&pause=1000&color=513C90&background=C1FCFF00&center=true&vCenter=true&random=true&width=1000&height=115&lines=The-four-primitives-and-Weapons)](https://git.io/typing-svg)
***
## リポジトリ管理者
この作品は、今のところは、  
数人の学生によって作られています  
そのため全体的にクオリティが低いです  
改善点あったら教えていただけるとありがたいです  
後嫌なことがあると更新頻度が少し遅くなります  
***
## Apache-2.0 license

Apache-2.0 licenseですが僕に許可くれれば、発酵させてもいいし天日干しにしてもいいよでも使う時は  
userID乗っけてくれると嬉しいよ、程度です  
***
## my mod link

<a href="https://www.curseforge.com/minecraft/mc-mods/the-four-primitives-and-weapons/files" target="_blank">
  <img src="https://github.com/hrmcngs/The-four-primitives-and-Weapons/blob/0afbf5c653e9fd2ea29179ea99e93ef791a0462c/src/main/resources/logo.png" alt="The four primitives and Weapons mod logo" style="width:100px; height:auto;">
</a>

https://curseforge.com/minecraft/mc-mods/the-four-primitives-and-weapons

## Wiki

使い方・仕様・設定方法は[GitHub Wiki](https://github.com/Drowse-Lab/The-four-primitives-and-Weapons/wiki)に、未実装事項・確認待ちの作業は[Issues](https://github.com/Drowse-Lab/The-four-primitives-and-Weapons/issues)にまとめています。

- [レアリティシステム・ツールチップ枠・giveコマンド](https://github.com/Drowse-Lab/The-four-primitives-and-Weapons/wiki/Rarity-System)

***
## other
<img src="https://github-readme-stats-git-master-airopis-projects.vercel.app/api?username=hrmcngs&count_private=true&show_icons=true&locale=en&theme=dark&count_private=true&role=OWNER,ORGANIZATION_MEMBER,COLLABORATOR" />  
<img src="https://github-readme-stats-git-master-airopis-projects.vercel.app/api?username=aya526dev&count_private=true&show_icons=true&locale=en&theme=dark&count_private=true&role=OWNER,ORGANIZATION_MEMBER,COLLABORATOR" />  
<img src="https://github-readme-stats-git-master-airopis-projects.vercel.app/api?username=Renkon65&count_private=true&show_icons=true&locale=en&theme=dark&count_private=true&role=OWNER,ORGANIZATION_MEMBER,COLLABORATOR" />  

***
## 参考リンク
doragon Armorのtextureはここからダウンロードしました  

<a href="https://github.com/GitPois1x/DragonLoot/tree/1.20/src/main/resources/assets/dragonloot/textures" target="_blank">
  <img src="https://github.com/janosh/awesome-normalizing-flows/actions/workflows/link-check.yml/badge.svg" alt="Link Check status badge" style="width:150px; height:auto;">
</a>


dagger鞘をslot backとbeltに入れても3Dmodelが出ない
簡単にできるようにして自動で外部addonでも適応できるように

### 任意MODを選んでビルド・導入

`bash sh/menu.sh` のビルド一覧から「選択MOD＋必須の軽量化MODでビルド・導入」を選びます。
「ビルド」「導入のみ」「クライアント起動」を選択でき、JARの番号を空白区切りで複数指定します。
候補は `libs/external`・`libs/local`・`libs/runtime_selected` と入力したフォルダです。
TiCEXなどの **Forge 1.20.1版JARと必須依存MODも一緒に選択**してください。
自動ダウンロードや、必須依存の自動追加は行いません。

```bash
bash sh/build/build-with-mods.sh
# JARを直接指定（パスに空白がある場合も対応）
bash sh/build/build-with-mods.sh --jar "/path/TiCEX.jar" --jar "/path/TaCZ.jar" --offline
# 導入だけ行い、後で実行する起動コマンドを表示
bash sh/build/build-with-mods.sh --jar "/path/mod.jar" --prepare
```

選択ごとに `libs/interactive_<hash>/` を作り、元のJAR・既存の起動プロファイルは変更しません。
追加MODは開発環境の実行依存として読み込み、本体の配布JARには同梱しません。
ビルドでは選択MODの依存解決・リマップも確認しますが、起動互換性は `--run` で別途確認が必要です。
同じMOD IDの複数バージョン、本体・既存のCurios/JEIとの二重導入は拒否します。

TiCEX一式は `bash sh/build/build-with-mods.sh --ticex --offline` で選択できます。
現在の構成は TiCEX 0.7.0-hotfix（beta）、Tinkers’ Construct 3.11.2.166、
Mantle 1.11.117、AddonAPI-DynLoad 2.1.0 です。
JARと配布元・SHA-512の記録は `libs/external/ticex/` に保存しています（Git対象外）。
Tinkers’ Constructは、既存のJEI 15.20.0.129を満たす3.11系を選択しています。

選択式ビルドでは **Embeddium・Oculus・extra_video_settingsを必ず導入**します。
追加MODは選択したものだけです（本体のCurios・JEI依存は引き続き有効）。
軽量化JARが不足している場合はビルド前に停止します。
追加MODなしの場合は `bash sh/build/build-with-mods.sh --performance-only --offline` を使えます。
この操作ではシェーダーパックや既存のゲーム設定を書き換えません。

開発実行は `-PdevRunDirectory=/tmp/maw-test-profile` でゲームデータの保存先を分離できます。
通常は従来どおり `run/` を使います。`runServer` ではEmbeddium・Oculus・extra_video_settingsを読み込みません。
これらはクライアント専用であり、専用サーバーの `mods` フォルダへ入れないでください。

### TiCEXの刀身・鋳型

Tinkers’ Construct（とその必須依存MOD）がある場合、「刀身（TiCEX素材）」と「刀身用の金の鋳型」を登録します。

1. パーツビルダーで「刀身」を選び、対応素材3個分から最初の刀身を作ります。
2. 鋳造台に刀身を置き、溶融金90mBを流して金の鋳型を作ります。この刀身は消費されます。
3. 金の鋳型へ対応する溶融金属3素材分（通常の金属は270mB）を流して刀身を鋳造します。鋳型は再使用できます。
4. **作業台で刀身＋棒**を組み合わせると、その素材の攻撃力・耐久値を引き継いだ本MODの刀になります。

パーツビルダー・鋳造台のレシピと、鉄素材の刀身を使った組み立て例をJEIでも確認できます。
刀身・鋳型は本MODの武器クリエイティブタブにも表示します。Tinkers’ Construct未導入時には登録しません。
刀身＋棒の簡易組み立ては旧方式として利用できます。Tinkersの設備で組み立てる四部品の刀は次項を参照してください。

### 柄・鍔・頭・刀身から組み立てる刀

TiCEX連携の「四部品の刀」は、Tinkersの素材・特性・改造枠を持つ刀です。
各部品はパーツビルダーで個別に製作でき、金の鋳型を取れば鋳造台で金属製パーツも作れます。

| 部品 | 素材量 | 主な役割 |
| --- | --- | --- |
| 刀身 | 3素材分 | 刃の攻撃力・耐久値 |
| 柄 | 2素材分 | ハンドルの性能補正 |
| 鍔 | 1素材分 | 素材特性 |
| 頭 | 1素材分 | 素材特性 |

**組み立てはTinkersの金床（上位ツールステーション）で行います。**
普通のツールステーションは部品枠が3つのため、4部品を同時に入れられません。
設備の「四部品の刀」を選び、刀身・柄・鍔・頭を指定された枠へ入れてください。
部品ごとの素材情報と特性を引き継ぎ、本MODの刀の武器タイプとしても扱います。
各部品用の金の鋳型、パーツビルダー・鋳造・組み立てのレシピはJEIでも確認できます。

### TiCEX連携の部品表示と任意導入

刀身・柄・鍔・頭は完成した刀の形状を分離した3Dモデルで表示します。Tinkers’ ConstructとTiCEXは任意MODです。Tinkers’ Constructだけで、この連携の部品・鋳型・刀を登録します。TiCEXを追加すると、Tinkersの素材・溶融金属として登録されたTiCEXの金属も、それぞれの部品に適合する範囲で使用できます。未導入でも本体は使用できます。メニューで追加MODを選択するか、`--ticex`で連携一式、`--performance-only`で軽量化MODのみを選べます。追加MODを本体JARへ同梱しません。

鋳造レシピは鉄専用ではなく、Tinkers側に対応する部品素材と溶融金属が登録されている金属を使用できます。刀身270mB・柄180mB・鍔90mB・頭90mBが基本量です。金の鋳型は繰り返し使え、各部品で異なる素材も選べます。素材によって部品への適性や必要な鋳造設備が異なります。

TiCEX連携部品と四部品の刀は、`tinkering/materials`の素材色を使って表示します。完成品も刀身・柄・鍔・頭を個別に着色し、リソースパックの素材色変更は再読み込みで反映します。

柄（メイン部分）は木製に変更しました。作業台で板材を縦に2枚並べると木製の柄が1個でき、四部品の刀の組み立てに使用できます。柄のパーツビルダー・金属鋳造・金の鋳型製作レシピは廃止しました。既存ワールドの互換性のため旧鋳型のIDは残しますが、クリエイティブ一覧には表示しません。刀身・鍔・頭は従来どおり金属を選べます。

### 特定MODを選んで起動

`bash sh/menu.sh` →「ゲームを起動したい」→「特定MODを選んで起動（軽量化MODは必須）」で、任意のJAR・TiCEX一式・軽量化のみを選べます。任意JARは番号を空白区切りで複数選択してください。依存MODも選択が必要です。

直接使う場合は `bash sh/run/run-with-mods.sh` または `bash sh/run/run_client_mac.sh --select-mods`。既定はオフラインで、オンラインは `online` を指定します。`--ticex` でTiCEXと依存一式、`--performance-only` で軽量化のみを指定できます。Embeddium・Oculus・extra_video_settingsは常に追加され、選ばなかった任意MODはその起動構成へ入りません。

### 外部MODの金属・合金を鋳造する

Tinkersに素材・溶融金属が登録済みなら、その性能・特性をそのまま使います。
未登録金属については、導入MOD内の `tconstruct:casting_table` レシピから、
再使用できるインゴット鋳型と1インゴットの流体量を検出し、互換素材を自動生成します。
刀身は3インゴット分、鍔・頭は各1インゴット分です。柄は木製のままです。
未登録素材の初期性能は鉄相当（耐久250・素材攻撃力2）、色は銀色で、独自の素材特性は追加しません。
既存の合金製造・溶融レシピは変更しません。

自動検出対象はMODに含まれるインゴット鋳造レシピです。ワールドの追加データパックだけにあるレシピ、
特殊なレシピ形式、溶融流体のない金属は自動検出できません。
対応する流体がある金属は、ゲームフォルダの
`config/the_four_primitives_and_weapons/external_metals.json` に以下の配列形式で指定できます。
流体の `amount` はそのMODの1インゴット当たりのmBです。IDは実際に導入したMODのものへ置き換えてください。

```json
[
  {
    "name": "example:custom_alloy",
    "display_name": "Custom Alloy",
    "ingot": { "item": "example:custom_alloy_ingot" },
    "fluid": { "name": "example:molten_custom_alloy", "amount": 90 },
    "temperature": 800,
    "durability": 250,
    "attack": 2,
    "tier": 2,
    "color": "FFD8D8D8"
  }
]
```

`fluid` は `{"tag":"forge:molten_custom_alloy","amount":90}`、
`ingot` は `{"tag":"forge:ingots/custom_alloy"}` の形式も使えます。
設定はゲーム／サーバーの再起動で反映されます。マルチプレイではサーバーとクライアントに同じ設定を配置してください。
この設定だけで新しい溶融流体や合金レシピを作ることはできません。

### Tetra連携（バージョン7）

Tetra 6系（Forge 1.20.1）とその必須依存MODを別途導入すると、以下を利用できます。
Tetraは任意導入で、配布JARには同梱しません。

- 木・石・鉄・金・ダイヤモンド・ネザライトの刀をTetraの作業台でモジュール式の剣に改造し、部品交換・強化できます。元の基本素材に対応する初期部品を設定します。
- 改造後の外観・素材性能・改造方法はTetraの仕様になります。本MODの属性・武器成長・名前と耐久消耗率を引き継ぎ、エンチャントはTetra標準の移行処理を使用します。元の固定攻撃力や素材NBTは移さず、交換したTetra部品の性能を使用します。
- Tetraの剣・短い刃・重い刃は、それぞれ本MODの剣・短剣・大剣のスキルに対応します。本MODの刀から改造した基本の刃は刀スキルを使用します。
- Tetraの盾・弓・クロスボウも対応するスキル体系を使用します。攻撃力・耐久値・攻撃速度はTetra側の計算を維持します。

Tinkersの四部品の刀や専用能力を持つ特殊な刀は、Tetraへの変換対象に含みません。
Tetra部品の能力が本MODのすべての独自スキルで発動することまでは保証しません。
公式の変換データ形式は [Tetra 1.20公式ソース](https://github.com/mickelus/tetra/tree/1.20/src/main/resources/data/tetra/replacements) を参照しています。

### Tetraをオフラインで起動する

`bash sh/menu.sh` のMOD選択に「Tetra＋必須依存（オフライン対応）」を追加しました。
ローカルの `libs/external/tetra/` からTetraとmutilを選びます。自動ダウンロードは行いません。

```bash
bash sh/run/run-with-mods.sh --tetra
# オフラインでビルド
bash sh/build/build-with-mods.sh --tetra --offline
# 導入のみ（起動コマンドを表示）
bash sh/build/build-with-mods.sh --tetra --prepare --offline
```

必要なファイルは `tetra-1.20.1-6.17.0.jar` と `mutil-1.20.1-6.3.0.jar` です。
Tetra本体は指定されたDownloadsのJARをコピー済みです。
現時点ではmutilの取得が回線側で拒否されたため、mutilを上記フォルダへ配置する必要があります。
不足時は起動前にエラーを表示します。Minecraft本体のアセットとGradle依存も事前にキャッシュされている必要があります。

### TetraとTinkersを一緒にビルド・起動する

MOD選択メニューの「Tetra＋Tinkers＋必須依存」で、Tetra・mutil・Tinkers’ Construct・Mantleを同時に選択できます。
TiCEXも追加する場合は「Tetra＋TiCEX＋Tinkers＋必須依存」を選びます。

```bash
# Tetra＋Tinkers（TiCEXなし）でオフラインビルド
bash sh/build/build-with-mods.sh --tetra --tinkers --offline
# TiCEXも含める
bash sh/build/build-with-mods.sh --tetra --ticex --offline
# Tetra＋Tinkersで起動（既定でオフライン）
bash sh/run/run-with-mods.sh --tetra --tinkers
# Tinkers＋Mantleだけ選択することも可能
bash sh/build/build-with-mods.sh --tinkers --offline
```

mutilが未配置の場合は、前項の手順で `libs/external/tetra/` に配置してください。
これらのMODは実行依存として同時に検証し、本体の配布JARには同梱しません。
