#!/bin/bash
# 使い方・運用メモ (help / --help / -h でも表示)
# # 開発用クライアント起動ガイド
#
# 開発用 Minecraft クライアント (`runClient`) を起動するためのスクリプトと、各種フラグ / 環境変数 / トラブルシュート手順をまとめます。
#
# ## スクリプト
#
# 蝶・刀のコマンドを対話式で生成するには `bash sh/generate/generate_commands.sh` を実行します。
# 起動やコンパイルは不要です。[使い方](sh/generate/generate_commands.sh)
#
# | 環境 | スクリプト |
# |---|---|
# | macOS / Linux / WSL / Git Bash | `bash sh/run/run_client_mac.sh` |
# | Windows ( cmd / PowerShell ) | `run_client_windows.bat` |
#
# macOS版では、外部MODを軽量化MODのみ・追加機能MODあり・重いMODありから対話で選べます。Windows版は `libs/local/` の一括取り込みです。
#
# ## 引数
#
# 複数併用可 ( 順不同 )。
#
# | 引数 | 意味 |
# |---|---|
# | ( なし ) | macOS版はオフライン。オンラインは online を明示 |
# | `offline` | `--offline -x downloadAssets` 付き ( キャッシュのみで起動 ) |
# | `notls` / `no-tls` | TLS workaround を切る ( テザリング等で素の TLS を使う ) |
# | `keepdaemon` / `keep-daemon` | gradle daemon を `--stop` で kill しない ( 2 回目以降の高速起動用 ) |
#
# 例:
# ```bash
# # macOS / Linux
# bash sh/run/run_client_mac.sh offline
# bash sh/run/run_client_mac.sh online notls
# bash sh/run/run_client_mac.sh offline notls
# bash sh/run/run_client_mac.sh offline keepdaemon
#
# :: Windows
# run_client_windows.bat offline
# run_client_windows.bat notls
# run_client_windows.bat offline keepdaemon
# ```
#
# ## 起動フロー
#
# ```
# 1. 引数パース (macOS版は既定でオフライン)
# 2. gradle daemon を --stop ( keepdaemon が無ければ )
#    → JAVA_TOOL_OPTIONS / gradle.properties の変更を daemon に確実反映
# 3. オフライン時は回線判定を省略。オンライン時はTLSを自動判定、tls/notlsで指定可能
# 4. 外部 mod 取り込みを対話で確認 ( y → -PwithExternalMods=true を gradle に渡す )
# 5. gradlew runClient を起動
# ```
#
# ## macOS版の外部MOD選択
#
# `bash sh/run/run_client_mac.sh` の外部MOD確認で `y` を選ぶと、続けて選択できます。
#
# 1. 「軽量化MODのみで起動しますか?」→ Enter / `y` で **Embeddium + Oculus + extra_video_settings（VanillaLiteを選択可能）**。
# 2. 上で `n` を選ぶと「負荷の大きいMODも入れますか?」→ Enter / `n` で Embeddium + Oculus + chuzume-addon / extra_video_settings / RPGish-HPDisplay。
# 3. 重いMODの質問で `y` を選ぶと TACZ / Gun and Weapon / Backpack Arsenal / Mekanism / Sophisticated も追加。
#
# EmbeddiumとOculusのJARは `libs/local/` に配置します。選択したMODは毎回 `libs/runtime_selected/` に同期され、前回の重いMODは残りません。軽量化MODのみの場合、`libs/external/` の任意MODも取り込みません。それ以外では従来どおり取り込みます。同期に失敗した場合は起動を中止します。
#
# ```bash
# # 対話なしで軽量化MODのみ
# WITH_EXTERNAL_MODS=1 PERFORMANCE_ONLY_MODS=1 bash sh/run/run_client_mac.sh
# # 重いMODなしで追加機能MODも入れる (従来互換)
# WITH_EXTERNAL_MODS=1 LIGHT_EXTERNAL_MODS=1 bash sh/run/run_client_mac.sh
# # 重いMODも入れる (従来互換)
# WITH_EXTERNAL_MODS=1 LIGHT_EXTERNAL_MODS=0 bash sh/run/run_client_mac.sh
# ```
#
# `PERFORMANCE_ONLY_MODS=0` は軽量化MODのみの質問を省略し、重いMODの質問へ進みます。`PERFORMANCE_ONLY_MODS=1` は `LIGHT_EXTERNAL_MODS` より優先されます。外部MODを有効にした非対話実行でどちらも未指定なら、軽量化MODのみになります。
#
# ## 軽量化MOD使用時のシェーダー
#
# macOS版で外部MODを有効にすると、軽量化MODのみ・追加機能MODあり・全部入りのいずれでも、Embeddium・Oculus・自作のextra_video_settingsを読み込みます。
# `/Users/hiromichi/Documents/github/mods/VanillaLite/dist/` 内のZIPを、起動時に `run/shaderpacks/` へコピーします。別の場所を使う場合は `VANILLA_LITE_DIST=/path/to/dist` を指定できます。
#
# ゲーム内の **設定 → ビデオ設定 → シェーダーパック** でVanillaLiteを選び、シェーダーを有効にして適用してください。同じ画面でOFFや別パックへの切り替えもできます。起動スクリプトはゲーム内で選んだ設定を上書きしません。
#
# 前提JARは `libs/local/` に配置します。現在の組み合わせは Embeddium `0.3.31+mc1.20.1` と [Oculus `1.20.1-1.8.0`](https://modrinth.com/mod/oculus/version/1.20.1-1.8.0) です。起動スクリプト自体はダウンロードを行わず、配置済みのJARとZIPをオフラインで同期します。
#
# ### オフラインでシェーダーを使う
#
# ```bash
# WITH_EXTERNAL_MODS=1 PERFORMANCE_ONLY_MODS=1 bash sh/run/run_client_mac.sh offline
# ```
#
# 対話で選ぶ場合は `bash sh/run/run_client_mac.sh offline` で外部MODを `y` にします。
# Oculus・EmbeddiumとVanillaLiteはローカルのファイルを使用するため、シェーダーの選択・ON/OFFにネット接続は不要です。
# 配布元の `dist/` がない場合も、`run/shaderpacks/VanillaLite*.zip` があれば保存済みパックを使って起動します。ゲーム内で選んだ設定は維持します。
# Oculus・Embeddium・extra_video_settingsの配布JARは `libs/offline-performance/` にも保存します。`libs/local/` にJARがない場合はこのキャッシュ、または前回の `libs/runtime_selected/` から再利用します。同期先を消す前に保存するため、繰り返しオフライン起動できます。
# 初回に必要なMODのJAR・Gradle依存キャッシュとシェーダーZIPは、この端末に配置済みです。
#
#
# `extra_video_settings` は自作プロジェクトの `forge/forge/build/libs/` から更新日時が最新のForge用JARを取り込みます。ビルド元がない場合も保存済みキャッシュからオフラインで再利用できます。別のビルド先を使う場合は `EXTRA_VIDEO_SETTINGS_DIR=/path/to/build/libs` を指定してください。
#
# ## Windows版の外部 mod ( libs/local/ )
#
# 任意の `.jar` を `libs/local/` 配下に置くと、 起動時に自動取り込みされます。
#
# - フラット配置 ( `libs/local/foo-1.2.3.jar` ) でも
# - Maven 階層 ( `libs/local/foo/1.2.3/foo-1.2.3.jar` ) でも OK
#
# スクリプトの起動時にプロンプトが出るので、 `y` を入力すると `-PwithExternalMods=true` が gradle に渡され、 `build.gradle` 側で:
#
# 1. ファイル名から artifact / version を自動推定
# 2. Maven 階層に自動配置 ( フラット配置の場合 )
# 3. `fg.deobf("local:<artifact>:<version>")` で取り込み ( SRG → official のリマップが効く )
#
# mod 名 / version を build.gradle に書き込む必要は無し。
#
# ## 環境変数 ( CI / 非対話用 )
#
# | 環境変数 | 効果 |
# |---|---|
# | `WITH_EXTERNAL_MODS=1` | プロンプトを `y` 扱いにする |
# | `SKIP_EXTERNAL_MODS_PROMPT=1` | プロンプトを `N` 扱いにする |
# | ( 後方互換 ) `WITH_SPELLBOOKS=1` | 同上 ( WITH_EXTERNAL_MODS と等価 ) |
# | ( 後方互換 ) `SKIP_SPELLBOOKS_PROMPT=1` | 同上 ( SKIP_EXTERNAL_MODS_PROMPT と等価 ) |
#
# ## TLS workaround について
#
# 開発端末の ISP ( マンション ) が Cisco Umbrella の透過 SSL 検査を経由しており、 中間 Proxy が古くて TLS 1.3 と ECDHE / DHE 系 cipher を理解できないことがあります。 そのままだと `maven.minecraftforge.net` への接続が handshake_failure で落ちるため、 `JAVA_TOOL_OPTIONS` で以下を強制しています:
#
# - TLS 1.2 のみ ( `-Djdk.tls.client.protocols=TLSv1.2` )
# - RSA 系 cipher のみ ( `-Djdk.tls.client.cipherSuites=...` )
# - 該当 cipher は JDK 17 でデフォルト無効化されているので `tls_workaround.properties` で再有効化
#
# 逆にテザリング等 で `maven.minecraftforge.net` の現代 TLS を直接叩ける場合は、 RSA-only 制約が邪魔になります。 その時は `notls` 引数を指定して workaround を切ってください。
#
# ## キャッシュ場所 ( オフライン起動の前提 )
#
# すべて gradle のキャッシュに乗っていれば `offline` で起動できます。
#
# - Forge userdev jar: `~/.gradle/caches/modules-2/files-2.1/net.minecraftforge/forge/<version>/<hash>/`
# - mappings / patches: `~/.gradle/caches/forge_gradle/`
# - deobf 済み依存 ( Curios / JEI 等 ): `~/.gradle/caches/forge_gradle/deobf_dependencies/`
# - libs/local の deobf 結果: 上と同じ場所にキャッシュされる
#
# 一度オンラインで `runClient` を完走すれば、 上記キャッシュが全部揃って以後 `offline` で動きます。
#
# ## オフライン環境で何を準備すれば良いか
#
# 1. **Forge userdev** を一度オンラインで取得 ( gradle が自動 DL )
# 2. **libs/local/** に使いたい外部 mod jar を全部入れる
# 3. ⇒ あとは `bash sh/run/run_client_mac.sh offline` ( または `run_client_windows.bat offline` ) でずっと オフライン起動可能
#
# ## トラブルシュート
#
# ### `handshake_failure` で落ちる
# TLS workaround の cipher と Forge maven が噛み合っていない。 テザリングなど別回線で:
# ```bash
# bash sh/run/run_client_mac.sh online notls
# ```
#
# ### `Cannot deobfuscate dependency of type DefaultSelfResolvingDependency_Decorated`
# 旧版の `fg.deobf(files(...))` が混ざっている。 現行 build.gradle は Maven 階層 + `fg.deobf("local:...")` で取り込むので発生しないはず。 もし出たら build.gradle が古い可能性。
#
# ### 「変更したフラグ ( notls 等 ) が効いていない」
# gradle daemon に古い JAVA_TOOL_OPTIONS が残っている。 デフォルトでスクリプトが `--stop` を打つので通常は発生しないが、 `keepdaemon` を付けて起動した場合は次回 `keepdaemon` 抜きで起動するか、 手動で `./gradlew --stop` を打つ。
#
# ### `ResourceLocation.fromNamespaceAndPath` で NoSuchMethodError
# 外部 mod が Forge 47.4.0 以降の API を使っている。 `FORGE_VERSION` を 1.20.1-47.4.0 に bump 必要 ( `gradle.properties` 参照 )。
#
# ### `Mod ID 'irons_spellbooks' requires forge [47.4.0,)`
# 同上。 Forge のバージョンを bump して再キャッシュ。
#
# ### Spellbooks を使いたいが Forge 47.4.0 を引けない
# - テザリングで `bash sh/run/run_client_mac.sh online notls` を一度実行して Forge 47.4.0 をキャッシュ
# - もしくは Spellbooks の旧版 ( 47.1.0 互換 ) を CurseForge から DL して libs/local/ に差し替え
#
# Minecraftクライアントを起動するスクリプト（mac / WSL / Windows Git Bash 対応）
#
# 使い方:
#   bash sh/run/run_client_mac.sh                オフライン ( 学校Wi-Fi向けのデフォルト )
#   bash sh/run/run_client_mac.sh online         オンライン ( TLS workaround は回線を実測して自動判定 )
#   bash sh/run/run_client_mac.sh offline        明示的なオフライン (従来互換)
#   bash sh/run/run_client_mac.sh notls          テザリング等 ( workaround off を強制 — 素の TLS )
#   bash sh/run/run_client_mac.sh tls            Cisco Umbrella 検査回線 ( workaround on を強制 )
#   bash sh/run/run_client_mac.sh offline notls  併用も可
#   bash sh/run/run_client_mac.sh keepdaemon     gradle daemon を kill しない ( デフォルトは kill )
#   bash sh/run/run_client_mac.sh help           この説明を表示して終了
#
# 起動時の質問:
#   1. 外部 mod をオンにしますか?
#      y = 下記の外部MODを同期して起動 / Enter または n = 本体MODだけで起動
#   2. 軽量化MODのみで起動しますか?
#      Enter または y = Embeddium + Oculus + extra_video_settings / VanillaLite / n = 追加機能MODも入れる
#   3. 負荷の大きいMODも入れますか? (2でnの場合)
#      y = TACZ/Mekanism系も追加 / Enter または n = 追加の2MODまで
#
# 追加機能MOD (軽量化MODのみでは入れない):
#   - chuzume-addon
#   - RPGish-HPDisplay (mh_rpgish)
#
# 全部入りで追加されるMOD:
#   - gun_and_weapon + TACZ
#   - Backpack Arsenal
#   - Mekanism
#   - Sophisticated Core + Sophisticated Backpacks
#
# 質問を省略する例:
#   WITH_EXTERNAL_MODS=1 PERFORMANCE_ONLY_MODS=1 bash sh/run/run_client_mac.sh  # Embeddium + Oculus + extra_video_settings / VanillaLite
#   WITH_EXTERNAL_MODS=1 LIGHT_EXTERNAL_MODS=1 bash sh/run/run_client_mac.sh offline  # 追加2MODも有効
#   WITH_EXTERNAL_MODS=1 LIGHT_EXTERNAL_MODS=0 bash sh/run/run_client_mac.sh offline  # 全部入り
#
# 起動前に外部MODを含めるか対話で尋ねる。選択したJARは
# libs/runtime_selected/ へ同期し、-PwithExternalMods=true で読み込む。


