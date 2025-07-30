package com.mrbluesweet12.recyclerviewcardview;

/**
 * Kelas Model Barang untuk merepresentasikan data dari tabel tblbarang
 * Struktur tabel: idbarang (INTEGER), barang (TEXT), stok (REAL), harga (REAL)
 */
public class BarangModel {
    
    // Atribut sesuai dengan kolom di tabel tblbarang
    private String id;      // idbarang - handle sebagai String untuk fleksibilitas
    private String nama;    // barang - nama barang
    private String stok;    // stok - handle sebagai String untuk input/output mudah
    private String harga;   // harga - handle sebagai String untuk input/output mudah
    
    /**
     * Konstruktor lengkap dengan semua atribut
     * @param id ID barang (idbarang)
     * @param nama Nama barang (barang)
     * @param stok Stok barang (stok)
     * @param harga Harga barang (harga)
     */
    public BarangModel(String id, String nama, String stok, String harga) {
        this.id = id;
        this.nama = nama;
        this.stok = stok;
        this.harga = harga;
    }
    
    /**
     * Konstruktor tanpa ID (untuk data baru yang belum diinsert)
     * @param nama Nama barang
     * @param stok Stok barang
     * @param harga Harga barang
     */
    public BarangModel(String nama, String stok, String harga) {
        this.nama = nama;
        this.stok = stok;
        this.harga = harga;
        this.id = null; // ID akan di-set saat data diinsert ke database
    }
    
    /**
     * Konstruktor default
     */
    public BarangModel() {
        // Konstruktor kosong untuk fleksibilitas
    }
    
    // === GETTER METHODS ===
    
    /**
     * Mendapatkan ID barang
     * @return ID barang sebagai String
     */
    public String getId() {
        return id;
    }
    
    /**
     * Mendapatkan nama barang
     * @return Nama barang
     */
    public String getNama() {
        return nama;
    }
    
    /**
     * Mendapatkan stok barang
     * @return Stok barang sebagai String
     */
    public String getStok() {
        return stok;
    }
    
    /**
     * Mendapatkan harga barang
     * @return Harga barang sebagai String
     */
    public String getHarga() {
        return harga;
    }
    
    // === SETTER METHODS ===
    
    /**
     * Mengatur ID barang
     * @param id ID barang baru
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Mengatur nama barang
     * @param nama Nama barang baru
     */
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    /**
     * Mengatur stok barang
     * @param stok Stok barang baru
     */
    public void setStok(String stok) {
        this.stok = stok;
    }
    
    /**
     * Mengatur harga barang
     * @param harga Harga barang baru
     */
    public void setHarga(String harga) {
        this.harga = harga;
    }
    
    // === UTILITY METHODS ===
    
    /**
     * Mendapatkan stok sebagai nilai double
     * @return Stok dalam bentuk double, atau 0.0 jika parsing gagal
     */
    public double getStokAsDouble() {
        try {
            return stok != null ? Double.parseDouble(stok) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Mendapatkan harga sebagai nilai double
     * @return Harga dalam bentuk double, atau 0.0 jika parsing gagal
     */
    public double getHargaAsDouble() {
        try {
            return harga != null ? Double.parseDouble(harga) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Mendapatkan ID sebagai integer
     * @return ID dalam bentuk int, atau -1 jika parsing gagal
     */
    public int getIdAsInt() {
        try {
            return id != null ? Integer.parseInt(id) : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Format harga untuk ditampilkan dengan prefix Rupiah
     * @return String harga dengan format "Rp X.XXX"
     */
    public String getFormattedHarga() {
        try {
            double hargaValue = getHargaAsDouble();
            if (hargaValue >= 1000000) {
                return String.format("Rp %.0f jt", hargaValue / 1000000);
            } else if (hargaValue >= 1000) {
                return String.format("Rp %.0f rb", hargaValue / 1000);
            } else {
                return String.format("Rp %.0f", hargaValue);
            }
        } catch (Exception e) {
            return "Rp " + (harga != null ? harga : "0");
        }
    }
    
    /**
     * Format stok untuk ditampilkan
     * @return String stok dengan suffix "pcs"
     */
    public String getFormattedStok() {
        try {
            double stokValue = getStokAsDouble();
            if (stokValue % 1 == 0) {
                // Jika stok adalah bilangan bulat, tampilkan tanpa desimal
                return String.format("%.0f pcs", stokValue);
            } else {
                // Jika stok memiliki desimal, tampilkan dengan 1 desimal
                return String.format("%.1f pcs", stokValue);
            }
        } catch (Exception e) {
            return (stok != null ? stok : "0") + " pcs";
        }
    }
    
    /**
     * Cek apakah barang valid (semua field tidak null/empty)
     * @return true jika valid, false jika ada field yang kosong
     */
    public boolean isValid() {
        return nama != null && !nama.trim().isEmpty() &&
               stok != null && !stok.trim().isEmpty() &&
               harga != null && !harga.trim().isEmpty();
    }
    
    /**
     * Representasi string dari objek BarangModel
     * @return String representasi objek
     */
    @Override
    public String toString() {
        return "BarangModel{" +
                "id='" + id + '\'' +
                ", nama='" + nama + '\'' +
                ", stok='" + stok + '\'' +
                ", harga='" + harga + '\'' +
                '}';
    }
    
    /**
     * Equals method untuk membandingkan objek BarangModel
     * @param obj Objek yang akan dibandingkan
     * @return true jika sama, false jika berbeda
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BarangModel that = (BarangModel) obj;
        
        // Bandingkan berdasarkan ID jika ada, atau berdasarkan nama jika ID null
        if (id != null && that.id != null) {
            return id.equals(that.id);
        } else {
            return nama != null ? nama.equals(that.nama) : that.nama == null;
        }
    }
    
    /**
     * HashCode method
     * @return HashCode dari objek
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : (nama != null ? nama.hashCode() : 0);
    }
}
