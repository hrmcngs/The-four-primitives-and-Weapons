#!/usr/bin/env bash
# 天文周期・流星の分布・手動設定・保存・同期・コマンド生成を検証します。
# Java 17 / SBCL / Gradle依存キャッシュが必要です。オフライン実行。
set -euo pipefail
cd "$(dirname "$0")/../.."
astronomy_test_dir=$(mktemp -d)
trap 'rm -rf "$astronomy_test_dir"' EXIT
javac -d "$astronomy_test_dir" src/main/java/the_four_primitives_and_weapons/world/LunarCycle.java \
  src/main/java/the_four_primitives_and_weapons/world/AstronomicalEvents.java \
  src/main/java/the_four_primitives_and_weapons/world/AstronomySettings.java tests/AstronomicalEventsTest.java
java -cp "$astronomy_test_dir" AstronomicalEventsTest
bash sh/generate/generate-time-commands.sh --self-test
cat > "$astronomy_test_dir/test.gradle" <<'GRADLE'
allprojects {
    afterEvaluate {
        tasks.register('compileAstronomyTest', JavaCompile) {
            dependsOn tasks.named('classes')
            source = project.files(project.file('tests/AstronomyPersistenceTest.java'))
            classpath = project.sourceSets.main.runtimeClasspath
            destinationDirectory = project.layout.buildDirectory.dir('astronomyTest')
            options.compilerArgs += ['-proc:none']
        }
        tasks.register('testAstronomyPersistence', JavaExec) {
            dependsOn tasks.named('compileAstronomyTest')
            workingDir = project.layout.buildDirectory.dir('astronomyTest').get().asFile
            classpath = project.files(project.layout.buildDirectory.dir('astronomyTest')) + project.sourceSets.main.runtimeClasspath
            mainClass = 'AstronomyPersistenceTest'
        }
    }
}
GRADLE
./gradlew -I "$astronomy_test_dir/test.gradle" testAstronomyPersistence --offline --console=plain
