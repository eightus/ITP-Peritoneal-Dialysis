package com.itp.pdbuddy.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.itp.pdbuddy.utils.navigate
import com.itp.pdbuddy.utils.Result

@Composable
fun PrescriptionManagementScreen(navController: NavController) {
    val prescriptionViewModel: PrescriptionViewModel = hiltViewModel()
    val latestPrescriptionState by prescriptionViewModel.latestPrescription.collectAsState()
    val errorMessage by prescriptionViewModel.errorMessage.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        prescriptionViewModel.fetchLatestPrescription()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,

    ) {

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Current Prescription section
        Text(
            text = "Current Prescription",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                .padding(24.dp)
        ) {
            Column {
                when (latestPrescriptionState) {
                    is Result.Loading -> {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is Result.Success -> {
                        val prescription = (latestPrescriptionState as Result.Success).data
                        PrescriptionDetails(prescription = prescription)
                    }
                    is Result.Failure -> {
                        Text(
                            text = "Failed to load the latest prescription",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp
                        )
                    }
                    else -> {
                        // No data
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Update Options section
        Text(
            text = "Update Options",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Column {
                UpdateOptionButton(text = "Manual Update", onClick = { navigate(navController, "prescriptionManual") })
                Spacer(modifier = Modifier.height(12.dp))
                UpdateOptionButton(text = "Automatic Update", onClick = {
                    prescriptionViewModel.fetchLatestPrescription()
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                })
                Spacer(modifier = Modifier.height(12.dp))
                UpdateOptionButton(text = "Prescription History", onClick = { navigate(navController, "prescriptionHistory") })
            }
        }
    }
}

@Composable
fun PrescriptionDetails(prescription: Prescription) {
    Column {
        prescription.solutions.forEachIndexed { index, solution ->
            PrescriptionItem(label = "Solution ${index + 1}:", value = "${solution.type} - ${solution.bagVolume} L")
        }
        PrescriptionItem(label = "Fill Volume:", value = "${prescription.fillVolume} L")
        PrescriptionItem(label = "Number of Cycles:", value = prescription.numberOfCycles)
        PrescriptionItem(label = "Total Cycles:", value = prescription.totalCycles)
        PrescriptionItem(label = "Total Volume:", value = "${prescription.totalVolume} L")
        PrescriptionItem(label = "Cap Up:", value = if (prescription.capUp) "Yes" else "No")
        if (!prescription.capUp) {
            PrescriptionItem(label = "Last Fill:", value = "${prescription.lastFill} L")
        }
        Text(
            text = "Additional Instructions:",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium, fontSize = 18.sp),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Column(modifier = Modifier.padding(start = 16.dp)) {
            prescription.additionalInstructions.split("\n").forEach {
                Text(
                    text = "• $it",
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
                )
            }
        }
    }
}

@Composable
fun PrescriptionItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium, fontSize = 18.sp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary, fontSize = 18.sp)
        )
    }
}

@Composable
fun UpdateOptionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
    ) {
        Text(text, fontSize = 18.sp)
    }
}

@Composable
@Preview(showBackground = true)
fun PrescriptionManagementScreenPreview() {
    PDBuddyTheme {
        val mockNavController = rememberNavController()
        PrescriptionManagementScreen(navController = mockNavController)
    }
}
