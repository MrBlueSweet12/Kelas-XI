package com.example.aplikasimonitoringkelas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aplikasimonitoringkelas.ui.theme.AplikasiMonitoringKelasTheme

class KepalaSekolahActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplikasiMonitoringKelasTheme {
                KepalaSekolahScreen()
            }
        }
    }
}

// Menu Item
sealed class KepsekNavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Jadwal : KepsekNavItem("Jadwal", Icons.Default.Home)
    object KelasKosong : KepsekNavItem("Kelas Kosong", Icons.Default.Info)
    object List : KepsekNavItem("List", Icons.Default.List)
}

@Composable
fun KepalaSekolahScreen() {
    val items = listOf(
        KepsekNavItem.Jadwal,
        KepsekNavItem.KelasKosong,
        KepsekNavItem.List
    )
    var selectedItem by remember { mutableStateOf<KepsekNavItem>(KepsekNavItem.Jadwal) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = selectedItem == item,
                        onClick = { selectedItem = item },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedItem) {
                is KepsekNavItem.Jadwal -> JadwalPelajaranKepsekPage()
                is KepsekNavItem.KelasKosong -> KelasKosongPage()
                is KepsekNavItem.List -> ListPageKepsek()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalPelajaranKepsekPage() {
    // State spinner Hari
    var selectedHari by remember { mutableStateOf("Senin") }
    var expandedHari by remember { mutableStateOf(false) }
    val daftarHari = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

    // State spinner Kelas
    var selectedKelas by remember { mutableStateOf("X RPL") }
    var expandedKelas by remember { mutableStateOf(false) }
    val daftarKelas = listOf("X RPL", "XI RPL", "XII RPL")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Informasi user login
        Text(
            text = "Login sebagai: Kepala Sekolah",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // === Spinner Hari ===
        ExposedDropdownMenuBox(
            expanded = expandedHari,
            onExpandedChange = { expandedHari = !expandedHari }
        ) {
            TextField(
                value = selectedHari,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Hari") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedHari) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedHari,
                onDismissRequest = { expandedHari = false }
            ) {
                daftarHari.forEach { hari ->
                    DropdownMenuItem(
                        text = { Text(hari) },
                        onClick = {
                            selectedHari = hari
                            expandedHari = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // === Spinner Kelas ===
        ExposedDropdownMenuBox(
            expanded = expandedKelas,
            onExpandedChange = { expandedKelas = !expandedKelas }
        ) {
            TextField(
                value = selectedKelas,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Kelas") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKelas) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedKelas,
                onDismissRequest = { expandedKelas = false }
            ) {
                daftarKelas.forEach { kelas ->
                    DropdownMenuItem(
                        text = { Text(kelas) },
                        onClick = {
                            selectedKelas = kelas
                            expandedKelas = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === Card Jadwal (10 card) ===
        repeat(10) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Jam ke ${index + 1} - ${index + 3}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Mata Pelajaran: Matematika")
                    Text("Kode Guru: G${index + 1}")
                    Text("Nama Guru: Pak Guru ${index + 1}")
                }
            }
        }
    }
}

@Composable
fun KelasKosongPage() {
    Text("Halaman Kelas Kosong")
}

@Composable
fun ListPageKepsek() {
    Text("Halaman List Data")
}

@Preview(showBackground = true)
@Composable
fun KepalaSekolahPreview() {
    AplikasiMonitoringKelasTheme {
        KepalaSekolahScreen()
    }
}
