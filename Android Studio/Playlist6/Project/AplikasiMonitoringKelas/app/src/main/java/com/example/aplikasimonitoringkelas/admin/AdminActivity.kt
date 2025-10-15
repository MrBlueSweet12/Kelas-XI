package com.example.aplikasimonitoringkelas.admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aplikasimonitoringkelas.api.ApiClient
import com.example.aplikasimonitoringkelas.data.JadwalResponse
import com.example.aplikasimonitoringkelas.data.KelasResponse
import com.example.aplikasimonitoringkelas.ui.theme.AplikasiMonitoringKelasTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
sealed class AdminNavItem(val title: String, val icon: ImageVector) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntriUserPage() {
    val context = LocalContext.current

    // State untuk popup tambah user
    var showAddDialog by remember { mutableStateOf(false) }

    // State input tambah user
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Siswa") }
    var expandedRole by remember { mutableStateOf(false) }
    val daftarRole = listOf("Siswa", "Kurikulum", "Kepala Sekolah", "Admin")

    // User list dari API
    var userList by remember { mutableStateOf(listOf<UserResponse>()) }

    // ambil data user saat pertama kali masuk
    LaunchedEffect(true) {
        ApiClient.instance.getUsers().enqueue(object : retrofit2.Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: retrofit2.Response<List<UserResponse>>
            ) {
                if (response.isSuccessful) {
                    userList = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                Toast.makeText(context, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // === Tombol Tambah User ===
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = "Tambah")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Tambah User")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // === List User ===
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(userList) { user ->
                var showDialog by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Nama: ${user.nama}")
                            Text("Email: ${user.email}")
                            Text("Role: ${user.role}")
                        }

                        IconButton(onClick = { showDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit User")
                        }
                        IconButton(onClick = {
                            ApiClient.instance.deleteUser(user.id).enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "User berhasil dihapus", Toast.LENGTH_SHORT).show()
                                        // refresh list setelah delete
                                        ApiClient.instance.getUsers().enqueue(object : Callback<List<UserResponse>> {
                                            override fun onResponse(
                                                call: Call<List<UserResponse>>,
                                                response: Response<List<UserResponse>>
                                            ) {
                                                if (response.isSuccessful) {
                                                    userList = response.body() ?: emptyList()
                                                }
                                            }

                                            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                                                Toast.makeText(context, "Gagal refresh data", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    } else {
                                        Toast.makeText(context, "Gagal menghapus user", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete User")
                        }
                    }
                }

                // === Popup Edit User (sama seperti sebelumnya) ===
                if (showDialog) {
                    var editNama by remember { mutableStateOf(user.nama) }
                    var editEmail by remember { mutableStateOf(user.email) }
                    var editPassword by remember { mutableStateOf("") }
                    var editRole by remember { mutableStateOf(user.role) }
                    var expandedRole by remember { mutableStateOf(false) }
                    val daftarRole = listOf("Siswa", "Kurikulum", "Kepala Sekolah", "Admin")

                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Edit User") },
                        text = {
                            Column {
                                // Role Spinner
                                ExposedDropdownMenuBox(
                                    expanded = expandedRole,
                                    onExpandedChange = { expandedRole = !expandedRole }
                                ) {
                                    OutlinedTextField(
                                        value = editRole,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Pilih Role") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRole)
                                        },
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
                                                    editRole = role
                                                    expandedRole = false
                                                }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = editNama,
                                    onValueChange = { editNama = it },
                                    label = { Text("Nama") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = editEmail,
                                    onValueChange = { editEmail = it },
                                    label = { Text("Email") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = editPassword,
                                    onValueChange = { editPassword = it },
                                    label = { Text("Password (opsional)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    visualTransformation = PasswordVisualTransformation()
                                )
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                val updateRequest = UserRequest(
                                    nama = editNama,
                                    email = editEmail,
                                    password = if (editPassword.isNotBlank()) editPassword else null,
                                    role = editRole
                                )

                                ApiClient.instance.updateUser(user.id, updateRequest)
                                    .enqueue(object : Callback<UserResponse> {
                                        override fun onResponse(
                                            call: Call<UserResponse>,
                                            response: Response<UserResponse>
                                        ) {
                                            if (response.isSuccessful) {
                                                Toast.makeText(context, "User berhasil diupdate", Toast.LENGTH_SHORT).show()
                                                ApiClient.instance.getUsers().enqueue(object : Callback<List<UserResponse>> {
                                                    override fun onResponse(
                                                        call: Call<List<UserResponse>>,
                                                        response: Response<List<UserResponse>>
                                                    ) {
                                                        if (response.isSuccessful) {
                                                            userList = response.body() ?: emptyList()
                                                        }
                                                    }
                                                    override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {}
                                                })
                                            } else {
                                                Toast.makeText(context, "Gagal update user", Toast.LENGTH_SHORT).show()
                                            }
                                            showDialog = false
                                        }

                                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                                            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                            showDialog = false
                                        }
                                    })
                            }) {
                                Text("Update")
                            }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = { showDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }

    // === Popup Tambah User ===
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Tambah User") },
            text = {
                Column {
                    // Role Spinner
                    ExposedDropdownMenuBox(
                        expanded = expandedRole,
                        onExpandedChange = { expandedRole = !expandedRole }
                    ) {
                        OutlinedTextField(
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

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = { Text("Nama") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (nama.isNotBlank() && email.contains("@") && password.isNotBlank()) {
                        val request = UserRequest(nama, email, password, selectedRole)
                        ApiClient.instance.createUser(request)
                            .enqueue(object : retrofit2.Callback<UserResponse> {
                                override fun onResponse(
                                    call: Call<UserResponse>,
                                    response: retrofit2.Response<UserResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "User berhasil disimpan", Toast.LENGTH_SHORT).show()
                                        ApiClient.instance.getUsers().enqueue(object : retrofit2.Callback<List<UserResponse>> {
                                            override fun onResponse(
                                                call: Call<List<UserResponse>>,
                                                response: retrofit2.Response<List<UserResponse>>
                                            ) {
                                                if (response.isSuccessful) {
                                                    userList = response.body() ?: emptyList()
                                                }
                                            }
                                            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {}
                                        })
                                        // reset form
                                        nama = ""
                                        email = ""
                                        password = ""
                                        selectedRole = "Siswa"
                                        showAddDialog = false
                                    } else {
                                        Toast.makeText(context, "Gagal simpan user", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                }) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

data class KelasResponseAdmin(val id: Int, val kelas: String)
data class GuruResponse(val id: Int, val guru: String)
data class MapelResponse(val id: Int, val mapel: String)
data class TahunResponse(val id: Int, val tahun: String)

data class JadwalRequestAdmin (
    val guru_id: Int,
    val mapel_id: Int,
    val tahun_ajaran_id: Int,
    val kelas_id: Int,
    val jam_ke: String,
    val hari: String
)

data class JadwalResponseAdmin(
    val id: Int,
    val guru: GuruResponse,
    val mapel: MapelResponse,
    val kelas: KelasResponse,
    val tahun: TahunResponse,
    val jam_ke: String,
    val hari: String
)


@Composable
fun EntriJadwalPage() {
    val context = LocalContext.current

    // State Hari (static list)
    val daftarHari = listOf("Senin","Selasa","Rabu","Kamis","Jumat","Sabtu","Minggu")
    var selectedHari by remember { mutableStateOf<String?>(null) }

    // State API data
    var daftarKelas by remember { mutableStateOf(listOf<KelasResponseAdmin>()) }
    var daftarGuru by remember { mutableStateOf(listOf<GuruResponse>()) }
    var daftarMapel by remember { mutableStateOf(listOf<MapelResponse>()) }
    var daftarTahun by remember { mutableStateOf(listOf<TahunResponse>()) }

    var selectedKelas by remember { mutableStateOf<KelasResponseAdmin?>(null) }
    var selectedGuru by remember { mutableStateOf<GuruResponse?>(null) }
    var selectedMapel by remember { mutableStateOf<MapelResponse?>(null) }
    var selectedTahun by remember { mutableStateOf<TahunResponse?>(null) }

    var jamKe by remember { mutableStateOf("") }

    // Load data dari API
    LaunchedEffect(true) {
        ApiClient.instance.getKelasAdmin().enqueue(object: Callback<List<KelasResponseAdmin>> {
            override fun onResponse(c: Call<List<KelasResponseAdmin>>, r: Response<List<KelasResponseAdmin>>) {
                if (r.isSuccessful) daftarKelas = r.body() ?: emptyList()
            }
            override fun onFailure(c: Call<List<KelasResponseAdmin>>, t: Throwable) {}
        })
        ApiClient.instance.getGuru().enqueue(object: Callback<List<GuruResponse>> {
            override fun onResponse(c: Call<List<GuruResponse>>, r: Response<List<GuruResponse>>) {
                if (r.isSuccessful) daftarGuru = r.body() ?: emptyList()
            }
            override fun onFailure(c: Call<List<GuruResponse>>, t: Throwable) {}
        })
        ApiClient.instance.getMapel().enqueue(object: Callback<List<MapelResponse>> {
            override fun onResponse(c: Call<List<MapelResponse>>, r: Response<List<MapelResponse>>) {
                if (r.isSuccessful) daftarMapel = r.body() ?: emptyList()
            }
            override fun onFailure(c: Call<List<MapelResponse>>, t: Throwable) {}
        })
        ApiClient.instance.getTahun().enqueue(object: Callback<List<TahunResponse>> {
            override fun onResponse(c: Call<List<TahunResponse>>, r: Response<List<TahunResponse>>) {
                if (r.isSuccessful) daftarTahun = r.body() ?: emptyList()
            }
            override fun onFailure(c: Call<List<TahunResponse>>, t: Throwable) {}
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Spinner Hari
        DropdownField(
            label = "Pilih Hari",
            items = daftarHari,
            selectedItem = selectedHari,
            itemLabel = { it },
            onItemSelected = { selectedHari = it }
        )
        Spacer(Modifier.height(12.dp))

        // Spinner Kelas
        DropdownField(
            label = "Pilih Kelas",
            items = daftarKelas,
            selectedItem = selectedKelas,
            itemLabel = { it.kelas },
            onItemSelected = { selectedKelas = it }
        )
        Spacer(Modifier.height(12.dp))

        // Spinner Guru
        DropdownField(
            label = "Pilih Guru",
            items = daftarGuru,
            selectedItem = selectedGuru,
            itemLabel = { it.guru },
            onItemSelected = { selectedGuru = it }
        )
        Spacer(Modifier.height(12.dp))

        // Spinner Mapel
        DropdownField(
            label = "Pilih Mapel",
            items = daftarMapel,
            selectedItem = selectedMapel,
            itemLabel = { it.mapel },
            onItemSelected = { selectedMapel = it }
        )
        Spacer(Modifier.height(12.dp))

        // Spinner Tahun Ajaran
        DropdownField(
            label = "Pilih Tahun Ajaran",
            items = daftarTahun,
            selectedItem = selectedTahun,
            itemLabel = { it.tahun },
            onItemSelected = { selectedTahun = it }
        )
        Spacer(Modifier.height(12.dp))

        // Input Jam ke
        OutlinedTextField(
            value = jamKe,
            onValueChange = { jamKe = it },
            label = { Text("Jam Ke") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Spacer(Modifier.height(16.dp))

        // Tombol Simpan
        Button(
            onClick = {
                if (selectedHari != null && selectedGuru != null && selectedMapel != null &&
                    selectedKelas != null && selectedTahun != null && jamKe.isNotBlank()
                ) {
                    val request = JadwalRequestAdmin(
                        guru_id = selectedGuru!!.id,
                        mapel_id = selectedMapel!!.id,
                        tahun_ajaran_id = selectedTahun!!.id,
                        kelas_id = selectedKelas!!.id,
                        jam_ke = jamKe,
                        hari = selectedHari!!
                    )
                    ApiClient.instance.createJadwal(request)
                        .enqueue(object: Callback<JadwalResponseAdmin> {
                            override fun onResponse(c: Call<JadwalResponseAdmin>, r: Response<JadwalResponseAdmin>) {
                                if (r.isSuccessful) {
                                    Toast.makeText(context, "Jadwal berhasil disimpan", Toast.LENGTH_SHORT).show()
                                    jamKe = ""
                                    selectedHari = null
                                    selectedGuru = null
                                    selectedMapel = null
                                    selectedKelas = null
                                    selectedTahun = null
                                } else {
                                    Toast.makeText(context, "Gagal simpan", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(c: Call<JadwalResponseAdmin>, t: Throwable) {
                                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                } else {
                    Toast.makeText(context, "Lengkapi semua input", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = "Simpan")
            Spacer(Modifier.width(8.dp))
            Text("Simpan Jadwal")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownField(
    label: String,
    items: List<T>,
    selectedItem: T?,
    itemLabel: (T) -> String,
    onItemSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedItem?.let { itemLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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
                    text = { Text(itemLabel(item)) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPageAdmin() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // === Spinner Hari ===
    var selectedHari by remember { mutableStateOf("Senin") }
    var expandedHari by remember { mutableStateOf(false) }
    val daftarHari = listOf("Senin","Selasa","Rabu","Kamis","Jumat","Sabtu","Minggu")

    // === Spinner Kelas (from API) ===
    var kelasList by remember { mutableStateOf<List<KelasResponse>>(emptyList()) }
    var selectedKelas by remember { mutableStateOf<KelasResponse?>(null) }
    var expandedKelas by remember { mutableStateOf(false) }

    // === Jadwal List ===
    var jadwalList by remember { mutableStateOf<List<JadwalResponse>>(emptyList()) }

    // Fetch kelas saat pertama kali
    LaunchedEffect(Unit) {
        try {
            val response = ApiClient.instance.getKelas()
            kelasList = response
            if (response.isNotEmpty()) {
                selectedKelas = response.first()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Gagal load kelas", Toast.LENGTH_SHORT).show()
        }
    }

    // Fetch jadwal setiap kali Hari atau Kelas berubah
    LaunchedEffect(selectedHari, selectedKelas) {
        if (selectedKelas != null) {
            try {
                val response = ApiClient.instance.getJadwal(
                    kelasId = selectedKelas!!.id,
                    hari = selectedHari
                )
                jadwalList = response
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal load jadwal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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

        // === List Jadwal ===
        LazyColumn {
            items(jadwalList) { jadwal ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nama Guru: ${jadwal.nama_guru}")
                        Text("Mapel: ${jadwal.mata_pelajaran}")
                        Text("Tahun Ajaran: ${jadwal.tahun_ajaran}")
                        Text("Jam Ke: ${jadwal.jam_ke}")
                    }
                }
            }
        }
    }
}

// === Data Model Dummy ===
data class JadwalDataKepsek(
    val id: Int,
    var guru: String,
    var mapel: String,
    var tahunAjaran: String,
    var jamKe: String
)


@Preview(showBackground = true)
@Composable
fun AdminPreview() {
    AplikasiMonitoringKelasTheme {
        AdminScreen()
    }
}
