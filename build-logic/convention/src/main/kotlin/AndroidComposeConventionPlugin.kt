import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Compose 有効化用 Convention Plugin。
 *
 * Android モジュールに Compose コンパイラプラグインと buildFeatures を適用する。
 */
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure<CommonExtension<*, *, *, *, *, *>> {
                buildFeatures {
                    compose = true
                }
            }
        }
    }
}
