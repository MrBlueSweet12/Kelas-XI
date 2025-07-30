package com.MrBlueSweet12.helloworld;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity2 untuk aplikasi mobile
 */
public class MainActivity2 extends AppCompatActivity {

    private int counter = 0;
    private TextView textViewCounter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        
        // Inisialisasi UI
        textViewCounter = findViewById(R.id.textViewCounter);
        Button buttonUp = findViewById(R.id.buttonUp);
        Button buttonDown = findViewById(R.id.buttonDown);
        
        // Set listener untuk tombol
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                updateCounterDisplay();
                Toast.makeText(MainActivity2.this, "Counter: " + counter, Toast.LENGTH_SHORT).show();
            }
        });
        
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                updateCounterDisplay();
            }
        });
    }
    
    /**
     * Memperbarui tampilan counter
     */
    private void updateCounterDisplay() {
        textViewCounter.setText(String.valueOf(counter));
    }
}