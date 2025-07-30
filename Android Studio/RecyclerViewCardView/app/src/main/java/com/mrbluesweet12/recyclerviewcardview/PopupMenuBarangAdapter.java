package com.mrbluesweet12.recyclerviewcardview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * Adapter khusus untuk PopupMenuDemoActivity yang mendukung delete functionality
 * Menggunakan layout item_barang.xml dan model Barang.java
 */
public class PopupMenuBarangAdapter extends RecyclerView.Adapter<PopupMenuBarangAdapter.ViewHolder> {
    
    private Context context;
    private List<Barang> barangList;
    
    public PopupMenuBarangAdapter(Context context, List<Barang> barangList) {
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
                            android.util.Log.d("PopupMenuBarangAdapter", "UBAH clicked for: " + barang.getNama());
                            
                        } else if (item.getItemId() == R.id.hapus) {
                            // Tampilkan dialog konfirmasi sebelum menghapus
                            showDeleteConfirmationDialog(barang);
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
                android.util.Log.d("PopupMenuBarangAdapter", "Item clicked: " + barang.getNama());
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
     * Menampilkan dialog konfirmasi sebelum menghapus data
     * @param barang Objek barang yang akan dihapus
     */
    private void showDeleteConfirmationDialog(Barang barang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Konfirmasi Hapus");
        builder.setMessage("Apakah Anda yakin ingin menghapus '" + barang.getNama() + "'?\n\nTindakan ini tidak dapat dibatalkan.");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        
        // Tombol Ya - hapus data
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Panggil deleteDataDirect setelah konfirmasi (hindari double dialog)
                try {
                    // Cast context ke PopupMenuDemoActivity dan panggil deleteDataDirect
                    if (context instanceof PopupMenuDemoActivity) {
                        ((PopupMenuDemoActivity) context).deleteDataDirect(barang.getIdbarang());
                        android.util.Log.d("PopupMenuBarangAdapter", "DELETE confirmed for ID: " + barang.getIdbarang() + ", Nama: " + barang.getNama());
                    } else if (context instanceof MainActivity) {
                        // Fallback untuk MainActivity - gunakan deleteDataDirect untuk menghindari double dialog
                        ((MainActivity) context).deleteDataDirect(barang.getIdbarang());
                        android.util.Log.d("PopupMenuBarangAdapter", "DELETE confirmed via MainActivity for ID: " + barang.getIdbarang());
                    } else {
                        // Fallback jika context bukan expected activity
                        Toast.makeText(context, "Error: Context tidak dikenali", Toast.LENGTH_SHORT).show();
                        android.util.Log.e("PopupMenuBarangAdapter", "Context is not recognized activity instance");
                    }
                } catch (Exception e) {
                    // Handle potential exceptions
                    Toast.makeText(context, "Error menghapus data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    android.util.Log.e("PopupMenuBarangAdapter", "Error calling deleteDataDirect: " + e.getMessage(), e);
                }
                dialog.dismiss();
            }
        });
        
        // Tombol Tidak - batalkan
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                android.util.Log.d("PopupMenuBarangAdapter", "Delete cancelled for: " + barang.getNama());
            }
        });
        
        // Tampilkan dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    /**
     * ViewHolder untuk item barang
     * Sesuai spesifikasi dengan ID yang benar dari item_barang.xml
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
