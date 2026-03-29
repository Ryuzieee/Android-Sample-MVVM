package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.yamamuto.android_sample_mvvm.ui.util.asUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * [Flow<PagingData<T>>] の初回読み込み状態に応じて Loading / Error / Content を切り替える共通コンポーネント。
 *
 * [LazyPagingItems] をそのまま渡すことで、末端到達時の自動 prefetch を維持する。
 * レイアウト (Grid / Column / Row) の選択は呼び出し側に委ねる。
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
    ) {
        content(pagingItems)
    }
}

/**
 * [PagingData] を直接受け取るオーバーロード。
 *
 * ViewModel が `collect { updateState }` で [PagingData] を状態として保持するケースで使用する。
 */
@Composable
fun <T : Any> PagingContent(
    pagingData: PagingData<T>,
    modifier: Modifier = Modifier,
    content: @Composable (LazyPagingItems<T>) -> Unit,
) {
    val flow = remember { MutableStateFlow(PagingData.empty<T>()) }
    LaunchedEffect(pagingData) { flow.emit(pagingData) }
    PagingContent(pagingData = flow, modifier = modifier, content = content)
}

/** append（次ページ）が読み込み中かどうか。 */
val <T : Any> LazyPagingItems<T>.isAppendLoading: Boolean
    get() {
        return loadState.append is LoadState.Loading
    }