for shell_help_arg in "$@"; do
    case "$shell_help_arg" in
        help|--help|-h)
            awk 'NR == 1 { next } /^#/ { sub(/^# ?/, ""); print; next } /^[[:space:]]*$/ { print; next } { exit }' "$0"
            exit 0
            ;;
    esac
done

cd "$(dirname "$0")/../.."

show_help() {
    sed -n '2,/^$/p' "$0" | sed 's/^# \{0,1\}//'
}

for arg in "$@"; do
    case "$arg" in
        help|--help|-h)
            show_help
            exit 0
            ;;
    esac
done

GRADLE_ARGS="runClient --offline -x downloadAssets"
USE_TLS_WORKAROUND="auto"   # auto = 回線を実測して自動判定 ( notls / tls で明示上書き可 )
KILL_DAEMON="yes"   # JAVA_TOOL_OPTIONS / gradle.properties 変更が daemon に反映されない問題対策
OFFLINE_MODE="yes"
for arg in "$@"; do
    case "$arg" in
        offline)
            OFFLINE_MODE="yes"
            GRADLE_ARGS="runClient --offline -x downloadAssets"
            echo "=== Offline mode (using cached dependencies, skipping downloadAssets) ==="
            ;;
        online)
            OFFLINE_MODE="no"
            GRADLE_ARGS="runClient"
            echo "=== Online mode ==="
            ;;
        notls|no-tls)
            USE_TLS_WORKAROUND="no"
            echo "=== TLS workaround OFF (デフォルト TLS で接続 — テザリング等 直接回線向け) ==="
            ;;
        tls|force-tls|forcetls)
            USE_TLS_WORKAROUND="yes"
            echo "=== TLS workaround ON (強制 — Cisco Umbrella 検査回線向け) ==="
            ;;
        keepdaemon|keep-daemon)
            KILL_DAEMON="no"
            ;;
    esac
