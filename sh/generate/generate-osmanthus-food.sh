#!/usr/bin/env bash
# 金木犀料理の素材・レシピを再生成。SBCL・Java 17・unzip、Minecraft 1.20.1のclient.jarが必要。
# bash sh/generate/generate-osmanthus-food.sh [--vanilla-jar /path/to/client.jar]
# 既定はForgeGradleのローカルキャッシュ。バニラの蜂蜜瓶・ケーキ・クッキー・タンポポを色調整します。
# 花付き金木犀の葉を空手で右クリックすると花1〜3個。花なし葉へ骨粉を使うと再開花します。
# シロップ1本: 金木犀の花3個＋砂糖2個＋ガラス瓶1個＋水入りバケツ1個（空バケツは戻る）。
# ケーキ1個: バニラのケーキ1個＋金木犀シロップ1本（空き瓶は戻る）。置いて7切れ食べられます。
# クッキー8個: 小麦2個＋金木犀シロップ1本（空き瓶は戻る）。すべて不定形レシピ。
# /give @s the_four_primitives_and_weapons:kinmokusei_syrup
# /give @s the_four_primitives_and_weapons:kinmokusei_cake
# /give @s the_four_primitives_and_weapons:kinmokusei_cookie
# 金木犀シロップを飲んだ場合も、サバイバルでは空き瓶が戻ります。
# 出力はsrc/main/resourcesの料理用テクスチャ・モデル・レシピ・翻訳などです。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
exec sbcl --script lisp/assets/osmanthus-food.lisp "$@"
