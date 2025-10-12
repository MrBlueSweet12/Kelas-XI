package com.mrbluesweet12.newsapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrbluesweet12.newsapp.model.Article
import com.mrbluesweet12.newsapp.model.LocalNews
import com.mrbluesweet12.newsapp.model.NewsResponse
import com.mrbluesweet12.newsapp.util.Constants
import com.mrbluesweet12.newsapp.api.NewsApiClient
import com.mrbluesweet12.newsapp.repository.LocalNewsRepository
import android.util.Log
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel(context: Context) : ViewModel() {
    private val localNewsRepository = LocalNewsRepository(context)
    
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles
    
    private val _localNews = MutableLiveData<List<LocalNews>>()
    val localNews: LiveData<List<LocalNews>> = _localNews
    
    private val _combinedArticles = MutableLiveData<List<Article>>()
    val combinedArticles: LiveData<List<Article>> = _combinedArticles
    
    // Add LiveData for current category to make it observable
    private val _currentCategory = MutableLiveData<String>("general")
    val currentCategoryLiveData: LiveData<String> = _currentCategory
    
    private var currentCategory = "general"
        set(value) {
            field = value
            _currentCategory.postValue(value)
        }

    init {
        // Observe changes in local news repository
        viewModelScope.launch {
            localNewsRepository.localNews.collect { localNewsList ->
                Log.d("NewsViewModel", "Local news updated: ${localNewsList.size} items")
                _localNews.postValue(localNewsList)
                combineArticles(_articles.value ?: emptyList())
            }
        }
        fetchNewsTopHeadlines()
    }

    fun fetchNewsTopHeadlines(category: String = "general") {
        Log.d("NewsViewModel", "=== SWITCHING CATEGORY ===")
        Log.d("NewsViewModel", "From: $currentCategory -> To: $category")
        
        currentCategory = category.lowercase()
        Log.d("NewsViewModel", "Current category updated to: $currentCategory")
        
        // Immediately update combined articles with new category filter before API call
        combineArticles(_articles.value ?: emptyList())
        
        viewModelScope.launch {
            try {
                val call = NewsApiClient.service.getTopHeadlines(
                    apiKey = Constants.API_KEY,
                    language = "en",
                    category = category.lowercase()
                )
                
                call.enqueue(object : Callback<NewsResponse> {
                    override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.let { newsResponse ->
                                Log.d("NewsViewModel", "API news fetched for category: $category, articles: ${newsResponse.articles.size}")
                                _articles.postValue(newsResponse.articles)
                                combineArticles(newsResponse.articles)
                            }
                        } else {
                            Log.e("NewsViewModel", "Error response: ${response.errorBody()?.string()}")
                        }
                    }
                    
                    override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                        Log.e("NewsViewModel", "Error fetching news", t)
                    }
                })
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Exception in fetchNewsTopHeadlines", e)
            }
        }
    }

    fun fetchEverythingWithQuery(query: String) {
        if (query.isBlank()) {
            fetchNewsTopHeadlines()
            return
        }
        
        viewModelScope.launch {
            try {
                val call = NewsApiClient.service.getEverything(
                    apiKey = Constants.API_KEY,
                    query = query,
                    language = "en"
                )
                
                call.enqueue(object : Callback<NewsResponse> {
                    override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.let { newsResponse ->
                                Log.d("NewsViewModel", "Search results from API: ${newsResponse.articles.size}")
                                _articles.postValue(newsResponse.articles)
                                
                                // Search local news by title and content
                                val filteredLocal = localNewsRepository.getAllNews().filter { localNews ->
                                    localNews.title.contains(query, ignoreCase = true) || 
                                    localNews.content.contains(query, ignoreCase = true) ||
                                    localNews.category.contains(query, ignoreCase = true)
                                }
                                Log.d("NewsViewModel", "Search results from local: ${filteredLocal.size}")
                                
                                // For search, show all matching local news regardless of current category
                                val convertedLocalArticles = filteredLocal.map { local ->
                                    local.toArticle()
                                }
                                
                                val combined = convertedLocalArticles + newsResponse.articles
                                Log.d("NewsViewModel", "Total search results: ${combined.size}")
                                _combinedArticles.postValue(combined)
                            }
                        } else {
                            Log.e("NewsViewModel", "Error response: ${response.errorBody()?.string()}")
                        }
                    }
                    
                    override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                        Log.e("NewsViewModel", "Error fetching search results", t)
                    }
                })
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Exception in fetchEverythingWithQuery", e)
            }
        }
    }
    
    // Local news management
    fun addLocalNews(localNews: LocalNews) {
        Log.d("NewsViewModel", "Adding local news: ${localNews.title} in category: ${localNews.category}")
        localNewsRepository.addNews(localNews)
        
        // Switch to the category of the added news
        currentCategory = localNews.category.lowercase()
        if (currentCategory == "general") {
            fetchNewsTopHeadlines("general")
        } else {
            fetchNewsTopHeadlines(currentCategory)
        }
    }

    fun deleteLocalNews(id: String) {
        Log.d("NewsViewModel", "Deleting local news with id: $id")
        localNewsRepository.deleteNews(id)
    }
    
    fun updateLocalNews(id: String, updatedNews: LocalNews) {
        Log.d("NewsViewModel", "Updating news $id with new content: ${updatedNews.title}")
        localNewsRepository.updateNews(id, updatedNews)
        
        // Refresh the current category view
        if (currentCategory == "general") {
            fetchNewsTopHeadlines("general")
        } else {
            fetchNewsTopHeadlines(currentCategory)
        }
    }
    
    fun getLocalNewsById(id: String): LocalNews? {
        return localNewsRepository.getNewsById(id)
    }
    
    // Debug function to check current state
    fun debugCurrentState() {
        Log.d("NewsViewModel", "=== DEBUG STATE ===")
        Log.d("NewsViewModel", "Current category: $currentCategory")
        Log.d("NewsViewModel", "All local news count: ${localNewsRepository.getAllNews().size}")
        Log.d("NewsViewModel", "API articles count: ${_articles.value?.size ?: 0}")
        Log.d("NewsViewModel", "Combined articles count: ${_combinedArticles.value?.size ?: 0}")
        localNewsRepository.getAllNews().forEach { news ->
            Log.d("NewsViewModel", "Local news: ${news.title} - Category: ${news.category}")
        }
        Log.d("NewsViewModel", "=== END DEBUG ===")
    }
    
    // Provide access to repository for UI components
    fun getLocalNewsRepository() = localNewsRepository
    
    private fun combineArticles(apiArticles: List<Article>) {
        val allLocalNews = localNewsRepository.getAllNews()
        
        // Filter local news by current category - STRICT filtering
        val filteredLocalNews = allLocalNews.filter { localNews ->
            localNews.category.lowercase() == currentCategory.lowercase()
        }
        
        Log.d("NewsViewModel", "Combining articles - Category: $currentCategory")
        Log.d("NewsViewModel", "All local news: ${allLocalNews.size}")
        Log.d("NewsViewModel", "Filtered local news: ${filteredLocalNews.size}")
        Log.d("NewsViewModel", "API articles: ${apiArticles.size}")
        
        // Debug: Show which local news are being included
        filteredLocalNews.forEach { news ->
            Log.d("NewsViewModel", "Including local news: ${news.title} (Category: ${news.category})")
        }
        
        val convertedLocalArticles = filteredLocalNews.map { local ->
            local.toArticle()
        }
        
        val combined = convertedLocalArticles + apiArticles
        Log.d("NewsViewModel", "Total combined articles: ${combined.size}")
        _combinedArticles.postValue(combined)
    }
}
