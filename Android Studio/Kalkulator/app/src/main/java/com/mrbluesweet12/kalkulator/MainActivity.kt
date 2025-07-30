package com.mrbluesweet12.kalkulator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    // Deklarasi variabel untuk komponen UI
    private lateinit var textViewResult: TextView
    private lateinit var editTextNumber1: EditText
    private lateinit var editTextNumber2: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonSubtract: Button
    private lateinit var buttonMultiply: Button
    private lateinit var buttonDivide: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi komponen UI
        initializeViews()
        
        // Set listener untuk tombol operasi
        setupButtonListeners()
    }

    /**
     * Menginisialisasi semua komponen UI
     */
    private fun initializeViews() {
        textViewResult = findViewById(R.id.textViewResult)
        editTextNumber1 = findViewById(R.id.editTextNumber1)
        editTextNumber2 = findViewById(R.id.editTextNumber2)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonSubtract = findViewById(R.id.buttonSubtract)
        buttonMultiply = findViewById(R.id.buttonMultiply)
        buttonDivide = findViewById(R.id.buttonDivide)
    }

    /**
     * Mengatur listener untuk semua tombol operasi
     */
    private fun setupButtonListeners() {
        // Listener untuk tombol penjumlahan
        buttonAdd.setOnClickListener {
            performOperation { num1, num2 -> num1 + num2 }
        }

        // Listener untuk tombol pengurangan
        buttonSubtract.setOnClickListener {
            performOperation { num1, num2 -> num1 - num2 }
        }

        // Listener untuk tombol perkalian
        buttonMultiply.setOnClickListener {
            performOperation { num1, num2 -> num1 * num2 }
        }

        // Listener untuk tombol pembagian
        buttonDivide.setOnClickListener {
            performOperation { num1, num2 ->
                if (num2 == 0.0) {
                    // Menampilkan pesan error jika pembagian dengan nol
                    showError("Tidak dapat melakukan pembagian dengan nol")
                    null // Mengembalikan null untuk menandakan operasi gagal
                } else {
                    num1 / num2
                }
            }
        }
    }

    /**
     * Melakukan operasi matematika berdasarkan lambda yang diberikan
     * @param operation Lambda yang menerima dua parameter Double dan mengembalikan hasil operasi
     */
    private fun performOperation(operation: (Double, Double) -> Double?) {
        try {
            // Mendapatkan nilai dari EditText
            val number1 = editTextNumber1.text.toString().toDoubleOrNull()
            val number2 = editTextNumber2.text.toString().toDoubleOrNull()

            // Validasi input
            if (number1 == null || number2 == null) {
                showError("Masukkan angka yang valid pada kedua kolom")
                return
            }

            // Melakukan operasi
            val result = operation(number1, number2)
            
            // Menampilkan hasil jika operasi berhasil
            if (result != null) {
                // Format hasil agar tidak menampilkan desimal jika hasilnya bilangan bulat
                val formattedResult = if (result % 1 == 0.0) {
                    result.toInt().toString()
                } else {
                    result.toString()
                }
                textViewResult.text = formattedResult
            }
        } catch (e: Exception) {
            // Menangani exception yang mungkin terjadi
            showError("Terjadi kesalahan: ${e.message}")
        }
    }

    /**
     * Menampilkan pesan error menggunakan Toast
     * @param message Pesan error yang akan ditampilkan
     */
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}