package com.mrbluesweet12.recyclerviewcardview;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DatabaseTestActivity extends AppCompatActivity {
    
    private Database database;
    private TextView tvStatus;
    private static final String TAG = "DatabaseTest";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
        
        // Inisialisasi views
        tvStatus = findViewById(R.id.tv_status);
        
        // Inisialisasi Database SQLite sesuai tutorial
        load();
        
        // Test pembuatan tabel dan verifikasi
        testDatabase();
    }
    
    // Metode untuk inisialisasi database sesuai spesifikasi tutorial
    private void load() {
        database = new Database(this);
        
        // Membuat tabel tblbarang
        database.buatTabel();
        
        Toast.makeText(this, "Database dbtoko dan tabel tblbarang berhasil diinisialisasi!", Toast.LENGTH_LONG).show();
    }
    
    /**
     * Fungsi untuk memverifikasi bahwa tabel tblbarang telah dibuat
     */
    private void testDatabase() {
        try {
            SQLiteDatabase db = database.getReadableDatabase();
            
            // Query untuk memeriksa apakah tabel tblbarang ada
            String checkTableQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='tblbarang'";
            Cursor cursor = db.rawQuery(checkTableQuery, null);
            
            StringBuilder statusText = new StringBuilder();
            statusText.append("📊 VERIFIKASI DATABASE SQLite\n");
            statusText.append("==============================\n\n");
            
            statusText.append("🗄️ Database: dbtoko\n");
            statusText.append("📍 Path: ").append(db.getPath()).append("\n");
            statusText.append("🔢 Versi: ").append(db.getVersion()).append("\n\n");
            
            if (cursor.moveToFirst()) {
                statusText.append("✅ Tabel 'tblbarang' berhasil dibuat!\n\n");
                
                // Query untuk memeriksa struktur tabel
                String tableInfoQuery = "PRAGMA table_info(tblbarang)";
                Cursor structureCursor = db.rawQuery(tableInfoQuery, null);
                
                statusText.append("📋 STRUKTUR TABEL tblbarang:\n");
                statusText.append("--------------------------------\n");
                while (structureCursor.moveToNext()) {
                    int cid = structureCursor.getInt(0);
                    String columnName = structureCursor.getString(1);
                    String columnType = structureCursor.getString(2);
                    int notNull = structureCursor.getInt(3);
                    String defaultValue = structureCursor.getString(4);
                    int isPrimaryKey = structureCursor.getInt(5);
                    
                    statusText.append("• ").append(columnName)
                              .append(" (").append(columnType).append(")");
                    
                    if (isPrimaryKey == 1) {
                        statusText.append(" [PRIMARY KEY]");
                    }
                    if (notNull == 1) {
                        statusText.append(" [NOT NULL]");
                    }
                    if (defaultValue != null && !defaultValue.isEmpty()) {
                        statusText.append(" [DEFAULT: ").append(defaultValue).append("]");
                    }
                    statusText.append("\n");
                }
                structureCursor.close();
                
                // Hitung jumlah record dalam tabel
                statusText.append("\n📊 STATISTIK TABEL:\n");
                Cursor countCursor = db.rawQuery("SELECT COUNT(*) FROM tblbarang", null);
                if (countCursor.moveToFirst()) {
                    int count = countCursor.getInt(0);
                    statusText.append("• Jumlah record: ").append(count).append("\n");
                }
                countCursor.close();
                
                statusText.append("\n🔧 OPERASI CRUD TERSEDIA:\n");
                statusText.append("• CREATE: Menambah data barang\n");
                statusText.append("• READ: Membaca data barang\n");
                statusText.append("• UPDATE: Mengubah data barang\n");
                statusText.append("• DELETE: Menghapus data barang\n\n");
                
                statusText.append("✅ Database siap untuk operasi CRUD!");
                Log.d(TAG, "Tabel tblbarang berhasil diverifikasi dengan lengkap");
                
            } else {
                statusText.append("❌ Tabel 'tblbarang' tidak ditemukan!\n");
                statusText.append("⚠️ Periksa method buatTabel() di Database.java\n");
                statusText.append("💡 Pastikan SQL CREATE TABLE sudah benar");
                Log.e(TAG, "Tabel tblbarang tidak ditemukan");
            }
            
            cursor.close();
            db.close();
            
            // Update status text
            tvStatus.setText(statusText.toString());
            
        } catch (Exception e) {
            String errorText = "❌ ERROR VERIFIKASI DATABASE:\n\n" + 
                              e.getMessage() + "\n\n" +
                              "📝 Periksa Logcat untuk detail lengkap";
            tvStatus.setText(errorText);
            Log.e(TAG, "Error verifikasi database", e);
        }
    }
    
    @Override
    protected void onDestroy() {
        if (database != null) {
            database.close();
        }
        super.onDestroy();
    }
}
