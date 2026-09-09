#!/bin/bash
# 使い方・運用メモ (help / --help / -h でも表示)
# WSLからWindows側の固定パスへ移動し、gradlew.batを実行します。
# 使い方: bash sh/build/build_win.sh / bash sh/build/build_win.sh clean
# このスクリプトはoffline引数に対応していません。オフラインは bash sh/build/build.sh offline を使います。
# Windows側Java 17が必要です。スクリプト内の固定プロジェクトパスを環境に合わせてください。
# セットアップ・トラブル対処はsh/build/build.sh冒頭にあります。
#
# WSLからjarをビルドするスクリプト（Windows側のJavaを使用）
#
# 使い方:
#   bash sh/build/build_win.sh          通常ビルド
#   bash sh/build/build_win.sh clean    クリーンビルド


for shell_help_arg in "$@"; do
    case "$shell_help_arg" in
        help|--help|-h)
            awk 'NR == 1 { next } /^#/ { sub(/^# ?/, ""); print; next } /^[[:space:]]*$/ { print; next } { exit }' "$0"
            exit 0
            ;;
    esac
done

if [ "$1" = "clean" ]; then
    echo "=== Clean Build ==="
    cmd.exe /c "cd /d C:\Users\hrmcn\MCreatorWorkspaces\minecraft_armor_weapon && gradlew.bat clean build"
else
    echo "=== Build ==="
    cmd.exe /c "cd /d C:\Users\hrmcn\MCreatorWorkspaces\minecraft_armor_weapon && gradlew.bat build"
fi
