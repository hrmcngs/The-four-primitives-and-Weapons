#!/usr/bin/env bash
# 任意のForge MODを対話選択してビルド（追加MODは本体JARへ同梱しません）。
# bash sh/build/build-with-mods.sh                 対話式。番号を空白で区切って複数選択
# bash sh/build/build-with-mods.sh --jar /path/mod.jar --jar /path/dependency.jar --offline
# --performance-only: 軽量化MODのみ（軽量化MODはすべての選択で必須）
# --ticex: TiCEX＋必須依存4MODを選択（取得済みJARが必要）
# --tinkers: Tinkers’ Construct＋Mantleを選択。--ticex と併用可能
# --source DIR: 候補フォルダ追加 / --prepare: 導入のみ / --run: 選択MODで起動
# --dry-run: コピー・Gradle実行なし。--prepare以外の未知の引数はGradleへ渡します。
# TiCEX等はForge 1.20.1版と、その必須依存MODのJARも一緒に選んでください。
# libs/external・libs/local・libs/runtime_selectedを探索。元のJARは変更しません。
# macOS Bash 3.2 treats an empty array as unbound under set -u.
set -eo pipefail
cd "$(dirname "$0")/../.."
selected=() candidates=() extra_sources=() gradle_args=()
prepare=0 dry=0 run=0 performance_only=0
while [ "$#" -gt 0 ]; do
    case "$1" in
        --help|-h|help) sed -n '2,11s/^# //p' "$0"; exit 0 ;;
        --jar|--source)
            [ "$#" -ge 2 ] || { echo "$1 にはパスが必要です" >&2; exit 1; }
            if [ "$1" = --jar ]; then selected+=("$2"); else extra_sources+=("$2"); fi
            shift 2 ;;
        --performance-only) performance_only=1; shift ;;
        --tinkers)
            for name in TConstruct-1.20.1-3.11.2.166.jar Mantle-1.20.1-1.11.117.jar; do
                selected+=("libs/external/ticex/$name")
            done
            shift ;;
        --ticex)
            for name in ticex-mc1.20.1-0.7.0-hotfix-all.jar TConstruct-1.20.1-3.11.2.166.jar Mantle-1.20.1-1.11.117.jar addonapi-mc1.20.1-2.1.0.jar; do
                selected+=("libs/external/ticex/$name")
            done
            shift ;;
        --prepare) prepare=1; shift ;;
        --dry-run) dry=1; shift ;;
        --run) run=1; shift ;;
        --offline|offline) gradle_args+=(--offline); shift ;;
        *) gradle_args+=("$1"); shift ;;
    esac
