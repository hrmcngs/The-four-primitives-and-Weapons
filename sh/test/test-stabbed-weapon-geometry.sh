#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
geometry_test_dir=$(mktemp -d)
trap 'rm -rf "$geometry_test_dir"' EXIT
cat > "$geometry_test_dir/test.gradle" <<'GRADLE'
allprojects {
    afterEvaluate {
        tasks.register('compilePlantedGeometryTest', JavaCompile) {
            dependsOn tasks.named('classes')
            source = project.files(project.file('tests/StabbedWeaponGeometryTest.java'))
            classpath = project.sourceSets.main.runtimeClasspath
            destinationDirectory = project.layout.buildDirectory.dir('plantedGeometryTest')
            options.compilerArgs += ['-proc:none']
        }
        tasks.register('testPlantedGeometry', JavaExec) {
            dependsOn tasks.named('compilePlantedGeometryTest')
            workingDir = project.layout.buildDirectory.dir('plantedGeometryTest').get().asFile
            classpath = project.files(project.layout.buildDirectory.dir('plantedGeometryTest')) + project.sourceSets.main.runtimeClasspath
            mainClass = 'StabbedWeaponGeometryTest'
        }
    }
}
GRADLE
./gradlew -I "$geometry_test_dir/test.gradle" testPlantedGeometry --offline --console=plain
