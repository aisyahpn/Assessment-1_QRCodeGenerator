package com.aisyahpn0033.qrcodegenerator.ui.theme.auth

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aisyahpn0033.qrcodegenerator.R
import com.aisyahpn0033.qrcodegenerator.Screen
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppTheme
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppThemeOption
import com.aisyahpn0033.qrcodegenerator.ui.theme.data.UserPreferences
import kotlinx.coroutines.launch


// Fungsi utama layar register dengan navigasi
@Composable
fun RegisterScreen(navController: NavController) {
    RegisterScreenContent(
        onRegisterSuccess = {
            navController.navigate(Screen.Login.route) // Navigasi ke login jika register sukses
        },
        onLoginClick = {
            navController.navigate(Screen.Login.route) // Navigasi ke login jika tombol login diklik
        }
    )
}

// Preview untuk tampilan design tanpa navController
@Preview(showSystemUi = true, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RegisterScreenPreview() {
    AppTheme(themeOption = AppThemeOption.LIGHT) {
        RegisterScreenContent(
            onRegisterSuccess = {},
            onLoginClick = {}
        )
    }
}

// Fungsi utama isi layar register
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenContent(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Instance UserPreferences
    val userPreferences = remember { UserPreferences(context) }

    // State untuk input
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // State untuk error handling
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") }
    var genderExpanded by remember { mutableStateOf(false) }
    var genderError by remember { mutableStateOf(false) }

    val genderOptions = listOf("Laki-laki", "Perempuan")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo register
                    Image(
                        painter = painterResource(id = R.drawable.register_logo),
                        contentDescription = "Register Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(bottom = 8.dp)
                    )

                    // Judul
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Input Nama
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama Lengkap") },
                        isError = nameError,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                    if (nameError) {
                        Text(
                            text = "Nama tidak boleh kosong",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dropdown Jenis Kelamin
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = !genderExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedGender,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Jenis Kelamin") },
                            isError = genderError,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )

                        // Isi menu dropdown
                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            genderOptions.forEach { gender ->
                                DropdownMenuItem(
                                    text = { Text(gender) },
                                    onClick = {
                                        selectedGender = gender
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    if (genderError) {
                        Text(
                            text = "Pilih jenis kelamin",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        isError = emailError,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                    if (emailError) {
                        Text(
                            text = "Email tidak valid",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        isError = passwordError,
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                    if (passwordError) {
                        Text(
                            text = "Password minimal 6 karakter",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tombol Daftar
                    ElevatedButton(
                        onClick = {
                            // Validasi form
                            nameError = name.isEmpty()
                            emailError = email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                            passwordError = password.length < 6
                            genderError = selectedGender.isEmpty()

                            if (!nameError && !emailError && !passwordError && !genderError) {
                                // Simpan profil menggunakan DataStore via UserPreferences
                                scope.launch {
                                    userPreferences.saveUser(name, email)
                                    userPreferences.saveUserPassword(password)  // Simpan password di sini
                                    Toast.makeText(context, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                                    onRegisterSuccess()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0xFFFF8A80),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Daftar", style = MaterialTheme.typography.titleMedium)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Teks tombol login
                    TextButton(onClick = onLoginClick) {
                        Text(
                            "Sudah punya akun? Login",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    }
}

