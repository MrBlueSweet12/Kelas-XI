package com.mrbluesweet12.recyclerviewcardview;

import android.annotation.SuppressLint;
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

public class SiswaAdapter extends RecyclerView.Adapter<SiswaAdapter.ViewHolder> {
    private Context context;
    private List<Siswa> siswaList;

    // Konstruktor
    public SiswaAdapter(Context context, List<Siswa> siswaList) {
        this.context = context;
        this.siswaList = siswaList;
    }

    // Inner class ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNama;
        public TextView tvAlamat;
        public TextView tvMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvMenu = itemView.findViewById(R.id.tvMenu);
        }
    }

    @Override
    @NonNull
    public SiswaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_siswa, parent, false);
        return new SiswaAdapter.ViewHolder(view);
    }

    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull SiswaAdapter.ViewHolder holder, final int position) {
        Siswa siswa = siswaList.get(position);
        holder.tvNama.setText(siswa.getNama());
        holder.tvAlamat.setText(siswa.getAlamat());
        
        // Menambahkan OnClickListener ke itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mendapatkan posisi yang benar
                final int adapterPosition = holder.getAdapterPosition();
                
                // Memastikan posisi valid
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Mengambil data siswa berdasarkan posisi
                    Siswa siswaClicked = siswaList.get(adapterPosition);
                    
                    // Menampilkan data dengan Toast
                    Toast.makeText(holder.itemView.getContext(), 
                        "Nama: " + siswaClicked.getNama() + " Alamat: " + siswaClicked.getAlamat(), 
                        Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        // Menambahkan OnClickListener ke tvMenu untuk PopupMenu
        holder.tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Membuat PopupMenu
                PopupMenu popupMenu = new PopupMenu(context, holder.tvMenu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_option, popupMenu.getMenu());
                popupMenu.show();

                // Menambahkan listener untuk item menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.menu_simpan) {
                            // Aksi untuk "Simpan"
                            Toast.makeText(context, "Simpan Data " + siswaList.get(position).getNama(), Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (itemId == R.id.menu_hapus) {
                            // Aksi untuk "Hapus"
                            String namaSiswaDihapus = siswaList.get(position).getNama(); // Simpan nama sebelum dihapus
                            siswaList.remove(position);
                            notifyDataSetChanged(); // Beri tahu adapter bahwa data telah berubah
                            Toast.makeText(context, namaSiswaDihapus + " Sudah di Hapus", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return siswaList.size();
    }
}
