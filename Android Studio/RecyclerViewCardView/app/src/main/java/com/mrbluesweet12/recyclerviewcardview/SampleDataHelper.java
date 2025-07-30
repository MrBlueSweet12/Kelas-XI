package com.mrbluesweet12.recyclerviewcardview;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Utility class untuk menambahkan data contoh ke database
 * Memudahkan testing SELECT query dan RecyclerView
 */
public class SampleDataHelper {
    
    private static final String TAG = "SampleDataHelper";
    
    /**
     * Menambahkan data contoh ke database untuk testing
     * @param context Context aplikasi
     * @param dataSource DataSource untuk operasi database
     */
    public static void insertSampleData(Context context, DataSource dataSource) {
        Log.d(TAG, "Inserting sample data...");
        
        try {
            // Data contoh untuk testing
            String[][] sampleData = {
                {"Pensil HB", "100", "2500"},
                {"Buku Tulis 58 Lembar", "75", "7500"},
                {"Penghapus Putih", "50", "1500"},
                {"Bolpoint Biru", "200", "3000"},
                {"Penggaris 30cm", "30", "5000"},
                {"Spidol Permanent", "40", "8500"},
                {"Correction Tape", "25", "12000"},
                {"Stapler Kecil", "15", "25000"},
                {"Lem Stick", "60", "6500"},
                {"Kertas HVS A4", "10", "45000"}
            };
            
            int successCount = 0;
            int errorCount = 0;
            
            for (String[] data : sampleData) {
                try {
                    String nama = data[0];
                    double stok = Double.parseDouble(data[1]);
                    double harga = Double.parseDouble(data[2]);
                    
                    long result = dataSource.createBarang(nama, stok, harga);
                    if (result != -1) {
                        successCount++;
                        Log.d(TAG, "Inserted: " + nama + " - ID: " + result);
                    } else {
                        errorCount++;
                        Log.w(TAG, "Failed to insert: " + nama);
                    }
                } catch (Exception e) {
                    errorCount++;
                    Log.e(TAG, "Error inserting " + data[0] + ": " + e.getMessage());
                }
            }
            
            String message = "Sample data inserted!\nSuccess: " + successCount + 
                           "\nErrors: " + errorCount;
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Log.d(TAG, message);
            
        } catch (Exception e) {
            String errorMsg = "Error inserting sample data: " + e.getMessage();
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
        }
    }
    
    /**
     * Mengecek apakah database sudah berisi data
     * @param dataSource DataSource untuk operasi database
     * @return true jika database kosong, false jika sudah ada data
     */
    public static boolean isDatabaseEmpty(DataSource dataSource) {
        try {
            int count = dataSource.getBarangCount();
            return count == 0;
        } catch (Exception e) {
            Log.e(TAG, "Error checking database: " + e.getMessage());
            return true;
        }
    }
    
    /**
     * Reset database dengan menghapus semua data dan memasukkan data sample baru
     * @param context Context aplikasi
     * @param dataSource DataSource untuk operasi database
     */
    public static void resetWithSampleData(Context context, DataSource dataSource) {
        try {
            // Hapus semua data lama (jika ada method deleteAll)
            // dataSource.deleteAllBarang(); // Implementasi optional
            
            // Insert data baru
            insertSampleData(context, dataSource);
            
        } catch (Exception e) {
            String errorMsg = "Error resetting database: " + e.getMessage();
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg, e);
        }
    }
}