done

# --- TLS workaround 自動判定 ---
# 引数で notls / tls を明示しなかった場合、実際に素の TLS で Mojang の
# メタサーバへ到達できるかを curl で 1 回だけ試す。
#   到達できる ( テザリング等 直接回線 )        → workaround OFF
#   到達できない ( Cisco Umbrella 検査回線など ) → workaround ON
# これで `bash sh/run/run_client_mac.sh` を引数なしで両方の回線に対応させる。
if [ "$OFFLINE_MODE" = "yes" ] && [ "$USE_TLS_WORKAROUND" = "auto" ]; then
    USE_TLS_WORKAROUND="no"
    echo "=== Offline mode: TLS 回線判定をスキップ ==="
elif [ "$USE_TLS_WORKAROUND" = "auto" ]; then
    echo "=== TLS 回線を自動判定中 (piston-meta.mojang.com へ素の TLS で接続テスト) ==="
    if curl -s -o /dev/null --max-time 8 https://piston-meta.mojang.com/mc/game/version_manifest_v2.json 2>/dev/null; then
        USE_TLS_WORKAROUND="no"
        echo "=== 判定: 素の TLS で到達可能 → TLS workaround OFF ==="
    else
        USE_TLS_WORKAROUND="yes"
        echo "=== 判定: 素の TLS が不通 → TLS workaround ON (Cisco Umbrella 検査回線とみなす) ==="
    fi
