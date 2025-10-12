# News App - Android Jetpack Compose

Aplikasi berita Android yang dibangun menggunakan Jetpack Compose dan Kotlin dengan fitur kategori berita dan pencarian.

## Fitur Utama

### 1. Tampilan Berita Utama

- Menampilkan berita utama terkini
- UI menggunakan LazyColumn untuk performa optimal
- Setiap item berita menampilkan gambar, judul, dan sumber berita

### 2. Kategori Berita

- 7 kategori berita tersedia: General, Business, Entertainment, Health, Science, Sports, Technology
- Navigasi horizontal untuk kategori
- Filter berita berdasarkan kategori yang dipilih

### 3. Pencarian Berita

- Tombol pencarian yang dapat di-expand/collapse
- Input field dengan styling CircleShape
- Pencarian berita menggunakan query text

### 4. Navigasi dan Detail Artikel

- **Navigation Compose** dengan Type-safe Navigation
- **Klik artikel** untuk membuka halaman detail lengkap
- **WebView** terintegrasi untuk menampilkan artikel original
- **Back navigation** untuk kembali ke halaman utama

## Setup dan Instalasi

### 1. API Key NewsAPI.org

1. Daftar di [newsapi.org](https://newsapi.org) untuk mendapatkan API key gratis
2. Buka file `Constants.kt` di `com.mrbluesweet12.newsapp.util`
3. Ganti `"YOUR_API_KEY_HERE"` dengan API key Anda:
   ```kotlin
   const val API_KEY = "api_key_anda_disini"
   ```

### 2. Sinkronisasi Gradle

1. Buka Android Studio
2. Klik "Sync Project with Gradle Files" atau tekan Ctrl+Shift+O
3. Tunggu proses sinkronisasi selesai

### 3. Menjalankan Aplikasi

1. Pastikan perangkat Android atau emulator sudah siap
2. Klik "Run" atau tekan Shift+F10
3. Aplikasi akan ter-install dan berjalan di perangkat

## Struktur Proyek

```
app/src/main/java/com/mrbluesweet12/newsapp/
├── api/
│   ├── NewsApiClient.kt      # Client untuk NewsAPI
│   └── NewsApiService.kt     # Interface Retrofit untuk API calls
├── model/
│   └── Article.kt            # Data class untuk Article dan NewsResponse
├── navigation/
│   └── Route.kt              # Definisi routes untuk navigasi
├── ui/
│   ├── ArticleItem.kt        # Composable untuk item berita
│   ├── CategoriesBar.kt      # Composable untuk kategori berita
│   ├── HomeScreen.kt         # Screen utama aplikasi
│   ├── NewsArticlePage.kt    # Halaman detail artikel dengan WebView
│   └── SearchBar.kt          # Composable untuk pencarian
├── util/
│   └── Constants.kt          # Konstanta aplikasi (API Key)
├── viewmodel/
│   └── NewsViewModel.kt      # ViewModel untuk manajemen state
└── MainActivity.kt           # Entry point aplikasi dengan NavHost
```

## Teknologi yang Digunakan

- **Kotlin** - Bahasa pemrograman
- **Jetpack Compose** - UI toolkit modern untuk Android
- **Navigation Compose** - Type-safe navigation untuk Jetpack Compose
- **Kotlinx Serialization** - Serialization library untuk navigation args
- **WebView** - Untuk menampilkan artikel lengkap dalam aplikasi
- **Retrofit** - HTTP client untuk API calls
- **Moshi** - JSON parsing
- **Coil** - Image loading library
- **Material Design 3** - Design system
- **LiveData & ViewModel** - Architecture components
- **Coroutines** - Asynchronous programming

## Cara Menggunakan

### 1. Melihat Berita Utama

- Buka aplikasi, berita utama kategori "General" akan ditampilkan secara default
- Scroll ke bawah untuk melihat lebih banyak berita

### 2. Filter Berdasarkan Kategori

- Tap salah satu tombol kategori di bagian atas (Business, Entertainment, dll.)
- Daftar berita akan difilter sesuai kategori yang dipilih

### 3. Mencari Berita

- Tap ikon pencarian di kiri atas
- Ketik kata kunci pencarian
- Tap ikon pencarian di kolom input untuk melakukan pencarian
- Untuk menutup pencarian, tap ikon close

### 4. Membaca Artikel Lengkap

- Tap pada item berita manapun untuk membuka artikel lengkap
- Artikel akan dibuka dalam WebView terintegrasi
- Gunakan tombol back perangkat atau gesture untuk kembali ke halaman utama
- WebView mendukung JavaScript dan semua fitur web modern

## Troubleshooting

### Error "Invalid API Key"

- Pastikan API key sudah benar di `Constants.kt`
- Cek quota penggunaan API di dashboard NewsAPI.org

### Berita Tidak Muncul

- Pastikan koneksi internet aktif
- Cek permission INTERNET sudah ada di AndroidManifest.xml

### Error Build

- Lakukan "Clean Project" kemudian "Rebuild Project"
- Pastikan Android Studio dan Gradle sudah versi terbaru

## Pengembangan Lebih Lanjut

Fitur yang dapat ditambahkan:

- ~~Detail artikel (WebView atau custom UI)~~ ✅ **Sudah Implementasi**
- Bookmark artikel
- Share artikel
- Dark mode toggle
- Offline caching
- Pull to refresh
- Pagination untuk loading lebih banyak berita
- History pembacaan artikel
- Notifikasi berita terbaru

## Kontributor

Proyek ini dikembangkan sebagai aplikasi pembelajaran Android dengan Jetpack Compose.
