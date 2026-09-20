#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/../.."
command_test_dir=$(mktemp -d)
trap 'rm -rf "$command_test_dir"' EXIT
cat > "$command_test_dir/test.gradle" <<'GRADLE'
allprojects {
    afterEvaluate {
        tasks.register('compileCommandLocalizationTest', JavaCompile) {
            dependsOn tasks.named('classes')
            source = project.files('tests/CommandLocalizationTest.java')
            classpath = project.sourceSets.main.runtimeClasspath
            destinationDirectory = project.layout.buildDirectory.dir('commandLocalizationTest')
            options.compilerArgs += ['-proc:none']
        }
        tasks.register('testCommandLocalization', JavaExec) {
            dependsOn tasks.named('compileCommandLocalizationTest')
            workingDir = project.layout.buildDirectory.dir('commandLocalizationTest').get().asFile
            args project.projectDir.absolutePath
            classpath = project.files(project.layout.buildDirectory.dir('commandLocalizationTest')) + project.sourceSets.main.runtimeClasspath
            mainClass = 'CommandLocalizationTest'
        }
    }
}
GRADLE
./gradlew -I "$command_test_dir/test.gradle" testCommandLocalization --offline --console=plain --max-workers=1
