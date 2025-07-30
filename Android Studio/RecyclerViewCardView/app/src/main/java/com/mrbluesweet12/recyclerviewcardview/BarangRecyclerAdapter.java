package com.mrbluesweet12.recyclerviewcardview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Adapter untuk RecyclerView yang menampilkan daftar barang dari tabel tblbarang
 * Menggunakan layout item_barang.xml untuk setiap item dalam list
 */
public class BarangRecyclerAdapter extends RecyclerView.Adapter<BarangRecyclerAdapter.ViewHolder> {
    
    // Atribut kelas
    private Context context;
    private List<BarangModel> barangList;
    
    /**
     * Konstruktor BarangRecyclerAdapter
     * @param context Context dari Activity/Fragment
     * @param barangList List data barang yang akan ditampilkan
     */
    public BarangRecyclerAdapter(Context context, List<BarangModel> barangList) {
        this.context = context;
        this.barangList = barangList;
    }
    
    /**
     * Kelas ViewHolder untuk memegang referensi view dalam setiap item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        
        // Deklarasi TextView sesuai dengan item_barang.xml
        public TextView tvBarang;    // Untuk nama barang (tvItemNamaBarang)
        public TextView tvStok;      // Untuk stok barang (tvItemStokBarang)
        public TextView tvHarga;     // Untuk harga barang (tvItemHargaBarang)
        public TextView tvMenu;      // Untuk menu tiga titik (tvOptions)
        
        /**
         * Konstruktor ViewHolder
         * @param itemView View item yang akan di-bind
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            // Inisialisasi TextView dengan findViewById
            tvBarang = itemView.findViewById(R.id.tvItemNamaBarang);
            tvStok = itemView.findViewById(R.id.tvItemStokBarang);
            tvHarga = itemView.findViewById(R.id.tvItemHargaBarang);
            tvMenu = itemView.findViewById(R.id.tvOptions);
        }
    }
    
    /**
     * Membuat ViewHolder baru ketika RecyclerView membutuhkannya
     * @param parent ViewGroup parent
     * @param viewType Tipe view
     * @return ViewHolder baru
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflasi layout item_barang.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_barang, parent, false);
        
        // Kembalikan ViewHolder baru dengan view yang telah diinflasi
        return new ViewHolder(view);
    }
    
    /**
     * Mengikat data ke ViewHolder pada posisi tertentu
     * @param holder ViewHolder yang akan diisi data
     * @param position Posisi item dalam list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Dapatkan objek BarangModel pada posisi tertentu
        BarangModel barang = barangList.get(position);
        
        // Set teks untuk setiap TextView dengan data dari objek BarangModel
        holder.tvBarang.setText(barang.getNama());
        holder.tvStok.setText("Stok: " + barang.getFormattedStok());
        holder.tvHarga.setText(barang.getFormattedHarga());
        
        // Set click listener untuk menu (tiga titik) dengan PopupMenu
        holder.tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inisialisasi PopupMenu
                PopupMenu popupMenu = new PopupMenu(context, holder.tvMenu);
                
                // Inflate Menu dari resource menu_item.xml
                popupMenu.inflate(R.menu.menu_item);
                
                // Tampilkan Menu
                popupMenu.show();
                
                // Set OnMenuItemClickListener untuk menangani klik item menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Penanganan klik item menu menggunakan if-else statement
                        if (item.getItemId() == R.id.ubah) {
                            // Tampilkan Toast dengan pesan "UBAH"
                            Toast.makeText(context, "UBAH - " + barang.getNama(), Toast.LENGTH_SHORT).show();
                            android.util.Log.d("BarangRecyclerAdapter", "UBAH clicked for: " + barang.getNama());
                            
                        } else if (item.getItemId() == R.id.hapus) {
                            // Panggil deleteData dari MainActivity (untuk BarangRecyclerAdapter)
                            try {
                                // Cast context ke MainActivity dan panggil deleteData
                                if (context instanceof MainActivity) {
                                    ((MainActivity) context).deleteData(barang.getId());
                                    android.util.Log.d("BarangRecyclerAdapter", "DELETE called for ID: " + barang.getId() + ", Nama: " + barang.getNama());
                                } else {
                                    // Fallback jika context bukan MainActivity
                                    Toast.makeText(context, "Error: Context bukan MainActivity", Toast.LENGTH_SHORT).show();
                                    android.util.Log.e("BarangRecyclerAdapter", "Context is not MainActivity instance");
                                }
                            } catch (Exception e) {
                                // Handle potential exceptions
                                Toast.makeText(context, "Error menghapus data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                android.util.Log.e("BarangRecyclerAdapter", "Error calling deleteData: " + e.getMessage(), e);
                            }
                        }
                        // Mengembalikan false agar event diteruskan ke listener lain jika ada
                        return false;
                    }
                });
            }
        });
        
        // Set click listener untuk keseluruhan item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implementasi action saat item diklik
                // Untuk saat ini, tampilkan toast dengan detail barang
                String message = "Detail Barang:\n" +
                               "Nama: " + barang.getNama() + "\n" +
                               "Stok: " + barang.getFormattedStok() + "\n" +
                               "Harga: " + barang.getFormattedHarga();
                
                android.widget.Toast.makeText(context, message, 
                    android.widget.Toast.LENGTH_LONG).show();
            }
        });
        
        // Set styling berdasarkan stok (contoh: warna berbeda jika stok sedikit)
        double stokValue = barang.getStokAsDouble();
        if (stokValue <= 0) {
            // Stok habis - tampilkan dengan warna merah
            holder.tvStok.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else if (stokValue < 10) {
            // Stok sedikit - tampilkan dengan warna orange
            holder.tvStok.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            // Stok cukup - tampilkan dengan warna normal
            holder.tvStok.setTextColor(context.getResources().getColor(android.R.color.black));
        }
    }
    
    /**
     * Mendapatkan jumlah total item dalam list
     * @return Jumlah item
     */
    @Override
    public int getItemCount() {
        return barangList != null ? barangList.size() : 0;
    }
    
    /**
     * Method untuk update data dalam adapter
     * @param newBarangList List data barang yang baru
     */
    public void updateData(List<BarangModel> newBarangList) {
        if (this.barangList != null) {
            this.barangList.clear();
        }
        
        if (newBarangList != null) {
            if (this.barangList == null) {
                this.barangList = newBarangList;
            } else {
                this.barangList.addAll(newBarangList);
            }
        }
        
        // Notify adapter bahwa data telah berubah
        notifyDataSetChanged();
    }
    
    /**
     * Method untuk menambah item baru ke dalam list
     * @param barang BarangModel yang akan ditambahkan
     */
    public void addItem(BarangModel barang) {
        if (barangList != null && barang != null) {
            barangList.add(barang);
            notifyItemInserted(barangList.size() - 1);
        }
    }
    
    /**
     * Method untuk menghapus item dari list berdasarkan posisi
     * @param position Posisi item yang akan dihapus
     */
    public void removeItem(int position) {
        if (barangList != null && position >= 0 && position < barangList.size()) {
            barangList.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    /**
     * Method untuk mendapatkan item pada posisi tertentu
     * @param position Posisi item
     * @return BarangModel pada posisi tersebut, atau null jika invalid
     */
    public BarangModel getItem(int position) {
        if (barangList != null && position >= 0 && position < barangList.size()) {
            return barangList.get(position);
        }
        return null;
    }
    
    /**
     * Method untuk clear semua data
     */
    public void clearData() {
        if (barangList != null) {
            int size = barangList.size();
            barangList.clear();
            notifyItemRangeRemoved(0, size);
        }
    }
}