fi

# --- gradle daemon を停止 ( JVM 引数 / 環境変数の変更を確実に反映させる ) ---
# 起動毎に 5 秒前後余分にかかるが、 「daemon が古い JAVA_TOOL_OPTIONS を保持していて
# notls / TLS workaround の切り替えが効かない」 問題を完全に潰せる。
# どうしても daemon を残したい時は引数に `keepdaemon` を追加 ( 既存 daemon が起動中ならそのまま使う )。
if [ "$KILL_DAEMON" = "yes" ]; then
    if [ -x ./gradlew ]; then
        echo "=== Stopping any running gradle daemon (--stop) ==="
        ./gradlew --stop > /dev/null 2>&1 || true
    fi
fi

# --- TLS workaround for Cisco Umbrella SSL inspection ---
# 開発端末のネット (マンション ISP) が Cisco Umbrella の透過 SSL 検査を経由しており、
# 中間 Proxy が古くて TLS 1.3 と ECDHE/DHE 系 cipher を理解できない。
# JDK 17 が ClientHello を送ると handshake_failure になるため、
# TLS 1.2 + RSA key exchange cipher のみに限定する。
# 該当 cipher は JDK 17 でデフォルト無効化されているので
# tls_workaround.properties で再有効化。
#
# JAVA_TOOL_OPTIONS は JVM 起動時に自動で picked up されるので
# Gradle daemon / worker 含む全 JVM に伝播する (org.gradle.jvmargs より確実)。
#
# `bash sh/run/run_client_mac.sh notls` で一時的に workaround を無効化できる
# ( テザリング等 で 直接 maven.minecraftforge.net に到達できる時に使用 )。
TLS_WORKAROUND_FILE="$(pwd)/tls_workaround.properties"
if [ "$OFFLINE_MODE" = "yes" ]; then
    # Gradle/Minecraftが認証・スキン・アセット取得を試みても、学校Wi-Fiへ出さず
    # localhost の閉じたポートで即時遮断する。Gradleは --offline なのでローカル依存のみ使う。
    export JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS:-} -Djava.net.useSystemProxies=false -Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=9 -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=9 -Dhttp.nonProxyHosts=localhost\|127.*\|[::1]"
    echo "=== Offline network block ON (HTTP/HTTPS を localhost で遮断) ==="
