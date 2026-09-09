#!/usr/bin/env bash
# 撮影ワールドを新規作成。SBCL・gzip・zipが必要。
# Python不要。実装: lisp/generate/photo-world.lisp。
# bash sh/generate/generate-photo-world.sh --world "run/saves/新しい撮影ワールド"
# 既存保存先は上書きしません。景観の編集元はphotography/blade_gallery内のmcfunctionです。配布ZIPも更新します。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
exec sbcl --script lisp/generate/photo-world.lisp "$@"
