package com.example.aplikasimonitoringkelas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aplikasimonitoringkelas.ui.theme.AplikasiMonitoringKelasTheme
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.aplikasimonitoringkelas.admin.AdminActivity
import com.example.aplikasimonitoringkelas.api.ApiClient
import kotlinx.coroutines.withContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplikasiMonitoringKelasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

data class LoginRequest(
    val role: String,
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: UserResponse
)

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    var selectedRole by remember { mutableStateOf("Siswa") }
    val roles = listOf("Siswa", "Waka Kurikulum", "Kepala Sekolah", "Admin")

    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }

    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Logo sekolah
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Sekolah",
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp)
        )

        // Spinner Role
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedRole,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Role") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                roles.forEach { role ->
                    DropdownMenuItem(
                        text = { Text(role) },
                        onClick = {
                            selectedRole = role
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = { Text("Email") },
            isError = !isEmailValid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (!isEmailValid) {
            Text(
                text = "Format email tidak valid",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = {
                scope.launch {
                    try {
                        val response = withContext(Dispatchers.IO) {
                            ApiClient.instance.login(LoginRequest(selectedRole, email, password))
                        }

                        // Simpan token
                        val sharedPref = context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
                        sharedPref.edit()
                            .putString("TOKEN", response.token)
                            .putString("ROLE", response.user.role)
                            .apply()

                        Toast.makeText(context, "Login Berhasil sebagai ${response.user.role}", Toast.LENGTH_SHORT).show()

                        when (response.user.role) {
                            "Siswa" -> context.startActivity(Intent(context, SiswaActivity::class.java))
                            "Waka Kurikulum" -> context.startActivity(Intent(context, KurikulumActivity::class.java))
                            "Kepala Sekolah" -> context.startActivity(Intent(context, KepalaSekolahActivity::class.java))
                            "Admin" -> context.startActivity(Intent(context, AdminActivity::class.java))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Login gagal: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    AplikasiMonitoringKelasTheme {
        LoginScreen()
    }
}
