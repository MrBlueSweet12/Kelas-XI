# 💾 **DATA PERSISTENCE - Fitur Simpan Berita Lokal**

## 🎯 **Overview**

Fitur data persistence memungkinkan berita lokal yang ditambahkan user **tersimpan secara permanen** dan **tetap ada** setelah aplikasi ditutup dan dibuka kembali. Data disimpan menggunakan **SharedPreferences** dengan **JSON serialization**.

---

## ✅ **Implementation Details**

### **1. Model Serialization - LocalNews.kt**

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class LocalNews(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val category: String,
    val imageUri: String? = null,
    val author: String = "User",
    val publishedAt: Long = System.currentTimeMillis(),
    val isLocal: Boolean = true
)
```

**Added:**

- ✅ `@Serializable` annotation untuk JSON serialization
- ✅ Support untuk kotlinx.serialization

### **2. Persistent Repository - LocalNewsRepository.kt**

#### **Storage Configuration:**

```kotlin
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
}
```

#### **Load from Storage (on app start):**

```kotlin
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
```

#### **Save to Storage (on every change):**

```kotlin
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
```

#### **Updated Operations with Auto-Save:**

```kotlin
fun addNews(news: LocalNews) {
    val currentList = _localNews.value.toMutableList()
    currentList.add(0, news)
    _localNews.value = currentList
    saveNewsToStorage() // ✅ Auto-save after add
}

fun deleteNews(newsId: String) {
    val currentList = _localNews.value.toMutableList()
    currentList.removeAll { it.id == newsId }
    _localNews.value = currentList
    saveNewsToStorage() // ✅ Auto-save after delete
}

fun updateNewsCategory(newsId: String, newCategory: String) {
    val currentList = _localNews.value.toMutableList()
    val newsIndex = currentList.indexOfFirst { it.id == newsId }
    if (newsIndex != -1) {
        val updatedNews = currentList[newsIndex].copy(category = newCategory)
        currentList[newsIndex] = updatedNews
        _localNews.value = currentList
        saveNewsToStorage() // ✅ Auto-save after update
    }
}
```

### **3. Context Injection - NewsViewModel.kt**

```kotlin
class NewsViewModel(context: Context) : ViewModel() {
    private val localNewsRepository = LocalNewsRepository(context)
    // ... rest of ViewModel code unchanged
}
```

### **4. ViewModelFactory - NewsViewModelFactory.kt**

```kotlin
class NewsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```

### **5. MainActivity Integration - MainActivity.kt**

```kotlin
import com.mrbluesweet12.newsapp.viewmodel.NewsViewModelFactory

