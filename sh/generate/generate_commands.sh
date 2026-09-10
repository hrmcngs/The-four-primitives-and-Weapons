#!/usr/bin/env bash
# 使い方・運用メモ (help / --help / -h でも表示)
# # ターミナルで蝶・刀のコマンドを生成
#
# リポジトリのターミナルで実行します。Common Lisp (SBCL)だけで動作し、オフラインで使えます。
#
# ```sh
# bash sh/generate/generate_commands.sh
# ```
#
# 番号を入力して選択します。Enterで既定値、Ctrl+Cで終了です。
#
# - 蝶：49種類から選択、羽・縁・模様・胴体の色、模様、尾、大きさ、羽の縦横比、羽ばたき、飛行速度、撮影用固定。
#   dreamwing: 夢見鳥風の赤橙・深紅・金色の蝶。模様10で専用の羽形状。色も変更できます。
#   触角の長さ・開き・傾き、胴体の幅・長さ、羽の開き角も「大きさ・羽の形・動き」で指定できます。
#   NBTの例: {AntennaLength:1.5f,AntennaSpread:35f,AntennaTilt:25f,BodyWidth:1.2f,FlapRestAngle:30f}
# - 刀：武器の種類、柄・鍔・頭・はばきの色、名前、回収時消滅。設置時は位置・向き・傾き・ひねり・大きさも指定。
# - 出力：蝶の `/summon` またはスポーンエッグの `/give`、刀の `/give` または刺した武器の `/summon`。
#
# 拵えの色は対応する部位を持つモデルで反映されます。バニラの剣など非対応モデルには反映されません。
# アイテムIDの直接入力では形式のみ確認します。導入環境に存在するIDを指定してください。
# 設置位置はプレイヤーからの相対座標、ワールド座標、ローカル座標に対応します。
#
# 出力は `run/generated_commands/<日時>/` に自動保存します。
# `commands.txt` はスラッシュ付きのコマンド一覧です。macOSでは生成後にクリップボードへコピーする選択肢も出ます。
#
# 長いコマンドはコマンドブロックに貼るか、生成された `command_builder` フォルダを
# 対象ワールドの `datapacks` にコピーして次を実行してください。
#
# ```mcfunction
# /reload
# /function command_builder:generated
# ```
#
# このfunctionはその対話セッションで生成した全コマンドを、実行するたびに繰り返します。
# 相対座標はfunctionを実行した位置が基準です。撮影ワールドへのコピー後に `/reload` すると、
# 撮影用データパック側に未適用の景観更新がある場合はその再生成も始まります。
# ツールはゲームへ直接送信しません。蝶の新しい種類・NBTには対応するMODビルドが必要です。
#

for shell_help_arg in "$@"; do
    case "$shell_help_arg" in
        help|--help|-h)
            awk 'NR == 1 { next } /^#/ { sub(/^# ?/, ""); print; next } /^[[:space:]]*$/ { print; next } { exit }' "$0"
            exit 0
            ;;
    esac
done

set -euo pipefail
cd "$(dirname "$0")/../.."
command -v sbcl >/dev/null || { echo "SBCL (Common Lisp) が必要です。" >&2; exit 1; }
exec sbcl --script lisp/generate/commands.lisp "$@"
