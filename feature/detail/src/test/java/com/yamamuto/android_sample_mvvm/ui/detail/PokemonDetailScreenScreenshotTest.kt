package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetailModel
import com.yamamuto.android_sample_mvvm.ui.component.ErrorContent
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * Roborazzi を使ったスクリーンショットテスト。
 *
 * 実行方法:
 * - 記録: ./gradlew :feature:detail:recordRoborazziDebug
 * - 比較: ./gradlew :feature:detail:verifyRoborazziDebug
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class PokemonDetailScreenScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val fakePokemonDetail =
        PokemonDetailModel(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            baseExperience = 64,
            types = listOf("grass", "poison"),
            abilities =
                listOf(
                    PokemonDetailModel.Ability("overgrow", "しんりょく", isHidden = false),
                    PokemonDetailModel.Ability("chlorophyll", "ようりょくそ", isHidden = true),
                ),
            imageUrl = "",
            stats =
                listOf(
                    PokemonDetailModel.Stat("hp", 45),
                    PokemonDetailModel.Stat("attack", 49),
                    PokemonDetailModel.Stat("defense", 49),
                ),
        )

    @Test
    fun loadingState() {
        composeRule.setContent {
            MaterialTheme {
                LoadingIndicator()
            }
        }
        composeRule.onRoot().captureRoboImage("screenshots/detail_loading.png")
    }

    @Test
    fun errorState() {
        composeRule.setContent {
            MaterialTheme {
                ErrorContent(
                    message = "ネットワークに接続できません",
                    onRetry = {},
                    isNetworkError = true,
                )
            }
        }
        composeRule.onRoot().captureRoboImage("screenshots/detail_error.png")
    }
}
