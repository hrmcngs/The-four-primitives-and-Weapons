#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
gate_test_dir=$(mktemp -d)
trap 'rm -rf "$gate_test_dir"' EXIT
javac -d "$gate_test_dir" \
  src/main/java/the_four_primitives_and_weapons/util/GateDropContext.java \
  tests/GateDropContextTest.java
java -cp "$gate_test_dir" GateDropContextTest
