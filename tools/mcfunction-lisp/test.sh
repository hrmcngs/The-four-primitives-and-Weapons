#!/usr/bin/env bash
# 汎用mcfunctionブリッジのテスト。SBCLが必要。出力は一時フォルダだけに作成。
# bash /path/to/mcfunction-lisp/test.sh
set -euo pipefail
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
bridge_dir=$(cd "$(dirname "$0")" && pwd)
bridge_test_dir=$(mktemp -d)
trap 'rm -rf "$bridge_test_dir"' EXIT
sbcl --script "$bridge_dir/tests.lisp" "$bridge_test_dir/"
