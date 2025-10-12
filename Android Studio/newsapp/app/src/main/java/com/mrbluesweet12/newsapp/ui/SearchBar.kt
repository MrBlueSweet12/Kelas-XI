package com.mrbluesweet12.newsapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.mrbluesweet12.newsapp.viewmodel.NewsViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchBar(
    newsViewModel: NewsViewModel,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    if (isExpanded) {
        // Auto search with debounce
        LaunchedEffect(searchQuery) {
            if (searchQuery.isNotEmpty()) {
                delay(500) // Debounce 500ms
                newsViewModel.fetchEverythingWithQuery(searchQuery)
            } else {
                // If search is empty, return to current category
                newsViewModel.fetchNewsTopHeadlines()
            }
        }
        
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Cari berita berdasarkan judul...") },
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onExpandedChange(false)
                        onSearchQueryChange("")
                        // Return to current category when closing search
                        newsViewModel.fetchNewsTopHeadlines()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            },
            singleLine = true
        )
    }
}
