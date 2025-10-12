package com.mrbluesweet12.newsapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.mrbluesweet12.newsapp.model.Article
import com.mrbluesweet12.newsapp.navigation.NewsArticleScreen
import com.mrbluesweet12.newsapp.navigation.LocalNewsDetailScreen
import com.mrbluesweet12.newsapp.viewmodel.NewsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleItem(
    article: Article,
    navController: NavController,
    newsViewModel: NewsViewModel? = null
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    // Check if this is a local news article
    val isLocalNews = article.url?.startsWith("local://") == true
    
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (isLocalNews) {
                        // Extract news ID from local URL (format: local://news/[ID])
                        val newsId = article.url?.substringAfterLast("/")
                        if (newsId != null) {
                            navController.navigate(LocalNewsDetailScreen(newsId))
                        }
                    } else {
                        article.url?.let { url ->
                            navController.navigate(NewsArticleScreen(url))
                        }
                    }
                },
                onLongClick = {
                    if (isLocalNews && newsViewModel != null) {
                        showDeleteDialog = true
                    }
                }
            ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLocalNews) Color(0xFFF5F5DC) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = article.urlToImage ?: "https://via.placeholder.com/80x80?text=No+Image",
                contentDescription = article.title,
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                // Error fallback for local URIs
                error = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_gallery)
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLocalNews) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Local News",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = article.title ?: "No Title",
                        fontWeight = FontWeight.Bold,
                        maxLines = 3,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = article.source.name ?: "Unknown Source",
                        fontSize = 14.sp,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (isLocalNews && newsViewModel != null) {
                        IconButton(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete News",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Berita") },
            text = { Text("Apakah Anda yakin ingin menghapus berita ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Extract ID from local URL
                        val newsId = article.url?.substringAfterLast("/")
                        if (newsId != null && newsViewModel != null) {
                            newsViewModel.deleteLocalNews(newsId)
                        }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Batal")
                }
            }
        )
    }
}
