package com.mrbluesweet12.kalkulator;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.mrbluesweet12.kalkulator.databinding.ActivityMainBinding;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Layout telah diubah menjadi LinearLayout sederhana untuk kalkulator
        // Kode navigasi tidak lagi diperlukan karena fragment telah dihapus
    }

    // Metode navigasi tidak lagi diperlukan karena layout telah diubah
    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }
}