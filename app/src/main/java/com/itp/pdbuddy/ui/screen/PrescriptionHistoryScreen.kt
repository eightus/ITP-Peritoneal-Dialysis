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
        Text("Solution Type: ${prescription.solutionType}", style = MaterialTheme.typography.bodyLarge)
        Text("Amount: ${prescription.amount} ml", style = MaterialTheme.typography.bodyLarge)
        Text("Therapy Schedule: ${prescription.therapySchedule} hrs", style = MaterialTheme.typography.bodyLarge)
        Text("Number of Cycles: ${prescription.numberOfCycles}", style = MaterialTheme.typography.bodyLarge)
        Text("Additional Instructions: ${prescription.additionalInstructions}", style = MaterialTheme.typography.bodyLarge)
        Text("Source: ${prescription.source}", style = MaterialTheme.typography.bodyLarge)
    }
}


@Composable
@Preview(showBackground = true)
fun PrescriptionHistoryScreenPreview() {
    val mockNavController = rememberNavController()
    val mockPrescriptions = listOf(
        Prescription("1.5% Dextrose", "2000", "10", "4", "Monitor drainage color. Contact healthcare provider if cloudy.", "2023-06-27 14:34", "Manual", "TestUser"),
        Prescription("2.5% Dextrose", "2500", "8", "3", "Follow diet plan. Measure blood pressure regularly.", "2023-06-26 14:34", "Automated", "TestUser"),
        Prescription("4.25% Dextrose", "1500", "12", "5", "Ensure proper hygiene. Contact healthcare provider if feeling unwell.", "2023-06-25 14:34", "Manual", "TestUser")
    )
    PDBuddyTheme {
        PrescriptionHistoryScreen(navController = mockNavController)
    }
}