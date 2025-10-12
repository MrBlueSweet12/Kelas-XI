# Fitur Tambah Berita - News App

## Gambaran Umum

Fitur ini memungkinkan pengguna untuk menambahkan berita mereka sendiri ke aplikasi dan mengkategorikannya sesuai dengan kategori yang tersedia (General, Business, Entertainment, Health, Science, Sports, Technology).

## Komponen yang Dibuat

### 1. Model Data (LocalNews.kt)

- **LocalNews**: Data class untuk berita yang ditambahkan pengguna
- **Fields**:
  - `id`: ID unik (UUID)
  - `title`: Judul berita
  - `content`: Konten berita
  - `category`: Kategori berita
  - `imageUrl`: URL gambar (opsional)
  - `publishedAt`: Waktu publikasi

### 2. Repository (LocalNewsRepository.kt)

- **LocalNewsRepository**: Mengelola penyimpanan berita lokal dalam memori
- **Fungsi**:
  - `addNews()`: Menambah berita baru
  - `getAllNews()`: Mengambil semua berita
  - `getNewsByCategory()`: Mengambil berita berdasarkan kategori
  - `deleteNews()`: Menghapus berita berdasarkan ID

### 3. UI Form (AddNewsScreen.kt)

- **AddNewsScreen**: Composable untuk form input berita baru
- **Input Fields**:
  - Judul berita (wajib)
  - Kategori (radio button selection)
  - URL gambar (opsional)
  - Konten berita (wajib)
- **Features**:
  - Validasi input (title dan content harus diisi)
  - Navigation back ke home screen
  - Save button dengan icon check

### 4. ViewModel Update (NewsViewModel.kt)

- **Fungsi Baru**:
  - `addLocalNews()`: Menambah berita lokal
  - `deleteLocalNews()`: Menghapus berita lokal
  - `combinedArticles`: LiveData yang menggabungkan berita API + lokal
  - `loadLocalNews()`: Load berita lokal berdasarkan kategori aktif
- **Integration**:
  - Berita lokal ditampilkan bersamaan dengan berita dari API
  - Filter kategori berlaku untuk berita lokal dan API
  - Search berfungsi untuk berita lokal dan API

### 5. Navigation Update

- **Route.kt**: Ditambah `AddNewsScreen` route
- **MainActivity.kt**: Ditambah composable untuk AddNewsScreen
- **HomeScreen.kt**:
  - FloatingActionButton untuk navigate ke AddNewsScreen
  - Menggunakan `combinedArticles` untuk menampilkan semua berita

## Cara Menggunakan Fitur

1. **Menambah Berita**:

   - Tekan tombol "+" (FloatingActionButton) di HomeScreen
   - Isi form dengan judul, pilih kategori, tambah URL gambar (opsional), dan tulis konten
   - Tekan tombol "Simpan Berita" atau icon check di top bar
   - Berita akan muncul di kategori yang dipilih

2. **Melihat Berita**:

   - Berita lokal akan muncul di atas berita dari API
   - Author berita lokal ditandai sebagai "User Added"
   - Berita lokal mengikuti filter kategori yang aktif

3. **Kategori yang Didukung**:
   - General
   - Business
   - Entertainment
   - Health
   - Science
   - Sports
   - Technology

## Penyimpanan Data

- Berita lokal disimpan dalam memori (in-memory storage)
- Data akan hilang ketika aplikasi ditutup
- Untuk persistensi permanen, bisa diintegrasikan dengan Room Database di masa depan

## Integration dengan Fitur Existing

- **Search**: Berita lokal ikut ter-filter ketika user melakukan pencarian
- **Categories**: Berita lokal ditampilkan sesuai kategori yang dipilih
- **Navigation**: Berita lokal yang tidak memiliki URL akan tetap bisa diklik (bisa dikembangkan untuk menampilkan detail view khusus)

## File Structure

```
app/src/main/java/com/mrbluesweet12/newsapp/
├── model/
│   └── LocalNews.kt
├── repository/
│   └── LocalNewsRepository.kt
├── ui/
│   ├── AddNewsScreen.kt
│   └── HomeScreen.kt (updated)
├── viewmodel/
│   └── NewsViewModel.kt (updated)
├── navigation/
│   └── Route.kt (updated)
└── MainActivity.kt (updated)
```

## Status

✅ **SELESAI** - Semua fitur sudah diimplementasikan dan terintegrasi dengan baik.

Fitur ini memberikan pengalaman user yang lengkap untuk tidak hanya mengonsumsi berita dari API, tetapi juga berkontribusi dengan menambahkan berita mereka sendiri.
