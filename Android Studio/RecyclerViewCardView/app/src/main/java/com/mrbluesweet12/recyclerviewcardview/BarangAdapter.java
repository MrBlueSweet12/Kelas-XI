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
 * Adapter untuk RecyclerView yang menampilkan daftar barang
 * Sesuai spesifikasi video: SELECT, Model, dan Adapter
 * Menggunakan layout item_barang.xml dan model Barang.java
 */
public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.ViewHolder> {
    
    private Context context;
    private List<Barang> barangList;
    
    public BarangAdapter(Context context, List<Barang> barangList) {
        this.context = context;
        this.barangList = barangList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_barang.xml dengan benar
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Dapatkan objek Barang dari barangList pada position saat ini
        Barang barang = barangList.get(position);
        
        // Set teks untuk setiap TextView di holder menggunakan data dari objek Barang
        holder.tvBarang.setText(barang.getNama());
        holder.tvStok.setText("Stok: " + barang.getStok());
        holder.tvHarga.setText("Harga: " + barang.getHarga());
        
        // Untuk tvMenu, set teksnya ke ••• (karakter tiga titik vertikal)
        holder.tvMenu.setText("\u22EE"); // Unicode untuk vertical ellipsis
        
        // Set click listener untuk menu options dengan PopupMenu
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
                            android.util.Log.d("BarangAdapter", "UBAH clicked for: " + barang.getNama());
                            
                        } else if (item.getItemId() == R.id.hapus) {
                            // Panggil deleteData dari MainActivity
                            try {
                                // Cast context ke MainActivity dan panggil deleteData
                                if (context instanceof MainActivity) {
                                    ((MainActivity) context).deleteData(barang.getIdbarang());
                                    android.util.Log.d("BarangAdapter", "DELETE called for ID: " + barang.getIdbarang() + ", Nama: " + barang.getNama());
                                } else {
                                    // Fallback jika context bukan MainActivity
                                    Toast.makeText(context, "Error: Context bukan MainActivity", Toast.LENGTH_SHORT).show();
                                    android.util.Log.e("BarangAdapter", "Context is not MainActivity instance");
                                }
                            } catch (Exception e) {
                                // Handle potential exceptions
                                Toast.makeText(context, "Error menghapus data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                android.util.Log.e("BarangAdapter", "Error calling deleteData: " + e.getMessage(), e);
                            }
                        }
                        // Mengembalikan false agar event diteruskan ke listener lain jika ada
                        return false;
                    }
                });
            }
        });
        
        // Set click listener untuk item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.d("BarangAdapter", "Item clicked: " + barang.getNama());
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return barangList.size();
    }
    
    /**
     * Update data adapter dengan list baru
     * @param newBarangList List barang yang baru
     */
    public void updateData(List<Barang> newBarangList) {
        this.barangList.clear();
        if (newBarangList != null) {
            this.barangList.addAll(newBarangList);
        }
        notifyDataSetChanged();
    }
    
    /**
     * Clear semua data
     */
    public void clearData() {
        this.barangList.clear();
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder untuk item barang
     * Sesuai spesifikasi video dengan ID yang benar dari item_barang.xml
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        
        TextView tvBarang;      // ID sesuai dengan item_barang.xml
        TextView tvStok;       // ID sesuai dengan item_barang.xml  
        TextView tvHarga;      // ID sesuai dengan item_barang.xml
        TextView tvMenu;       // ID sesuai dengan item_barang.xml
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            // Inisialisasi TextView sesuai dengan ID di item_barang.xml
            tvBarang = itemView.findViewById(R.id.tvItemNamaBarang);
            tvStok = itemView.findViewById(R.id.tvItemStokBarang);
            tvHarga = itemView.findViewById(R.id.tvItemHargaBarang);
            tvMenu = itemView.findViewById(R.id.tvOptions);
        }
    }
}
