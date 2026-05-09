package com.yamamuto.android_sample_mvvm.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.yamamuto.android_sample_mvvm.core.R

/**
 * [ErrorType] をユーザー向けの表示文字列に解決する。
 *
 * stringResource を使うため Composable 内でのみ呼び出せる。これにより
 * 文言は res/values/strings.xml に集約され、Configuration 切替や i18n に追従する。
 */
@Composable
@ReadOnlyComposable
fun ErrorType.userMessage(): String =
    when (this) {
        ErrorType.General -> stringResource(R.string.error_unknown)
        ErrorType.Network -> stringResource(R.string.error_network)
        ErrorType.SessionExpired -> stringResource(R.string.error_session_expired)
        is ErrorType.ForceUpdate -> stringResource(R.string.error_force_update)
        is ErrorType.NotFound -> stringResource(R.string.error_not_found, query)
        is ErrorType.Server -> stringResource(R.string.error_server, code)
        is ErrorType.Unknown -> rawMessage ?: stringResource(R.string.error_unknown)
    }
