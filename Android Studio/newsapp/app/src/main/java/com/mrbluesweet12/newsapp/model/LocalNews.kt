package com.mrbluesweet12.newsapp.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class LocalNews(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val category: String,
    val imageUri: String? = null, // Changed from imageUrl to imageUri for local gallery images
    val author: String = "User",
    val publishedAt: Long = System.currentTimeMillis(),
    val isLocal: Boolean = true
) {
    fun toArticle(): Article {
        return Article(
            source = Source(id = null, name = "Local News"),
            author = this.author,
            title = this.title,
            description = this.content.take(100) + "...",
            url = "local://news/${this.id}",
            urlToImage = this.imageUri, // Using imageUri instead of imageUrl
            publishedAt = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault()).format(java.util.Date(this.publishedAt)),
            content = this.content
        )
    }
}