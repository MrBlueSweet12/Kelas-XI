package com.mrbluesweet12.newsapp.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.mrbluesweet12.newsapp.model.LocalNews
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class LocalNewsRepository(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "local_news_prefs"
        private const val KEY_LOCAL_NEWS = "local_news_list"
    }
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val json = Json { 
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    private val _localNews = MutableStateFlow<List<LocalNews>>(emptyList())
    val localNews: StateFlow<List<LocalNews>> = _localNews.asStateFlow()

    init {
        loadNewsFromStorage()
    }

    private fun loadNewsFromStorage() {
        try {
            val newsJson = sharedPreferences.getString(KEY_LOCAL_NEWS, "[]")
            val newsList = json.decodeFromString<List<LocalNews>>(newsJson ?: "[]")
            _localNews.value = newsList
            Log.d("LocalNewsRepository", "Loaded ${newsList.size} news items from storage")
        } catch (e: Exception) {
            Log.e("LocalNewsRepository", "Error loading news from storage", e)
            _localNews.value = emptyList()
        }
    }

    private fun saveNewsToStorage() {
        try {
            val newsJson = json.encodeToString(_localNews.value)
            sharedPreferences.edit()
                .putString(KEY_LOCAL_NEWS, newsJson)
                .apply()
            Log.d("LocalNewsRepository", "Saved ${_localNews.value.size} news items to storage")
        } catch (e: Exception) {
            Log.e("LocalNewsRepository", "Error saving news to storage", e)
        }
    }

    fun addNews(news: LocalNews) {
        val currentList = _localNews.value.toMutableList()
        currentList.add(0, news) // Add to beginning of list
        _localNews.value = currentList
        saveNewsToStorage() // Persist to storage
    }

    fun getNewsByCategory(category: String): List<LocalNews> {
        return _localNews.value.filter { 
            it.category.equals(category, ignoreCase = true) 
        }
    }

    fun getAllNews(): List<LocalNews> {
        return _localNews.value
    }

    fun deleteNews(newsId: String) {
        val currentList = _localNews.value.toMutableList()
        currentList.removeAll { it.id == newsId }
        _localNews.value = currentList
        saveNewsToStorage() // Persist to storage
    }
    
    fun updateNews(newsId: String, updatedNews: LocalNews) {
        val currentList = _localNews.value.toMutableList()
        val newsIndex = currentList.indexOfFirst { it.id == newsId }
        if (newsIndex != -1) {
            // Keep the original ID and timestamps
            val finalUpdatedNews = updatedNews.copy(
                id = newsId,
                publishedAt = currentList[newsIndex].publishedAt
            )
            currentList[newsIndex] = finalUpdatedNews
            _localNews.value = currentList
            saveNewsToStorage() // Persist to storage
            Log.d("LocalNewsRepository", "Updated news: ${updatedNews.title}")
        }
    }
    
    fun getNewsById(newsId: String): LocalNews? {
        return _localNews.value.find { it.id == newsId }
    }
}