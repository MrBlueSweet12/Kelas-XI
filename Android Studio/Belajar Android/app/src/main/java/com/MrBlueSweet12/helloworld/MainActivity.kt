package com.MrBlueSweet12.helloworld

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * MainActivity untuk aplikasi CounterApp
 * Aplikasi ini menggunakan ConstraintLayout untuk menampilkan UI penghitung sederhana
 * dengan dua tombol untuk menaikkan dan menurunkan nilai penghitung
 */
class MainActivity : AppCompatActivity() {
    
    // Variabel untuk menyimpan nilai penghitung
    private var counter = 0
    
    // Referensi ke TextView yang menampilkan nilai penghitung
    private lateinit var textViewCounter: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Inisialisasi referensi ke komponen UI
        textViewCounter = findViewById(R.id.textViewCounter)
        val buttonUp = findViewById<Button>(R.id.buttonUp)
        val buttonDown = findViewById<Button>(R.id.buttonDown)
        
        // Set listener untuk tombol Counter Up
        buttonUp.setOnClickListener {
            // Tambah nilai penghitung
            counter++
            // Update tampilan
            updateCounterDisplay()
        }
        
        // Set listener untuk tombol Counter Down
        buttonDown.setOnClickListener {
            // Kurangi nilai penghitung
            counter--
            // Update tampilan
            updateCounterDisplay()
        }
    }
    
    /**
     * Fungsi untuk memperbarui tampilan nilai penghitung pada TextView
     */
    private fun updateCounterDisplay() {
        textViewCounter.text = counter.toString()
    }
}