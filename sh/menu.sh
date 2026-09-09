#!/usr/bin/env bash
# 全shの案内と「やりたいこと」から選ぶ対話式メニュー。SBCLが必要です。
#   bash sh/menu.sh                                  対話式メニュー
#   bash sh/menu.sh help                             全shの一覧・概要
#   bash sh/menu.sh help --all                       各sh内のメモ全文
#   bash sh/menu.sh help sh/run/run_client_mac.sh     個別の使い方
#   bash sh/menu.sh --dry-run                        実行せずコマンド選択を試す
# 起動・ビルド・生成・保守・検証から選べます。説明を読む操作では対象shを実行しません。
# Enterで既定値、Ctrl+Cまたは入力終了でメニューを終了します。
#
# Lisp化したツール（Python不要）:
#   蝶・刀のコマンド生成: sh/generate/generate_commands.sh
#   撮影ワールドの作成: sh/generate/generate-photo-world.sh
#   拵えテクスチャ・ノイズフォント: sh/generate/generate-assets.sh
#   Javaのlevel修正: sh/maintenance/codemod-level.sh
#   MCreatorのコードロック: sh/maintenance/lock_codes.sh
#   移植後の検証: sh/test/test-lisp-tools.sh / sh/test/test-lisp-migration.sh
# 必要環境: Common LispのSBCL。画像ツールにはJava 17、ワールド作成にはgzip・zipも必要です。
# shは入口、処理本体はlisp/内です。PNG入出力と文字描画にはJava標準機能を使います。
# 例: bash sh/menu.sh help sh/generate/generate-assets.sh
# 各コマンドの引数・出力先・注意点は、個別ヘルプまたはhelp --allで確認できます。
set -euo pipefail
cd "$(dirname "$0")/.."
command -v sbcl >/dev/null || { echo "SBCL (Common Lisp) が必要です。" >&2; exit 1; }
exec sbcl --script lisp/menu/menu.lisp "$@"
