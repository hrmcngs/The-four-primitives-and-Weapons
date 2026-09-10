#!/usr/bin/env bash
# 蝶の見た目設定・触角モデルの回帰テスト。Java 17とGradleのオフライン依存キャッシュが必要。
# bash sh/test/test-butterfly-appearance.sh
# 色・形状のNBT、不正値、既定値への復帰、触角の根元・角度・長さを検証。
# 一時ディレクトリでコンパイルし、起動中のゲームのbuild出力は変更しません。
set -euo pipefail
cd "$(dirname "$0")/../.."
case "${1:-}" in help|--help|-h) awk 'NR==1 {next} /^#/ {sub(/^# ?/, ""); print; next} {exit}' "$0"; exit 0;; esac
butterfly_test_dir=$(mktemp -d)
trap 'rm -rf "$butterfly_test_dir"' EXIT
cat > "$butterfly_test_dir/test.gradle" <<'GRADLE'
gradle.beforeProject { project -> project.layout.buildDirectory.set(new File(System.getProperty('maw.butterflyTestBuild'))) }
allprojects {
    afterEvaluate {
        tasks.register('compileButterflyAppearanceTest', JavaCompile) {
            dependsOn tasks.named('classes')
            source = project.files(project.file('tests/ButterflyAppearanceTest.java'))
            classpath = project.sourceSets.main.runtimeClasspath
            destinationDirectory = project.layout.buildDirectory.dir('performanceTest')
            options.compilerArgs += ['-proc:none']
        }
        tasks.register('testButterflyAppearance', JavaExec) {
            dependsOn tasks.named('compileButterflyAppearanceTest')
            workingDir = project.layout.buildDirectory.dir('performanceTest').get().asFile
            classpath = project.files(project.layout.buildDirectory.dir('performanceTest')) + project.sourceSets.main.runtimeClasspath
            mainClass = 'ButterflyAppearanceTest'
        }
    }
}
GRADLE
./gradlew -Dmaw.butterflyTestBuild="$butterfly_test_dir/build" -I "$butterfly_test_dir/test.gradle" testButterflyAppearance --offline --console=plain
