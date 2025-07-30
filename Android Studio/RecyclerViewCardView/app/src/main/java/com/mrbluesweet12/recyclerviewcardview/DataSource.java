package com.mrbluesweet12.recyclerviewcardview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Kelas DataSource untuk menangani operasi CRUD pada database SQLite
 * Menyediakan metode untuk Create, Read, Update, Delete data barang
 */
public class DataSource {
    
    // Variabel instance
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private final Context context;
    
    private static final String TAG = "DataSource";
    
    /**
     * Konstruktor untuk DataSource
     * @param context Context dari aplikasi
     */
    public DataSource(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
        Log.d(TAG, "DataSource initialized");
    }
    
    /**
     * Membuka koneksi database untuk operasi write
     * @throws SQLException jika terjadi error saat membuka database
     */
    public void open() throws SQLException {
        try {
            database = dbHelper.getWritableDatabase();
            Log.d(TAG, "Database connection opened");
        } catch (SQLException e) {
            Log.e(TAG, "Error opening database: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Menutup koneksi database
     */
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
            Log.d(TAG, "Database connection closed");
        }
    }
    
    /**
     * Menambahkan data barang baru ke database dengan validasi input
     * @param nama Nama barang
     * @param stok Jumlah stok barang
     * @param harga Harga barang
     * @return ID barang yang baru dibuat, atau -1 jika gagal
     */
    public long createBarang(String nama, double stok, double harga) {
        Log.d(TAG, "Creating new barang: " + nama + ", stok: " + stok + ", harga: " + harga);
        
        // Validasi input sebelum insert
        String validationError = DatabaseUtils.validateBarangInput(nama, stok, harga);
        if (validationError != null) {
            Log.e(TAG, "Validation failed: " + validationError);
            return -1;
        }
        
        try {
            // Buat ContentValues untuk menyimpan data
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_NAMA, nama.trim());
            values.put(DBHelper.COLUMN_STOK, stok);
            values.put(DBHelper.COLUMN_HARGA, harga);
            
            // Insert data ke database
            long insertId = database.insert(DBHelper.TABLE_NAME, null, values);
            
            if (insertId != -1) {
                Log.d(TAG, "Barang created successfully with ID: " + insertId);
            } else {
                Log.e(TAG, "Failed to create barang - database.insert() returned -1");
            }
            
            return insertId;
            
        } catch (SQLException e) {
            Log.e(TAG, "SQLException creating barang: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error creating barang: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Metode alternatif INSERT menggunakan execSQL (seperti yang dijelaskan di video)
     * @param nama Nama barang
     * @param stok Jumlah stok barang
     * @param harga Harga barang
     * @return true jika berhasil, false jika gagal
     */
    public boolean insertBarang(String nama, double stok, double harga) {
        Log.d(TAG, "Inserting barang using execSQL: " + nama + ", stok: " + stok + ", harga: " + harga);
        
        // Validasi input sebelum insert
        String validationError = DatabaseUtils.validateBarangInput(nama, stok, harga);
        if (validationError != null) {
            Log.e(TAG, "Validation failed: " + validationError);
            return false;
        }
        
        try {
            // Escape single quotes dalam nama untuk mencegah SQL injection
            String escapedNama = nama.trim().replace("'", "''");
            
            // Bangun SQL query string dengan penanganan tipe data yang benar
            String sqlQuery = "INSERT INTO " + DBHelper.TABLE_NAME + 
                             " (" + DBHelper.COLUMN_NAMA + ", " + 
                             DBHelper.COLUMN_STOK + ", " + 
                             DBHelper.COLUMN_HARGA + ") VALUES ('" + 
                             escapedNama + "', " + 
                             stok + ", " + 
                             harga + ");";
            
            Log.d(TAG, "Executing SQL: " + sqlQuery);
            
            // Eksekusi SQL query
            database.execSQL(sqlQuery);
            
            Log.d(TAG, "Insert berhasil menggunakan execSQL");
            return true;
            
        } catch (SQLException e) {
            Log.e(TAG, "SQLException executing INSERT: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error executing INSERT: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mengambil semua data barang dari database
     * @return Cursor yang berisi semua data barang, diurutkan berdasarkan nama
     */
    public Cursor getAllBarang() {
        Log.d(TAG, "Retrieving all barang data");
        
        try {
            // Query untuk mengambil semua data, diurutkan berdasarkan nama
            Cursor cursor = database.query(
                DBHelper.TABLE_NAME,           // nama tabel
                null,                          // kolom (null = semua kolom)
                null,                          // selection
                null,                          // selectionArgs
                null,                          // groupBy
                null,                          // having
                DBHelper.COLUMN_NAMA + " ASC"  // orderBy
            );
            
            Log.d(TAG, "Retrieved " + cursor.getCount() + " barang records");
            return cursor;
            
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving barang data: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Mengambil data barang berdasarkan ID
     * @param id ID barang yang dicari
     * @return Cursor yang berisi data barang dengan ID tersebut
     */
    public Cursor getBarangById(long id) {
        Log.d(TAG, "Retrieving barang with ID: " + id);
        
        try {
            Cursor cursor = database.query(
                DBHelper.TABLE_NAME,
                null,
                DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
            );
            
            return cursor;
            
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving barang by ID: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Mengupdate data barang berdasarkan ID
     * @param id ID barang yang akan diupdate
     * @param nama Nama barang baru
     * @param stok Stok barang baru
     * @param harga Harga barang baru
     * @return Jumlah baris yang terpengaruh (1 jika berhasil, 0 jika gagal)
     */
    public int updateBarang(long id, String nama, double stok, double harga) {
        Log.d(TAG, "Updating barang ID " + id + ": " + nama + ", stok: " + stok + ", harga: " + harga);
        
        try {
            // Buat ContentValues untuk data yang akan diupdate
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_NAMA, nama);
            values.put(DBHelper.COLUMN_STOK, stok);
            values.put(DBHelper.COLUMN_HARGA, harga);
            
            // Update data berdasarkan ID
            int rowsAffected = database.update(
                DBHelper.TABLE_NAME,
                values,
                DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
            );
            
            if (rowsAffected > 0) {
                Log.d(TAG, "Barang updated successfully. Rows affected: " + rowsAffected);
            } else {
                Log.w(TAG, "No barang found with ID: " + id);
            }
            
            return rowsAffected;
            
        } catch (Exception e) {
            Log.e(TAG, "Error updating barang: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Menghapus data barang berdasarkan ID
     * @param id ID barang yang akan dihapus
     * @return Jumlah baris yang terpengaruh (1 jika berhasil, 0 jika gagal)
     */
    public int deleteBarang(long id) {
        Log.d(TAG, "Deleting barang with ID: " + id);
        
        try {
            // Hapus data berdasarkan ID
            int rowsAffected = database.delete(
                DBHelper.TABLE_NAME,
                DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
            );
            
            if (rowsAffected > 0) {
                Log.d(TAG, "Barang deleted successfully. Rows affected: " + rowsAffected);
            } else {
                Log.w(TAG, "No barang found with ID: " + id);
            }
            
            return rowsAffected;
            
        } catch (Exception e) {
            Log.e(TAG, "Error deleting barang: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Menghitung total jumlah barang dalam database
     * @return Jumlah total barang
     */
    public int getBarangCount() {
        try {
            Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + DBHelper.TABLE_NAME, null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            
            Log.d(TAG, "Total barang count: " + count);
            return count;
            
        } catch (Exception e) {
            Log.e(TAG, "Error counting barang: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Menghapus semua data barang (clear table)
     * @return Jumlah baris yang dihapus
     */
    public int deleteAllBarang() {
        Log.d(TAG, "Deleting all barang data");
        
        try {
            int rowsAffected = database.delete(DBHelper.TABLE_NAME, null, null);
            Log.d(TAG, "All barang deleted. Rows affected: " + rowsAffected);
            return rowsAffected;
            
        } catch (Exception e) {
            Log.e(TAG, "Error deleting all barang: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Cek apakah database terbuka dan dapat digunakan
     * @return true jika database terbuka, false jika tidak
     */
    public boolean isDatabaseOpen() {
        return database != null && database.isOpen();
    }
    
    /**
     * Eksekusi SQL UPDATE statement menggunakan execSQL
     * Method ini untuk demonstrasi pembelajaran execSQL
     * @param sqlStatement SQL UPDATE statement yang akan dieksekusi
     * @return true jika berhasil, false jika gagal
     */
    public boolean executeUpdateSQL(String sqlStatement) {
        Log.d(TAG, "=== EXECUTING UPDATE SQL ===");
        Log.d(TAG, "SQL Statement: " + sqlStatement);
        
        try {
            // Pastikan database terbuka
            if (database == null || !database.isOpen()) {
                Log.e(TAG, "Database is not open for execSQL operation");
                return false;
            }
            
            // Eksekusi SQL statement
            database.execSQL(sqlStatement);
            
            Log.d(TAG, "✅ SQL UPDATE executed successfully");
            return true;
            
        } catch (SQLException e) {
            Log.e(TAG, "❌ SQL Error in executeUpdateSQL: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.e(TAG, "❌ Unexpected error in executeUpdateSQL: " + e.getMessage());
            return false;
        }
    }
}