elif [ "$USE_TLS_WORKAROUND" = "yes" ] && [ -f "$TLS_WORKAROUND_FILE" ]; then
    export JAVA_TOOL_OPTIONS="-Djava.security.properties=${TLS_WORKAROUND_FILE} -Djdk.tls.client.protocols=TLSv1.2 -Dhttps.protocols=TLSv1.2 -Djdk.tls.client.cipherSuites=TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_128_GCM_SHA256,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA256"
fi

# --- 外部 mod 同梱の対話確認 ---
# 非対話環境 (CI等) での誤検出を防ぐため、標準入力が TTY の時のみ問う。
# 環境変数で明示指定もできる: WITH_EXTERNAL_MODS=1 / SKIP_EXTERNAL_MODS_PROMPT=1
# ( 後方互換 ) WITH_SPELLBOOKS=1 / SKIP_SPELLBOOKS_PROMPT=1 も従来通り受け付ける。
if [ -n "$WITH_EXTERNAL_MODS" ] || [ -n "$WITH_SPELLBOOKS" ]; then
    USE_EXTERNAL_MODS="yes"
elif [ -n "$SKIP_EXTERNAL_MODS_PROMPT" ] || [ -n "$SKIP_SPELLBOOKS_PROMPT" ]; then
    USE_EXTERNAL_MODS="no"
elif [ -t 0 ]; then
    printf "外部 mod をオンにしますか? [y/N]: "
    read ANSWER
    case "$ANSWER" in
        y|Y|yes|YES|Yes) USE_EXTERNAL_MODS="yes" ;;
        *)               USE_EXTERNAL_MODS="no"  ;;
    esac
