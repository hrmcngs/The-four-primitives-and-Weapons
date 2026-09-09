#!/usr/bin/env bash
# Common Lisp製コマンド生成・JSON・コードロックのテスト。
# 使い方: bash sh/test/test-lisp-tools.sh
# SBCLが必要。作業用ワークスペースは/tmpに作り、本物のMCreator設定は変更しません。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) sed -n '2,4p' "$0" | sed 's/^# //'; exit 0;; esac
exec sbcl --script lisp/test/tools.lisp --test
