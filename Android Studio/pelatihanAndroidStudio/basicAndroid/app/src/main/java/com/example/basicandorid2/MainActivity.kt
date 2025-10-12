package com.example.basicandorid2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                FormPage()
            }
        }
    }
}

@Composable
fun FormPage() {
    var nama by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var hasil by remember { mutableStateOf("") } // untuk menampung hasil

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Image di atas
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Profile Image",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text Nama
        Text(text = "Nama", fontSize = 18.sp)

        // TextField Nama
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Masukkan Nama") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text Alamat
        Text(text = "Alamat", fontSize = 18.sp)

        // TextField Alamat
        OutlinedTextField(
            value = alamat,
            onValueChange = { alamat = it },
            label = { Text("Masukkan Alamat") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Button Simpan
        Button(
            onClick = {
                hasil = "Nama: $nama\nAlamat: $alamat"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tampilkan hasil di bawah tombol
        if (hasil.isNotEmpty()) {
            Text(text = hasil, fontSize = 18.sp)
        }
    }
}
