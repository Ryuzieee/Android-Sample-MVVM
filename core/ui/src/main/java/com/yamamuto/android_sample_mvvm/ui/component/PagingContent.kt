package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamamuto.android_sample_mvvm.ui.util.asUiState
import kotlinx.coroutines.flow.Flow

/**
 * [Flow<PagingData<T>>] の状態に応じて Loading / Error / Grid を切り替える共通コンポーネント。
 *
 * Screen 側は Paging の API を意識せず、アイテムのリストとして扱える。
 *
 * ```kotlin
 * PagingContent(pagingData = uiState.pagingData) { items ->
 *     items(items.size) { index ->
 *         PokemonCard(item = items[index], ...)
 *     }
 * }
 * ```
 */
@Composable
fun <T : Any> PagingContent(
    pagingData: Flow<PagingData<T>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyGridScope.(items: List<T>) -> Unit,
) {
    val pagingItems = pagingData.collectAsLazyPagingItems()
    UiStateContent(
        state = pagingItems.loadState.refresh.asUiState { pagingItems },
        onRetry = { pagingItems.retry() },
        modifier = modifier,
    ) {
        val items = List(pagingItems.itemCount) { index -> pagingItems[index] }
            .filterNotNull()
        AppLazyVerticalGrid(contentPadding = contentPadding) {
            content(items)
            if (pagingItems.loadState.append is LoadState.Loading) {
                item { LoadingIndicator() }
            }
        }
    }
}
