package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamamuto.android_sample_mvvm.ui.util.asUiState
import kotlinx.coroutines.flow.Flow

/**
 * Paging のアイテムと追加読み込み状態をまとめたデータクラス。
 *
 * Screen 側は Paging API を意識せず、通常のリストとして扱える。
 */
@Immutable
data class PagingState<T>(
    val items: List<T>,
    val isAppendLoading: Boolean,
)

/**
 * [Flow<PagingData<T>>] の初回読み込み状態に応じて Loading / Error / Content を切り替える共通コンポーネント。
 *
 * レイアウト (Grid / Column / Row) の選択は呼び出し側に委ねる。
 *
 * ```kotlin
 * PagingContent(pagingData = uiState.pagingData) { pagingState ->
 *     AppLazyVerticalGrid { items(pagingState.items) { ... } }
 * }
 * ```
 */
@Composable
fun <T : Any> PagingContent(
    pagingData: Flow<PagingData<T>>,
    modifier: Modifier = Modifier,
    content: @Composable (PagingState<T>) -> Unit,
) {
    val pagingItems = pagingData.collectAsLazyPagingItems()
    UiStateContent(
        state = pagingItems.loadState.refresh.asUiState { pagingItems },
        onRetry = { pagingItems.retry() },
        modifier = modifier,
    ) {
        val items = List(pagingItems.itemCount) { index -> pagingItems[index] }
            .filterNotNull()
        content(PagingState(items, pagingItems.loadState.append is LoadState.Loading))
    }
}
