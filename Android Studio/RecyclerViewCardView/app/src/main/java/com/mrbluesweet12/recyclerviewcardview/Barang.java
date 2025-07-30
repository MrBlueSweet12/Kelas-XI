package com.mrbluesweet12.recyclerviewcardview;

/**
 * Model class untuk data Barang
 * Merepresentasikan struktur data barang dalam aplikasi
 */
public class Barang {
    
    private long id;
    private String nama;
    private double stok;
    private double harga;
    
    /**
     * Konstruktor default
     */
    public Barang() {
    }
    
    /**
     * Konstruktor dengan parameter (tanpa ID - untuk data baru)
     * @param nama Nama barang
     * @param stok Jumlah stok barang
     * @param harga Harga barang
     */
    public Barang(String nama, double stok, double harga) {
        this.nama = nama;
        this.stok = stok;
        this.harga = harga;
    }
    
    /**
     * Konstruktor dengan semua parameter (termasuk ID - untuk data existing)
     * @param id ID barang
     * @param nama Nama barang
     * @param stok Jumlah stok barang
     * @param harga Harga barang
     */
    public Barang(long id, String nama, double stok, double harga) {
        this.id = id;
        this.nama = nama;
        this.stok = stok;
        this.harga = harga;
    }
    
    /**
     * Konstruktor dengan parameter String (untuk kompatibilitas dengan data dari database)
     * @param id ID barang dalam bentuk String
     * @param nama Nama barang
     * @param stok Stok barang dalam bentuk String
     * @param harga Harga barang dalam bentuk String
     */
    public Barang(String id, String nama, String stok, String harga) {
        this.id = Long.parseLong(id);
        this.nama = nama;
        this.stok = Double.parseDouble(stok);
        this.harga = Double.parseDouble(harga);
    }
    
    // Getter methods
    public long getId() {
        return id;
    }
    
    /**
     * Alias untuk getId() untuk kompatibilitas dengan kode existing
     * @return ID barang sebagai String
     */
    public String getIdbarang() {
        return String.valueOf(id);
    }
    
    public String getNama() {
        return nama;
    }
    
    public double getStok() {
        return stok;
    }
    
    public double getHarga() {
        return harga;
    }
    
    // Setter methods
    public void setId(long id) {
        this.id = id;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public void setStok(double stok) {
        this.stok = stok;
    }
    
    public void setHarga(double harga) {
        this.harga = harga;
    }
    
    /**
     * Method untuk mendapatkan harga dalam format Rupiah
     * @return String harga dalam format Rupiah
     */
    public String getFormattedHarga() {
        return String.format("Rp %.0f", harga);
    }
    
    /**
     * Method untuk mendapatkan stok dalam format string
     * @return String stok dengan format yang sesuai
     */
    public String getFormattedStok() {
        if (stok % 1 == 0) {
            // Jika stok adalah bilangan bulat, tampilkan tanpa desimal
            return String.format("%.0f", stok);
        } else {
            // Jika stok memiliki desimal, tampilkan dengan 1 desimal
            return String.format("%.1f", stok);
        }
    }
    
    /**
     * Method untuk cek apakah stok habis
     * @return true jika stok <= 0, false jika masih ada stok
     */
    public boolean isOutOfStock() {
        return stok <= 0;
    }
    
    /**
     * Method untuk cek apakah stok sedikit (kurang dari 10)
     * @return true jika stok < 10, false jika stok cukup
     */
    public boolean isLowStock() {
        return stok < 10 && stok > 0;
    }
    
    @Override
    public String toString() {
        return "Barang{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", stok=" + stok +
                ", harga=" + harga +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Barang barang = (Barang) obj;
        return id == barang.id;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
