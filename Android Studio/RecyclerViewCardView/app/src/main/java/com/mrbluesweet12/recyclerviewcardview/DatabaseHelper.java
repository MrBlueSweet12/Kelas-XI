package com.mrbluesweet12.recyclerviewcardview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    // Database Info
    private static final String DATABASE_NAME = "db_siswa.db";
    private static final int DATABASE_VERSION = 1;
    
    // Table Names
    private static final String TABLE_SISWA = "tbl_siswa";
    
    // Column Names
    private static final String COLUMN_ID = "id_siswa";
    private static final String COLUMN_NAMA = "nama";
    private static final String COLUMN_ALAMAT = "alamat";
    private static final String COLUMN_CREATED_AT = "created_at";
    
    // Create Table SQL
    private static final String CREATE_TABLE_SISWA = 
        "CREATE TABLE " + TABLE_SISWA + "(" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        COLUMN_NAMA + " TEXT NOT NULL," +
        COLUMN_ALAMAT + " TEXT NOT NULL," +
        COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
        ")";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SISWA);
        
        // Insert sample data
        insertSampleData(db);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SISWA);
        onCreate(db);
    }
    
    private void insertSampleData(SQLiteDatabase db) {
        String[] namaArray = {"Joni", "Budi", "Siti", "Ahmad", "Dewi", "Eko", "Fitri", "Galih", "Hana", "Irfan"};
        String[] alamatArray = {"Surabaya", "Malang", "Jakarta", "Bandung", "Yogyakarta", "Semarang", "Medan", "Solo", "Denpasar", "Makassar"};
        
        for (int i = 0; i < namaArray.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAMA, namaArray[i]);
            values.put(COLUMN_ALAMAT, alamatArray[i]);
            db.insert(TABLE_SISWA, null, values);
        }
    }
    
    // CRUD Operations
    
    // Create - Menambah siswa baru
    public long addSiswa(Siswa siswa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, siswa.getNama());
        values.put(COLUMN_ALAMAT, siswa.getAlamat());
        
        long id = db.insert(TABLE_SISWA, null, values);
        db.close();
        return id;
    }
    
    // Read - Mengambil semua siswa
    public List<Siswa> getAllSiswa() {
        List<Siswa> siswaList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SISWA + " ORDER BY " + COLUMN_NAMA + " ASC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Siswa siswa = new Siswa();
                siswa.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                siswa.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)));
                siswa.setAlamat(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALAMAT)));
                siswaList.add(siswa);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return siswaList;
    }
    
    // Read - Mengambil siswa berdasarkan ID
    public Siswa getSiswaById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SISWA,
                new String[]{COLUMN_ID, COLUMN_NAMA, COLUMN_ALAMAT},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            Siswa siswa = new Siswa();
            siswa.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            siswa.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)));
            siswa.setAlamat(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALAMAT)));
            cursor.close();
            db.close();
            return siswa;
        }
        
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }
    
    // Update - Memperbarui data siswa
    public int updateSiswa(Siswa siswa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, siswa.getNama());
        values.put(COLUMN_ALAMAT, siswa.getAlamat());
        
        int result = db.update(TABLE_SISWA, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(siswa.getId())});
        db.close();
        return result;
    }
    
    // Delete - Menghapus siswa
    public void deleteSiswa(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SISWA, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
    
    // Search - Mencari siswa berdasarkan nama
    public List<Siswa> searchSiswa(String keyword) {
        List<Siswa> siswaList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SISWA + 
                           " WHERE " + COLUMN_NAMA + " LIKE '%" + keyword + "%'" +
                           " OR " + COLUMN_ALAMAT + " LIKE '%" + keyword + "%'" +
                           " ORDER BY " + COLUMN_NAMA + " ASC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Siswa siswa = new Siswa();
                siswa.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                siswa.setNama(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)));
                siswa.setAlamat(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALAMAT)));
                siswaList.add(siswa);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return siswaList;
    }
    
    // Get count of students
    public int getSiswaCount() {
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_SISWA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        
        cursor.close();
        db.close();
        return count;
    }
}
