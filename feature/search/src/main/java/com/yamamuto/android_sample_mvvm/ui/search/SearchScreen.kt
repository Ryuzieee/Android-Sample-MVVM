package com.yamamuto.android_sample_mvvm.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.yamamuto.android_sample_mvvm.domain.model.PokemonDetail
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.ui.component.ErrorContent
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator
import com.yamamuto.android_sample_mvvm.ui.component.PokemonIdText
import com.yamamuto.android_sample_mvvm.ui.component.PokemonNameText

/**
 * ポケモン検索画面。
 *
 * ポケモン名を入力すると 500ms のデバウンス後に検索を実行し、結果をカードで表示する。
 * カードをタップすると詳細画面へ遷移する。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onPokemonClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val searchResult by viewModel.searchResult.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = query,
                        onValueChange = viewModel::onQueryChange,
                        placeholder = { Text("ポケモン名を入力...") },
                        singleLine = true,
                        colors =
                            TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                            ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        when {
            searchResult == null ->
                SearchIdleContent(modifier = Modifier.padding(padding))

            searchResult is UiState.Loading ->
                LoadingIndicator()

            searchResult is UiState.Error -> {
                val error = searchResult as UiState.Error
                ErrorContent(
                    message = error.message,
                    onRetry = { viewModel.onQueryChange(query) },
                    isNetworkError = error.isNetworkError,
                    modifier = Modifier.padding(padding),
                )
            }

            searchResult is UiState.Success -> {
                val detail = (searchResult as UiState.Success<PokemonDetail>).data
                SearchResultContent(
                    detail = detail,
                    onCardClick = { onPokemonClick(detail.name) },
                    modifier = Modifier.padding(padding),
                )
            }
        }
    }
}

/** 検索前のアイドル状態を表示するコンテンツ。 */
@Composable
private fun SearchIdleContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "ポケモン名を入力してください",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

/** 検索結果のポケモンカードコンポーネント。タップで詳細画面へ遷移する。 */
@Composable
private fun SearchResultContent(
    detail: PokemonDetail,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            onClick = onCardClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = detail.imageUrl,
                    contentDescription = detail.name,
                    modifier = Modifier.size(160.dp),
                )
                PokemonIdText(
                    id = detail.id,
                    modifier = Modifier.padding(top = 8.dp),
                )
                PokemonNameText(
                    name = detail.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    detail.types.forEach { type ->
                        AssistChip(
                            onClick = {},
                            label = { Text(type) },
                            modifier = Modifier.padding(horizontal = 4.dp),
                        )
                    }
                }
                Text(
                    text = "タップして詳細を見る",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
