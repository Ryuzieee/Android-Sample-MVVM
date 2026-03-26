package com.yamamuto.android_sample_mvvm.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.EmptyContent
import com.yamamuto.android_sample_mvvm.ui.component.ErrorContent
import com.yamamuto.android_sample_mvvm.ui.component.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onPokemonClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(
        title = {
            TextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChange,
                placeholder = { Text("ポケモン名を入力...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        onBack = onBack,
    ) { padding ->
        when (val result = uiState.result) {
            null ->
                EmptyContent(
                    message = "ポケモン名を入力してください",
                    modifier = Modifier.padding(padding),
                )

            is UiState.Loading ->
                LoadingIndicator()

            is UiState.Error ->
                ErrorContent(
                    message = result.message,
                    onRetry = viewModel::retrySearch,
                    isNetworkError = result.isNetworkError,
                    modifier = Modifier.padding(padding),
                )

            is UiState.Success ->
                LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                    items(result.data) { name ->
                        ListItem(
                            headlineContent = { Text(name.capitalize(Locale.current)) },
                            modifier = Modifier.clickable { onPokemonClick(name) },
                        )
                        HorizontalDivider()
                    }
                }
        }
    }
}
