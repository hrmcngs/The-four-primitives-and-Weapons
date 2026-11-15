#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
fittings_test_dir=$(mktemp -d)
trap 'rm -rf "$fittings_test_dir"' EXIT
javac -d "$fittings_test_dir" \
    src/main/java/the_four_primitives_and_weapons/util/TsubaOutline.java \
    src/main/java/the_four_primitives_and_weapons/util/TsukaPattern.java \
    tests/TsubaOutlineTest.java tests/TsukaPatternTest.java
java -cp "$fittings_test_dir" TsubaOutlineTest
java -cp "$fittings_test_dir" TsukaPatternTest
