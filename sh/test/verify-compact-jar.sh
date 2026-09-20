#!/usr/bin/env bash
# SBCLとunzipで圧縮前後のJARを比較します。引数: 圧縮前.jar 圧縮後.jar
set -euo pipefail
cd "$(dirname "$0")/../.."
exec sbcl --script lisp/test/verify-compact-jar.lisp "$@"
