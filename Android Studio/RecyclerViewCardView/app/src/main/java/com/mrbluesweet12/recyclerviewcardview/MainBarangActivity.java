package com.mrbluesweet12.recyclerviewcardview;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity untuk demonstrasi operasi INSERT SQLite
 * Menampilkan form input dan daftar barang dalam RecyclerView
 */
public class MainBarangActivity extends AppCompatActivity {
    
    private DataSource dataSource;
    private EditText etBarang, etStok, etHarga;
    private Button btnSimpan, btnLihatData, btnTestCRUD;
    private RecyclerView recyclerViewBarang;
    private TextView tvInfo;
    private BarangAdapter barangAdapter;
    
    // Variabel untuk mode update
    private String updateMode = "INSERT";
    private String updateId = null;
    
    private static final String TAG = "MainBarangActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_barang);
        
        initializeViews();
        initializeDatabase();
        setupEventListeners();
        setupRecyclerView();
        
        // Load data awal
        loadBarangData();
        
        // Check apakah ada data untuk update dari intent
        checkForUpdateData();
        
        // Jalankan simulasi testing untuk pembelajaran
        SQLQueryValidator.simulateQueryTesting();
        SQLQueryValidator.demonstrateDataTypeHandling();
        SQLQueryValidator.demonstrateErrorHandling();
    }
    
    private void initializeViews() {
        etBarang = findViewById(R.id.etBarang);
        etStok = findViewById(R.id.etStok);
        etHarga = findViewById(R.id.etHarga);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnLihatData = findViewById(R.id.btnLihatData);
        btnTestCRUD = findViewById(R.id.btnTestCRUD);
        recyclerViewBarang = findViewById(R.id.recyclerViewBarang);
        tvInfo = findViewById(R.id.tvInfo);
    }
    
    private void initializeDatabase() {
        dataSource = new DataSource(this);
        
        try {
            dataSource.open();
            Log.d(TAG, "Database opened successfully");
            
            // Tampilkan informasi database
            int count = dataSource.getBarangCount();
            tvInfo.setText("Database aplikasibarang.db siap digunakan\nJumlah barang: " + count + " items");
            
        } catch (Exception e) {
            Log.e(TAG, "Error opening database: " + e.getMessage());
            tvInfo.setText("Error: Gagal membuka database");
            Toast.makeText(this, "Error membuka database: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void setupEventListeners() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanBarang();
            }
        });
        
        btnLihatData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBarangData();
            }
        });
        
        btnTestCRUD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCRUDTestActivity();
            }
        });
    }
    
    private void setupRecyclerView() {
        recyclerViewBarang.setLayoutManager(new LinearLayoutManager(this));
        barangAdapter = new BarangAdapter(this, new ArrayList<>());
        recyclerViewBarang.setAdapter(barangAdapter);
    }
    
    /**
     * Method untuk menyimpan data barang baru atau memperbarui data yang sudah ada
     * Implementasi robust INSERT dan UPDATE dengan validasi dan error handling
     */
    private void simpanBarang() {
        // Ambil data dari EditText
        String nama = etBarang.getText().toString().trim();
        String stokStr = etStok.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        
        // Validasi input kosong
        if (nama.isEmpty()) {
            etBarang.setError("Nama barang tidak boleh kosong");
            etBarang.requestFocus();
            return;
        }
        
        if (stokStr.isEmpty()) {
            etStok.setError("Stok tidak boleh kosong");
            etStok.requestFocus();
            return;
        }
        
        if (hargaStr.isEmpty()) {
            etHarga.setError("Harga tidak boleh kosong");
            etHarga.requestFocus();
            return;
        }
        
        try {
            // Konversi string ke double
            double stok = Double.parseDouble(stokStr);
            double harga = Double.parseDouble(hargaStr);
            
            // Validasi nilai tidak negatif
            if (stok < 0) {
                etStok.setError("Stok tidak boleh negatif");
                etStok.requestFocus();
                return;
            }
            
            if (harga < 0) {
                etHarga.setError("Harga tidak boleh negatif");
                etHarga.requestFocus();
                return;
            }
            
            // Cek mode operasi: INSERT atau UPDATE
            if ("UPDATE".equals(updateMode) && updateId != null) {
                // === MODE UPDATE ===
                Log.d(TAG, "=== MEMULAI OPERASI UPDATE ===");
                Log.d(TAG, "Update ID: " + updateId + ", Nama: " + nama + ", Stok: " + stok + ", Harga: " + harga);
                
                performUpdateBarang(nama, stok, harga);
                
            } else {
                // === MODE INSERT ===
                Log.d(TAG, "=== MEMULAI OPERASI INSERT ===");
                Log.d(TAG, "Insert Nama: " + nama + ", Stok: " + stok + ", Harga: " + harga);
                
                performInsertBarang(nama, stok, harga);
            }
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Format angka tidak valid", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "NumberFormatException: " + e.getMessage());
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Unexpected error: " + e.getMessage());
        }
    }
    
    /**
     * Method untuk melakukan operasi UPDATE data barang
     * @param nama Nama barang yang baru
     * @param stok Stok barang yang baru
     * @param harga Harga barang yang baru
     */
    private void performUpdateBarang(String nama, double stok, double harga) {
        try {
            // Method 1: Menggunakan ContentValues (Recommended)
            Log.d(TAG, "Attempting to update barang using ContentValues method");
            int rowsAffected = dataSource.updateBarang(Long.parseLong(updateId), nama, stok, harga);
            
            if (rowsAffected > 0) {
                // UPDATE berhasil
                Toast.makeText(this, "✅ Data sudah diubah! ID: " + updateId, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "✅ Update berhasil dengan ContentValues, ID: " + updateId + ", Rows affected: " + rowsAffected);
                
                // Reset ke mode INSERT
                resetToInsertMode();
                
                // Clear form
                clearForm();
                
                // Refresh data
                loadBarangData();
                
                // Update info
                updateDatabaseInfo();
                
            } else {
                // UPDATE gagal
                Toast.makeText(this, "❌ Data tidak bisa diubah! ID tidak ditemukan", Toast.LENGTH_LONG).show();
                Log.e(TAG, "❌ Update gagal dengan ContentValues untuk ID: " + updateId + ", Rows affected: " + rowsAffected);
            }
            
            // Method 2: Demonstrasi menggunakan execSQL (Alternative untuk pembelajaran)
            demonstrateUpdateExecSQLMethod(nama, stok, harga);
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: ID tidak valid - " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "NumberFormatException dalam performUpdateBarang: " + e.getMessage(), e);
        } catch (Exception e) {
            Toast.makeText(this, "Error UPDATE: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error dalam performUpdateBarang: " + e.getMessage(), e);
        }
    }
    
    /**
     * Method untuk melakukan operasi INSERT data barang baru
     * @param nama Nama barang
     * @param stok Stok barang
     * @param harga Harga barang
     */
    private void performInsertBarang(String nama, double stok, double harga) {
        try {
            // Method 1: Menggunakan ContentValues (Recommended)
            Log.d(TAG, "Attempting to insert barang using ContentValues method");
            long resultContentValues = dataSource.createBarang(nama, stok, harga);
            
            if (resultContentValues != -1) {
                // INSERT berhasil
                Toast.makeText(this, "✅ Insert berhasil! ID: " + resultContentValues, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "✅ Insert berhasil dengan ContentValues, ID: " + resultContentValues);
                
                // Clear form
                clearForm();
                
                // Refresh data
                loadBarangData();
                
                // Update info
                updateDatabaseInfo();
                
            } else {
                // INSERT gagal
                Toast.makeText(this, "❌ Insert gagal! Periksa data input", Toast.LENGTH_LONG).show();
                Log.e(TAG, "❌ Insert gagal dengan ContentValues");
            }
            
            // Method 2: Demonstrasi menggunakan execSQL (Alternative)
            demonstrateExecSQLMethod(nama, stok, harga);
            
        } catch (Exception e) {
            Toast.makeText(this, "Error INSERT: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error dalam performInsertBarang: " + e.getMessage(), e);
        }
    }
    
    /**
     * Method untuk reset mode kembali ke INSERT setelah UPDATE berhasil
     */
    private void resetToInsertMode() {
        updateMode = "INSERT";
        updateId = null;
        btnSimpan.setText("SIMPAN");
        tvInfo.setText("Mode INSERT - Siap untuk menambah data baru");
        Log.d(TAG, "✅ Mode direset ke INSERT");
    }
    
    /**
     * Demonstrasi method UPDATE menggunakan execSQL
     * (Sebagai pembelajaran alternatif seperti yang dijelaskan di video)
     */
    private void demonstrateUpdateExecSQLMethod(String nama, double stok, double harga) {
        Log.d(TAG, "=== DEMONSTRASI UPDATE execSQL (Educational Purpose) ===");
        
        try {
            // Membuat SQL UPDATE statement
            String sql = "UPDATE tblbarang SET " +
                        "barang = '" + nama + "', " +
                        "stok = " + stok + ", " +
                        "harga = " + harga +
                        " WHERE idbarang = '" + updateId + "'";
            
            Log.d(TAG, "SQL UPDATE Statement: " + sql);
            
            // Eksekusi SQL UPDATE menggunakan execSQL
            boolean resultExecSQL = dataSource.executeUpdateSQL(sql);
            
            if (resultExecSQL) {
                Log.d(TAG, "✅ Demo UPDATE execSQL method berhasil untuk ID: " + updateId);
            } else {
                Log.w(TAG, "⚠️ Demo UPDATE execSQL method gagal untuk ID: " + updateId);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error dalam demonstrateUpdateExecSQLMethod: " + e.getMessage(), e);
        }
        
        Log.d(TAG, "=== DEMONSTRASI UPDATE execSQL SELESAI ===");
    }
    
    /**
     * Demonstrasi method INSERT menggunakan execSQL
     * (Sebagai pembelajaran alternatif seperti yang dijelaskan di video)
     */
    private void demonstrateExecSQLMethod(String nama, double stok, double harga) {
        Log.d(TAG, "Demonstrating execSQL method for educational purpose");
        
        // Untuk demonstrasi, kita akan menggunakan data yang sedikit berbeda
        String demoNama = nama + " (Demo execSQL)";
        
        boolean resultExecSQL = dataSource.insertBarang(demoNama, stok + 1, harga + 100);
        
        if (resultExecSQL) {
            Log.d(TAG, "Demo execSQL method berhasil");
            Toast.makeText(this, "Demo execSQL juga berhasil!", Toast.LENGTH_SHORT).show();
            
            // Refresh data untuk menampilkan kedua hasil
            loadBarangData();
        } else {
            Log.e(TAG, "Demo execSQL method gagal");
        }
    }
    
    private void clearForm() {
        etBarang.setText("");
        etStok.setText("");
        etHarga.setText("");
        etBarang.requestFocus();
    }
    
    private void loadBarangData() {
        try {
            Cursor cursor = dataSource.getAllBarang();
            if (cursor != null) {
                List<Barang> barangList = DatabaseUtils.cursorToBarangList(cursor);
                barangAdapter.updateData(barangList);
                
                Log.d(TAG, "Loaded " + barangList.size() + " barang items");
                Toast.makeText(this, "Data dimuat: " + barangList.size() + " items", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading barang data: " + e.getMessage());
            Toast.makeText(this, "Error memuat data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void updateDatabaseInfo() {
        try {
            int count = dataSource.getBarangCount();
            tvInfo.setText("Database aplikasibarang.db\nJumlah barang: " + count + " items\nStatus: Ready untuk operasi");
        } catch (Exception e) {
            Log.e(TAG, "Error updating database info: " + e.getMessage());
        }
    }
    
    private void openCRUDTestActivity() {
        Intent intent = new Intent(this, DatabaseCRUDTestActivity.class);
        startActivity(intent);
    }
    
    /**
     * Method untuk testing query secara manual (simulasi testing di SQLite browser)
     */
    private void simulateQueryTesting() {
        Log.d(TAG, "=== SIMULASI TESTING QUERY ===");
        
        // Simulasi query yang akan ditest di SQLite browser
        String[] testQueries = {
            "INSERT INTO tabel_barang (nama, stok, harga) VALUES ('Pensil', 50.0, 2500.0);",
            "INSERT INTO tabel_barang (nama, stok, harga) VALUES ('Buku Tulis', 100.0, 7500.0);",
            "INSERT INTO tabel_barang (nama, stok, harga) VALUES ('Penghapus', 25.5, 1500.0);"
        };
        
        for (String query : testQueries) {
            Log.d(TAG, "Test Query: " + query);
        }
        
        Log.d(TAG, "=== END SIMULASI ===");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data ketika kembali dari activity lain
        if (dataSource != null && dataSource.isDatabaseOpen()) {
            loadBarangData();
            updateDatabaseInfo();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Tutup koneksi database dengan benar
        if (dataSource != null) {
            dataSource.close();
            Log.d(TAG, "Database connection closed in onDestroy");
        }
    }
    
    /**
     * Check apakah ada data untuk update dari intent dan isi form jika ada
     */
    private void checkForUpdateData() {
        Intent intent = getIntent();
        if (intent != null && "UPDATE".equals(intent.getStringExtra("MODE"))) {
            // Mode update - isi form dengan data yang diterima
            updateMode = "UPDATE";
            updateId = intent.getStringExtra("ID");
            
            String nama = intent.getStringExtra("NAMA");
            String stok = intent.getStringExtra("STOK");
            String harga = intent.getStringExtra("HARGA");
            
            // Isi bidang EditText di UI dengan nilai yang diambil
            etBarang.setText(nama);
            etStok.setText(stok);
            etHarga.setText(harga);
            
            // Ubah teks pada tombol "Simpan" menjadi "UPDATE"
            btnSimpan.setText("UPDATE");
            
            // Update info UI
            tvInfo.setText("Mode UPDATE - ID: " + updateId + "\nSilakan edit data dan klik UPDATE");
            
            Log.d(TAG, "✅ Form filled for UPDATE mode - ID: " + updateId);
            Toast.makeText(this, "Mode UPDATE - Data dimuat untuk ID: " + updateId, Toast.LENGTH_LONG).show();
            
        } else {
            // Mode insert normal
            updateMode = "INSERT";
            updateId = null;
            btnSimpan.setText("SIMPAN");
            Log.d(TAG, "Mode INSERT - Normal mode");
        }
    }
    
    /**
     * Fungsi untuk implementasi SELECT WHERE langsung di MainBarangActivity
     * @param id ID barang yang akan diambil datanya
     */
    public void selectUpdate(String id) {
        Log.d(TAG, "=== SELECT UPDATE di MainBarangActivity ===");
        Log.d(TAG, "ID yang akan diambil: " + id);
        
        try {
            // Gunakan DataSource untuk mengambil data spesifik
            Cursor cursor = dataSource.getAllBarang(); // Get all first, then filter
            
            if (cursor != null && cursor.moveToFirst()) {
                boolean found = false;
                
                do {
                    String currentId = cursor.getString(cursor.getColumnIndexOrThrow("idbarang"));
                    
                    if (id.equals(currentId)) {
                        // Data ditemukan - ambil nilai dari kolom
                        String namaBarang = cursor.getString(cursor.getColumnIndexOrThrow("barang"));
                        String stokStr = cursor.getString(cursor.getColumnIndexOrThrow("stok"));
                        String hargaStr = cursor.getString(cursor.getColumnIndexOrThrow("harga"));
                        
                        // Isi bidang EditText di UI dengan nilai yang diambil
                        etBarang.setText(namaBarang);
                        etStok.setText(stokStr);
                        etHarga.setText(hargaStr);
                        
                        // Ubah teks pada tombol "Simpan" menjadi "UPDATE"
                        btnSimpan.setText("UPDATE");
                        
                        // Set mode update
                        updateMode = "UPDATE";
                        updateId = id;
                        
                        // Update info UI
                        tvInfo.setText("Mode UPDATE - ID: " + id + "\nData dimuat untuk editing");
                        
                        Toast.makeText(this, "✅ Data dimuat untuk update - ID: " + id, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "✅ Data berhasil dimuat untuk update - ID: " + id);
                        
                        found = true;
                        break;
                    }
                    
                } while (cursor.moveToNext());
                
                if (!found) {
                    Toast.makeText(this, "Data dengan ID " + id + " tidak ditemukan", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Data tidak ditemukan untuk ID: " + id);
                }
                
                cursor.close();
            }
            
        } catch (Exception e) {
            String errorMsg = "Error saat mengambil data: " + e.getMessage();
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
        }
        
        Log.d(TAG, "=== SELECT UPDATE SELESAI ===");
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        // Optional: close database di onPause untuk menghemat resource
        // Namun untuk aplikasi sederhana, bisa dibiarkan terbuka sampai onDestroy
    }
}
