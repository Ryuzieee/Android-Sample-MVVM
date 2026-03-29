package com.yamamuto.android_sample_mvvm.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yamamuto.android_sample_mvvm.ui.Strings
import com.yamamuto.android_sample_mvvm.ui.component.AppScaffold
import com.yamamuto.android_sample_mvvm.ui.component.EmptyContent
import com.yamamuto.android_sample_mvvm.ui.component.SearchTextField
import com.yamamuto.android_sample_mvvm.ui.component.UiStateContent

@Composable
fun SearchScreen(
    onPokemonClick: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(
        title = {
            SearchTextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChange,
                placeholder = Strings.Search.SEARCH_PLACEHOLDER,
            )
        },
        onBack = onBack,
    ) { padding ->
        UiStateContent(
            state = uiState.content,
            onRetry = viewModel::retrySearch,
            modifier = Modifier.padding(padding),
            idleContent = { SearchIdle(padding) },
        ) { names ->
            SearchResults(names, onPokemonClick, padding)
        }
    }
}

@Composable
private fun SearchIdle(padding: PaddingValues) {
    EmptyContent(
        message = Strings.Search.SEARCH_IDLE_MESSAGE,
        modifier = Modifier.padding(padding),
    )
}

@Composable
private fun SearchResults(
    names: List<String>,
    onPokemonClick: (String) -> Unit,
    padding: PaddingValues,
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
        items(names) { name ->
            ListItem(
                headlineContent = { Text(name.capitalize(Locale.current)) },
                modifier = Modifier.clickable { onPokemonClick(name) },
            )
            HorizontalDivider()
        }
    }
}
