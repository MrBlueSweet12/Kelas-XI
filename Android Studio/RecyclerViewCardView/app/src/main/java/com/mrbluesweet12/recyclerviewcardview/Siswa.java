package com.mrbluesweet12.recyclerviewcardview;

public class Siswa {
    private int id;
    private String nama;
    private String alamat;

    // Konstruktor default
    public Siswa() {
    }

    // Konstruktor dengan nama dan alamat
    public Siswa(String nama, String alamat) {
        this.nama = nama;
        this.alamat = alamat;
    }

    // Konstruktor lengkap dengan id
    public Siswa(int id, String nama, String alamat) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    // Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @Override
    public String toString() {
        return "Siswa{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", alamat='" + alamat + '\'' +
                '}';
    }
}
