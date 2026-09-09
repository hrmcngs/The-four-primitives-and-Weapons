#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
rack_test_dir=$(mktemp -d)
trap 'rm -rf "$rack_test_dir"' EXIT
cat > "$rack_test_dir/test.gradle" <<'GRADLE'
allprojects {
    afterEvaluate {
        tasks.register('compileRackDisplayTest', JavaCompile) {
            dependsOn tasks.named('classes')
            source = project.files(project.file('tests/RackDisplaySettingsTest.java'))
            classpath = project.sourceSets.main.runtimeClasspath
            destinationDirectory = project.layout.buildDirectory.dir('rackDisplayTest')
            options.compilerArgs += ['-proc:none']
        }
        tasks.register('testRackDisplay', JavaExec) {
            dependsOn tasks.named('compileRackDisplayTest')
            workingDir = project.layout.buildDirectory.dir('rackDisplayTest').get().asFile
            classpath = project.files(project.layout.buildDirectory.dir('rackDisplayTest')) + project.sourceSets.main.runtimeClasspath
            mainClass = 'RackDisplaySettingsTest'
        }
    }
}
GRADLE
./gradlew -I "$rack_test_dir/test.gradle" testRackDisplay --offline --console=plain
