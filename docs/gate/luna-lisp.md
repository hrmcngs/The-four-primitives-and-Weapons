# LunaのLisp設定

編集元は `src/main/resources/data/the_four_primitives_and_weapons/luna/formula.lisp`。
初期値は従来の護衛Lunaに合わせている。通常の突き攻撃ではなく、召喚中の護衛の追従・索敵・レーザー・属性計算を調整する。

## ゲーム内で反映する

ワールドの `datapacks/luna-tuning/` に次の2ファイルを置く。

- `pack.mcmeta`
- `data/the_four_primitives_and_weapons/luna/formula.lisp`（上記の編集元をコピー）

Minecraft 1.20.1の `pack.mcmeta`:

```json
{"pack":{"pack_format":15,"description":"Luna behavior tuning"}}
```

ファイルを編集して `/reload`。サーバー側の設定として適用され、召喚済みLunaも次の行動処理から新設定を参照する。
進行中の発射クールダウンや現在の演出カウンタはリセットしない。
プロジェクト内のソースファイルだけを変更した場合は、ビルドとゲーム再起動が必要。再ビルドなしで調整するときはワールドのデータパックを編集する。

## 数値設定

`define` は読み込み時に評価して保持する。

| 設定 | 初期値 | 意味 |
| --- | --- | --- |
| `anchor-side`, `anchor-height` | 1.5 / 1.4 | 主人からの横距離・高さ。横を負にすると左側 |
| `idle-speed`, `combat-speed` | 0.22 / 0.32 | 待機時・攻撃時の追従速度、ブロック/tick |
| `stop-distance`, `teleport-distance` | 0.08 / 32 | 到着と判定する距離・主人の近くへワープする距離 |
| `scan-interval`, `scan-range` | 10 / 12 | 索敵間隔tick・主人を狙う敵を検索する範囲 |
| `target-range` | 24 | 主人からの最大対象距離、およびLunaからの射程 |
| `anchor-tolerance` | 2 | 発射時、待機位置から許容する距離 |
| `rotation-blend`, `aim-tolerance` | 0.22 / 7 | 照準を追従させる割合・許容角度（度） |
| `laser-damage` | 8 | 基本ダメージ |
| `min-cooldown`, `cooldown-extra` | 10 / 5 | 最短発射間隔・主人の攻撃回復時間に加えるtick |
| `firing-ticks` | 7 | 発射姿勢の演出時間 |
| `particle-interval` | 5 | 周囲の粒子を出す間隔。0で停止 |
| `laser-step` | 0.2 | レーザーを描く粒子の間隔（ブロック） |

初期のクールダウン式は `max(10, ceil(主人の攻撃回復時間) + 5)`。
例として追従速度を上げる場合は、既存の行を `(define idle-speed 0.3)` に置き換える。

## 行動と計算式

`rule` は式を読み込み時に解析し、判断が必要なときに最新の値で評価する。

| ルール | 返す値 |
| --- | --- |
| `target-priority` | 対象の優先度。正の値で最大の候補を選び、同点なら近い相手。0以下は対象外 |
| `attack-enabled` | trueで攻撃、falseで主人の横に待機 |
| `damage` | レーザーのダメージ |
| `cooldown` | 発射後の待ち時間tick。小数は切り上げ |
| `prefer-luna-element` | 属性が異なる場合、trueでLuna自身、falseで主人の属性本を優先 |
| `combined-element-level` | Lunaと本が同じ属性の場合の合成レベル。小数は切り捨て |

使える入力:

- `owner-health-ratio`: 主人の残り体力/最大体力
- `owner-attack-delay`: 主人の現在の攻撃回復時間tick
- `target-distance`: Lunaから候補/攻撃対象までの距離
- `owner-attacked-target`: 主人が最後に攻撃した対象か
- `target-attacked-owner`: 主人を最後に攻撃した相手か
- `luna-element-level`, `book-element-level`: Luna自身と主人の属性本のレベル
- 上表の数値設定名

主人の体力が30%未満なら、主人を攻撃した相手を優先する例:

```lisp
(rule target-priority
  (if (< owner-health-ratio 0.3)
      (if target-attacked-owner 9 1)
      (if owner-attacked-target 3 (if target-attacked-owner 2 1))))
```

距離6未満でだけ攻撃する例:

```lisp
(rule attack-enabled (< target-distance 6))
```

同属性のレベルを単純加算する例:

```lisp
(rule combined-element-level (+ luna-element-level book-element-level))
```

例は既存の同名ルールを置き換えて使う。重複定義は読み込みエラーになる。
対象候補はJava側が集める「主人が攻撃した相手・主人を攻撃した相手・範囲内で主人を狙う敵」。生存・射程・自己攻撃除外はJava側で確認する。

## 対応構文と失敗時の扱い

既存の軽量Lispインタプリタを使う。数値、true/false、`if`、`and`、`or`、`not`、比較、四則演算、`min`、`max`、`abs`、`ceil`、`floor` に対応。
四則演算とmin/maxは2引数。これはゲーム内計算用の構文で、SBCLの任意コードや `defun` は実行しない。

括弧の誤り、未定義名、未対応関数、数値設定の範囲外、tick設定の小数は読み込みエラーとしてログに出し、直前の正常な設定を維持する。
計算ルールが実行時に型違い・非有限値・範囲外の結果を返した場合は、その計算だけ既定の結果へ戻す。警告はルールごとに1回。
省略した設定・ルールは既定値になり、前のデータパック設定は残らない。

UUID照合、死亡ドロップ、アイテム消費・返却、エンティティ生成はJavaが担当する。Lispから所持品やUUIDを書き換える機能は提供していない。

## 検証

`bash sh/test/test-luna-lisp.sh` で初期値、属性合成441通り、体力/距離による条件分岐、設定の差し替え、不正入力と計算結果のフォールバックを確認。
Javaコンパイルとリソース生成も成功。ゲーム内での実際の追従・戦闘・ `/reload` 操作は未確認。
