package com.mrbluesweet12.recyclerviewcardview;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity untuk testing operasi database
 * Menyediakan interface untuk testing CRUD operations
 */
public class DatabaseCRUDTestActivity extends AppCompatActivity {
    
    private DataSource dataSource;
    private EditText etNama, etStok, etHarga, etId;
    private Button btnCreate, btnRead, btnUpdate, btnDelete, btnClear;
    private TextView tvResults;
    
    private static final String TAG = "DatabaseCRUDTest";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_crud_test);
        
        initializeViews();
        initializeDatabase();
        setupEventListeners();
        
        // Load initial data untuk testing
        loadInitialData();
    }
    
    private void initializeViews() {
        etNama = findViewById(R.id.et_nama);
        etStok = findViewById(R.id.et_stok);
        etHarga = findViewById(R.id.et_harga);
        etId = findViewById(R.id.et_id);
        
        btnCreate = findViewById(R.id.btn_create);
        btnRead = findViewById(R.id.btn_read);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        btnClear = findViewById(R.id.btn_clear);
        
        tvResults = findViewById(R.id.tv_results);
    }
    
    private void initializeDatabase() {
        dataSource = new DataSource(this);
        
        try {
            dataSource.open();
            Log.d(TAG, "Database opened successfully");
            tvResults.setText("Database initialized successfully!\nReady for CRUD operations.\n\n");
        } catch (Exception e) {
            Log.e(TAG, "Error opening database: " + e.getMessage());
            tvResults.setText("Error initializing database: " + e.getMessage());
        }
    }
    
    private void setupEventListeners() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBarang();
            }
        });
        
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAllBarang();
            }
        });
        
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBarang();
            }
        });
        
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBarang();
            }
        });
        
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllData();
            }
        });
    }
    
    private void createBarang() {
        String nama = etNama.getText().toString().trim();
        String stokStr = etStok.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        
        if (nama.isEmpty() || stokStr.isEmpty() || hargaStr.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field untuk create", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            double stok = Double.parseDouble(stokStr);
            double harga = Double.parseDouble(hargaStr);
            
            long result = dataSource.createBarang(nama, stok, harga);
            
            if (result != -1) {
                String message = "✅ CREATE SUCCESS!\nBarang created with ID: " + result + "\n\n";
                tvResults.setText(message + tvResults.getText());
                Toast.makeText(this, "Barang berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                clearInputFields();
            } else {
                tvResults.setText("❌ CREATE FAILED!\n\n" + tvResults.getText());
                Toast.makeText(this, "Gagal menambahkan barang", Toast.LENGTH_SHORT).show();
            }
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Format angka tidak valid", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void readAllBarang() {
        Cursor cursor = dataSource.getAllBarang();
        
        if (cursor != null) {
            StringBuilder result = new StringBuilder();
            result.append("📋 READ ALL BARANG:\n");
            result.append("==================\n");
            
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
                    String nama = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAMA));
                    double stok = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_STOK));
                    double harga = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_HARGA));
                    
                    result.append("ID: ").append(id).append("\n");
                    result.append("Nama: ").append(nama).append("\n");
                    result.append("Stok: ").append(stok).append("\n");
                    result.append("Harga: Rp ").append(String.format("%.0f", harga)).append("\n");
                    result.append("-------------------\n");
                    
                } while (cursor.moveToNext());
                
                result.append("Total: ").append(cursor.getCount()).append(" items\n\n");
            } else {
                result.append("No data found.\n\n");
            }
            
            cursor.close();
            tvResults.setText(result.toString() + tvResults.getText());
        } else {
            tvResults.setText("❌ READ FAILED!\n\n" + tvResults.getText());
        }
    }
    
    private void updateBarang() {
        String idStr = etId.getText().toString().trim();
        String nama = etNama.getText().toString().trim();
        String stokStr = etStok.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        
        if (idStr.isEmpty() || nama.isEmpty() || stokStr.isEmpty() || hargaStr.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field untuk update", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            long id = Long.parseLong(idStr);
            double stok = Double.parseDouble(stokStr);
            double harga = Double.parseDouble(hargaStr);
            
            int result = dataSource.updateBarang(id, nama, stok, harga);
            
            if (result > 0) {
                String message = "✅ UPDATE SUCCESS!\nBarang with ID " + id + " updated.\n\n";
                tvResults.setText(message + tvResults.getText());
                Toast.makeText(this, "Barang berhasil diupdate!", Toast.LENGTH_SHORT).show();
                clearInputFields();
            } else {
                tvResults.setText("❌ UPDATE FAILED!\nNo barang found with ID: " + id + "\n\n" + tvResults.getText());
                Toast.makeText(this, "Barang tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Format angka tidak valid", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void deleteBarang() {
        String idStr = etId.getText().toString().trim();
        
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Mohon isi ID untuk delete", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            long id = Long.parseLong(idStr);
            int result = dataSource.deleteBarang(id);
            
            if (result > 0) {
                String message = "✅ DELETE SUCCESS!\nBarang with ID " + id + " deleted.\n\n";
                tvResults.setText(message + tvResults.getText());
                Toast.makeText(this, "Barang berhasil dihapus!", Toast.LENGTH_SHORT).show();
                clearInputFields();
            } else {
                tvResults.setText("❌ DELETE FAILED!\nNo barang found with ID: " + id + "\n\n" + tvResults.getText());
                Toast.makeText(this, "Barang tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Format ID tidak valid", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void clearAllData() {
        int result = dataSource.deleteAllBarang();
        String message = "🗑️ CLEAR ALL DATA!\n" + result + " items deleted.\n\n";
        tvResults.setText(message + tvResults.getText());
        Toast.makeText(this, "Semua data berhasil dihapus!", Toast.LENGTH_SHORT).show();
    }
    
    private void clearInputFields() {
        etId.setText("");
        etNama.setText("");
        etStok.setText("");
        etHarga.setText("");
    }
    
    private void loadInitialData() {
        // Menambahkan beberapa data sample untuk testing
        long id1 = dataSource.createBarang("Laptop Gaming", 5, 15000000);
        long id2 = dataSource.createBarang("Mouse Wireless", 25, 250000);
        long id3 = dataSource.createBarang("Keyboard Mechanical", 12, 800000);
        
        String message = "🎯 INITIAL DATA LOADED:\n" +
                        "Sample data added for testing\n" +
                        "- Laptop Gaming (ID: " + id1 + ")\n" +
                        "- Mouse Wireless (ID: " + id2 + ")\n" +
                        "- Keyboard Mechanical (ID: " + id3 + ")\n\n";
        
        tvResults.setText(message + tvResults.getText());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataSource != null) {
            dataSource.close();
            Log.d(TAG, "Database connection closed");
        }
    }
}
