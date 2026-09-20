#!/usr/bin/env bash
# SBCLとffmpegで天候の環境音を生成。第1引数で出力ディレクトリを指定できます。
set -euo pipefail
cd "$(dirname "$0")/../.."
exec sbcl --script lisp/generate/generate-weather-sounds.lisp "$@"
