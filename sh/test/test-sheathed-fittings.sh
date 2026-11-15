#!/usr/bin/env bash
# 納刀時の鍔形状・柄巻・素材tint・陰影・鞘本体の保持を検証。
set -euo pipefail
cd "$(dirname "$0")/../.."
sheathed_test_dir=$(mktemp -d)
trap 'rm -rf "$sheathed_test_dir"' EXIT
cat > "$sheathed_test_dir/test.gradle" <<'GRADLE'
allprojects {
    afterEvaluate {
        tasks.register('compileSheathedFittingsTest', JavaCompile) {
            dependsOn tasks.named('classes')
            source = project.files(project.file('tests/SheathedFittingsTest.java'))
            classpath = project.sourceSets.main.runtimeClasspath
            destinationDirectory = project.layout.buildDirectory.dir('sheathedFittingsTest')
            options.compilerArgs += ['-proc:none']
        }
        tasks.register('testSheathedFittings', JavaExec) {
            dependsOn tasks.named('compileSheathedFittingsTest')
            workingDir = project.layout.buildDirectory.dir('sheathedFittingsTest').get().asFile
            classpath = project.files(project.layout.buildDirectory.dir('sheathedFittingsTest')) + project.sourceSets.main.runtimeClasspath
            mainClass = 'SheathedFittingsTest'
        }
    }
}
GRADLE
./gradlew -I "$sheathed_test_dir/test.gradle" testSheathedFittings --offline --console=plain
