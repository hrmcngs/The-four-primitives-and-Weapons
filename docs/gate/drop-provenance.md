# Gateのドロップ理由とUUID

Lunaにも同じ判定を適用。[Lunaの保存先と召喚・回収動作](luna-drop-provenance.md) を参照。

UUIDは識別子であり、単独では死亡・操作・コンテナ排出を判定できない。
サーバー上の実際の呼び出し経路を、使用者UUIDに紐づく同期コンテキストで記録する。
タイマーや最寄りプレイヤーから理由・所有者を推測しない。対象は通常のGate。

| 経路 | Reason | 動作 |
| --- | --- | --- |
| 画面を開いていない状態のQ / Ctrl+Q | `DROP_KEY` | 生存中・接続中・非スペクテイターなら元スロットへ回収して召喚 |
| Shift+Q | `SNEAK_DROP_KEY` | 通常ドロップ |
| インベントリ/コンテナ画面のQ・画面外クリック・交換時排出 | `INVENTORY` | 通常ドロップ |
| カーソルや作業枠の後片付け時の排出 | `MENU_CLEANUP` | 通常ドロップ |
| 所持品へ戻せず満杯で排出 | `INVENTORY_OVERFLOW` | 通常ドロップ |
| 死亡時の所持品・装備・作業枠の排出 | `DEATH` | Minecraft/Forgeの死亡ドロップ処理に従う |
| 切断時のカーソル・作業枠の排出 | `DISCONNECT` | 通常排出。通常の所持品はMinecraftが保存 |
| チェスト等からContainers経由で排出 | `CONTAINER` | 通常ドロップ |
| ディスペンサー/ドロッパーからアイテムとして排出 | `DISPENSER` | 通常ドロップ |
| Block.popResource経由の排出 | `BLOCK` | 通常ドロップ |
| その他のPlayer.drop経由の投棄 | `PLAYER_TOSS_OTHER` | 通常ドロップ |
| コマンド・他MOD独自経路・旧セーブ等、理由が不明な生成 | `UNKNOWN` | 通常ドロップ。召喚しない |

死亡時はkeepInventory、消滅の呪い、墓MOD等のForgeイベントキャンセルを上書きしない。
死亡ドロップを新規作成・回収せず、実際に出るGateへ理由を付けるだけ。
モブが装備していたGateの死亡ドロップも、そのモブのUUIDでDEATHとして記録する。

Q召喚はForgeの投棄イベントとワールド追加が正常に終わってから行う。イベントがキャンセルされた場合は複製・補填しない。
元の選択スロットが変わった/埋まった場合は上書きせず、通常ドロップにする。
回収時はドロップの実際のItemStackを使うため、名前・エンチャント・NBTを保持する。
クールダウン中は回収だけ行う。UUIDコンテキストはfinallyで解除するので、死亡・切断・例外後に発動権限が残らない。

## 記録の取得

ドロップしたItemEntityの永続データ `the_four_primitives_and_weapons:gate_drop` に以下を保存。

- `ItemEntityUUID`: このドロップのUUID
- `ActorUUID`: 発生元のプレイヤー/死亡したエンティティのUUID。発生元不明・ブロック系では記録しない
- `Reason`: 上表の理由
- `Dimension`, `GameTime`: 発生ディメンションとゲーム内時刻
- `Outcome`: `DROPPED` / `SUMMONED` / `RETURNED_COOLDOWN` / `DROPPED_SLOT_CHANGED`
- `DamageSource`, `AttackerUUID`: LivingDropsEventの死亡原因と攻撃者。攻撃者がいない場合は省略

回収してItemEntityを消した場合も、プレイヤーの永続データ
`the_four_primitives_and_weapons:gate_last_drop` に最新1件を保持。リスポーン時にもコピーする。
アイテムのItemStackへ発動用タグを埋め込まないため、拾い直して再投棄した場合は新たな理由になる。
保存済みItemEntityを再ロードした場合は記録を維持し、自動発動しない。

Javaからは `GateDropHandler.findByUUID(serverLevel, itemEntityUUID)` で記録のコピーを取得できる。
対象レベルのロード済みItemEntity、またはオンラインプレイヤーの最新回収記録を検索する。
アンロード中のエンティティ・オフラインプレイヤー・過去全件の履歴を検索するAPIではない。
通常の保存データには記録が残るが、未知の他MODの排出理由をUUIDだけから復元することはしない。

## 検証

`bash sh/test/test-gate-drop.sh` でUUID分離、入れ子、例外時解除、スレッド分離、死亡/切断/スペクテイター/各通常排出での召喚禁止を確認。
Forgeコンパイル・リソース生成・refmap生成が成功。Minecraftのコンパイル済みクラスをjavapで調べ、対象8メソッドの11呼び出し箇所も照合した。
サーバーの起動試験はEULA確認段階で停止したため、Mixinの実適用とゲーム内の死亡・墓MOD連携・各画面操作は未確認。
