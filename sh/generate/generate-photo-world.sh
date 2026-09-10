#!/usr/bin/env bash
# 撮影ワールドを新規作成。SBCL・gzip・zipが必要。
# Python不要。実装: lisp/generate/photo-world.lisp。
# bash sh/generate/generate-photo-world.sh --world "run/saves/新しい撮影ワールド"
# 実行ごとに新しいランダムシードで、丘・水路・植生・廃墟・剣の配置を生成します。
# 同じ景色を再現: bash sh/generate/generate-photo-world.sh --seed 12345 --world "run/saves/撮影-12345"
# --world省略時は日時・シード入りの新規保存先。既存保存先は上書きしません。
# --zip 出力.zip で配布先変更（既定: photography/blade-gallery-world.zip、更新されます）。
# 地形編集: lisp/generate/photo-terrain.lisp → 出力ワールドのdatapacks/blade_gallery/内のmcfunction。
# 入場後に自動で分割生成。/function blade_gallery:camera/flowers 等で各スポットへ移動。
# /function blade_gallery:random/setup は同じシードで再構築（対象4エリアの建築物も上書き）。
# 別の地形にするにはshを再実行。ゲーム内からLispを実行する仕組みではありません。
# シードは出力データパックのterrain-seed.txtに保存。編集元の既存mcfunctionは保持します。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
exec sbcl --script lisp/generate/photo-world.lisp "$@"