done
command -v unzip >/dev/null || { echo 'unzipが必要です' >&2; exit 1; }
if [ "${#selected[@]}" -eq 0 ] && [ "$performance_only" = 0 ]; then
    printf '追加するJARまたはフォルダ（Enterで既定候補のみ）: '
    IFS= read -r added || exit 1
    if [ -n "$added" ]; then
        if [ -f "$added" ]; then candidates+=("$added"); else extra_sources+=("$added"); fi
    fi
    for source in libs/external libs/local libs/runtime_selected "${extra_sources[@]}"; do
        [ -d "$source" ] || continue
        while IFS= read -r -d '' candidate; do
            case "$(basename "$candidate")" in embeddium-*|oculus-*|extra_video_settings-forge-*) continue ;; esac
            candidates+=("$candidate")
        done < <(
            find "$source" -type f -name '*.jar' ! -name '*-sources.jar' ! -name '*-dev.jar' -print0)
    done
    [ "${#candidates[@]}" -gt 0 ] || { echo 'JARがありません。libs/externalへ配置するか --jar で指定してください。' >&2; exit 1; }
    for ((i=0; i<${#candidates[@]}; i++)); do printf '%3d) %s\n' "$((i+1))" "${candidates[$i]}"; done
    printf '導入する番号（空白区切り。必須依存も選択。Enterで中止）: '
    IFS= read -r answer || exit 1
    [ -n "$answer" ] || exit 0
    read -r -a numbers <<< "$answer"
    for number in "${numbers[@]}"; do
        [[ "$number" =~ ^[0-9]+$ ]] || { echo "不正な番号: $number" >&2; exit 1; }
        number=$((10#$number))
        ((number >= 1 && number <= ${#candidates[@]})) || { echo '番号が範囲外です' >&2; exit 1; }
        selected+=("${candidates[$((number-1))]}")
    done
fi
# Always add the established lightweight runtime. Do not sync runtime_selected:
# that script also changes shaderpacks and can bring in unrelated gameplay MODs.
performance_dirs=(libs/offline-performance libs/local libs/runtime_selected)
if [ -n "${MAW_PERFORMANCE_DIR:-}" ]; then performance_dirs=("$MAW_PERFORMANCE_DIR"); fi
for prefix in embeddium oculus extra_video_settings-forge; do
    performance_jar=""
    for directory in "${performance_dirs[@]}"; do
        [ -d "$directory" ] || continue
        while IFS= read -r -d '' candidate; do
            if [ -z "$performance_jar" ] || [ "$candidate" -nt "$performance_jar" ]; then performance_jar="$candidate"; fi
        done < <(find "$directory" -type f -name "$prefix-*.jar" ! -name '*-sources.jar' ! -name '*-dev.jar' -print0)
        [ -z "$performance_jar" ] || break
    done
    [ -n "$performance_jar" ] || { echo "必須の軽量化MODがありません: $prefix。libs/offline-performance/へ配置してください。" >&2; exit 1; }
    selected+=("$performance_jar")
done
echo '必須構成: Embeddium + Oculus + extra_video_settings（追加MODとは別に常時導入）'
# Check metadata before any installation. Detect duplicate mod IDs, not just filenames.
ids=() hashes=() unique=()
for jar in "${selected[@]}"; do
    [ -f "$jar" ] || { echo "JARがありません: $jar" >&2; exit 1; }
    metadata=$(unzip -p "$jar" META-INF/mods.toml) || { echo "Forge MODではありません: $jar" >&2; exit 1; }
    mod_ids=$(printf '%s\n' "$metadata" | awk '/^\[\[mods\]\]/{m=1;next} /^\[/{m=0} m && /^[[:space:]]*modId[[:space:]]*=/{sub(/#.*/, "", $0);gsub(/[[:space:]"\047]/, "", $0);sub(/^modId=/," "); print $0}')
    [ -n "$mod_ids" ] || { echo "modIdを読めません: $jar" >&2; exit 1; }
    hash=$(shasum -a 256 "$jar" | awk '{print $1}')
    duplicate=0
    for old in "${hashes[@]}"; do [ "$old" != "$hash" ] || duplicate=1; done
    [ "$duplicate" = 0 ] || continue
    while IFS= read -r id; do
        id="${id# }"
        case "$id" in
            minecraft|forge|curios|jei|the_four_primitives_and_weapons)
                echo "本体・既存依存と重複します: $id ($jar)" >&2; exit 1 ;;
        esac
        for old in "${ids[@]}"; do [ "$old" != "$id" ] || { echo "MOD ID重複: $id" >&2; exit 1; }; done
        ids+=("$id")
    done <<< "$mod_ids"
    hashes+=("$hash"); unique+=("$jar")
done
profile_hash=$(printf '%s\n' "${hashes[@]}" | LC_ALL=C sort | shasum -a 256 | awk '{print substr($1,1,20)}')
profile="interactive_$profile_hash"
printf '導入MOD: %s\n' "${ids[*]}"
if [ "$dry" = 0 ]; then
    for ((i=0; i<${#unique[@]}; i++)); do
        artifact="mod-${hashes[$i]}"
        target="libs/$profile/$artifact/1/$artifact-1.jar"
        mkdir -p "$(dirname "$target")"
        [ -f "$target" ] || cp "${unique[$i]}" "$target"
    done
fi
args=(-PwithExternalMods=true "-PexternalModsGroup=$profile")
[ "${#gradle_args[@]}" -eq 0 ] || args+=("${gradle_args[@]}")
printf '選択MOD用プロファイル: %s\n' "$profile"
if [ "$prepare" = 1 ]; then
    printf '起動: ./gradlew runClient'; printf ' %q' "${args[@]}"; printf '\n'; exit 0
fi
task=build
[ "$run" = 0 ] || task=runClient
printf '実行: ./gradlew %s' "$task"; printf ' %q' "${args[@]}"; printf '\n'
[ "$dry" = 0 ] || exit 0
exec ./gradlew "$task" "${args[@]}"
