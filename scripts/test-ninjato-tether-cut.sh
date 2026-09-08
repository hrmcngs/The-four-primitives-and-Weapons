#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
tether_test_dir=$(mktemp -d)
trap 'rm -rf "$tether_test_dir"' EXIT
javac -d "$tether_test_dir" \
  src/main/java/the_four_primitives_and_weapons/util/NinjatoTetherCutRule.java \
  tests/NinjatoTetherCutRuleTest.java
java -cp "$tether_test_dir" NinjatoTetherCutRuleTest
