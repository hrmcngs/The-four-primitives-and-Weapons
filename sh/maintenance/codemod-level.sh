#!/usr/bin/env bash
# Javaの .level を .level() に移行。SBCLが必要。
# Python不要。実装: lisp/maintenance/codemod-level.lisp。
# bash sh/maintenance/codemod-level.sh [対象ディレクトリ] [--apply]
# 既定は候補だけ表示。--applyで更新し、元ファイルを.java.bakに保存します。
# コメント・文字列は変更せず、既存バックアップは上書きしません。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
exec sbcl --script lisp/maintenance/codemod-level.lisp "$@"
