package com.example.aplikasimonitoringkelas

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aplikasimonitoringkelas.api.ApiClient
import com.example.aplikasimonitoringkelas.ui.theme.AplikasiMonitoringKelasTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

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

data class KelasKepsek (
    val id: Int,
    val kelas: String
)

data class JadwalKepsek(
    val jam_ke: String,
    val mata_pelajaran: String,
    val kode_guru: String,
    val nama_guru: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalPelajaranKepsekPage() {
    val daftarHari = listOf("Senin","Selasa","Rabu","Kamis","Jumat","Sabtu","Minggu")
    var selectedHari by remember { mutableStateOf(daftarHari.first()) }
    var expandedHari by remember { mutableStateOf(false) }

    var daftarKelas by remember { mutableStateOf<List<KelasKepsek>>(emptyList()) }
    var selectedKelas by remember { mutableStateOf<KelasKepsek?>(null) }
    var expandedKelas by remember { mutableStateOf(false) }

    var daftarJadwal by remember { mutableStateOf<List<JadwalKepsek>>(emptyList()) }

    val context = LocalContext.current

    // Load kelas
    LaunchedEffect(Unit) {
        try {
            val kelas = withContext(Dispatchers.IO) {
                ApiClient.instance.getKelasKepsek()
            }
            daftarKelas = kelas
            if (kelas.isNotEmpty()) {
                selectedKelas = kelas.first()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal ambil kelas: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("RetrofitError", "Error: ${e.message}")
        }
    }

    // Load jadwal ketika kelas / hari berubah
    LaunchedEffect(selectedKelas, selectedHari) {
        selectedKelas?.let { kelas ->
            try {
                val jadwal = withContext(Dispatchers.IO) {
                    ApiClient.instance.getJadwalKepsek(kelas.id, selectedHari)
                }
                daftarJadwal = jadwal
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal ambil jadwal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Login sebagai: Kepala Sekolah", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(16.dp))

        // === Spinner Hari ===
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
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedHari, onDismissRequest = { expandedHari = false }) {
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

        Spacer(Modifier.height(12.dp))

        // === Spinner Kelas ===
        ExposedDropdownMenuBox(
            expanded = expandedKelas,
            onExpandedChange = { expandedKelas = !expandedKelas }
        ) {
            OutlinedTextField(
                value = selectedKelas?.kelas ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Kelas") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKelas) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedKelas, onDismissRequest = { expandedKelas = false }) {
                daftarKelas.forEach { kelas ->
                    DropdownMenuItem(
                        text = { Text(kelas.kelas) },
                        onClick = {
                            selectedKelas = kelas
                            expandedKelas = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // === Daftar Jadwal ===
        LazyColumn {
            items(daftarJadwal) { jadwal ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(jadwal.jam_ke, style = MaterialTheme.typography.titleMedium)
                        Text("Mata Pelajaran: ${jadwal.mata_pelajaran}")
                        Text("Kode Guru: ${jadwal.kode_guru}")
                        Text("Nama Guru: ${jadwal.nama_guru}")
                    }
                }
            }
        }
    }
}




data class GuruMengajarResponse(
    val nama_guru: String,
    val mapel: String,
    val jam_ke: String,
    val status: String,
    val keterangan: String
)

data class BodyData(
    val hari: String,
    val kelas_id: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelasKosongPage() {
    val hariList = listOf("Senin","Selasa","Rabu","Kamis","Jumat","Sabtu","Minggu")
    var selectedHari by remember { mutableStateOf(hariList.first()) }

    var daftarKelas by remember { mutableStateOf<List<KelasKepsek>>(emptyList()) }
    var selectedKelas by remember { mutableStateOf<KelasKepsek?>(null) }

    var jadwalList by remember { mutableStateOf<List<GuruMengajarResponse>>(emptyList()) }

    val context = LocalContext.current

    // 🔹 Ambil daftar kelas
    LaunchedEffect(Unit) {
        try {
            val kelas = withContext(Dispatchers.IO) {
                ApiClient.instance.getKelasKepsek()
            }
            daftarKelas = kelas
            if (kelas.isNotEmpty()) {
                selectedKelas = kelas.first()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal ambil kelas: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // 🔹 Ambil jadwal kalau filter berubah
    LaunchedEffect(selectedHari, selectedKelas) {
        selectedKelas?.let { kelas ->
            try {
                val body = BodyData(
                    hari = selectedHari,
                    kelas_id = kelas.id
                )
                val data = withContext(Dispatchers.IO) {
                    ApiClient.instance.getGuruMengajarTidakMasuk(body)
                }
                jadwalList = data
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal ambil data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Spinner Hari
        DropdownSpinner(label = "Pilih Hari", items = hariList, selectedItem = selectedHari) {
            selectedHari = it
        }

        Spacer(Modifier.height(12.dp))

        // Spinner Kelas
        DropdownSpinner(
            label = "Pilih Kelas",
            items = daftarKelas.map { it.kelas },
            selectedItem = selectedKelas?.kelas ?: "",
        ) { kelasNama ->
            selectedKelas = daftarKelas.find { it.kelas == kelasNama }
        }

        Spacer(Modifier.height(16.dp))

        // Daftar Data
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(jadwalList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Guru: ${item.nama_guru}", style = MaterialTheme.typography.titleMedium)
                        Text("Mapel: ${item.mapel}")
                        Text("Jam ke: ${item.jam_ke}")
                        Text("Status: ${item.status}")
                        Text("Keterangan: ${item.keterangan}")
                    }
                }
            }
        }
    }
}


data class JadwalKepsekResponse(
    val id: Int,
    val nama_guru: String,
    val mapel: String,
    val status: String,
    val keterangan: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPageKepsek() {
    val hariList = listOf("Senin","Selasa","Rabu","Kamis","Jumat","Sabtu","Minggu")
    var selectedHari by remember { mutableStateOf(hariList.first()) }

    var daftarKelas by remember { mutableStateOf<List<KelasKepsek>>(emptyList()) }
    var selectedKelas by remember { mutableStateOf<KelasKepsek?>(null) }

    var daftarJadwal by remember { mutableStateOf<List<JadwalKepsekResponse>>(emptyList()) }

    val context = LocalContext.current

    // 🔹 Ambil kelas saat pertama kali load
    LaunchedEffect(Unit) {
        try {
            val kelas = withContext(Dispatchers.IO) {
                ApiClient.instance.getKelasKepsek()
            }
            daftarKelas = kelas
            if (kelas.isNotEmpty()) {
                selectedKelas = kelas.first()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal ambil kelas: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // 🔹 Ambil jadwal tiap kali hari/kelas berubah
    LaunchedEffect(selectedHari, selectedKelas) {
        selectedKelas?.let { kelas ->
            try {
                val body = BodyData(
                    hari = selectedHari,
                    kelas_id = kelas.id
                )
                val jadwal = withContext(Dispatchers.IO) {
                    ApiClient.instance.getJadwalByHariKelas(body)
                }
                daftarJadwal = jadwal
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal ambil jadwal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Spinner Hari
        DropdownSpinner(label = "Pilih Hari", items = hariList, selectedItem = selectedHari) {
            selectedHari = it
        }

        Spacer(Modifier.height(12.dp))

        // Spinner Kelas
        DropdownSpinner(
            label = "Pilih Kelas",
            items = daftarKelas.map { it.kelas },
            selectedItem = selectedKelas?.kelas ?: "",
        ) { kelasNama ->
            selectedKelas = daftarKelas.find { it.kelas == kelasNama }
        }

        Spacer(Modifier.height(16.dp))

        // List Jadwal
        LazyColumn {
            items(daftarJadwal) { jadwal ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("ID: ${jadwal.id}", style = MaterialTheme.typography.titleMedium)
                        Text("Guru: ${jadwal.nama_guru}")
                        Text("Mapel: ${jadwal.mapel}")
                        Text("Status: ${jadwal.status}")
                        Text("Keterangan: ${jadwal.keterangan}")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KepalaSekolahPreview() {
    AplikasiMonitoringKelasTheme {
        KepalaSekolahScreen()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSpinner(
    label: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}