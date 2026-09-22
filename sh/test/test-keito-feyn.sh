#!/usr/bin/env bash
# 計都星の刀の初期Feynと既存NBT保持を検証。Java 17 / Gradleキャッシュが必要。
set -euo pipefail
cd "$(dirname "$0")/../.."
keito_test_dir=$(mktemp -d)
trap 'rm -rf "$keito_test_dir"' EXIT
cat > "$keito_test_dir/test.gradle" <<'GRADLE'
allprojects {
    afterEvaluate {
        tasks.register('compileKeitoTest', JavaCompile) {
            dependsOn tasks.named('classes')
            source = project.files('tests/KeitoFeynTest.java')
            classpath = project.sourceSets.main.runtimeClasspath
            destinationDirectory = project.layout.buildDirectory.dir('keitoTest')
            options.compilerArgs += ['-proc:none']
        }
        tasks.register('testKeito', JavaExec) {
            dependsOn tasks.named('compileKeitoTest')
            workingDir = project.layout.buildDirectory.dir('keitoTest').get().asFile
            classpath = project.files(project.layout.buildDirectory.dir('keitoTest')) + project.sourceSets.main.runtimeClasspath
            mainClass = 'KeitoFeynTest'
        }
    }
}
GRADLE
./gradlew -I "$keito_test_dir/test.gradle" testKeito --offline --console=plain --no-daemon
