#!/usr/bin/env bash
# 通信軽量化の回帰テスト。Java 17が必要です。
# bash sh/test/test-performance.sh
# bash sh/test/test-performance.sh --full  実際のNBT・パケットも検証（Gradleのオフライン依存キャッシュが必要）。
# 初回・変更・値の復帰・強制同期・再接続を検証。ゲームのビルド出力は変更しません。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
case "${1:-}" in ""|--full) ;; *) echo "使い方: bash sh/test/test-performance.sh [--full]" >&2; exit 1;; esac
performance_test_dir=$(mktemp -d)
trap 'rm -rf "$performance_test_dir"' EXIT
javac -d "$performance_test_dir" src/main/java/the_four_primitives_and_weapons/performance/SyncSnapshot.java tests/SyncSnapshotTest.java
java -cp "$performance_test_dir" SyncSnapshotTest
if [[ "${1:-}" == --full ]]; then
    cat > "$performance_test_dir/test.gradle" <<'GRADLE'
gradle.beforeProject { project -> project.layout.buildDirectory.set(new File(System.getProperty('maw.performanceBuild'))) }
allprojects {
    afterEvaluate {
        tasks.register('compilePlayerSnapshotTest', JavaCompile) {
            dependsOn tasks.named('classes')
            source = project.files(project.file('tests/PlayerVariableSnapshotTest.java'))
            classpath = project.sourceSets.main.runtimeClasspath
            destinationDirectory = project.layout.buildDirectory.dir('performanceTest')
            options.compilerArgs += ['-proc:none']
        }
        tasks.register('testPlayerSnapshot', JavaExec) {
            dependsOn tasks.named('compilePlayerSnapshotTest')
            workingDir = project.layout.buildDirectory.dir('performanceTest').get().asFile
            classpath = project.files(project.layout.buildDirectory.dir('performanceTest')) + project.sourceSets.main.runtimeClasspath
            mainClass = 'PlayerVariableSnapshotTest'
        }
    }
}
GRADLE
    ./gradlew -Dmaw.performanceBuild="$performance_test_dir/build" -I "$performance_test_dir/test.gradle" testPlayerSnapshot --offline --console=plain
fi
