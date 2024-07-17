package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.data.model.Prescription
import com.itp.pdbuddy.data.model.Solution
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import com.itp.pdbuddy.ui.viewmodel.PrescriptionViewModel
import com.itp.pdbuddy.utils.Result

@Composable
fun PrescriptionHistoryScreen(navController: NavController) {
    val viewModel: PrescriptionViewModel = hiltViewModel()
    val prescriptionsState by viewModel.allPrescriptions.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllPrescriptions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Prescription History",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        when (prescriptionsState) {
            is Result.Loading -> {
                CircularProgressIndicator()
            }
            is Result.Success -> {
                val prescriptions = (prescriptionsState as Result.Success<List<Prescription>>).data
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(prescriptions) { prescription ->
                        PrescriptionHistoryItem(prescription)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            is Result.Failure -> {
                Text(
                    text = "Failed to load prescriptions",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp
                )
            }
            else -> {}
        }
    }
}

@Composable
fun PrescriptionHistoryItem(prescription: Prescription) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small)
            .padding(16.dp)
    ) {
        Text("Date & Time: ${prescription.dateTime}", style = MaterialTheme.typography.bodyLarge)
        prescription.solutions.forEachIndexed { index, solution ->
            Text("Solution ${index + 1}: ${solution.type} - ${solution.bagVolume} L", style = MaterialTheme.typography.bodyLarge)
        }
        Text("Fill Volume: ${prescription.fillVolume} L", style = MaterialTheme.typography.bodyLarge)
        Text("Number of Cycles: ${prescription.numberOfCycles}", style = MaterialTheme.typography.bodyLarge)
        Text("Total Cycles: ${prescription.totalCycles}", style = MaterialTheme.typography.bodyLarge)
        Text("Total Volume: ${prescription.totalVolume} L", style = MaterialTheme.typography.bodyLarge)
        Text("Cap Up: ${if (prescription.capUp) "Yes" else "No"}", style = MaterialTheme.typography.bodyLarge)
        if (!prescription.capUp) {
            Text("Last Fill: ${prescription.lastFill} L", style = MaterialTheme.typography.bodyLarge)
        }
        Text("Additional Instructions: ${prescription.additionalInstructions}", style = MaterialTheme.typography.bodyLarge)
        Text("Source: ${prescription.source}", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
@Preview(showBackground = true)
fun PrescriptionHistoryScreenPreview() {
    val mockNavController = rememberNavController()
    val mockPrescriptions = listOf(
        Prescription(
            solutions = listOf(
                Solution("1.5% Dextrose", "5"),
                Solution("2.5% Dextrose", "5")
            ),
            fillVolume = "1.8",
            numberOfCycles = "4",
            totalCycles = "4",
            totalVolume = "7.2",
            lastFill = "2.0",
            capUp = false,
            additionalInstructions = "Monitor drainage color. Contact healthcare provider if cloudy.",
            dateTime = "2023-06-27 14:34",
            source = "Manual",
            username = "TestUser"
        ),
        Prescription(
            solutions = listOf(
                Solution("2.5% Dextrose", "5"),
                Solution("4.25% Dextrose", "5")
            ),
            fillVolume = "2.0",
            numberOfCycles = "5",
            totalCycles = "5",
            totalVolume = "9.0",
            lastFill = "2.5",
            capUp = false,
            additionalInstructions = "Ensure proper hygiene. Contact healthcare provider if feeling unwell.",
            dateTime = "2023-06-26 14:34",
            source = "Automated",
            username = "TestUser"
        )
    )
    PDBuddyTheme {
        PrescriptionHistoryScreen(navController = mockNavController)
    }
}
