#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
conduction_test_dir=$(mktemp -d)
trap 'rm -rf "$conduction_test_dir"' EXIT
javac -d "$conduction_test_dir" \
  src/main/java/the_four_primitives_and_weapons/util/BoundedConduction.java \
  tests/BoundedConductionTest.java
java -cp "$conduction_test_dir" BoundedConductionTest
