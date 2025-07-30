package com.mrbluesweet12.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    private EditText etBarang, etStok;
    private TextView tvHasil;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Initialize EditText fields and SharedPreferences
        etBarang = findViewById(R.id.etBarang);
        etStok = findViewById(R.id.etStok);
        tvHasil = findViewById(R.id.tvHasil);
        sharedPreferences = getSharedPreferences("barang", MODE_PRIVATE);
        
        // Load and display saved data when app starts
        tampilData();
    }

    public void simpan(View view) {
        // Get text from EditText fields
        String namaBarang = etBarang.getText().toString();
        float stokBarang = 0.0f;
        
        try {
            stokBarang = Float.parseFloat(etStok.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Stok tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Get SharedPreferences Editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        
        // Save values to SharedPreferences
        editor.putString("barang", namaBarang);
        editor.putFloat("stok", stokBarang);
        
        // Apply changes
        editor.apply();
        
        // Clear EditText fields after saving
        etBarang.setText("");
        etStok.setText("");
        
        // Update display
        tampilData();
    }

    public void tampil(View view) {
        // Retrieve values from SharedPreferences
        String retrievedNamaBarang = sharedPreferences.getString("barang", "");
        float retrievedStokBarang = sharedPreferences.getFloat("stok", 0.0f);
        
        // Set retrieved values to EditText fields
        etBarang.setText(retrievedNamaBarang);
        etStok.setText(String.valueOf(retrievedStokBarang));
    }
    
    private void tampilData() {
        // Retrieve values from SharedPreferences
        String retrievedNamaBarang = sharedPreferences.getString("barang", "");
        float retrievedStokBarang = sharedPreferences.getFloat("stok", 0.0f);
        
        // Display data in TextView
        if (retrievedNamaBarang.isEmpty() || retrievedStokBarang == 0.0f) {
            tvHasil.setText("Belum ada data tersimpan");
        } else {
            tvHasil.setText("Nama Barang: " + retrievedNamaBarang + "\nStok: " + retrievedStokBarang);
        }
    }
}