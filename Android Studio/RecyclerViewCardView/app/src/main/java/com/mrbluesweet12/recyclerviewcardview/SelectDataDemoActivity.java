package com.mrbluesweet12.recyclerviewcardview;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity untuk mendemonstrasikan SELECT query dan menampilkan data dengan RecyclerView
 * Implementasi sesuai dengan spesifikasi video: SELECT, Model, dan Adapter
 */
public class SelectDataDemoActivity extends AppCompatActivity {
    
    // Views
    private RecyclerView recyclerViewBarang;
    private TextView tvDataInfo, tvQueryInfo;
    private Button btnSelectData, btnRefreshData, btnTestQuery;
    
    // Data dan Adapter
    private Database database;
    private BarangRecyclerAdapter barangAdapter;
    private List<BarangModel> barangList;
    
    private static final String TAG = "SelectDataDemo";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_data_demo);
        
        initializeViews();
        initializeDatabase();
        setupRecyclerView();
        setupEventListeners();
        
        // Tampilkan informasi awal
        showInitialInfo();
        
        // Auto-load data saat pertama kali dibuka
        selectData();
    }
    
    private void initializeViews() {
        recyclerViewBarang = findViewById(R.id.recyclerViewBarang);
        tvDataInfo = findViewById(R.id.tvDataInfo);
        tvQueryInfo = findViewById(R.id.tvQueryInfo);
        btnSelectData = findViewById(R.id.btnSelectData);
        btnRefreshData = findViewById(R.id.btnRefreshData);
        btnTestQuery = findViewById(R.id.btnTestQuery);
    }
    
    private void initializeDatabase() {
        try {
            database = new Database(this);
            Log.d(TAG, "Database initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing database: " + e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void setupRecyclerView() {
        // Inisialisasi list dan adapter
        barangList = new ArrayList<>();
        barangAdapter = new BarangRecyclerAdapter(this, barangList);
        
        // Setup RecyclerView
        recyclerViewBarang.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBarang.setAdapter(barangAdapter);
        
        Log.d(TAG, "RecyclerView setup completed");
    }
    
    private void setupEventListeners() {
        btnSelectData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectData();
            }
        });
        
        btnRefreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });
        
        btnTestQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testVariousQueries();
            }
        });
    }
    
    private void showInitialInfo() {
        String info = "📋 SELECT Data Demo\n" +
                     "===================\n" +
                     "Tabel: tblbarang\n" +
                     "Query: SELECT * FROM tblbarang ORDER BY barang ASC\n" +
                     "Status: Ready";
        
        tvQueryInfo.setText(info);
        tvDataInfo.setText("Klik 'SELECT Data' untuk memuat data dari database");
    }
    
    /**
     * Fungsi utama untuk SELECT data sesuai spesifikasi video
     */
    public void selectData() {
        Log.d(TAG, "=== MEMULAI SELECT DATA ===");
        
        // Definisi SQL query string
        String sql = "SELECT * FROM tblbarang ORDER BY barang ASC";
        
        // Update info query
        String queryInfo = "📋 EXECUTING SELECT QUERY\n" +
                          "==========================\n" +
                          "Query: " + sql + "\n" +
                          "Target: tblbarang\n" +
                          "Order: ASC by barang name\n" +
                          "Status: Executing...";
        tvQueryInfo.setText(queryInfo);
        
        try {
            // Eksekusi query menggunakan fungsi select dari Database.java
            Cursor cursor = database.select(sql, null);
            
            // Verifikasi awal (jumlah baris) - sesuai spesifikasi video
            if (cursor != null) {
                int jumlahBaris = cursor.getCount();
                
                // Tampilkan jumlah baris dengan Toast (sesuai spesifikasi)
                Toast.makeText(this, "Jumlah baris: " + jumlahBaris, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SELECT berhasil, jumlah baris: " + jumlahBaris);
                
                // Update query info dengan hasil
                String resultInfo = "📋 SELECT QUERY RESULT\n" +
                                   "======================\n" +
                                   "Query: " + sql + "\n" +
                                   "Result: " + jumlahBaris + " rows found\n" +
                                   "Status: ✅ SUCCESS";
                tvQueryInfo.setText(resultInfo);
                
                // Proses data dari cursor
                processSelectResults(cursor);
                
                // Tutup cursor
                cursor.close();
                
            } else {
                // Handle cursor null
                String errorInfo = "📋 SELECT QUERY ERROR\n" +
                                  "======================\n" +
                                  "Query: " + sql + "\n" +
                                  "Result: NULL cursor\n" +
                                  "Status: ❌ FAILED";
                tvQueryInfo.setText(errorInfo);
                
                Toast.makeText(this, "SELECT gagal - cursor null", Toast.LENGTH_LONG).show();
                Log.e(TAG, "SELECT query returned null cursor");
                
                tvDataInfo.setText("❌ SELECT query gagal atau tabel tidak ditemukan");
            }
            
        } catch (Exception e) {
            // Handle exception
            String errorMsg = "Error SELECT: " + e.getMessage();
            
            String errorInfo = "📋 SELECT QUERY EXCEPTION\n" +
                              "==========================\n" +
                              "Query: " + sql + "\n" +
                              "Error: " + e.getMessage() + "\n" +
                              "Status: ❌ EXCEPTION";
            tvQueryInfo.setText(errorInfo);
            
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
            
            tvDataInfo.setText("❌ Exception: " + e.getMessage());
        }
        
        Log.d(TAG, "=== SELECT DATA SELESAI ===");
    }
    
    /**
     * Memproses hasil SELECT dan konversi ke BarangModel
     * @param cursor Cursor hasil SELECT query
     */
    private void processSelectResults(Cursor cursor) {
        Log.d(TAG, "Memproses hasil SELECT...");
        
        List<BarangModel> tempList = new ArrayList<>();
        
        try {
            // Pindah ke baris pertama
            if (cursor.moveToFirst()) {
                
                StringBuilder dataDetail = new StringBuilder();
                dataDetail.append("📊 DATA DARI tblbarang:\n");
                dataDetail.append("========================\n");
                
                do {
                    // Ekstrak data dari cursor sesuai kolom tabel
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("idbarang"));
                    String nama = cursor.getString(cursor.getColumnIndexOrThrow("barang"));
                    String stok = cursor.getString(cursor.getColumnIndexOrThrow("stok"));
                    String harga = cursor.getString(cursor.getColumnIndexOrThrow("harga"));
                    
                    // Buat objek BarangModel
                    BarangModel barang = new BarangModel(id, nama, stok, harga);
                    tempList.add(barang);
                    
                    // Log detail untuk debugging
                    Log.d(TAG, "Data row: ID=" + id + ", Nama=" + nama + 
                             ", Stok=" + stok + ", Harga=" + harga);
                    
                    // Tambahkan ke detail display (max 5 items untuk preview)
                    if (tempList.size() <= 5) {
                        dataDetail.append("• ").append(nama)
                                  .append(" - ").append(barang.getFormattedHarga())
                                  .append(" (").append(barang.getFormattedStok()).append(")\n");
                    }
                    
                } while (cursor.moveToNext());
                
                // Update info display
                if (tempList.size() > 5) {
                    dataDetail.append("... dan ").append(tempList.size() - 5).append(" item lainnya\n");
                }
                dataDetail.append("\nTotal: ").append(tempList.size()).append(" items");
                tvDataInfo.setText(dataDetail.toString());
                
                // Update adapter dengan data baru
                barangAdapter.updateData(tempList);
                
                Log.d(TAG, "✅ Berhasil memproses " + tempList.size() + " item");
                Toast.makeText(this, "✅ Data berhasil dimuat: " + tempList.size() + " items", 
                              Toast.LENGTH_SHORT).show();
                
            } else {
                Log.d(TAG, "Cursor kosong - tidak ada data di tabel");
                tvDataInfo.setText("📋 Tabel tblbarang kosong\n\nTidak ada data untuk ditampilkan.");
                barangAdapter.clearData();
                Toast.makeText(this, "Tabel kosong", Toast.LENGTH_SHORT).show();
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error memproses SELECT results: " + e.getMessage(), e);
            tvDataInfo.setText("❌ Error memproses data: " + e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Refresh data dengan query ulang
     */
    private void refreshData() {
        Log.d(TAG, "Refreshing data...");
        Toast.makeText(this, "🔄 Refreshing data...", Toast.LENGTH_SHORT).show();
        selectData();
    }
    
    /**
     * Test berbagai variasi query SELECT
     */
    private void testVariousQueries() {
        Log.d(TAG, "=== TESTING VARIOUS QUERIES ===");
        
        String[] testQueries = {
            "SELECT COUNT(*) as total FROM tblbarang",
            "SELECT barang, harga FROM tblbarang WHERE harga > 10000",
            "SELECT * FROM tblbarang ORDER BY harga DESC LIMIT 3",
            "SELECT barang, stok FROM tblbarang WHERE stok < 10"
        };
        
        StringBuilder results = new StringBuilder();
        results.append("🧪 QUERY TEST RESULTS:\n");
        results.append("=======================\n");
        
        for (String query : testQueries) {
            try {
                Cursor cursor = database.select(query, null);
                if (cursor != null) {
                    results.append("✅ ").append(query).append(" → ").append(cursor.getCount()).append(" rows\n");
                    cursor.close();
                } else {
                    results.append("❌ ").append(query).append(" → NULL\n");
                }
            } catch (Exception e) {
                results.append("❌ ").append(query).append(" → ERROR\n");
            }
        }
        
        tvDataInfo.setText(results.toString());
        Log.d(TAG, "=== QUERY TESTING COMPLETED ===");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Tidak perlu close database di sini karena Database.java menggunakan SQLiteOpenHelper
        Log.d(TAG, "Activity destroyed");
    }
}
