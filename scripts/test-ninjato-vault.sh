#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
vault_test_dir=$(mktemp -d)
trap 'rm -rf "$vault_test_dir"' EXIT
javac -d "$vault_test_dir" \
  src/main/java/the_four_primitives_and_weapons/util/NinjatoVaultGesture.java \
  tests/NinjatoVaultGestureTest.java
java -cp "$vault_test_dir" NinjatoVaultGestureTest
