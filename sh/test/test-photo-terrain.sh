#!/usr/bin/env bash
# Lisp→mcfunction地形生成を検証。SBCLが必要です。
# bash sh/test/test-photo-terrain.sh
# 同じシードの再現性、異なるシードの差、地形の高さ・傾斜を確認します。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
exec sbcl --script lisp/test/photo-terrain.lisp
