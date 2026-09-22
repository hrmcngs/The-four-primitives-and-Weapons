#!/usr/bin/env bash
# 天文現象・timeコマンドを対話式で生成します（SBCL、オフライン）。
#   bash sh/generate/generate-time-commands.sh
#   bash sh/menu.sh → 生成 → 天文現象・timeコマンド
# 日時、バニラの三日月・半月など8相、月の大きさ・色、流星群、日食・月食、効果を個別指定できます。
# run/generated_commands/astronomy-<日時>/ にcommands.txtとgenerated.mcfunctionを保存します。
# ゲームへ自動送信しません。WMO参考分類の天候も選択可能。コマンドを順に実行してください。
# /astronomyには対応する本体MODと管理者権限が必要です。設定はワールドへ保存されます。
# /astronomy reset で自然周期に戻せます（日時・天候・時間停止は別設定）。
# help / --help / -h: 説明のみ。--self-test: コマンド生成の自己検証。
# 起動後「月の効果をすぐ試す」は色を選ぶだけで夜・満月・晴れ・効果ONに設定します。
# 「細かく設定する」は従来の全設定。「説明を読む」はファイルを生成しません。
# 緑=成長促進、青=釣り、金=採掘経験値、紫=耐久節約、桃=繁殖待ち短縮。ポーション効果なし。
# 既定の「自然発生で遊ぶ」は天文・追加天候の固定を解除し、時間・天候の自然変化を再開します。日時は変更しません。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in
  help|--help|-h) sed -n '2,/^set /{ /^#/s/^# \{0,1\}//p; }' sh/generate/generate-time-commands.sh; exit 0 ;;
esac
command -v sbcl >/dev/null || { echo "SBCL (Common Lisp) が必要です。" >&2; exit 1; }
exec sbcl --script lisp/generate/time-commands.lisp "$@"
