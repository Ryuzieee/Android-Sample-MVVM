package com.yamamuto.android_sample_mvvm.data.paging

import androidx.paging.PagingSource

/**
 * [PagingSource] のファクトリインターフェース。
 *
 * ViewModel に注入して [UiStateViewModel.collectPaging] で使用する。
 */
fun interface PagingSourceFactory<T : Any> {
    fun create(): PagingSource<Int, T>
}
