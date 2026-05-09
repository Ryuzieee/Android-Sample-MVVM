package com.yamamuto.android_sample_mvvm.ui.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.core.R
import com.yamamuto.android_sample_mvvm.ui.component.AppIconButton
import com.yamamuto.android_sample_mvvm.ui.component.AppPullRefresh
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.AppText
import com.yamamuto.android_sample_mvvm.ui.component.UiStateContent
import com.yamamuto.android_sample_mvvm.ui.detail.view.PokemonDetailContent
import com.yamamuto.android_sample_mvvm.ui.detail.view.PokemonInfoBottomSheet
import com.yamamuto.android_sample_mvvm.ui.util.getOrNull

@Composable
fun PokemonDetailScreen(
    onBack: () -> Unit,
    onPokemonClick: (String) -> Unit = {},
    viewModel: PokemonDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showInfo by rememberSaveable { mutableStateOf(false) }

    val fullDetail = uiState.content.getOrNull()
    val displayName = fullDetail?.species?.japaneseName?.ifEmpty { null } ?: viewModel.pokemonName

    AppScaffold(
        title = {
            AppText(
                text = displayName,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        onBack = onBack,
        actions = {
            AppIconButton(
                imageVector = Icons.Filled.Info,
                contentDescription = stringResource(R.string.detail_info_button_description),
                onClick = { showInfo = true },
            )
            AppIconButton(
                imageVector = if (uiState.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription =
                    if (uiState.isFavorite) {
                        stringResource(R.string.detail_remove_favorite_description)
                    } else {
                        stringResource(R.string.detail_add_favorite_description)
                    },
                onClick = viewModel::toggleFavorite,
                tint = if (uiState.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            )
        },
    ) { padding ->
        AppPullRefresh(
            isRefreshing = uiState.isRefreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier.padding(padding),
        ) {
            UiStateContent(
                state = uiState.content,
                onRetry = viewModel::retry,
            ) { content ->
                PokemonDetailContent(
                    detail = content.detail,
                    species = content.species,
                    evolutionChain = content.evolutionChain,
                    onEvolutionClick = onPokemonClick,
                )
            }
        }
    }

    if (showInfo && fullDetail != null) {
        PokemonInfoBottomSheet(
            detail = fullDetail.detail,
            species = fullDetail.species,
            onDismiss = { showInfo = false },
        )
    }
}
