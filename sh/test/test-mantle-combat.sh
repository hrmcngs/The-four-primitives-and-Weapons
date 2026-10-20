#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
mantle_test_dir=$(mktemp -d)
trap 'rm -rf "$mantle_test_dir"' EXIT
javac -d "$mantle_test_dir" src/main/java/the_four_primitives_and_weapons/util/MantleCombatRules.java tests/MantleCombatRulesTest.java
java -cp "$mantle_test_dir" MantleCombatRulesTest
