package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamamuto.android_sample_mvvm.ui.util.asUiState
import kotlinx.coroutines.flow.Flow

/**
 * [Flow<PagingData<T>>] の状態に応じて Loading / Error / Content を切り替える共通コンポーネント。
 *
 * `collectAsLazyPagingItems` / `loadState` のマッピング / リトライをすべてラップする。
 *
 * ```kotlin
 * PagingContent(pagingData = uiState.pagingData, modifier = Modifier.padding(padding)) { items ->
 *     LazyVerticalGrid(...) { items(items.itemCount) { ... } }
 * }
 * ```
 */
@Composable
fun <T : Any> PagingContent(
    pagingData: Flow<PagingData<T>>,
    modifier: Modifier = Modifier,
    content: @Composable (LazyPagingItems<T>) -> Unit,
) {
    val pagingItems = pagingData.collectAsLazyPagingItems()
    UiStateContent(
        state = pagingItems.loadState.refresh.asUiState { pagingItems },
        onRetry = { pagingItems.retry() },
        modifier = modifier,
    ) { items ->
        content(items)
    }
}
