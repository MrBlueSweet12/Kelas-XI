package com.example.aplikasimonitoringkelas

import android.content.Context
import android.os.Bundle
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SiswaActivity : ComponentActivity() {
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

data class KelasSiswa(
    val id: Int,
    val kelas: String
)

data class JadwalResponseSiswa(
    val jam_ke: String,
    val mata_pelajaran: String,
    val kode_guru: String,
    val nama_guru: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalPage() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val role = context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE).getString("ROLE", "")

    // State spinner Hari
    var selectedHari by remember { mutableStateOf("Senin") }
    val hariList = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")
    var expandedHari by remember { mutableStateOf(false) }

    // State spinner Kelas
    var selectedKelas by remember { mutableStateOf<KelasSiswa?>(null) }
    var kelasList by remember { mutableStateOf<List<KelasSiswa>>(emptyList()) }
    var expandedKelas by remember { mutableStateOf(false) }

    // State jadwal
    var jadwalList by remember { mutableStateOf<List<JadwalResponseSiswa>>(emptyList()) }

    // Load kelas saat pertama kali
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) { ApiClient.instance.getKelasSiswa() }
                kelasList = result
                if (result.isNotEmpty()) selectedKelas = result[0]
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Load jadwal setiap kali hari atau kelas berubah
    LaunchedEffect(selectedHari, selectedKelas) {
        selectedKelas?.let { kelas ->
            scope.launch {
                try {
                    val result = withContext(Dispatchers.IO) {
                        ApiClient.instance.getJadwalSiswa(kelas.id, selectedHari)
                    }
                    jadwalList = result
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Informasi User
        Text(
            text = "Login sebagai: ${role}",
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
                value = selectedKelas?.kelas ?: "",
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
                        text = { Text(kelas.kelas) },
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
            items(jadwalList) { jadwal ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = jadwal.jam_ke,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Mata Pelajaran: ${jadwal.mata_pelajaran}")
                        Text(text = "Kode Guru: ${jadwal.kode_guru}")
                        Text(text = "Nama Guru: ${jadwal.nama_guru}")
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntriPage() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val daftarHari = listOf("Senin","Selasa","Rabu","Kamis","Jumat","Sabtu","Minggu")

    // UI state
    var selectedHari by remember { mutableStateOf(daftarHari.first()) }
    var expandedHari by remember { mutableStateOf(false) }

    var kelasList by remember { mutableStateOf<List<KelasKurikulum>>(emptyList()) }
    var selectedKelas by remember { mutableStateOf<KelasKurikulum?>(null) }
    var expandedKelas by remember { mutableStateOf(false) }

    var guruList by remember { mutableStateOf<List<GuruSimple>>(emptyList()) }
    var selectedGuru by remember { mutableStateOf<GuruSimple?>(null) }
    var expandedGuru by remember { mutableStateOf(false) }

    var mapelList by remember { mutableStateOf<List<MapelJam>>(emptyList()) }
    var selectedMapel by remember { mutableStateOf<MapelJam?>(null) }
    var expandedMapel by remember { mutableStateOf(false) }

    var jamKe by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("Masuk") }
    val daftarStatus = listOf("Masuk", "Tidak Masuk")
    var expandedStatus by remember { mutableStateOf(false) }

    var keterangan by remember { mutableStateOf("") }

    // Load kelas awal
    LaunchedEffect(Unit) {
        try {
            val kelas = withContext(Dispatchers.IO) { ApiClient.instance.getKelasKurikulum() }
            kelasList = kelas
            if (kelas.isNotEmpty()) selectedKelas = kelas.first()
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal ambil kelas: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // When hari or kelas changes -> load guru list (distinct) for that hari+kelas
    LaunchedEffect(selectedHari, selectedKelas) {
        selectedKelas?.let { kelas ->
            try {
                val gurus = withContext(Dispatchers.IO) {
                    ApiClient.instance.getGurusByHariKelas(selectedHari, kelas.id)
                }
                guruList = gurus
                selectedGuru = gurus.firstOrNull()
                // reset mapel/jam when guru changes (will be loaded in next effect)
                mapelList = emptyList()
                selectedMapel = null
                jamKe = ""
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal ambil guru: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // When hari+kelas+guru changes -> load mapel+jam list
    LaunchedEffect(selectedHari, selectedKelas, selectedGuru) {
        if (selectedKelas != null && selectedGuru != null) {
            try {
                val items = withContext(Dispatchers.IO) {
                    ApiClient.instance.getMapelAndJamByHariKelasGuru(
                        selectedHari,
                        selectedKelas!!.id,
                        selectedGuru!!.id
                    )
                }
                mapelList = items
                if (items.isNotEmpty()) {
                    selectedMapel = items.first()
                    jamKe = items.first().jam_ke
                } else {
                    selectedMapel = null
                    jamKe = ""
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal ambil mapel/jam: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // When mapel selected -> set jamKe accordingly
    LaunchedEffect(selectedMapel) {
        selectedMapel?.let { jamKe = it.jam_ke ?: "" }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Hari spinner
        ExposedDropdownMenuBox(expanded = expandedHari, onExpandedChange = { expandedHari = !expandedHari }) {
            OutlinedTextField(
                value = selectedHari,
                onValueChange = {},
                readOnly = true,
                label = { Text("Hari") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedHari) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedHari, onDismissRequest = { expandedHari = false }) {
                daftarHari.forEach { h ->
                    DropdownMenuItem(text = { Text(h) }, onClick = { selectedHari = h; expandedHari = false })
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Kelas spinner (from API)
        ExposedDropdownMenuBox(expanded = expandedKelas, onExpandedChange = { expandedKelas = !expandedKelas }) {
            OutlinedTextField(
                value = selectedKelas?.kelas ?: "Pilih Kelas",
                onValueChange = {},
                readOnly = true,
                label = { Text("Kelas") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKelas) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedKelas, onDismissRequest = { expandedKelas = false }) {
                kelasList.forEach { k ->
                    DropdownMenuItem(text = { Text(k.kelas) }, onClick = {
                        selectedKelas = k
                        expandedKelas = false
                    })
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Guru spinner (filtered)
        ExposedDropdownMenuBox(expanded = expandedGuru, onExpandedChange = { expandedGuru = !expandedGuru }) {
            OutlinedTextField(
                value = selectedGuru?.guru ?: "Pilih Guru",
                onValueChange = {},
                readOnly = true,
                label = { Text("Guru") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGuru) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedGuru, onDismissRequest = { expandedGuru = false }) {
                guruList.forEach { g ->
                    DropdownMenuItem(text = { Text(g.guru) }, onClick = {
                        selectedGuru = g
                        expandedGuru = false
                    })
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Mapel spinner (filtered by hari+kelas+guru)
        ExposedDropdownMenuBox(expanded = expandedMapel, onExpandedChange = { expandedMapel = !expandedMapel }) {
            OutlinedTextField(
                value = selectedMapel?.mapel ?: "Pilih Mapel",
                onValueChange = {},
                readOnly = true,
                label = { Text("Mata Pelajaran") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMapel) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedMapel, onDismissRequest = { expandedMapel = false }) {
                mapelList.forEach { m ->
                    DropdownMenuItem(text = { Text(m.mapel) }, onClick = {
                        selectedMapel = m
                        expandedMapel = false
                    })
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Jam ke (auto filled)
        OutlinedTextField(
            value = jamKe,
            onValueChange = { jamKe = it }, // user boleh edit jika perlu
            label = { Text("Jam Ke") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        // Status spinner
        ExposedDropdownMenuBox(expanded = expandedStatus, onExpandedChange = { expandedStatus = !expandedStatus }) {
            OutlinedTextField(
                value = selectedStatus,
                onValueChange = {},
                readOnly = true,
                label = { Text("Status") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedStatus, onDismissRequest = { expandedStatus = false }) {
                daftarStatus.forEach { s ->
                    DropdownMenuItem(text = { Text(s) }, onClick = {
                        selectedStatus = s
                        expandedStatus = false
                    })
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Keterangan
        OutlinedTextField(
            value = keterangan,
            onValueChange = { keterangan = it },
            label = { Text("Keterangan") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Tombol tambah -> panggil API store guru_mengajar
        Button(
            onClick = {
                // Validasi minimal
                if (selectedKelas == null || selectedGuru == null || selectedMapel == null || jamKe.isBlank()) {
                    Toast.makeText(context, "Lengkapi data sebelum tambah", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Gunakan coroutineScope, bukan LaunchedEffect
                scope.launch {
                    try {
                        withContext(Dispatchers.IO) {
                            ApiClient.instance.addGuruMengajar(
                                GuruMengajarRequest(
                                    hari = selectedHari,
                                    kelas_id = selectedKelas!!.id,
                                    guru_id = selectedGuru!!.id,
                                    mapel_id = selectedMapel!!.mapel_id,
                                    jam_ke = jamKe,
                                    status = selectedStatus,
                                    keterangan = keterangan.ifBlank { null }
                                )
                            )
                        }
                        Toast.makeText(context, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        keterangan = ""
                    } catch (e: Exception) {
                        Toast.makeText(context, "Gagal tambah: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah")
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPage() {
    val scope = rememberCoroutineScope()

    // State spinner
    var selectedHari by remember { mutableStateOf("Senin") }
    var selectedKelas by remember { mutableStateOf<KelasKurikulum?>(null) }
    var kelasList by remember { mutableStateOf<List<KelasKurikulum>>(emptyList()) }

    // State jadwal
    var jadwalList by remember { mutableStateOf<List<JadwalResponseListKurikulum>>(emptyList()) }

    // State edit dialog
    var showEditDialog by remember { mutableStateOf(false) }
    var jadwalToEdit by remember { mutableStateOf<JadwalResponseListKurikulum?>(null) }
    var newStatus by remember { mutableStateOf("Masuk") }
    var newKeterangan by remember { mutableStateOf("") }

    // Load kelas saat pertama kali
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                kelasList = withContext(Dispatchers.IO) { ApiClient.instance.getKelasKurikulum() }
                if (kelasList.isNotEmpty()) selectedKelas = kelasList[0]
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Ambil jadwal ketika spinner berubah
    LaunchedEffect(selectedHari, selectedKelas) {
        selectedKelas?.let { kelas ->
            scope.launch {
                try {
                    jadwalList = withContext(Dispatchers.IO) {
                        ApiClient.instance.getJadwalListKurikulum(
                            RequestBodyHariKelas(selectedHari, kelas.id)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        // Spinner Hari
        DropdownSpinner(
            label = "Pilih Hari",
            items = listOf("Senin","Selasa","Rabu","Kamis","Jumat","Sabtu","Minggu"),
            selectedItem = selectedHari
        ) { selectedHari = it }

        Spacer(Modifier.height(12.dp))

        // Spinner Kelas
        DropdownSpinner(
            label = "Pilih Kelas",
            items = kelasList.map { it.kelas },
            selectedItem = selectedKelas?.kelas ?: ""
        ) { nama ->
            selectedKelas = kelasList.find { it.kelas == nama }
        }

        Spacer(Modifier.height(16.dp))

        // Daftar jadwal
        LazyColumn {
            items(jadwalList) { jadwal ->
                Card(
                    Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Guru: ${jadwal.nama_guru}", style = MaterialTheme.typography.titleMedium)
                            Text("Mapel: ${jadwal.mapel}")
                            Text("Status: ${jadwal.status}")
                            Text("Keterangan: ${jadwal.keterangan}")
                        }

                        IconButton(onClick = {
                            jadwalToEdit = jadwal
                            newStatus = jadwal.status
                            newKeterangan = jadwal.keterangan
                            showEditDialog = true
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }

                        IconButton(onClick = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    ApiClient.instance.deleteJadwal(jadwal.id)
                                }
                                // refresh
                                selectedKelas?.let {
                                    jadwalList = withContext(Dispatchers.IO) {
                                        ApiClient.instance.getJadwalListKurikulum(RequestBodyHariKelas(selectedHari, it.id))
                                    }
                                }
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }

    // Dialog Edit
    if (showEditDialog && jadwalToEdit != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Jadwal") },
            text = {
                Column {
                    DropdownSpinner(
                        label = "Status",
                        items = listOf("Masuk", "Tidak Masuk"),
                        selectedItem = newStatus
                    ) { newStatus = it }

                    OutlinedTextField(
                        value = newKeterangan,
                        onValueChange = { newKeterangan = it },
                        label = { Text("Keterangan") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        jadwalToEdit?.let {
                            withContext(Dispatchers.IO) {
                                ApiClient.instance.updateJadwal(
                                    it.id,
                                    UpdateJadwalRequest(newStatus, newKeterangan)
                                )
                            }
                            // refresh
                            selectedKelas?.let { kelas ->
                                jadwalList = withContext(Dispatchers.IO) {
                                    ApiClient.instance.getJadwalListKurikulum(RequestBodyHariKelas(selectedHari, kelas.id))
                                }
                            }
                        }
                        showEditDialog = false
                    }
                }) { Text("Update") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SiswaScreenPreview() {
    AplikasiMonitoringKelasTheme {
        SiswaScreen()
    }
}
