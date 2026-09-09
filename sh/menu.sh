#!/usr/bin/env bash
# 全shの案内と「やりたいこと」から選ぶ対話式メニュー。SBCLが必要です。
#   bash sh/menu.sh                                  対話式メニュー
#   bash sh/menu.sh help                             全shの一覧・概要
#   bash sh/menu.sh help --all                       各sh内のメモ全文
#   bash sh/menu.sh help sh/run/run_client_mac.sh     個別の使い方
#   bash sh/menu.sh --dry-run                        実行せずコマンド選択を試す
# 起動・ビルド・生成・保守・検証から選べます。説明を読む操作では対象shを実行しません。
# Enterで既定値、Ctrl+Cまたは入力終了でメニューを終了します。
set -euo pipefail
cd "$(dirname "$0")/.."
command -v sbcl >/dev/null || { echo "SBCL (Common Lisp) が必要です。" >&2; exit 1; }
exec sbcl --script lisp/menu/menu.lisp "$@"
