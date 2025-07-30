package com.mrbluesweet12.recyclerviewcardview;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    
    // Konstanta Database
    private static final String DATABASE_NAME = "dbtoko";
    private static final int VERSION = 1;
    
    // Tag untuk logging
    private static final String TAG = "Database";
    
    // Konstruktor
    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Inisialisasi database - mendapatkan instance writable database
        SQLiteDatabase database = this.getWritableDatabase();
        
        // Panggil fungsi untuk membuat tabel
        buatTabel();
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Biarkan kosong untuk saat ini
        // Logika upgrade database akan ditambahkan nanti
    }
    
    /**
     * Fungsi untuk mengeksekusi perintah SQL SELECT
     * @param sql Perintah SQL SELECT yang akan dieksekusi
     * @param selectionArgs Argumen untuk query (dapat null untuk query sederhana)
     * @return Cursor yang berisi hasil query, atau null jika terjadi error
     */
    public Cursor select(String sql, String[] selectionArgs) {
        try {
            // Mendapatkan instance database yang dapat dibaca
            SQLiteDatabase database = this.getReadableDatabase();
            
            // Log query yang akan dieksekusi
            Log.d(TAG, "Mengeksekusi SELECT query: " + sql);
            if (selectionArgs != null) {
                Log.d(TAG, "Dengan arguments: " + java.util.Arrays.toString(selectionArgs));
            }
            
            // Eksekusi query SELECT menggunakan rawQuery
            Cursor cursor = database.rawQuery(sql, selectionArgs);
            
            // Log hasil query
            if (cursor != null) {
                Log.d(TAG, "SELECT berhasil, jumlah baris: " + cursor.getCount());
            } else {
                Log.w(TAG, "SELECT mengembalikan cursor null");
            }
            
            return cursor;
            
        } catch (SQLException e) {
            // Log error SQLException
            Log.e(TAG, "SQLException saat mengeksekusi SELECT: " + sql, e);
            e.printStackTrace();
            return null;
            
        } catch (Exception e) {
            // Log error umum
            Log.e(TAG, "Exception saat mengeksekusi SELECT: " + sql, e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fungsi untuk mengeksekusi perintah SQL mentah
     * @param sql Perintah SQL yang akan dieksekusi
     * @return true jika berhasil, false jika gagal
     */
    public boolean runSQL(String sql) {
        try {
            // Mendapatkan instance database yang dapat ditulis
            SQLiteDatabase database = this.getWritableDatabase();
            
            // Mengeksekusi perintah SQL
            database.execSQL(sql);
            
            // Log sukses
            Log.d(TAG, "SQL berhasil dieksekusi: " + sql);
            return true;
            
        } catch (SQLiteException e) {
            // Log error SQLiteException
            Log.e(TAG, "SQLiteException saat mengeksekusi SQL: " + sql, e);
            return false;
            
        } catch (Exception e) {
            // Log error umum
            Log.e(TAG, "Exception saat mengeksekusi SQL: " + sql, e);
            return false;
        }
    }
    
    /**
     * Fungsi untuk membuat tabel tblbarang
     */
    public void buatTabel() {
        // Definisi SQL CREATE TABLE untuk tblbarang
        String createTableQuery = "CREATE TABLE IF NOT EXISTS tblbarang (" +
                "idbarang INTEGER PRIMARY KEY AUTOINCREMENT," +
                "barang TEXT," +
                "stok REAL," +
                "harga REAL" +
                ")";
        
        // Log informasi pembuatan tabel
        Log.d(TAG, "Memulai pembuatan tabel tblbarang...");
        
        // Eksekusi pembuatan tabel menggunakan fungsi runSQL
        boolean hasil = runSQL(createTableQuery);
        
        // Log hasil pembuatan tabel
        if (hasil) {
            Log.d(TAG, "Tabel tblbarang berhasil dibuat atau sudah ada");
        } else {
            Log.e(TAG, "Gagal membuat tabel tblbarang");
        }
    }
}
