package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.itp.pdbuddy.data.model.Prescription
import com.itp.pdbuddy.ui.viewmodel.AuthViewModel
import com.itp.pdbuddy.ui.viewmodel.PrescriptionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PrescriptionManualScreen(navController: NavController) {

    var prescriptionViewModel: PrescriptionViewModel = hiltViewModel()
    val errorMessage by prescriptionViewModel.errorMessage.collectAsState()


    var solutionType by remember { mutableStateOf("1.5% Dextrose") }
    var amount by remember { mutableStateOf(TextFieldValue("")) }
    var therapySchedule by remember { mutableStateOf(TextFieldValue("")) }
    var numberOfCycles by remember { mutableStateOf(TextFieldValue("")) }
    var additionalInstructions by remember { mutableStateOf(TextFieldValue("")) }

    val solutionTypes = listOf(
        "1.5% Dextrose", "2.5% Dextrose", "4.25% Dextrose",
        "7.5% Icodextrin", "1.1% Amino Acid", "1.5% Dextrose, Low Calcium", "Bicarbonate/Lactate"
    )
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Text(
            text = "Manual Update",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = solutionType,
                onValueChange = { },
                label = { Text("Solution Type") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent { expanded = it.isFocused },
                readOnly = true,
                singleLine = true
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                solutionTypes.forEach { solution ->
                    DropdownMenuItem(text = { Text(text = solution) }, onClick = {
                        solutionType = solution
                        expanded = false
                    })
                }
            }
        }

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount (ml)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        OutlinedTextField(
            value = therapySchedule,
            onValueChange = { therapySchedule = it },
            label = { Text("Therapy Schedule (hrs)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = numberOfCycles,
            onValueChange = { numberOfCycles = it },
            label = { Text("Number of Cycles") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = additionalInstructions,
            onValueChange = { additionalInstructions = it },
            label = { Text("Additional Instructions") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val currentDateTime = LocalDateTime.now()
                val formattedDateTime = currentDateTime.format(formatter)

                val prescription = Prescription(
                    solutionType = solutionType,
                    amount = amount.text,
                    therapySchedule = therapySchedule.text,
                    numberOfCycles = numberOfCycles.text,
                    additionalInstructions = additionalInstructions.text,
                    dateTime = formattedDateTime,
                    source = "Manual",
                    username = "" // This will be populated in the ViewModel
                )
                prescriptionViewModel.addPrescription(prescription)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text("Save", fontSize = 18.sp)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PrescriptionManualScreenPreview() {
    PDBuddyTheme {
        val mockNavController = rememberNavController()
        PrescriptionManualScreen(navController = mockNavController)
    }
}
