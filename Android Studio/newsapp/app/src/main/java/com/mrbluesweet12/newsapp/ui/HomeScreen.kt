package com.mrbluesweet12.newsapp.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mrbluesweet12.newsapp.viewmodel.NewsViewModel
import com.mrbluesweet12.newsapp.model.Article
import com.mrbluesweet12.newsapp.navigation.AddNewsScreen

@Composable
fun HomeScreen(
    newsViewModel: NewsViewModel,
    navController: NavController
) {
    // Use combined articles that include both API and local news
    val combinedArticles by newsViewModel.combinedArticles.observeAsState(emptyList())
    val currentCategory by newsViewModel.currentCategoryLiveData.observeAsState("general")
    val localNews by newsViewModel.localNews.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var showDebug by remember { mutableStateOf(true) } // Show debug by default
    
    // Debug logging
    LaunchedEffect(combinedArticles) {
        Log.d("HomeScreen", "Combined articles updated: ${combinedArticles.size}")
        combinedArticles.forEach { article ->
            Log.d("HomeScreen", "Article: ${article.title} - Source: ${article.source.name}")
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Debug Panel (toggleable)
            if (showDebug) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "🐛 DEBUG INFO",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = "Current Category: $currentCategory",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = "Total Local News: ${localNews.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        val localFilteredCount = localNews.count { 
                            it.category.lowercase() == currentCategory.lowercase() 
                        }
                        Text(
                            text = "Local News in '$currentCategory': $localFilteredCount",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = "Combined Articles: ${combinedArticles.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        
                        // Show all local news with their categories
                        if (localNews.isNotEmpty()) {
                            Text(
                                text = "All Local News:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            localNews.forEach { news ->
                                Text(
                                    text = "  • ${news.title} (${news.category})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                        
                        // Toggle debug button
                        Button(
                            onClick = { showDebug = false },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Hide Debug")
                        }
                    }
                }
            } else {
                // Show Debug button when debug is hidden
                Button(
                    onClick = { showDebug = true },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Show Debug")
                }
            }
            
            // Search Bar (when expanded)
            SearchBar(
                newsViewModel = newsViewModel,
                isExpanded = isSearchExpanded,
                onExpandedChange = { isSearchExpanded = it },
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
            
            // Categories Bar (when search is not expanded)
            if (!isSearchExpanded) {
                CategoriesBar(
                    newsViewModel = newsViewModel,
                    onSearchClick = { 
                        isSearchExpanded = true
                        searchQuery = ""
                    }
                )
            }
            
            // Articles List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 0.dp)
            ) {
                items(combinedArticles.size) { index ->
                    combinedArticles[index].let { article ->
                        ArticleItem(
                            article = article,
                            navController = navController,
                            newsViewModel = newsViewModel
                        )
                    }
                }
            }
        }
        
        // Floating Action Button untuk menambah berita
        FloatingActionButton(
            onClick = {
                navController.navigate(AddNewsScreen)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Tambah Berita"
            )
        }
    }
}
