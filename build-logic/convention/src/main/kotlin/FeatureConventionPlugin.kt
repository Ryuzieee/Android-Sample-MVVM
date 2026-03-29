import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

/**
 * Feature モジュール用 Convention Plugin。
 *
 * convention.android.library + convention.android.compose に加えて、
 * ktlint / Hilt / KSP / Serialization プラグインと
 * 全 feature モジュール共通の依存関係を一括で適用する。
 *
 * 各 feature の build.gradle.kts は namespace とモジュール固有の依存だけ書けばよい。
 */
class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("convention.android.library")
            pluginManager.apply("convention.android.compose")
            pluginManager.apply("org.jlleitschuh.gradle.ktlint")
            pluginManager.apply("com.google.dagger.hilt.android")
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            extensions.configure<LibraryExtension> {
                // :feature:list → com.yamamuto.android_sample_mvvm.feature.list
                namespace = "com.yamamuto.android_sample_mvvm.${path.removePrefix(":").replace(":", ".")}"

                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
            }

            tasks.withType<Test>().configureEach {
                failOnNoDiscoveredTests.set(false)
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("implementation", project(":core"))

                // Compose
                add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
                add("implementation", libs.findLibrary("androidx-compose-ui").get())
                add("implementation", libs.findLibrary("androidx-compose-material3").get())
                add("implementation", libs.findLibrary("androidx-compose-material-icons-core").get())

                // Lifecycle
                add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())

                // Navigation
                add("implementation", libs.findLibrary("androidx-navigation-compose").get())

                // Image loading
                add("implementation", libs.findLibrary("coil-compose").get())
                add("implementation", libs.findLibrary("coil-network-okhttp").get())

                // DI
                add("implementation", libs.findLibrary("hilt-android").get())
                add("implementation", libs.findLibrary("hilt-navigation-compose").get())
                add("ksp", libs.findLibrary("hilt-compiler").get())

                // Serialization
                add("implementation", libs.findLibrary("kotlinx-serialization-json").get())

                // Test
                add("testImplementation", project(":core"))
                add("testImplementation", libs.findLibrary("mockk").get())
                add("testImplementation", libs.findLibrary("turbine").get())
                add("testImplementation", libs.findLibrary("androidx-core-testing").get())
                add("testImplementation", libs.findLibrary("kotlinx-coroutines-test").get())
            }
        }
    }
}
