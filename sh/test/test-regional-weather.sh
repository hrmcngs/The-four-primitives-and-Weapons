#!/usr/bin/env bash
# WMO参考天候のバイオーム制限・砂漠夜雨・保存同期をオフラインで検証します。
# Java 17とGradle依存キャッシュが必要です。
set -euo pipefail
cd "$(dirname "$0")/../.."
weather_test_dir=$(mktemp -d)
trap 'rm -rf "$weather_test_dir"' EXIT
javac -d "$weather_test_dir" src/main/java/the_four_primitives_and_weapons/weather/WeatherKind.java \
  src/main/java/the_four_primitives_and_weapons/weather/WeatherRules.java src/main/java/the_four_primitives_and_weapons/weather/WeatherSky.java src/main/java/the_four_primitives_and_weapons/weather/WeatherAtmosphere.java tests/RegionalWeatherRulesTest.java
java -cp "$weather_test_dir" RegionalWeatherRulesTest
cat > "$weather_test_dir/test.gradle" <<'GRADLE'
allprojects {
    afterEvaluate {
        tasks.register('compileRegionalWeatherTest', JavaCompile) {
            dependsOn tasks.named('classes')
            source = project.files(project.file('tests/RegionalWeatherPersistenceTest.java'))
            classpath = project.sourceSets.main.runtimeClasspath
            destinationDirectory = project.layout.buildDirectory.dir('regionalWeatherTest')
            options.compilerArgs += ['-proc:none']
        }
        tasks.register('testRegionalWeather', JavaExec) {
            dependsOn tasks.named('compileRegionalWeatherTest')
            workingDir = project.layout.buildDirectory.dir('regionalWeatherTest').get().asFile
            classpath = project.files(project.layout.buildDirectory.dir('regionalWeatherTest')) + project.sourceSets.main.runtimeClasspath
            mainClass = 'RegionalWeatherPersistenceTest'
        }
    }
}
GRADLE
./gradlew -I "$weather_test_dir/test.gradle" testRegionalWeather --offline --console=plain
