# 錫杖・仕込み錫杖の持ち手設定

通常の錫杖と納刀中の仕込み錫杖は、片手で握り、環のある側を上に向ける。
左右の利き手とオフハンドに対応。使用中・ガード中などは既存のポーズを優先する。

モデルは軸を Y 方向に伸ばし、環を +Y 側に置く。握らせたい位置を
Blockbench の `(8, 8, 8)` に合わせる。長さに応じた最終調整はモデル追加後に行う。

通常の錫杖のモデルには次の親を指定する。

```json
"parent": "the_four_primitives_and_weapons:custom/weapon/shakujo/shakujo_parent"
```

納刀中の仕込み錫杖には次の親を指定する。

```json
"parent": "the_four_primitives_and_weapons:custom/saya/shakujo/shikomi_shakujo_parent"
```

子モデルに同じ `display` 項目があると親の持ち手設定を上書きするため、
親の設定を使う場合は子の手持ち用 `display` を削除する。

腕のポーズは `data/the_four_primitives_and_weapons/tags/items/shakujo_hold.json`
で指定する。仮のアイテム ID は `shakujo` と `shikomi_shakujo`。
未登録でもエラーにならない設定なので、アイテム追加時に実際の ID に合わせる。
抜いた刀にはこのタグを付けず、刀用のモデルと持ち方を使う。

三人称の腕の角度 −35° とモデルの X 回転 55° を組み合わせて軸を立てている。
握る高さだけ変えるときは、腕の角度ではなくモデルの握り位置を調整する。
この設定はモデルとアイテムの追加準備であり、本体・抜刀機能・レシピはまだ登録しない。

## 錫杖刀の分類

錫杖から抜いた刀のアイテム ID は `the_four_primitives_and_weapons:shakujo_blade` とし、
`weapon_types/weapon_types.json` の `straight_sword`（直刀）に登録している。
通常の錫杖 `shakujo`、納刀中の `shikomi_shakujo` とは別のアイテムとして追加する。
抜刀後は直刀のスキル分類を使い、錫杖の持ち手タグは付けない。
アイテムクラスは `ShakujoBladeItem` などにする（既存の直刀判定は
クラス名に `Katana` を含むものを曲刀として除外する）。
