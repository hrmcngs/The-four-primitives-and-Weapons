#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
luna_test_dir=$(mktemp -d)
trap 'rm -rf "$luna_test_dir"' EXIT
javac -encoding UTF-8 -d "$luna_test_dir" \
  src/main/java/the_four_primitives_and_weapons/ai/lisp/LispInterpreter.java \
  src/main/java/the_four_primitives_and_weapons/util/LunaBehaviorScript.java \
  tests/LunaBehaviorScriptTest.java
java -cp "$luna_test_dir" LunaBehaviorScriptTest
