package com.mrbluesweet12.konversisuhu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    // Deklarasi komponen UI
    private lateinit var editTextTemperatureInput: EditText
    private lateinit var spinnerConversionType: Spinner
    private lateinit var buttonConvert: Button
    private lateinit var textViewResult: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Inisialisasi komponen UI
        editTextTemperatureInput = findViewById(R.id.editTextTemperatureInput)
        spinnerConversionType = findViewById(R.id.spinnerConversionType)
        buttonConvert = findViewById(R.id.buttonConvert)
        textViewResult = findViewById(R.id.textViewResult)
        
        // Metode 1: Spinner sudah diisi melalui XML dengan android:entries="@array/pilihan"
        // Tidak perlu kode tambahan untuk metode 1
        
        // Metode 2: Pengisian Spinner secara programatis (dikomentari untuk saat ini)
        // Hapus atribut android:entries dari layout XML untuk menggunakan metode ini
        // isiSpinner()
        
        // Pulihkan state jika ada
        if (savedInstanceState != null) {
            editTextTemperatureInput.setText(savedInstanceState.getString("input", ""))
            textViewResult.text = savedInstanceState.getString("result", "0")
            spinnerConversionType.setSelection(savedInstanceState.getInt("spinner_position", 0))
        }
        
        // Implementasi OnClickListener untuk tombol konversi
        buttonConvert.setOnClickListener {
            // Validasi input
            val inputText = editTextTemperatureInput.text.toString()
            if (inputText.isEmpty()) {
                editTextTemperatureInput.error = "Masukkan nilai suhu"
                return@setOnClickListener
            }
            
            val suhuInput = inputText.toDoubleOrNull()
            if (suhuInput == null) {
                editTextTemperatureInput.error = "Nilai suhu tidak valid"
                return@setOnClickListener
            }
            
            // Mendapatkan nilai yang dipilih dari Spinner
            val pilihanKonversi = spinnerConversionType.selectedItem.toString()
            
            // Cetak nilai yang dipilih ke Logcat untuk verifikasi
            Log.d("MainActivity", "Pilihan: $pilihanKonversi")
            
            // Konversi suhu berdasarkan pilihan
            val hasilKonversi = konversiSuhu(suhuInput, pilihanKonversi)
            
            // Tampilkan hasil konversi dengan format 2 angka desimal
            textViewResult.text = String.format("%.2f", hasilKonversi)
            
            // Sembunyikan keyboard setelah konversi
            hideKeyboard()
        }
    }
    
    /**
     * Metode untuk mengisi Spinner secara programatis menggunakan ArrayAdapter
     * Untuk menggunakan metode ini, hapus atribut android:entries dari Spinner di XML
     * dan hapus komentar pada pemanggilan metode ini di onCreate()
     */
    private fun isiSpinner() {
        // Deklarasi dan inisialisasi array String dengan opsi konversi suhu
        val isi = arrayOf(
            "Celsius Ke Reamur",
            "Celsius Ke Fahrenheit",
            "Celsius Ke Kelvin",
            "Reamur Ke Celsius",
            "Reamur Ke Fahrenheit"
        )
        
        // Buat ArrayAdapter baru yang menghubungkan array isi dengan Spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            isi
        )
        
        // Tentukan layout untuk tampilan dropdown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        
        // Set adapter untuk spinnerConversionType
        spinnerConversionType.adapter = adapter
    }
    
    /**
     * Metode untuk melakukan konversi suhu berdasarkan pilihan yang dipilih
     * @param suhu Nilai suhu yang akan dikonversi
     * @param pilihan Jenis konversi yang dipilih dari Spinner
     * @return Hasil konversi suhu
     */
    private fun konversiSuhu(suhu: Double, pilihan: String): Double {
        return when (pilihan) {
            "Celsius Ke Reamur" -> suhu * 0.8
            "Celsius Ke Fahrenheit" -> suhu * 9.0/5.0 + 32.0
            "Celsius Ke Kelvin" -> suhu + 273.15
            "Reamur Ke Celsius" -> suhu * 5.0/4.0
            "Reamur Ke Fahrenheit" -> suhu * 9.0/4.0 + 32.0
            else -> 0.0
        }
    }
    
    /**
     * Metode untuk menyembunyikan keyboard setelah pengguna menekan tombol konversi
     */
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    
    /**
     * Menyimpan state aplikasi saat orientasi layar berubah
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("input", editTextTemperatureInput.text.toString())
        outState.putString("result", textViewResult.text.toString())
        outState.putInt("spinner_position", spinnerConversionType.selectedItemPosition)
    }
}