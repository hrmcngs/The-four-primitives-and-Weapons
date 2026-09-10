#!/usr/bin/env bash
# 花木4種類の樹形テスト。Java 17が必要。
# bash sh/test/test-flowering-trees.sh
# 400本の枝の接続・葉の支持距離・再現性・ブロック数上限を確認します。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
tree_test_dir=$(mktemp -d)
trap 'rm -rf "$tree_test_dir"' EXIT
javac -d "$tree_test_dir" src/main/java/the_four_primitives_and_weapons/world/tree/FloweringTreeShape.java tests/FloweringTreeShapeTest.java
java -cp "$tree_test_dir" FloweringTreeShapeTest
