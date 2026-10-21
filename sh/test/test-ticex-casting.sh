#!/usr/bin/env bash
# TiCEX入り開発用サーバーで刀身の鋳造・組み立てを実行検証します。Java 17が必要です。
# bash sh/test/test-ticex-casting.sh SERVER_PID
# 指定したサーバーに一時診断Agentを接続します。検証にはForgeのFakePlayerを使用します。
# 検証サーバー以外のPIDは指定しないでください。Agentは配布JARに含めません。
set -euo pipefail
cd "$(dirname "$0")/../.."
[[ "${1:-}" =~ ^[0-9]+$ ]] || { echo '使い方: bash sh/test/test-ticex-casting.sh SERVER_PID' >&2; exit 1; }
smoke_ticex_dir=$(mktemp -d)
trap 'rm -rf "$smoke_ticex_dir"' EXIT
# A fresh name per run avoids reusing an older Agent class in the target JVM.
smoke_agent_name="TicexCastingSmokeAgent_$$_${RANDOM}"
sed "s/TicexCastingSmokeAgent/$smoke_agent_name/g" tests/TicexCastingSmokeAgent.java > "$smoke_ticex_dir/$smoke_agent_name.java"
javac --add-modules jdk.attach -d "$smoke_ticex_dir" "$smoke_ticex_dir/$smoke_agent_name.java"
printf 'Agent-Class: %s\n' "$smoke_agent_name" > "$smoke_ticex_dir/MANIFEST.MF"
jar cfm "$smoke_ticex_dir/agent.jar" "$smoke_ticex_dir/MANIFEST.MF" -C "$smoke_ticex_dir" "$smoke_agent_name.class"
java --add-modules jdk.attach -cp "$smoke_ticex_dir" "$smoke_agent_name" "$1" "$smoke_ticex_dir/agent.jar" "$smoke_ticex_dir/result.txt"
for ((attempt=0; attempt<30; attempt++)); do
    if [ -f "$smoke_ticex_dir/result.txt" ]; then
        cat "$smoke_ticex_dir/result.txt"
        grep -qx PASS "$smoke_ticex_dir/result.txt"
        exit $?
    fi
    sleep 1
done
echo '検証がタイムアウトしました' >&2
exit 1
