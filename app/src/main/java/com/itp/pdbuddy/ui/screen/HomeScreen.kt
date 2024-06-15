// HomeScreen.kt

package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import com.itp.pdbuddy.ui.viewmodel.AuthViewModel
import com.itp.pdbuddy.ui.viewmodel.HomeViewModel
import com.itp.pdbuddy.utils.Result

@Composable
fun HomeScreen(navController: NavHostController) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val sampleData by homeViewModel.sampleData.collectAsState()
    val loginResult by authViewModel.loginResult.collectAsState()

    HomeScreenContent(
        navController = navController,
        sampleData = sampleData,
        loginResult = loginResult,
        onButtonClick = {
            authViewModel.login("mail@mail.com", "ubuntu123")
            homeViewModel.doTest("Test String!")
        }
    )
}

@Composable
fun HomeScreenContent(
    navController: NavHostController,
    sampleData: Result<String>,
    loginResult: Result<Boolean>,
    onButtonClick: () -> Unit
) {
    Column {
        when (sampleData) {
            is Result.Idle -> Text("Idle")
            is Result.Loading -> Text("Loading...")
            is Result.Success -> Text("Success: ${sampleData.data}")
            is Result.Failure -> Text("Failure: ${sampleData.exception.message}")
        }

        Button(onClick = { onButtonClick() }) {
            Text("Start Test")
        }

        when (loginResult) {
            is Result.Idle -> Text("Login Idle")
            is Result.Loading -> Text("Logging in...")
            is Result.Success -> Text("Login successful!")
            is Result.Failure -> Text("Login failed: ${(loginResult as Result.Failure).exception.message}")
        }

        Button(onClick = { navController.navigate("profile") }) {
            Text("Go to Profile Screen")
        }
        Button(onClick = { navController.navigate("splash") }) {
            Text("Go to Splash Screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PDBuddyTheme {
        val mockNavController = rememberNavController()
        val mockSampleData: Result<String> = Result.Success("Preview Success")
        val mockLoginResult: Result<Boolean> = Result.Idle

        HomeScreenContent(
            navController = mockNavController,
            sampleData = mockSampleData,
            loginResult = mockLoginResult,
            onButtonClick = { /* Empty for preview */ }
        )
    }
}
