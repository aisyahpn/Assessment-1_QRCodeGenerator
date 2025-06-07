package com.aisyahpn0033.qrcodegenerator.ui.theme.auth

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aisyahpn0033.qrcodegenerator.R
import com.aisyahpn0033.qrcodegenerator.Screen
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppTheme
import com.aisyahpn0033.qrcodegenerator.ui.theme.AppThemeOption
import com.aisyahpn0033.qrcodegenerator.ui.theme.data.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

// Fungsi utama LoginScreen yang menangani navigasi saat login berhasil atau klik register
@Composable
fun LoginScreen(navController: NavController) {
    LoginScreenContent(
        onLoginSuccess = {
            navController.navigate(Screen.Home.route)
        },
        onRegisterClick = {
            navController.navigate(Screen.Register.route)
        }
    )
}

// Fungsi Preview agar bisa dilihat dalam mode terang dan gelap
@Preview(showSystemUi = true, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppTheme(themeOption = AppThemeOption.LIGHT) {
        LoginScreenContent(
            onLoginSuccess = {},
            onRegisterClick = {}
        )
    }
}

// UI utama dari halaman login
@Composable
fun LoginScreenContent(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userPrefs = remember { UserPreferences(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var loginErrorMsg by remember { mutableStateOf<String?>(null) } // untuk pesan error login

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo aplikasi
                    Image(
                        painter = painterResource(id = R.drawable.login_logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(bottom = 16.dp)
                    )

                    // Judul sambutan
                    Text(
                        text = "Welcome Back!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Email Input
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = false
                            loginErrorMsg = null
                        },
                        label = { Text("Email", color = MaterialTheme.colorScheme.onSurface) },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = "Email Icon", tint = MaterialTheme.colorScheme.onSurface)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = emailError,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (emailError) {
                        Text(
                            "Email tidak valid",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = false
                            loginErrorMsg = null
                        },
                        label = { Text("Password", color = MaterialTheme.colorScheme.onSurface) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "Password Icon", tint = MaterialTheme.colorScheme.onSurface)
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = passwordError,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (passwordError) {
                        Text(
                            "Password harus lebih dari 6 karakter",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    // Pesan error login (email & password tidak cocok)
                    if (loginErrorMsg != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = loginErrorMsg!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tombol Login
                    Button(
                        onClick = {
                            emailError = email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                            passwordError = password.length < 6

                            if (!emailError && !passwordError) {
                                scope.launch {
                                    // Ambil data user yang sudah tersimpan
                                    val savedEmail = userPrefs.getUserEmail().firstOrNull()
                                    val savedPassword = userPrefs.getUserPassword().firstOrNull()

                                    if (savedEmail == email && savedPassword == password) {
                                        userPrefs.setLoginStatus(true)
                                        Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
                                        onLoginSuccess()
                                    } else {
                                        // Email/password tidak cocok
                                        loginErrorMsg = "Email atau password salah"
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Login", fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onRegisterClick) {
                        Text(
                            "Belum punya akun? Register",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}