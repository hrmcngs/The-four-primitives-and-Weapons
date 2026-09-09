#!/usr/bin/env bash
# Lispへ移植した画像加工・PNG入出力・Java修正ツールを検証します。
# 使い方: bash sh/test/test-lisp-migration.sh
# SBCLとJava 17が必要。PNGの作業ファイルは/tmpだけに作成します。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) sed -n '2,4p' "$0" | sed 's/^# //'; exit 0;; esac
exec sbcl --script lisp/test/migration.lisp --test
