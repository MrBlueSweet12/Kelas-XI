package com.mrbluesweet12.recyclerviewcardview;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class untuk konversi data antara Cursor dan Object Barang
 * Menyediakan utility methods untuk mempermudah pengelolaan data
 */
public class DatabaseUtils {
    
    /**
     * Konversi single row dari Cursor ke object Barang
     * @param cursor Cursor yang menunjuk ke row tertentu
     * @return Object Barang atau null jika error
     */
    public static Barang cursorToBarang(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            return null;
        }
        
        try {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
            String nama = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAMA));
            double stok = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_STOK));
            double harga = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_HARGA));
            
            return new Barang(id, nama, stok, harga);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Konversi semua data dari Cursor ke List of Barang
     * @param cursor Cursor yang berisi data barang
     * @return List<Barang> atau empty list jika tidak ada data
     */
    public static List<Barang> cursorToBarangList(Cursor cursor) {
        List<Barang> barangList = new ArrayList<>();
        
        if (cursor == null || cursor.isClosed()) {
            return barangList;
        }
        
        try {
            if (cursor.moveToFirst()) {
                do {
                    Barang barang = cursorToBarang(cursor);
                    if (barang != null) {
                        barangList.add(barang);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        
        return barangList;
    }
    
    /**
     * Validasi input data barang
     * @param nama Nama barang
     * @param stok Stok barang
     * @param harga Harga barang
     * @return String error message atau null jika valid
     */
    public static String validateBarangInput(String nama, double stok, double harga) {
        if (nama == null || nama.trim().isEmpty()) {
            return "Nama barang tidak boleh kosong";
        }
        
        if (nama.trim().length() < 2) {
            return "Nama barang minimal 2 karakter";
        }
        
        if (nama.trim().length() > 100) {
            return "Nama barang maksimal 100 karakter";
        }
        
        if (stok < 0) {
            return "Stok tidak boleh negatif";
        }
        
        if (harga < 0) {
            return "Harga tidak boleh negatif";
        }
        
        if (harga > 999999999) {
            return "Harga terlalu besar";
        }
        
        return null; // Valid
    }
    
    /**
     * Format harga ke format Rupiah
     * @param harga Harga dalam double
     * @return String harga dalam format Rupiah
     */
    public static String formatHarga(double harga) {
        if (harga >= 1000000000) {
            return String.format("Rp %.1f M", harga / 1000000);
        } else if (harga >= 1000000) {
            return String.format("Rp %.1f Jt", harga / 1000000);
        } else if (harga >= 1000) {
            return String.format("Rp %.0f rb", harga / 1000);
        } else {
            return String.format("Rp %.0f", harga);
        }
    }
    
    /**
     * Format stok dengan unit yang sesuai
     * @param stok Stok dalam double
     * @return String stok dengan format yang sesuai
     */
    public static String formatStok(double stok) {
        if (stok % 1 == 0) {
            return String.format("%.0f pcs", stok);
        } else {
            return String.format("%.1f pcs", stok);
        }
    }
    
    /**
     * Mendapatkan status stok berdasarkan jumlah
     * @param stok Jumlah stok
     * @return String status stok
     */
    public static String getStockStatus(double stok) {
        if (stok <= 0) {
            return "Habis";
        } else if (stok < 5) {
            return "Stok Menipis";
        } else if (stok < 20) {
            return "Stok Sedikit";
        } else {
            return "Stok Cukup";
        }
    }
    
    /**
     * Mendapatkan warna untuk status stok
     * @param stok Jumlah stok
     * @return String color hex untuk status
     */
    public static String getStockStatusColor(double stok) {
        if (stok <= 0) {
            return "#F44336"; // Red
        } else if (stok < 5) {
            return "#FF9800"; // Orange
        } else if (stok < 20) {
            return "#FFC107"; // Amber
        } else {
            return "#4CAF50"; // Green
        }
    }
    
    /**
     * Mencari barang berdasarkan nama (case insensitive)
     * @param barangList List barang untuk dicari
     * @param searchQuery Query pencarian
     * @return List barang yang sesuai dengan pencarian
     */
    public static List<Barang> searchBarang(List<Barang> barangList, String searchQuery) {
        List<Barang> filteredList = new ArrayList<>();
        
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return new ArrayList<>(barangList);
        }
        
        String query = searchQuery.toLowerCase().trim();
        
        for (Barang barang : barangList) {
            if (barang.getNama().toLowerCase().contains(query)) {
                filteredList.add(barang);
            }
        }
        
        return filteredList;
    }
    
    /**
     * Menghitung total nilai inventory
     * @param barangList List barang
     * @return Total nilai (stok * harga) dari semua barang
     */
    public static double calculateTotalInventoryValue(List<Barang> barangList) {
        double total = 0;
        
        for (Barang barang : barangList) {
            total += (barang.getStok() * barang.getHarga());
        }
        
        return total;
    }
    
    /**
     * Menghitung total stok semua barang
     * @param barangList List barang
     * @return Total stok dari semua barang
     */
    public static double calculateTotalStock(List<Barang> barangList) {
        double total = 0;
        
        for (Barang barang : barangList) {
            total += barang.getStok();
        }
        
        return total;
    }
}
