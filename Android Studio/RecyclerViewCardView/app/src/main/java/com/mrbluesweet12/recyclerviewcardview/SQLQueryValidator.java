package com.mrbluesweet12.recyclerviewcardview;

import android.util.Log;

/**
 * Class untuk demonstrasi dan testing SQL queries
 * Simulasi testing yang biasanya dilakukan di SQLite Browser
 */
public class SQLQueryValidator {
    
    private static final String TAG = "SQLQueryValidator";
    
    /**
     * Simulasi testing query INSERT di SQLite browser
     * Method ini menunjukkan query yang harus ditest secara manual
     */
    public static void simulateQueryTesting() {
        Log.d(TAG, "=== SIMULASI TESTING QUERY DI SQLITE BROWSER ===");
        
        // Query untuk membuat tabel (sudah dihandle oleh DBHelper)
        String createTableQuery = "CREATE TABLE tabel_barang (" +
                                 "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                 "nama TEXT, " +
                                 "stok REAL, " +
                                 "harga REAL" +
                                 ");";
        
        Log.d(TAG, "1. Create Table Query:");
        Log.d(TAG, createTableQuery);
        Log.d(TAG, "");
        
        // Query INSERT yang harus divalidasi
        String[] insertQueries = {
            "INSERT INTO tabel_barang (nama, stok, harga) VALUES ('Pensil', 50.0, 2500.0);",
            "INSERT INTO tabel_barang (nama, stok, harga) VALUES ('Buku Tulis', 100.0, 7500.0);",
            "INSERT INTO tabel_barang (nama, stok, harga) VALUES ('Penghapus', 25.5, 1500.0);",
            "INSERT INTO tabel_barang (nama, stok, harga) VALUES ('Pulpen', 75.0, 3000.0);",
            "INSERT INTO tabel_barang (nama, stok, harga) VALUES ('Spidol', 30.0, 5000.0);"
        };
        
        Log.d(TAG, "2. INSERT Queries untuk testing:");
        for (int i = 0; i < insertQueries.length; i++) {
            Log.d(TAG, "Query " + (i + 1) + ": " + insertQueries[i]);
        }
        Log.d(TAG, "");
        
        // Query SELECT untuk verifikasi
        String[] selectQueries = {
            "SELECT * FROM tabel_barang;",
            "SELECT nama, stok FROM tabel_barang WHERE harga > 3000;",
            "SELECT COUNT(*) FROM tabel_barang;",
            "SELECT * FROM tabel_barang ORDER BY nama ASC;"
        };
        
        Log.d(TAG, "3. SELECT Queries untuk verifikasi:");
        for (int i = 0; i < selectQueries.length; i++) {
            Log.d(TAG, "Verify " + (i + 1) + ": " + selectQueries[i]);
        }
        Log.d(TAG, "");
        
        // Tips untuk testing
        Log.d(TAG, "4. TIPS TESTING DI SQLITE BROWSER:");
        Log.d(TAG, "   - Buka database aplikasibarang.db");
        Log.d(TAG, "   - Jalankan CREATE TABLE (jika belum ada)");
        Log.d(TAG, "   - Test setiap INSERT query satu per satu");
        Log.d(TAG, "   - Verifikasi dengan SELECT queries");
        Log.d(TAG, "   - Perhatikan tipe data: TEXT dengan quotes, REAL tanpa quotes");
        Log.d(TAG, "   - Pastikan tidak ada SQL injection");
        
        Log.d(TAG, "=== END SIMULASI ===");
    }
    
    /**
     * Validasi format query INSERT
     * @param nama Nama barang
     * @param stok Stok barang
     * @param harga Harga barang
     * @return Query string yang telah divalidasi
     */
    public static String buildValidatedInsertQuery(String nama, double stok, double harga) {
        // Escape single quotes untuk mencegah SQL injection
        String escapedNama = nama.replace("'", "''");
        
        // Build query dengan format yang benar
        String query = "INSERT INTO tabel_barang (nama, stok, harga) VALUES ('" + 
                      escapedNama + "', " + 
                      stok + ", " + 
                      harga + ");";
        
        Log.d(TAG, "Validated INSERT Query: " + query);
        return query;
    }
    
    /**
     * Demonstrasi perbedaan penanganan tipe data
     */
    public static void demonstrateDataTypeHandling() {
        Log.d(TAG, "=== DEMONSTRASI PENANGANAN TIPE DATA ===");
        
        Log.d(TAG, "BENAR - TEXT dengan single quotes:");
        Log.d(TAG, "INSERT INTO tabel_barang (nama) VALUES ('Laptop Gaming');");
        
        Log.d(TAG, "SALAH - TEXT tanpa quotes:");
        Log.d(TAG, "INSERT INTO tabel_barang (nama) VALUES (Laptop Gaming); -- ERROR!");
        
        Log.d(TAG, "BENAR - REAL tanpa quotes:");
        Log.d(TAG, "INSERT INTO tabel_barang (stok, harga) VALUES (10.5, 2500000.0);");
        
        Log.d(TAG, "SALAH - REAL dengan quotes:");
        Log.d(TAG, "INSERT INTO tabel_barang (stok, harga) VALUES ('10.5', '2500000.0'); -- Bisa jalan tapi tidak optimal");
        
        Log.d(TAG, "=== END DEMONSTRASI ===");
    }
    
    /**
     * Contoh query yang bisa gagal dan cara mengatasinya
     */
    public static void demonstrateErrorHandling() {
        Log.d(TAG, "=== DEMONSTRASI ERROR HANDLING ===");
        
        Log.d(TAG, "1. MASALAH: Single quote dalam nama");
        Log.d(TAG, "   Nama: O'Reilly Book");
        Log.d(TAG, "   SALAH: INSERT INTO tabel_barang (nama) VALUES ('O'Reilly Book'); -- Syntax Error!");
        Log.d(TAG, "   BENAR: INSERT INTO tabel_barang (nama) VALUES ('O''Reilly Book'); -- Escape quote");
        
        Log.d(TAG, "2. MASALAH: Nilai NULL");
        Log.d(TAG, "   SALAH: INSERT INTO tabel_barang (nama, stok) VALUES (NULL, 10); -- Nama tidak boleh NULL");
        Log.d(TAG, "   BENAR: INSERT INTO tabel_barang (nama, stok) VALUES ('Unknown Item', 10);");
        
        Log.d(TAG, "3. MASALAH: Tipe data salah");
        Log.d(TAG, "   SALAH: INSERT INTO tabel_barang (nama, stok) VALUES ('Item', 'banyak'); -- stok harus angka");
        Log.d(TAG, "   BENAR: INSERT INTO tabel_barang (nama, stok) VALUES ('Item', 100.0);");
        
        Log.d(TAG, "=== END ERROR HANDLING ===");
    }
}
