#!/usr/bin/env bash
# 選択MODのJARメタデータ、重複検出、空白パス、dry-runの非変更を検証。
set -euo pipefail
cd "$(dirname "$0")/../.."
test_mod_dir=$(mktemp -d)
trap 'rm -rf "$test_mod_dir"' EXIT
python3 - "$test_mod_dir" <<'PY'
import sys, zipfile
from pathlib import Path
root = Path(sys.argv[1])
for filename, mod, version in [('space mod.jar','test_selected','1'),('other.jar','test_selected','2'),('fabric.jar',None,'1')]:
    with zipfile.ZipFile(root / filename, 'w') as jar:
        if mod:
            jar.writestr('META-INF/mods.toml', 'modLoader="javafml"\n[[mods]] # metadata comment\nmodId="'+mod+'" # mandatory\nversion="'+version+'"\n')
        else:
            jar.writestr('fabric.mod.json', '{}')
PY
selector=sh/build/build-with-mods.sh
bash "$selector" --jar "$test_mod_dir/space mod.jar" --dry-run > "$test_mod_dir/out"
rg -q '導入MOD: test_selected' "$test_mod_dir/out"
printf '%s\n1\n' "$test_mod_dir/space mod.jar" | bash "$selector" --dry-run > "$test_mod_dir/interactive"
rg -q '導入MOD: test_selected' "$test_mod_dir/interactive"
profile=$(sed -n 's/^選択MOD用プロファイル: //p' "$test_mod_dir/out")
[ ! -d "libs/$profile" ]
bash "$selector" --jar "$test_mod_dir/space mod.jar" --jar "$test_mod_dir/space mod.jar" --dry-run >/dev/null
if bash "$selector" --jar "$test_mod_dir/space mod.jar" --jar "$test_mod_dir/other.jar" --dry-run > "$test_mod_dir/error" 2>&1; then exit 1; fi
rg -q 'MOD ID重複' "$test_mod_dir/error"
if bash "$selector" --jar "$test_mod_dir/fabric.jar" --dry-run >/dev/null 2>&1; then exit 1; fi
if printf '\n0\n' | bash "$selector" --source "$test_mod_dir" --dry-run >/dev/null 2>&1; then exit 1; fi
rg -q '導入MOD: .*embeddium.*oculus.*extra_video_settings' "$test_mod_dir/out"
if rg -q '導入MOD: .*ticex' "$test_mod_dir/out"; then exit 1; fi
bash "$selector" --performance-only --dry-run > "$test_mod_dir/performance"
rg -q '導入MOD: embeddium.*oculus.*extra_video_settings' "$test_mod_dir/performance"
if rg -q 'test_selected|ticex' "$test_mod_dir/performance"; then exit 1; fi
mkdir "$test_mod_dir/missing-performance"
if MAW_PERFORMANCE_DIR="$test_mod_dir/missing-performance" bash "$selector" --performance-only --dry-run > "$test_mod_dir/missing" 2>&1; then exit 1; fi
rg -q '必須の軽量化MODがありません' "$test_mod_dir/missing"
# Run entrypoints must use only the selected profile, with mandatory performance MODs.
bash sh/run/run-with-mods.sh --jar "$test_mod_dir/space mod.jar" --dry-run > "$test_mod_dir/run"
rg -q '実行: ./gradlew runClient .*--offline.*downloadAssets' "$test_mod_dir/run"
rg -q '導入MOD: test_selected.*embeddium.*oculus.*extra_video_settings' "$test_mod_dir/run"
bash sh/run/run-with-mods.sh --performance-only online --dry-run > "$test_mod_dir/run-online"
rg -q '実行: ./gradlew runClient' "$test_mod_dir/run-online"
if rg -q -- '--offline|downloadAssets' "$test_mod_dir/run-online"; then exit 1; fi
bash sh/run/run_client_mac.sh --select-mods --performance-only --dry-run > "$test_mod_dir/mac-run"
rg -q '実行: ./gradlew runClient' "$test_mod_dir/mac-run"
if rg -q 'ticex|test_selected' "$test_mod_dir/mac-run"; then exit 1; fi
printf '%s\n1\n' "$test_mod_dir/space mod.jar" | bash sh/run/run-with-mods.sh --dry-run > "$test_mod_dir/run-interactive"
rg -q '導入MOD: test_selected' "$test_mod_dir/run-interactive"
rg -q '実行: ./gradlew runClient' "$test_mod_dir/run-interactive"
echo 'MOD selection tests passed.'
# Tetra preset must select local Tetra and mutil, and fail before install if either is absent.
mkdir "$test_mod_dir/tetra"
python3 - "$test_mod_dir/tetra" <<'PY'
import sys, zipfile
from pathlib import Path
for mod in ('tetra', 'mutil'):
    with zipfile.ZipFile(Path(sys.argv[1]) / (mod+'-test.jar'), 'w') as jar:
        jar.writestr('META-INF/mods.toml', 'modLoader="javafml"\n[[mods]]\nmodId="'+mod+'"\nversion="6.3.0"\n')
PY
MAW_TETRA_DIR="$test_mod_dir/tetra" bash sh/run/run-with-mods.sh --tetra --dry-run > "$test_mod_dir/tetra-run"
rg -q '導入MOD: tetra mutil.*embeddium.*oculus.*extra_video_settings' "$test_mod_dir/tetra-run"
rg -q -- '--offline.*downloadAssets' "$test_mod_dir/tetra-run"
MAW_TETRA_DIR="$test_mod_dir/tetra" bash "$selector" --tetra --tinkers --offline --dry-run > "$test_mod_dir/tetra-tinkers"
rg -q '導入MOD: tetra mutil tconstruct mantle.*embeddium.*oculus.*extra_video_settings' "$test_mod_dir/tetra-tinkers"
if rg -q '導入MOD: .*ticex' "$test_mod_dir/tetra-tinkers"; then exit 1; fi
MAW_TETRA_DIR="$test_mod_dir/tetra" bash "$selector" --tetra --tinkers --ticex --offline --dry-run > "$test_mod_dir/tetra-ticex"
rg -q '導入MOD: .*tconstruct.*mantle.*ticex.*addonapi' "$test_mod_dir/tetra-ticex"
bash "$selector" --tinkers --offline --dry-run > "$test_mod_dir/tinkers"
rg -q '導入MOD: tconstruct mantle.*embeddium' "$test_mod_dir/tinkers"
if rg -q '導入MOD: .*ticex' "$test_mod_dir/tinkers"; then exit 1; fi
rm "$test_mod_dir/tetra/mutil-test.jar"
if MAW_TETRA_DIR="$test_mod_dir/tetra" bash "$selector" --tetra --dry-run > "$test_mod_dir/tetra-missing" 2>&1; then exit 1; fi
rg -q 'Tetraオフライン用JARがありません: mutil' "$test_mod_dir/tetra-missing"
echo 'Tetra offline preset tests passed.'
