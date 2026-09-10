#!/usr/bin/env bash
# Common Lisp → Minecraftデータパック。必要環境: Bash、SBCL（外部Lispライブラリ・Python不要）。
# このmcfunction-lispフォルダをコピーすれば、別リポジトリ・任意の作業ディレクトリから使えます。
# bash /path/to/mcfunction-lisp/run.sh --source terrain.lisp --output ./my_pack --namespace demo
# --source: 実行するLispソース。自分で作成した／信頼できるプログラムを指定してください。
# --output: 新規データパックの保存先。既存のフォルダは上書きしません。
# --namespace: 名前空間（既定example）。ゲーム内: /function demo:build
# --pack-format: pack.mcmetaの番号（既定15、Minecraft Java 1.20〜1.20.1向け）。
# --function-directory: functions または function（既定functions）。対象バージョンに合わせて指定。
# パスは呼び出した場所を基準に解決します。MODやJava、ワールドへの依存はありません。
# 作成したフォルダをワールドのdatapacks/へ配置し、/reloadして/functionを実行します。
# Lispは事前にコマンドを書き出します。ゲーム内からLispを呼ぶ仕組みではありません。
# 構文・ブロックID・コマンド数制限などのMinecraft側の妥当性は利用者側で確認してください。
set -euo pipefail
case "${1:-}" in ""|help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
command -v sbcl >/dev/null || { echo "SBCL is required." >&2; exit 1; }
bridge_dir=$(cd "$(dirname "$0")" && pwd)
exec sbcl --script "$bridge_dir/cli.lisp" "$@"
