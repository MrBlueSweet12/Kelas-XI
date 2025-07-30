package com.mrbluesweet12.recyclerviewcardview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // RecyclerView untuk siswa (original)
    private RecyclerView recyclerView;
    private SiswaAdapter adapter;
    private List<Siswa> siswaList;
    
    // Database SQLite
    private Database database;
    
    // Buttons
    private Button btnBarangApp, btnDbTest, btnSelectData, btnSelectDemo, btnPopupMenuDemo;
    
    // Variabel global untuk RecyclerView barang (sesuai spesifikasi video)
    private List<Barang> dataBarang;
    private BarangAdapter barangAdapter;
    private RecyclerView rcvBarang;
    
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Inisialisasi Database SQLite
        load();
        
        // Inisialisasi views
        initViews();
        
        // Inisialisasi RecyclerView dan data
        initRecyclerView();
        isiData();
        
        // Setup event listeners
        setupEventListeners();
        
        // Check dan insert sample data jika database kosong
        insertSampleDataIfEmpty();
        
        // Panggil selectData untuk demo
        selectData();
    }

    // Metode untuk inisialisasi database dan RecyclerView (sesuai spesifikasi video)
    private void load() {
        // Inisialisasi instance Database
        database = new Database(this);
        
        // Membuat tabel tblbarang
        database.buatTabel();
        
        // Inisialisasi dataBarang (sesuai spesifikasi video)
        dataBarang = new ArrayList<>();
        
        // Inisialisasi adapter (sesuai spesifikasi video)
        barangAdapter = new BarangAdapter(this, dataBarang);
        
        // Inisialisasi rcvBarang (pastikan ID sesuai dengan activity_main.xml)
        rcvBarang = findViewById(R.id.rcv_siswa); // Menggunakan RecyclerView yang sudah ada
        
        // Set LayoutManager untuk RecyclerView
        rcvBarang.setLayoutManager(new LinearLayoutManager(this));
        
        // Set hasFixedSize
        rcvBarang.setHasFixedSize(true);
        
        // Set adapter ke RecyclerView
        rcvBarang.setAdapter(barangAdapter);
        
        Log.d(TAG, "RecyclerView initialized with BarangAdapter");
    }
    
    private void initViews() {
        btnBarangApp = findViewById(R.id.btnBarangApp);
        btnDbTest = findViewById(R.id.btnDbTest);
        btnSelectData = findViewById(R.id.btnSelectData);
        btnSelectDemo = findViewById(R.id.btnSelectDemo);
        btnPopupMenuDemo = findViewById(R.id.btnPopupMenuDemo);
        
        // Inisialisasi RecyclerView untuk barang (akan ditambahkan ke layout nanti)
        // recyclerViewBarang = findViewById(R.id.recyclerViewBarang);
    }
    
    private void setupEventListeners() {
        btnBarangApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke aplikasi barang SQLite
                Intent intent = new Intent(MainActivity.this, MainBarangActivity.class);
                startActivity(intent);
            }
        });
        
        btnDbTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke database test
                Intent intent = new Intent(MainActivity.this, DatabaseCRUDTestActivity.class);
                startActivity(intent);
            }
        });
        
        // Button untuk testing selectData
        if (btnSelectData != null) {
            btnSelectData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectData();
                }
            });
        }
        
        // Button untuk navigasi ke SELECT Demo Activity
        if (btnSelectDemo != null) {
            btnSelectDemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigasi ke SELECT Data Demo Activity
                    Intent intent = new Intent(MainActivity.this, SelectDataDemoActivity.class);
                    startActivity(intent);
                }
            });
        }
        
        // Button untuk navigasi ke PopupMenu Demo Activity
        if (btnPopupMenuDemo != null) {
            btnPopupMenuDemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigasi ke PopupMenu Demo Activity
                    Intent intent = new Intent(MainActivity.this, PopupMenuDemoActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
    
    /**
     * Fungsi untuk mengeksekusi SELECT query dan memproses hasilnya
     * Sesuai spesifikasi video: SELECT, Model, dan Adapter
     * Menggunakan model Barang.java dan BarangAdapter
     */
    public void selectData() {
        Log.d(TAG, "=== MEMULAI SELECT DATA (Spesifikasi Video) ===");
        
        // Hapus data lama sebelum menambahkan data baru (sesuai spesifikasi video)
        dataBarang.clear();
        
        // Definisi SQL query untuk SELECT
        String sql = "SELECT * FROM tblbarang ORDER BY barang ASC";
        
        Log.d(TAG, "SQL Query: " + sql);
        
        try {
            // Eksekusi query menggunakan fungsi select dari Database.java
            Cursor cursor = database.select(sql, null);
            
            // Pemeriksaan if (cursor.getCount() > 0) - sesuai spesifikasi video
            if (cursor != null && cursor.getCount() > 0) {
                
                // Tampilkan jumlah baris dengan Toast
                Toast.makeText(this, "Jumlah baris: " + cursor.getCount(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SELECT berhasil, jumlah baris: " + cursor.getCount());
                
                // Pindahkan cursor ke posisi pertama
                cursor.moveToFirst();
                
                // Loop while (!cursor.isAfterLast()) untuk mengiterasi setiap baris
                while (!cursor.isAfterLast()) {
                    
                    // Dapatkan nilai dari setiap kolom menggunakan cursor.getString
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("idbarang"));
                    String nama = cursor.getString(cursor.getColumnIndexOrThrow("barang"));
                    String stok = cursor.getString(cursor.getColumnIndexOrThrow("stok")); // Ambil sebagai String
                    String harga = cursor.getString(cursor.getColumnIndexOrThrow("harga")); // Ambil sebagai String
                    
                    // Buat objek Barang baru dengan data yang diperoleh
                    Barang barang = new Barang(id, nama, stok, harga);
                    
                    // Tambahkan objek Barang ke dataBarang
                    dataBarang.add(barang);
                    
                    Log.d(TAG, "Data row: ID=" + id + ", Nama=" + nama + 
                             ", Stok=" + stok + ", Harga=" + harga);
                    
                    // Pindahkan cursor ke baris berikutnya
                    cursor.moveToNext();
                }
                
                // Tutup cursor setelah loop selesai
                cursor.close();
                
                // Beritahu adapter bahwa dataset telah berubah
                barangAdapter.notifyDataSetChanged();
                
                Log.d(TAG, "✅ Data berhasil dimuat: " + dataBarang.size() + " items");
                Toast.makeText(this, "✅ Data berhasil dimuat: " + dataBarang.size() + " items", 
                              Toast.LENGTH_SHORT).show();
                
            } else {
                // Jika tidak ada data - tampilkan Toast message "Data Kosong"
                Toast.makeText(this, "Data Kosong", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Tabel kosong - tidak ada data");
                
                // Pastikan dataBarang.clear() sudah terpanggil dan notifyDataSetChanged
                dataBarang.clear();
                barangAdapter.notifyDataSetChanged();
                
                // Tutup cursor jika tidak null
                if (cursor != null) {
                    cursor.close();
                }
            }
            
        } catch (Exception e) {
            // Handle exception
            String errorMsg = "Error saat SELECT: " + e.getMessage();
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
            
            // Clear data jika terjadi error
            dataBarang.clear();
            barangAdapter.notifyDataSetChanged();
        }
        
        Log.d(TAG, "=== SELECT DATA SELESAI ===");
    }
    
    /**
     * Fungsi untuk menghapus data dari database SQLite dengan konfirmasi AlertDialog
     * @param id ID barang yang akan dihapus (idbarang)
     */
    public void deleteData(String id) {
        Log.d(TAG, "=== MEMULAI DELETE DATA DENGAN KONFIRMASI ===");
        Log.d(TAG, "ID yang akan dihapus: " + id);
        
        // Deklarasikan variabel String bernama idbarang dan tetapkan nilai dari parameter id
        final String idbarang = id;
        
        // Buat pernyataan SQL DELETE sebagai String bernama sql
        final String sql = "DELETE FROM tblbarang WHERE idbarang = '" + idbarang + "'";
        
        Log.d(TAG, "SQL Query: " + sql);
        
        // === IMPLEMENTASI ALERT DIALOG KONFIRMASI ===
        
        // Buat objek AlertDialog.Builder
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        
        // Set judul dialog
        al.setTitle("PERINGATAN !");
        
        // Set pesan dialog
        al.setMessage("Yakin akan menghapus data barang dengan ID: " + idbarang + " ?");
        
        // Set icon peringatan (optional)
        al.setIcon(android.R.drawable.ic_dialog_alert);
        
        // Tambahkan tombol positif ("YA")
        al.setPositiveButton("YA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // === LOGIKA PENGHAPUSAN YANG SUDAH ADA DIPINDAHKAN KE SINI ===
                
                try {
                    // Gunakan pernyataan if untuk memeriksa apakah eksekusi query berhasil
                    if (database.runSQL(sql)) {
                        // Jika penghapusan berhasil
                        Toast.makeText(MainActivity.this, "✅ Data sudah dihapus - ID: " + idbarang, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "✅ Data berhasil dihapus - ID: " + idbarang);
                        
                        // Panggil fungsi untuk memperbarui RecyclerView setelah penghapusan
                        selectData(); // Memuat ulang data ke RecyclerView
                        
                    } else {
                        // Jika penghapusan gagal
                        Toast.makeText(MainActivity.this, "❌ Data tidak bisa dihapus - ID: " + idbarang, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "❌ Gagal menghapus data - ID: " + idbarang);
                    }
                    
                } catch (Exception e) {
                    // Handle exception
                    String errorMsg = "Error saat menghapus data: " + e.getMessage();
                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    Log.e(TAG, errorMsg, e);
                }
                
                // Tutup dialog
                dialog.dismiss();
                
                Log.d(TAG, "=== DELETE DATA SELESAI ===");
            }
        });
        
        // Tambahkan tombol negatif ("TIDAK")
        al.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Batalkan dialog dan tidak melakukan penghapusan
                Toast.makeText(MainActivity.this, "Penghapusan dibatalkan", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Penghapusan dibatalkan oleh user untuk ID: " + idbarang);
                dialog.cancel();
            }
        });
        
        // Tampilkan Alert Dialog
        al.show();
        
        Log.d(TAG, "Alert Dialog konfirmasi ditampilkan untuk ID: " + idbarang);
    }
    
    /**
     * Fungsi untuk menghapus data langsung tanpa konfirmasi dialog
     * Digunakan ketika konfirmasi sudah dilakukan di adapter
     * @param id ID barang yang akan dihapus (idbarang)
     */
    public void deleteDataDirect(String id) {
        Log.d(TAG, "=== MEMULAI DELETE DATA DIRECT (NO DIALOG) ===");
        Log.d(TAG, "ID yang akan dihapus: " + id);
        
        // Deklarasikan variabel String bernama idbarang dan tetapkan nilai dari parameter id
        String idbarang = id;
        
        // Buat pernyataan SQL DELETE sebagai String bernama sql
        String sql = "DELETE FROM tblbarang WHERE idbarang = '" + idbarang + "'";
        
        Log.d(TAG, "SQL Query: " + sql);
        
        try {
            // Gunakan pernyataan if untuk memeriksa apakah eksekusi query berhasil
            if (database.runSQL(sql)) {
                // Jika penghapusan berhasil
                Toast.makeText(this, "✅ Data sudah dihapus - ID: " + idbarang, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "✅ Data berhasil dihapus - ID: " + idbarang);
                
                // Panggil fungsi untuk memperbarui RecyclerView setelah penghapusan
                selectData(); // Memuat ulang data ke RecyclerView
                
            } else {
                // Jika penghapusan gagal
                Toast.makeText(this, "❌ Data tidak bisa dihapus - ID: " + idbarang, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "❌ Gagal menghapus data - ID: " + idbarang);
            }
            
        } catch (Exception e) {
            // Handle exception
            String errorMsg = "Error saat menghapus data: " + e.getMessage();
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
        }
        
        Log.d(TAG, "=== DELETE DATA DIRECT SELESAI ===");
    }
    
    /**
     * Fungsi untuk mengambil data spesifik dari database berdasarkan ID (SELECT WHERE)
     * dan mengisi form untuk keperluan update
     * @param id ID barang yang akan diambil datanya (idbarang)
     */
    public void selectUpdate(String id) {
        Log.d(TAG, "=== MEMULAI SELECT UPDATE ===");
        Log.d(TAG, "ID yang akan diambil: " + id);
        
        // Buat pernyataan SQL SELECT WHERE sebagai String
        String sql = "SELECT * FROM tblbarang WHERE idbarang = '" + id + "'";
        
        Log.d(TAG, "SQL Query: " + sql);
        
        try {
            // Jalankan kueri SQL dan dapatkan Cursor
            android.database.Cursor cursor = database.select(sql, null);
            
            // Periksa apakah Cursor memiliki data
            if (cursor != null && cursor.moveToFirst()) {
                
                // Ambil nilai dari kolom menggunakan cursor.getString()
                String namaBarang = cursor.getString(cursor.getColumnIndexOrThrow("barang"));
                String stokStr = cursor.getString(cursor.getColumnIndexOrThrow("stok"));
                String hargaStr = cursor.getString(cursor.getColumnIndexOrThrow("harga"));
                
                Log.d(TAG, "Data ditemukan - Nama: " + namaBarang + ", Stok: " + stokStr + ", Harga: " + hargaStr);
                
                // Konversi ke double untuk validasi
                double stok = Double.parseDouble(stokStr);
                double harga = Double.parseDouble(hargaStr);
                
                // Isi bidang EditText di UI dengan nilai yang diambil
                // Note: Untuk MainActivity, kita akan menggunakan Toast sebagai demo
                // karena MainActivity tidak memiliki form input seperti MainBarangActivity
                
                String message = "📝 DATA UNTUK UPDATE:\n" +
                               "===================\n" +
                               "ID: " + id + "\n" +
                               "Nama: " + namaBarang + "\n" +
                               "Stok: " + stok + "\n" +
                               "Harga: " + harga + "\n\n" +
                               "Data siap untuk diupdate!";
                
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                
                // Log detail untuk debugging
                Log.d(TAG, "✅ Data berhasil diambil untuk update - ID: " + id);
                
                // Untuk demo, kita akan navigasi ke MainBarangActivity dengan data
                navigateToUpdateForm(id, namaBarang, stokStr, hargaStr);
                
            } else {
                // Jika tidak ada data ditemukan
                String errorMsg = "Data dengan ID " + id + " tidak ditemukan";
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                Log.w(TAG, errorMsg);
            }
            
            // Pastikan untuk menutup Cursor setelah selesai
            if (cursor != null) {
                cursor.close();
            }
            
        } catch (Exception e) {
            // Handle exception
            String errorMsg = "Error saat mengambil data: " + e.getMessage();
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
        }
        
        Log.d(TAG, "=== SELECT UPDATE SELESAI ===");
    }
    
    /**
     * Navigasi ke form update dengan data yang sudah diambil
     * @param id ID barang
     * @param nama Nama barang
     * @param stok Stok barang
     * @param harga Harga barang
     */
    private void navigateToUpdateForm(String id, String nama, String stok, String harga) {
        Intent intent = new Intent(this, MainBarangActivity.class);
        intent.putExtra("MODE", "UPDATE");
        intent.putExtra("ID", id);
        intent.putExtra("NAMA", nama);
        intent.putExtra("STOK", stok);
        intent.putExtra("HARGA", harga);
        startActivity(intent);
        
        Log.d(TAG, "Navigating to update form with data for ID: " + id);
    }
    
    /**
     * Method untuk insert sample data jika database kosong
     * Memudahkan testing SELECT dan RecyclerView
     */
    private void insertSampleDataIfEmpty() {
        try {
            // Buat DataSource untuk cek dan insert data
            DataSource dataSource = new DataSource(this);
            
            // Check apakah database kosong
            if (SampleDataHelper.isDatabaseEmpty(dataSource)) {
                Log.d(TAG, "Database kosong, inserting sample data...");
                SampleDataHelper.insertSampleData(this, dataSource);
            } else {
                Log.d(TAG, "Database sudah berisi data, skip insert sample data");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error checking/inserting sample data: " + e.getMessage());
        }
    }
    
    
    // ===== METODE INISIALISASI RECYCLER VIEW UNTUK SISWA (Original) =====
    
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rcv_siswa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void isiData() {
        siswaList = new ArrayList<>();
        
        // Menambahkan data siswa seperti dalam video
        siswaList.add(new Siswa("Joni", "Surabaya"));
        siswaList.add(new Siswa("Budi", "Malang"));
        siswaList.add(new Siswa("Siti", "Jakarta"));
        siswaList.add(new Siswa("Ahmad", "Bandung"));
        siswaList.add(new Siswa("Dewi", "Yogyakarta"));
        siswaList.add(new Siswa("Eko", "Semarang"));
        siswaList.add(new Siswa("Fitri", "Medan"));
        siswaList.add(new Siswa("Galih", "Solo"));
        siswaList.add(new Siswa("Hana", "Denpasar"));
        siswaList.add(new Siswa("Irfan", "Makassar"));
        siswaList.add(new Siswa("Jihan", "Palembang"));
        siswaList.add(new Siswa("Kiki", "Pontianak"));
        siswaList.add(new Siswa("Linda", "Samarinda"));
        siswaList.add(new Siswa("Maya", "Manado"));
        siswaList.add(new Siswa("Nita", "Pekanbaru"));
        
        // Inisialisasi adapter dan set ke RecyclerView
        adapter = new SiswaAdapter(this, siswaList);
        recyclerView.setAdapter(adapter);
    }
    
    // Metode untuk menambahkan data siswa baru
    public void btnTambah(View view) {
        // Menambahkan data siswa baru ke dalam list
        siswaList.add(new Siswa("Joni Rambo", "Jakarta"));
        
        // Memberitahu adapter bahwa data telah berubah
        adapter.notifyDataSetChanged();
    }
}