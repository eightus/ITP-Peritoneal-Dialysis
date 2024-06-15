package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.R
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import com.itp.pdbuddy.ui.viewmodel.AuthViewModel
import com.itp.pdbuddy.utils.Result

@Composable
fun LoginScreen(navController: NavController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val loginResult by authViewModel.loginResult.collectAsState()
    LoginScreenContent(
        navController = navController,
        loginResult = loginResult,
        onLoginClick = { email, password -> authViewModel.login(email, password) }
    )
}

@Composable
fun LoginScreenContent(
    navController: NavController,
    loginResult: Result<Boolean>,
    onLoginClick: (String, String) -> Unit
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val handleLogin = {
        onLoginClick(emailState.value, passwordState.value)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Heart image
            Image(
                painter = painterResource(id = R.drawable.splash_heart),
                contentDescription = "Heart Logo",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // App name
            Text(
                text = "PeritonealCare",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            // Subtitle
            Text(
                text = "Your Dialysis Health Companion",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Nurse image
        Image(
            painter = painterResource(id = R.drawable.login_image),
            contentDescription = "Nurse Image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(18.dp))

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Email field
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Password field
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        handleLogin()
                    }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Login button
            Button(
                onClick = {
                    handleLogin()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            when (loginResult) {
                is Result.Loading -> {
                    CircularProgressIndicator()
                }
                is Result.Success -> {
                    // Navigate to home screen on success
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                is Result.Failure -> {
                    Text(
                        text = "Login failed: ${loginResult.exception.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is Result.Idle -> { /* No-op */ }
            }
        }

    }
}
@Composable

@Preview(showBackground = true)
fun LoginScreenPreview() {
    PDBuddyTheme {
        val mockNavController = rememberNavController()
        LoginScreenContent(
            navController = mockNavController,
            loginResult = Result.Idle,
            onLoginClick = { _, _ -> run {} }
        )
    }
}
