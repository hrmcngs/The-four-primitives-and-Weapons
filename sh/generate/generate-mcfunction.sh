#!/usr/bin/env bash
# 汎用Lisp→mcfunctionツール。SBCLが必要。MOD・Minecraftのインストールは生成時に不要。
# bash sh/generate/generate-mcfunction.sh --source tools/mcfunction-lisp/examples/terrain.lisp --output /tmp/demo-pack --namespace demo
# 全オプション: bash sh/generate/generate-mcfunction.sh --help
# 他のrepoではtools/mcfunction-lisp/フォルダだけをコピーし、その中のrun.shを使ってください。
set -euo pipefail
bridge_root=$(cd "$(dirname "$0")/../.." && pwd)
exec bash "$bridge_root/tools/mcfunction-lisp/run.sh" "$@"
