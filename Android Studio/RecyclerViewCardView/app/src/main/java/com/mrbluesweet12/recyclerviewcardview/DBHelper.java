package com.mrbluesweet12.recyclerviewcardview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Kelas DBHelper untuk menangani pembuatan dan pengelolaan database SQLite
 * Database: aplikasibarang.db
 * Tabel: tabel_barang dengan kolom id, nama, stok, harga
 */
public class DBHelper extends SQLiteOpenHelper {
    
    // Konstanta untuk database
    private static final String DATABASE_NAME = "aplikasibarang.db";
    private static final int DATABASE_VERSION = 1;
    
    // Konstanta untuk tabel dan kolom
    public static final String TABLE_NAME = "tabel_barang";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_STOK = "stok";
    public static final String COLUMN_HARGA = "harga";
    
    // SQL statement untuk membuat tabel
    private static final String CREATE_TABLE_SQL = 
        "CREATE TABLE " + TABLE_NAME + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_NAMA + " TEXT, " +
        COLUMN_STOK + " REAL, " +
        COLUMN_HARGA + " REAL" +
        ");";
    
    private static final String TAG = "DBHelper";
    
    /**
     * Konstruktor untuk DBHelper
     * @param context Context dari aplikasi
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DBHelper constructor called");
    }
    
    /**
     * Metode yang dipanggil ketika database dibuat untuk pertama kali
     * @param db Instance SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database table: " + TABLE_NAME);
        
        try {
            // Eksekusi SQL untuk membuat tabel
            db.execSQL(CREATE_TABLE_SQL);
            Log.d(TAG, "Table " + TABLE_NAME + " created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating table: " + e.getMessage());
        }
    }
    
    /**
     * Metode yang dipanggil ketika versi database ditingkatkan
     * @param db Instance SQLiteDatabase
     * @param oldVersion Versi database lama
     * @param newVersion Versi database baru
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        
        try {
            // Hapus tabel lama jika ada
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            Log.d(TAG, "Old table dropped successfully");
            
            // Buat tabel baru
            onCreate(db);
            Log.d(TAG, "Database upgrade completed");
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading database: " + e.getMessage());
        }
    }
    
    /**
     * Metode untuk mendapatkan informasi database
     * @return String informasi database
     */
    public String getDatabaseInfo() {
        return "Database: " + DATABASE_NAME + ", Version: " + DATABASE_VERSION + ", Table: " + TABLE_NAME;
    }
}
