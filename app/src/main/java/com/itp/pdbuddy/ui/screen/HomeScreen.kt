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
import com.itp.pdbuddy.ui.viewmodel.HomeViewModel
import com.itp.pdbuddy.utils.Result

@Composable
fun HomeScreen(navController: NavHostController, homeViewModel: HomeViewModel = hiltViewModel()) {
    val sampleData by homeViewModel.sampleData.collectAsState()

    HomeScreenContent(
        navController = navController,
        sampleData = sampleData,
        onButtonClick = { homeViewModel.doTest("test") }
    )
}

@Composable
fun HomeScreenContent(
    navController: NavHostController,
    sampleData: Result<String>,
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

        Button(onClick = { navController.navigate("profile") }) {
            Text("Go to Profile Screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PDBuddyTheme {
        val mockNavController = rememberNavController()
        val mockSampleData: Result<String> = Result.Success("Preview Success")

        HomeScreenContent(
            navController = mockNavController,
            sampleData = mockSampleData,
            onButtonClick = { /* Empty for preview */ }
        )
    }
}
