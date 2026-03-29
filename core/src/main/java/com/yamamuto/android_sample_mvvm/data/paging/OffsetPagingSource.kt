package com.yamamuto.android_sample_mvvm.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yamamuto.android_sample_mvvm.data.util.toAppException

/**
 * offset/limit ベースの汎用 [PagingSource]。
 *
 * [fetch] に offset と limit を渡してデータを取得するだけで Paging に対応できる。
 */
class OffsetPagingSource<T : Any>(
    private val fetch: suspend (offset: Int, limit: Int) -> List<T>,
) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val offset = params.key ?: 0
            val items = fetch(offset, params.loadSize)
            LoadResult.Page(
                data = items,
                prevKey = if (offset == 0) null else offset - params.loadSize,
                nextKey = if (items.isEmpty()) null else offset + params.loadSize,
            )
        } catch (e: Exception) {
            LoadResult.Error(e.toAppException())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey
        }
    }
}
