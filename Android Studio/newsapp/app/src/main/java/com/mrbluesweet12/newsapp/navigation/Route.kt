package com.mrbluesweet12.newsapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomePageScreen

@Serializable
data class NewsArticleScreen(val url: String)

@Serializable
object AddNewsScreen

@Serializable
data class EditNewsScreen(val newsId: String)

@Serializable
data class LocalNewsDetailScreen(val newsId: String)
