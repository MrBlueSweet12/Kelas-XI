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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aplikasimonitoringkelas.ui.theme.AplikasiMonitoringKelasTheme

class SIswaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplikasiMonitoringKelasTheme {
                SiswaScreen()
            }
        }
    }
}

sealed class BottomNavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Jadwal : BottomNavItem("Jadwal", Icons.Default.Home)
    object Entri : BottomNavItem("Entri", Icons.Default.Edit)
    object List : BottomNavItem("List", Icons.Default.List)
}

@Composable
fun SiswaScreen() {
    val items = listOf(
        BottomNavItem.Jadwal,
        BottomNavItem.Entri,
        BottomNavItem.List
    )
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Jadwal) }

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
                is BottomNavItem.Jadwal -> JadwalPage()
                is BottomNavItem.Entri -> EntriPage()
                is BottomNavItem.List -> ListPage()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalPage() {
    var selectedHari by remember { mutableStateOf("Senin") }
    val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

    var selectedKelas by remember { mutableStateOf("X RPL") }
    val kelasList = listOf("X RPL", "XI RPL", "XII RPL")

    var expandedHari by remember { mutableStateOf(false) }
    var expandedKelas by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Informasi User
        Text(
            text = "Login sebagai: Siswa RPL",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Spinner Hari
        ExposedDropdownMenuBox(
            expanded = expandedHari,
            onExpandedChange = { expandedHari = !expandedHari }
        ) {
            OutlinedTextField(
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
                hariList.forEach { hari ->
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

        // Spinner Kelas
        ExposedDropdownMenuBox(
            expanded = expandedKelas,
            onExpandedChange = { expandedKelas = !expandedKelas }
        ) {
            OutlinedTextField(
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
                kelasList.forEach { kelas ->
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

        // Daftar Card Jadwal
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(10) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Jam ke ${index + 1} - ${index + 3}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Mata Pelajaran: Matematika")
                        Text(text = "Kode Guru: G${index + 1} | Nama Guru: Pak Guru ${index + 1}")
                    }
                }
            }
        }
    }
}


@Composable
fun EntriPage() {
    Text("Halaman Entri Data")
}

@Composable
fun ListPage() {
    Text("Halaman List Data")
}

@Preview(showBackground = true)
@Composable
fun SiswaScreenPreview() {
    AplikasiMonitoringKelasTheme {
        SiswaScreen()
    }
}
