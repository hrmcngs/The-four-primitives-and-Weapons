#!/usr/bin/env bash
# 任意のMODを対話で選び、必須の軽量化MODと一緒にMinecraftを起動します。
# bash sh/run/run-with-mods.sh                    JARを対話選択（既定はオフライン）
# bash sh/run/run-with-mods.sh --tetra             Tetra＋mutilでオフライン起動
# bash sh/run/run-with-mods.sh --ticex             TiCEX＋必須依存で起動
# bash sh/run/run-with-mods.sh --performance-only  軽量化MODのみで起動
# bash sh/run/run-with-mods.sh online              オンラインで選択・起動
# --jar PATH（複数指定可）/ --source DIR / --dry-run も使用できます。
# 追加MODの必須依存も選んでください。選択したMODは専用プロファイルへ隔離します。
set -eo pipefail
cd "$(dirname "$0")/../.."
run_mod_args=()
run_mod_offline=1
for run_mod_arg in "$@"; do
    case "$run_mod_arg" in
        help|--help|-h) sed -n '2,8s/^# //p' "$0"; exit 0 ;;
        online|--online) run_mod_offline=0 ;;
        offline|--offline) run_mod_offline=1 ;;
        --prepare|--run) echo "このスクリプトは起動専用です。導入のみは sh/build/build-with-mods.sh --prepare を使ってください。" >&2; exit 1 ;;
        *) run_mod_args+=("$run_mod_arg") ;;
    esac
done
if [ "$run_mod_offline" = 1 ]; then run_mod_args+=(--offline -x downloadAssets); fi
exec bash sh/build/build-with-mods.sh --run "${run_mod_args[@]}"
