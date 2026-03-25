import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Compose 有効化用 Convention Plugin。
 *
 * Android モジュールに Compose コンパイラプラグインと buildFeatures を適用する。
 * Stability 設定ファイルを指定して不要な recomposition を防止する。
 */
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.findByType<LibraryExtension>()?.buildFeatures { compose = true }
            extensions.findByType<BaseAppModuleExtension>()?.buildFeatures { compose = true }

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                val stabilityFile = rootProject.file("compose-stability.conf")
                if (stabilityFile.exists()) {
                    stabilityConfigurationFile.set(stabilityFile)
                }
            }
        }
    }
}
