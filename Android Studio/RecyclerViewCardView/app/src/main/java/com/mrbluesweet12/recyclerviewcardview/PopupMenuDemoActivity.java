package com.mrbluesweet12.recyclerviewcardview;

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
 * Activity untuk mendemonstrasikan PopupMenu pada RecyclerView items
 * Menampilkan implementasi menu UBAH dan HAPUS sesuai spesifikasi
 */
public class PopupMenuDemoActivity extends AppCompatActivity {
    
    // Views
    private RecyclerView recyclerViewDemo;
    private TextView tvDemoInfo;
    private Button btnAddSampleData, btnClearData, btnRefreshData;
    
    // Data dan Adapter
    private DataSource dataSource;
    private PopupMenuBarangAdapter barangAdapter;
    private List<Barang> barangList;
    
    private static final String TAG = "PopupMenuDemo";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_menu_demo);
        
        initializeViews();
        initializeDatabase();
        setupRecyclerView();
        setupEventListeners();
        
        // Load data awal
        loadData();
        
        showInitialInfo();
    }
    
    private void initializeViews() {
        recyclerViewDemo = findViewById(R.id.recyclerViewDemo);
        tvDemoInfo = findViewById(R.id.tvDemoInfo);
        btnAddSampleData = findViewById(R.id.btnAddSampleData);
        btnClearData = findViewById(R.id.btnClearData);
        btnRefreshData = findViewById(R.id.btnRefreshData);
    }
    
    private void initializeDatabase() {
        try {
            dataSource = new DataSource(this);
            Log.d(TAG, "Database initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing database: " + e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void setupRecyclerView() {
        // Inisialisasi list dan adapter
        barangList = new ArrayList<>();
        barangAdapter = new PopupMenuBarangAdapter(this, barangList);
        
        // Setup RecyclerView
        recyclerViewDemo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDemo.setAdapter(barangAdapter);
        
        Log.d(TAG, "RecyclerView setup completed with PopupMenuBarangAdapter");
    }
    
    private void setupEventListeners() {
        btnAddSampleData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSampleData();
            }
        });
        
        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllData();
            }
        });
        
        btnRefreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }
    
    private void showInitialInfo() {
        String info = "🔧 POPUP MENU DEMO\n" +
                     "==================\n" +
                     "Klik ikon ••• pada setiap item untuk:\n" +
                     "• UBAH - Edit item\n" +
                     "• HAPUS - Delete item\n\n" +
                     "Status: Ready untuk testing";
        
        tvDemoInfo.setText(info);
    }
    
    private void loadData() {
        Log.d(TAG, "Loading data from database...");
        
        try {
            // Clear existing data
            barangList.clear();
            
            // Load data dari database menggunakan DataSource
            android.database.Cursor cursor = dataSource.getAllBarang();
            
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                
                do {
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("idbarang"));
                    String nama = cursor.getString(cursor.getColumnIndexOrThrow("barang"));
                    String stok = cursor.getString(cursor.getColumnIndexOrThrow("stok"));
                    String harga = cursor.getString(cursor.getColumnIndexOrThrow("harga"));
                    
                    Barang barang = new Barang(id, nama, stok, harga);
                    barangList.add(barang);
                    
                } while (cursor.moveToNext());
                
                cursor.close();
                
                // Update adapter
                barangAdapter.notifyDataSetChanged();
                
                // Update info
                updateInfo("Data loaded: " + barangList.size() + " items");
                Log.d(TAG, "Successfully loaded " + barangList.size() + " items");
                
            } else {
                updateInfo("Database kosong. Klik 'Add Sample Data' untuk menambah data.");
                Log.d(TAG, "Database is empty");
                
                if (cursor != null) {
                    cursor.close();
                }
            }
            
        } catch (Exception e) {
            String errorMsg = "Error loading data: " + e.getMessage();
            updateInfo("❌ " + errorMsg);
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
        }
    }
    
    private void addSampleData() {
        Log.d(TAG, "Adding sample data...");
        
        // Data contoh untuk testing popup menu
        String[][] sampleData = {
            {"Mouse Gaming RGB", "25", "125000"},
            {"Keyboard Mechanical", "15", "350000"},
            {"Monitor LED 24 inch", "8", "2500000"},
            {"Webcam HD 1080p", "20", "450000"},
            {"Speaker Bluetooth", "30", "275000"}
        };
        
        try {
            int successCount = 0;
            
            for (String[] data : sampleData) {
                String nama = data[0];
                double stok = Double.parseDouble(data[1]);
                double harga = Double.parseDouble(data[2]);
                
                long result = dataSource.createBarang(nama, stok, harga);
                if (result != -1) {
                    successCount++;
                }
            }
            
            // Refresh data setelah insert
            loadData();
            
            String message = "✅ Added " + successCount + " sample items";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Log.d(TAG, message);
            
        } catch (Exception e) {
            String errorMsg = "Error adding sample data: " + e.getMessage();
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
        }
    }
    
    private void clearAllData() {
        // Note: Implementasi clear all data tergantung pada DataSource
        // Untuk demo, kita clear adapter saja
        barangList.clear();
        barangAdapter.notifyDataSetChanged();
        
        updateInfo("Data cleared from adapter (database tetap utuh)");
        Toast.makeText(this, "Data cleared from view", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Adapter data cleared");
    }
    
    /**
     * Fungsi untuk menghapus data dari database SQLite (untuk PopupMenuDemoActivity)
     * @param id ID barang yang akan dihapus (idbarang)
     */
    public void deleteData(String id) {
        Log.d(TAG, "=== MEMULAI DELETE DATA (Demo Activity) ===");
        Log.d(TAG, "ID yang akan dihapus: " + id);
        
        // Deklarasikan variabel String bernama idbarang dan tetapkan nilai dari parameter id
        String idbarang = id;
        
        // Buat pernyataan SQL DELETE sebagai String bernama sql
        String sql = "DELETE FROM tblbarang WHERE idbarang = '" + idbarang + "'";
        
        Log.d(TAG, "SQL Query: " + sql);
        
        try {
            // Gunakan pernyataan if untuk memeriksa apakah eksekusi query berhasil
            // Menggunakan DataSource untuk konsistensi dengan demo activity
            int rowsAffected = dataSource.deleteBarang(Long.parseLong(idbarang));
            
            if (rowsAffected > 0) {
                // Jika penghapusan berhasil
                Toast.makeText(this, "Data sudah dihapus", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "✅ Data berhasil dihapus - ID: " + idbarang + ", Rows affected: " + rowsAffected);
                
                // Panggil fungsi untuk memperbarui RecyclerView setelah penghapusan
                loadData(); // Memuat ulang data ke RecyclerView
                
            } else {
                // Jika penghapusan gagal
                Toast.makeText(this, "Data tidak bisa dihapus", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "❌ Gagal menghapus data - ID: " + idbarang + " (No rows affected)");
            }
            
        } catch (Exception e) {
            // Handle exception
            String errorMsg = "Error saat menghapus data: " + e.getMessage();
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
        }
        
        Log.d(TAG, "=== DELETE DATA SELESAI (Demo Activity) ===");
    }
    
    /**
     * Fungsi untuk menghapus data langsung tanpa konfirmasi dialog (untuk PopupMenuDemoActivity)
     * Digunakan ketika konfirmasi sudah dilakukan di adapter
     * @param id ID barang yang akan dihapus (idbarang)
     */
    public void deleteDataDirect(String id) {
        Log.d(TAG, "=== MEMULAI DELETE DATA DIRECT (Demo Activity) ===");
        Log.d(TAG, "ID yang akan dihapus: " + id);
        
        // Deklarasikan variabel String bernama idbarang dan tetapkan nilai dari parameter id
        String idbarang = id;
        
        Log.d(TAG, "Menghapus data dengan ID: " + idbarang);
        
        try {
            // Gunakan DataSource untuk konsistensi dengan demo activity
            int rowsAffected = dataSource.deleteBarang(Long.parseLong(idbarang));
            
            if (rowsAffected > 0) {
                // Jika penghapusan berhasil
                Toast.makeText(this, "✅ Data sudah dihapus - ID: " + idbarang, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "✅ Data berhasil dihapus - ID: " + idbarang + ", Rows affected: " + rowsAffected);
                
                // Panggil fungsi untuk memperbarui RecyclerView setelah penghapusan
                loadData(); // Memuat ulang data ke RecyclerView
                
            } else {
                // Jika penghapusan gagal
                Toast.makeText(this, "❌ Data tidak bisa dihapus - ID: " + idbarang, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "❌ Gagal menghapus data - ID: " + idbarang + " (No rows affected)");
            }
            
        } catch (Exception e) {
            // Handle exception
            String errorMsg = "Error saat menghapus data: " + e.getMessage();
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
        }
        
        Log.d(TAG, "=== DELETE DATA DIRECT SELESAI (Demo Activity) ===");
    }
    
    private void updateInfo(String message) {
        String info = "🔧 POPUP MENU DEMO\n" +
                     "==================\n" +
                     "Klik ikon ••• pada setiap item untuk:\n" +
                     "• UBAH - Edit item\n" +
                     "• HAPUS - Delete item\n\n" +
                     "Status: " + message;
        
        tvDemoInfo.setText(info);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Activity destroyed");
    }
}
