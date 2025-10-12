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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aplikasimonitoringkelas.ui.theme.AplikasiMonitoringKelasTheme

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplikasiMonitoringKelasTheme {
                AdminScreen()
            }
        }
    }
}

// Menu Item
sealed class AdminNavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object EntriJadwal : AdminNavItem("Entri User", Icons.Default.Edit)
    object UbahJadwal : AdminNavItem("Entri Jadwal", Icons.Default.Info)
    object List : AdminNavItem("List", Icons.Default.List)
}

@Composable
fun AdminScreen() {
    val items = listOf(
        AdminNavItem.EntriJadwal,
        AdminNavItem.UbahJadwal,
        AdminNavItem.List
    )
    var selectedItem by remember { mutableStateOf<AdminNavItem>(AdminNavItem.EntriJadwal) }

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
                is AdminNavItem.EntriJadwal -> EntriUserPage()
                is AdminNavItem.UbahJadwal -> EntriJadwalPage()
                is AdminNavItem.List -> ListPageAdmin()
            }
        }
    }
}

data class UserData(val nama: String, val email: String, val role: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntriUserPage() {
    // State input
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Spinner Role
    var selectedRole by remember { mutableStateOf("Siswa") }
    var expandedRole by remember { mutableStateOf(false) }
    val daftarRole = listOf("Siswa", "Kurikulum", "Kepala Sekolah", "Admin")

    // Data user yang sudah dientri
    val userList = remember { mutableStateListOf<UserData>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // === Spinner Role ===
        ExposedDropdownMenuBox(
            expanded = expandedRole,
            onExpandedChange = { expandedRole = !expandedRole }
        ) {
            TextField(
                value = selectedRole,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Role") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRole) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandedRole,
                onDismissRequest = { expandedRole = false }
            ) {
                daftarRole.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(role) },
                        onClick = {
                            selectedRole = role
                            expandedRole = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // === Input Nama ===
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // === Input Email ===
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // === Input Password ===
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Simpan
        Button(
            onClick = {
                if (nama.isNotBlank() && email.contains("@") && password.isNotBlank()) {
                    userList.add(UserData(nama, email, selectedRole))
                    nama = ""
                    email = ""
                    password = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = "Tambah")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Simpan User")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // === List Data User ===
        userList.forEachIndexed { index, user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Data ${index + 1}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Nama: ${user.nama}")
                    Text("Email: ${user.email}")
                    Text("Role: ${user.role}")
                }
            }
        }
    }
}

data class JadwalData(
    val hari: String,
    val kelas: String,
    val mapel: String,
    val guru: String,
    val jam: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntriJadwalPage() {
    // State Spinner Hari
    var selectedHari by remember { mutableStateOf("Senin") }
    var expandedHari by remember { mutableStateOf(false) }
    val daftarHari = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

    // State Spinner Kelas
    var selectedKelas by remember { mutableStateOf("X RPL") }
    var expandedKelas by remember { mutableStateOf(false) }
    val daftarKelas = listOf("X RPL", "XI RPL", "XII RPL")

    // State Spinner Mapel
    var selectedMapel by remember { mutableStateOf("IPA") }
    var expandedMapel by remember { mutableStateOf(false) }
    val daftarMapel = listOf("IPA", "IPS", "Bahasa")

    // State Spinner Guru
    var selectedGuru by remember { mutableStateOf("Siti") }
    var expandedGuru by remember { mutableStateOf(false) }
    val daftarGuru = listOf("Siti", "Budi", "Adi", "Agus")

    // Input Jam
    var jamKe by remember { mutableStateOf("") }

    // List Data Jadwal
    val jadwalList = remember { mutableStateListOf<JadwalData>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
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

        Spacer(modifier = Modifier.height(12.dp))

        // === Spinner Mapel ===
        ExposedDropdownMenuBox(
            expanded = expandedMapel,
            onExpandedChange = { expandedMapel = !expandedMapel }
        ) {
            TextField(
                value = selectedMapel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Mata Pelajaran") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMapel) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedMapel,
                onDismissRequest = { expandedMapel = false }
            ) {
                daftarMapel.forEach { mapel ->
                    DropdownMenuItem(
                        text = { Text(mapel) },
                        onClick = {
                            selectedMapel = mapel
                            expandedMapel = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // === Spinner Guru ===
        ExposedDropdownMenuBox(
            expanded = expandedGuru,
            onExpandedChange = { expandedGuru = !expandedGuru }
        ) {
            TextField(
                value = selectedGuru,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Guru") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGuru) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedGuru,
                onDismissRequest = { expandedGuru = false }
            ) {
                daftarGuru.forEach { guru ->
                    DropdownMenuItem(
                        text = { Text(guru) },
                        onClick = {
                            selectedGuru = guru
                            expandedGuru = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // === Text Field Jam ===
        OutlinedTextField(
            value = jamKe,
            onValueChange = { jamKe = it },
            label = { Text("Jam Ke") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // === Tombol Simpan ===
        Button(
            onClick = {
                if (jamKe.isNotBlank()) {
                    jadwalList.add(
                        JadwalData(
                            hari = selectedHari,
                            kelas = selectedKelas,
                            mapel = selectedMapel,
                            guru = selectedGuru,
                            jam = jamKe
                        )
                    )
                    jamKe = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = "Simpan")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Simpan Jadwal")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // === List Data Jadwal ===
        jadwalList.forEachIndexed { index, jadwal ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Data ${index + 1}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Hari: ${jadwal.hari}")
                    Text("Kelas: ${jadwal.kelas}")
                    Text("Mapel: ${jadwal.mapel}")
                    Text("Guru: ${jadwal.guru}")
                    Text("Jam ke: ${jadwal.jam}")
                }
            }
        }
    }
}

@Composable
fun ListPageAdmin() {
    Text("Halaman List Data")
}

@Preview(showBackground = true)
@Composable
fun AdminPreview() {
    AplikasiMonitoringKelasTheme {
        AdminScreen()
    }
}
