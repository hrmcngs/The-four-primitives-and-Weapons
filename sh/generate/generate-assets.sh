#!/usr/bin/env bash
# 拵えテクスチャ・ノイズフォント生成。SBCLとJava 17が必要。
# bash sh/generate/generate-assets.sh fit 素材.png tuka 出力名 --color E8A6C4
# fit: --weapon katana|tyokuto|rapier / --mode recolor|sample / --color RRGGBB / --output PNG
# 部位: tuka/tuba/kasira、rapierはgrip/guard/pommel。
# noise: bash sh/generate/generate-assets.sh noise [--output PNG] [--font TTF]
# 加工処理はLisp、PNG入出力と文字の描画のみJava標準機能を使用します。
# Python不要。実装: lisp/assets/generate.lisp、lisp/assets/images.lisp。
# 標準フォントはJavaの等幅フォント。旧版とノイズ配置・字形は完全一致しませんが、256x96・95文字の配置は維持します。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
exec sbcl --script lisp/assets/generate.lisp "$@"