else
    USE_EXTERNAL_MODS="no"
fi

if [ "$USE_EXTERNAL_MODS" = "yes" ]; then
    SYNC_ARGS=""
    case " $GRADLE_ARGS " in
        *" --offline "*) SYNC_ARGS="--offline" ;;
    esac
    # 従来の LIGHT_EXTERNAL_MODS 指定は追加機能MODの選択として維持する。
    USE_PERFORMANCE_ONLY_MODS="no"
    if [ "${PERFORMANCE_ONLY_MODS:-}" = "1" ]; then
        USE_PERFORMANCE_ONLY_MODS="yes"
    elif [ "${PERFORMANCE_ONLY_MODS:-}" = "0" ] \
            || [ "${LIGHT_EXTERNAL_MODS:-}" = "0" ] \
            || [ "${LIGHT_EXTERNAL_MODS:-}" = "1" ]; then
        USE_PERFORMANCE_ONLY_MODS="no"
    elif [ -t 0 ]; then
        printf "軽量化MODのみで起動しますか? (Embeddium + Oculus + extra_video_settings / VanillaLite) [Y/n]: "
        read -r PERFORMANCE_ANSWER
        case "$PERFORMANCE_ANSWER" in
            n|N|no|NO|No) USE_PERFORMANCE_ONLY_MODS="no" ;;
            *) USE_PERFORMANCE_ONLY_MODS="yes" ;;
        esac
    else
        USE_PERFORMANCE_ONLY_MODS="yes"
    fi

    USE_LIGHT_EXTERNAL_MODS="yes"
    if [ "$USE_PERFORMANCE_ONLY_MODS" = "yes" ]; then
        SYNC_ARGS="$SYNC_ARGS --performance-only"
    else
        if [ "${LIGHT_EXTERNAL_MODS:-}" = "0" ]; then
            USE_LIGHT_EXTERNAL_MODS="no"
        elif [ "${LIGHT_EXTERNAL_MODS:-}" = "1" ]; then
            USE_LIGHT_EXTERNAL_MODS="yes"
        elif [ -t 0 ]; then
            printf "負荷の大きいMODも入れますか? (TACZ / Gun and Weapon / Backpack Arsenal / Mekanism / Sophisticated) [y/N]: "
            read -r HEAVY_ANSWER
            case "$HEAVY_ANSWER" in
                y|Y|yes|YES|Yes) USE_LIGHT_EXTERNAL_MODS="no" ;;
                *) USE_LIGHT_EXTERNAL_MODS="yes" ;;
            esac
        fi
        if [ "$USE_LIGHT_EXTERNAL_MODS" = "yes" ]; then
            SYNC_ARGS="$SYNC_ARGS --light"
        fi
    fi
    if ! bash sh/maintenance/sync-selected-external-mods.sh $SYNC_ARGS; then
        echo "[error] 外部MODの同期に失敗したため、起動を中止します。" >&2
        exit 1
    fi
    GRADLE_ARGS="$GRADLE_ARGS -PwithExternalMods=true -PexternalModsGroup=runtime_selected"
    if [ "$USE_PERFORMANCE_ONLY_MODS" = "yes" ]; then
        echo "=== 外部 mod ON / 軽量化MODのみ (Embeddium + Oculus + extra_video_settings / VanillaLite切替対応) ==="
    elif [ "$USE_LIGHT_EXTERNAL_MODS" = "yes" ]; then
        echo "=== 外部 mod ON / 軽量化MOD + 追加機能MOD (重いMODなし) ==="
    else
        echo "=== 外部 mod ON / 全部入り (重いMODあり) ==="
    fi
    if [ -d libs/runtime_selected ]; then
        found=0
        while IFS= read -r jar; do
            echo "  → $jar"
            found=$((found + 1))
        done < <(find libs/runtime_selected -type f -name "*.jar" 2>/dev/null | sort)
        if [ "$found" = "0" ]; then
            echo "  ! 外部 mod jar がありません"
        fi
    else
        echo "  ! libs/runtime_selected/ の生成に失敗しました"
    fi
else
    echo "=== 外部 mod OFF ==="
fi

case "$(uname -s)" in
    MINGW*|CYGWIN*|MSYS*)
        ./gradlew.bat $GRADLE_ARGS
        ;;
    *)
        ./gradlew $GRADLE_ARGS
        ;;
esac
