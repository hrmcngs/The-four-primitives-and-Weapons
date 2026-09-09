#!/bin/bash
# 使い方・運用メモ (help / --help / -h でも表示)
# # ビルド手順 (WSL / macOS)
#
# ## 初回セットアップ
#
# ### WSL (Windows)
#
# #### 1. Java 17 インストール
#
# ```bash
# sudo apt update && sudo apt install -y openjdk-17-jdk
# ```
#
# 確認：
#
# ```bash
# java -version
# # openjdk version "17.x.x" と表示されればOK
# ```
#
# #### 2. JAVA_HOME 設定（永続化）
#
# ```bash
# echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
# echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.bashrc
# source ~/.bashrc
# ```
#
# ### macOS
#
# #### 1. Java 17 インストール (Homebrew)
#
# ```bash
# brew install --cask temurin@17
# ```
#
# 確認：
#
# ```bash
# java -version
# # openjdk version "17.x.x" と表示されればOK
# ```
#
# #### 2. JAVA_HOME 設定（永続化）
#
# ```bash
# echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
# echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc
# source ~/.zshrc
# ```
#
# ## ビルド
#
# ### WSL (Windows)
#
# ```bash
# bash sh/build/build_win.sh                   # 通常ビルド
# bash sh/build/build_win.sh clean             # クリーンビルド
# bash sh/build/build.sh offline           # オフラインビルド
# bash sh/build/build.sh clean offline     # クリーン＋オフライン
# ```
#
# ### macOS
#
# ```bash
# bash sh/build/build_mac.sh                   # 通常ビルド
# bash sh/build/build_mac.sh clean             # クリーンビルド
# bash sh/build/build_mac.sh offline           # オフラインビルド
# bash sh/build/build_mac.sh clean offline     # クリーン＋オフライン
# ```
#
# ### 手動で実行 (共通)
#
# ```bash
# # WSL:
# cd /mnt/c/Users/hrmcn/MCreatorWorkspaces/the_four_primitives_and_weapons
# # mac: ローカルにcloneしたパスへ
# ./gradlew build
# ```
#
# ## 出力先
#
# ```
# build/libs/ 内の生成されたJAR
# ```
#
# ## テストプレイ
#
# ### WSL (Windows)
#
# ```bash
# bash sh/run/run_client_win.sh              # 通常起動
# bash sh/run/run_client_win.sh offline      # オフライン起動
# ```
#
# ### macOS
#
# ```bash
# bash sh/run/run_client_mac.sh              # 通常起動
# bash sh/run/run_client_mac.sh offline      # オフライン起動
# ```
#
# ## トラブルシューティング
#
# ### JAVA_HOME is not set エラー
#
# WSL:
# ```bash
# export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
# ```
#
# macOS:
# ```bash
# export JAVA_HOME=$(/usr/libexec/java_home -v 17)
# ```
#
# ### Permission denied
#
# WSL:
# ```bash
# chmod +x gradlew
# chmod +x sh/build/build_win.sh sh/run/run_client_win.sh
# ```
#
# macOS:
# ```bash
# chmod +x gradlew
# chmod +x sh/build/build_mac.sh sh/run/run_client_mac.sh
# ```
#
# ### 改行コードエラー (`$'\r': command not found` など)
#
# Windowsで編集した `.sh` がCRLFになっている場合:
#
# ```bash
# sed -i 's/\r$//' sh/build/build_win.sh sh/run/run_client_win.sh
# # mac (BSD sed) の場合:
# sed -i '' 's/\r$//' sh/build/build_mac.sh sh/run/run_client_mac.sh
# ```
#
# ### キャッシュ問題
#
# ```bash
# # WSL
# bash sh/build/build_win.sh clean
# # mac
# bash sh/build/build_mac.sh clean
# ```
#
# それでもダメなら：
#
# ```bash
# # キャッシュ削除の前に --stacktrace のエラー箇所を確認してください。
# # 上記ビルドを再実行
# ```
#
#
# 共通のGradle操作・VSCodeのメモは sh/build/build.sh 冒頭も参照。
#
# jarをビルドするスクリプト（mac / WSL / Windows Git Bash 対応）
#
# 使い方:
#   bash sh/build/build.sh                  通常ビルド
#   bash sh/build/build.sh clean            クリーンビルド
#   bash sh/build/build.sh offline          オフラインビルド（キャッシュ済み依存のみ）
#   bash sh/build/build.sh clean offline    クリーン + オフライン
#   bash sh/build/build.sh offline clean    同上（順不同）


for shell_help_arg in "$@"; do
    case "$shell_help_arg" in
        help|--help|-h)
            awk 'NR == 1 { next } /^#/ { sub(/^# ?/, ""); print; next } /^[[:space:]]*$/ { print; next } { exit }' "$0"
            exit 0
            ;;
    esac
done

cd "$(dirname "$0")/../.."

TASKS="build"
GRADLE_ARGS=""
LABEL="Build"
DO_CLEAN=0
for arg in "$@"; do
    case "$arg" in
        clean)
            DO_CLEAN=1
            LABEL="Clean Build"
            ;;
        offline)
            GRADLE_ARGS="$GRADLE_ARGS --offline -Dnet.minecraftforge.gradle.check.certs=false"
            LABEL="$LABEL (Offline)"
            ;;
    esac
done

echo "=== $LABEL ==="

# クリーン: build/ を掃除するが build/fg_cache ( 再コンパイル済み Minecraft 依存 ) は残す。
#   gradle の `clean` タスクは fg_cache も消すため、 同一実行の compileJava が
#   「設定時にマップ済み MC jar が未生成」 となり net.minecraft.* を解決できず
#   "package net.minecraft.* does not exist" で大量に失敗する ( 2 回目以降は通る )。
#   依存キャッシュを残せば 1 回の build で通り、 対話プロンプトも 1 回で済む。
#   ※ Forge バージョン変更等で MC を完全に再生成したい時だけ手動で `rm -rf build` を。
if [ "$DO_CLEAN" = "1" ] && [ -d build ]; then
    find build -mindepth 1 -maxdepth 1 ! -name fg_cache -exec rm -rf {} +
fi

case "$(uname -s)" in
    MINGW*|CYGWIN*|MSYS*)
        ./gradlew.bat $TASKS $GRADLE_ARGS
        ;;
    *)
        ./gradlew $TASKS $GRADLE_ARGS
        ;;
esac