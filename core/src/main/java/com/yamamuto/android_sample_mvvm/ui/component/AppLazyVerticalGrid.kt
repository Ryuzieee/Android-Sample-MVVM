package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val LOAD_MORE_THRESHOLD = 4

/**
 * アプリ共通の LazyVerticalGrid。2カラムをデフォルトで適用。
 *
 * [onLoadMore] を指定すると、末尾付近までスクロールしたときに自動で呼び出される。
 */
@Composable
fun AppLazyVerticalGrid(
    modifier: Modifier = Modifier,
    columns: GridCells = GridCells.Fixed(2),
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onLoadMore: (() -> Unit)? = null,
    content: LazyGridScope.() -> Unit,
) {
    if (onLoadMore != null) {
        val shouldLoadMore by remember {
            derivedStateOf {
                val lastVisible =
                    state.layoutInfo.visibleItemsInfo
                        .lastOrNull()
                        ?.index ?: 0
                lastVisible >= state.layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
            }
        }

        LaunchedEffect(shouldLoadMore) {
            if (shouldLoadMore) onLoadMore()
        }
    }

    LazyVerticalGrid(
        columns = columns,
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        content = content,
    )
}
