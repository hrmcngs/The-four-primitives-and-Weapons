# Lunaのドロップ理由とUUID

Lunaにも [Gateの判定](drop-provenance.md) を共有する。通常Q / Ctrl+Qだけが護衛召喚の操作となる。
Shift投棄、インベントリ画面からの投棄、死亡、切断、満杯による排出、コンテナ・ディスペンサー・ブロックからの排出、理由不明の生成は通常のアイテムとして扱う。

従来のItemTossEventから直接召喚する処理は廃止。投棄イベントとアイテムのワールド追加が成功してから、使用者UUID・ドロップUUID・理由・生存/接続状態を照合する。
護衛のワールド追加が成功した場合だけLunaを1個消費する。追加がキャンセルされた場合は元のドロップを残す。
名前・エンチャント等のNBTは護衛へ引き継ぐ。通常はスタックしないが、コマンドで複数個にした場合も余剰分を削除しない。

Lunaを右クリックで回収した際、所持品が満杯でこぼれた場合の理由は `COMPANION_RETURN`。回収から再召喚するループを防ぐ。
死亡直前に召喚中のLunaを回収する既存処理も維持し、所持品へ戻ったLunaはkeepInventory・消滅の呪い・墓MOD等の死亡処理に従う。
所有者不在時など、発生経路を特定できない生成は `UNKNOWN` として残し、自動召喚しない。

## 保存と取得

- ドロップ側: `the_four_primitives_and_weapons:luna_drop`
- プレイヤーの最新1件: `the_four_primitives_and_weapons:luna_last_drop`。リスポーンにも引き継ぐ
- `ActorUUID`, `ItemEntityUUID`, `Reason`, `Dimension`, `GameTime`, `Outcome` はGateと同じ形式
- 召喚成功時は `SummonedEntityUUID` に護衛のUUIDを保存
- 召喚失敗時は `Outcome=DROPPED_SUMMON_FAILED`。死亡イベントでは死亡原因・攻撃者UUIDも記録

`GateDropHandler.findByUUID(serverLevel, itemEntityUUID)` はLunaにも対応。GateとLunaの最新記録は別々に保持する。
対象レベルでロード済みのドロップ、またはオンラインプレイヤーの最新記録を取得する範囲はGateと同じ。

共有判定のテストは `bash sh/test/test-gate-drop.sh`。ゲーム内の召喚・死亡・他MODによる召喚キャンセルは未確認。