// In onCreate():
val newsViewModel: NewsViewModel = viewModel(
    factory = NewsViewModelFactory(this@MainActivity)
)
```

---

## 🔄 **Data Flow**

### **App Launch Flow:**

```
1. MainActivity creates NewsViewModel with Context
2. NewsViewModel creates LocalNewsRepository with Context
3. LocalNewsRepository init() calls loadNewsFromStorage()
4. SharedPreferences data loaded and parsed as JSON
5. StateFlow updated with persisted data
6. UI automatically shows saved news items
```

### **Add News Flow:**

```
1. User adds news through AddNewsScreen
2. NewsViewModel.addLocalNews() called
3. LocalNewsRepository.addNews() called
4. Data added to in-memory StateFlow
5. saveNewsToStorage() automatically called
6. Data serialized to JSON and saved to SharedPreferences
7. UI immediately updates with new item
```

### **App Close/Reopen Flow:**

```
1. User closes app → Data remains in SharedPreferences
2. User reopens app → loadNewsFromStorage() executed
3. All previously saved news restored
4. UI shows exactly same state as before closing
```

---

## 🎯 **Benefits**

### **User Experience:**

- ✅ **Persistent Data**: News items never lost when closing app
- ✅ **Instant Restore**: All data immediately available on app launch
- ✅ **Seamless UX**: No difference between fresh news and restored news
- ✅ **Reliable Storage**: Data survives app updates and device restarts

### **Technical Advantages:**

- ✅ **Automatic Saving**: Every operation auto-saves immediately
- ✅ **Error Handling**: Graceful handling of JSON parsing errors
- ✅ **Lightweight**: SharedPreferences is fast and efficient
- ✅ **Local Storage**: No internet connection required
- ✅ **Backwards Compatible**: Handles missing or corrupted data

### **Development Benefits:**

- ✅ **Type Safe**: Kotlinx serialization with compile-time safety
- ✅ **Maintainable**: Clean separation of concerns
- ✅ **Testable**: Repository pattern enables easy testing
- ✅ **Scalable**: Easy to extend with more fields or features

---

## 🧪 **Testing Scenarios**

### **Basic Persistence Test:**

```
1. Open app → Add news "Test Article" in Technology category
2. Close app completely (swipe away from recent apps)
3. Reopen app → Verify "Test Article" still appears in Technology
4. Switch to Business category → Verify article not visible
5. Switch back to Technology → Verify article still visible
```

### **CRUD Operations Test:**

```
1. Add multiple news items in different categories
2. Edit category of one item
3. Delete one item
4. Close and reopen app
5. Verify all changes persisted correctly
```

### **Edge Cases Test:**

```
1. Add news with special characters in title/content
2. Add news with very long content
3. Add news with image URIs
4. Close app during add operation
5. Reopen and verify data integrity
```

### **Category Filtering Test:**

```
1. Add news in Technology: "AI News"
2. Add news in Business: "Market Update"
3. Close app
4. Reopen app in General category → Both items should not appear
5. Switch to Technology → Only "AI News" appears
6. Switch to Business → Only "Market Update" appears
```

---

## 📊 **Storage Format**

### **SharedPreferences Structure:**

```
Preference Name: "local_news_prefs"
Key: "local_news_list"
Value: JSON Array of LocalNews objects
```

### **JSON Example:**

```json
[
  {
    "id": "abc123-def456-ghi789",
    "title": "AI Revolution in 2025",
    "content": "Artificial intelligence continues to transform...",
    "category": "Technology",
    "imageUri": "content://media/external/images/media/12345",
    "author": "User",
    "publishedAt": 1703123456789,
    "isLocal": true
  },
  {
    "id": "xyz789-uvw456-rst123",
    "title": "Market Analysis",
    "content": "Stock markets showed significant growth...",
    "category": "Business",
    "imageUri": null,
    "author": "User",
    "publishedAt": 1703234567890,
    "isLocal": true
  }
]
```

---

## ⚠️ **Considerations**

### **Data Limitations:**

- **Storage Size**: SharedPreferences suitable for moderate amounts of data
- **Performance**: JSON parsing happens on main thread (acceptable for small datasets)
- **Memory Usage**: All news loaded into memory on app start

### **Future Enhancements:**

- **Database Migration**: Consider Room database for large datasets
- **Background Loading**: Load data on background thread for large datasets
- **Data Compression**: Compress JSON for storage efficiency
- **Backup/Restore**: Export/import functionality for user data

---

## 🎉 **Result Summary**

### **Before Implementation:**

- ❌ All local news lost when closing app
- ❌ Users need to re-add news every session
- ❌ Poor user experience with data loss
- ❌ No permanent storage solution

### **After Implementation:**

- ✅ **Complete Persistence**: All local news permanently saved
- ✅ **Automatic Restore**: Data loads immediately on app launch
- ✅ **Seamless Experience**: Users never lose their content
- ✅ **Reliable Storage**: Data survives app restarts and updates
- ✅ **Performance Optimized**: Fast save/load operations
- ✅ **Error Resilient**: Graceful handling of storage errors

**Local news data is now truly persistent and provides a professional, reliable user experience!** 💾✨

---

## 📱 **User Instructions**

### **How It Works:**

1. **Add News**: Create local news as usual - automatically saved
2. **Edit/Delete**: Modify news - changes saved immediately
3. **Close App**: Exit normally or force close - data preserved
4. **Reopen App**: Launch again - all news restored exactly as left
5. **Categories**: Filtering still works perfectly with saved data

**Users can now confidently add local news knowing it will never disappear!** 🚀
