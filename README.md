# Clean Architecture Base - Android

# Architecture
- The Base App follows the [official architecture guidance](https://developer.android.com/topic/architecture?hl=ko)
- [Architecture Learning Journey](https://github.com/android/nowinandroid/blob/main/docs/ArchitectureLearningJourney.md#ui-layer)

# [Modularization](https://github.com/android/nowinandroid/blob/main/docs/ModularizationLearningJourney.md)

# Build

# Testing

# UI
The Screens and UI elements are built entirely using [Jetpack Compose.](https://developer.android.com/jetpack/compose?hl=ko)

The app has two themes:
    - Dynamic color - uses colors based on the user's current color theme (if supported)
    - Default theme - uses predefined colors when dynamic color is not supported
Each theme also supports dark mode.

- [UI architecture](https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes?hl=ko)
- [Now In Android Case Study](https://www.figma.com/community/file/1164313362327941158)

# Performance
## Benchmarks
앱 시작과 복잡한 UI 조작(RecyclerView 스크롤 또는 애니메이션 실행)과 같이 앱의 대규모 사용 사례를 테스트하는 데 Macrobenchmark 라이브러리를 사용합니다.
코드의 작은 영역을 테스트하려면 Microbenchmark 라이브러리를 참고하세요. 
이 페이지에서는 Macrobenchmark 라이브러리를 설정하는 방법을 보여줍니다.
라이브러리는 Android 스튜디오 콘솔과 JSON 파일 양쪽에 자세한 정보가 포함된 벤치마킹 결과를 출력합니다. 또한 Android 스튜디오에서 로드하고 분석할 수 있는 트레이스 파일도 제공합니다.
지속적 통합의 벤치마크에 설명된 대로 지속적 통합(CI) 환경에서 Macrobenchmark 라이브러리를 사용하세요.
Macrobenchmark를 사용하여 기준 프로필을 생성할 수 있습니다. 먼저 Macrobenchmark 라이브러리를 설정하고 기준 프로필을 생성하면 됩니다.

- [Macrobenchmark](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-overview?hl=ko)

## [Baseline profiles](https://developer.android.com/studio/profile/baselineprofiles?hl=ko)
The baseline profile for this app is located at app/src/main/baseline-prof.txt. It contains rules that enable AOT compilation of the critical user path taken during app launch. For more information on baseline profiles, read this document.

**Note**
- The baseline profile needs to be re-generated for release builds that touch code which changes app startup.

To generate the baseline profile, select the benchmark build variant and run the BaselineProfileGenerator benchmark test on an AOSP Android Emulator. 
Then copy the resulting baseline profile from the emulator to `app/src/main/baseline-prof.txt.`

## Compose compiler metrics
Run the following command to get and analyse compose compiler metrics:

`./gradlew assembleRelease -PenableComposeCompilerMetrics=true -PenableComposeCompilerReports=true`

The reports files will be added to build/compose-reports. The metrics files will also be added to build/compose-metrics.

For more information on Compose compiler metrics, see [this blog post.](https://medium.com/androiddevelopers/jetpack-compose-stability-explained-79c10db270c8)
# Reference
- [Now in Android App](https://github.com/android/nowinandroid)