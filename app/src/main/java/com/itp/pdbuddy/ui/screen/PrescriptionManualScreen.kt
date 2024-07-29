package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.itp.pdbuddy.data.model.Solution
import com.itp.pdbuddy.ui.viewmodel.PrescriptionViewModel
import com.itp.pdbuddy.utils.navigate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptionManualScreen(navController: NavController) {
    val prescriptionViewModel: PrescriptionViewModel = hiltViewModel()
    val errorMessage by prescriptionViewModel.errorMessage.collectAsState()
    val scrollState = rememberScrollState()
    var solutionType by remember { mutableStateOf("1.5% Dextrose") }
    var bagVolume by remember { mutableStateOf(TextFieldValue("")) }
    var solutions = remember { mutableStateListOf<Solution>() }

    var fillVolume by remember { mutableStateOf(TextFieldValue("")) }
    var numberOfCycles by remember { mutableStateOf(TextFieldValue("")) }
    var totalCycles by remember { mutableStateOf(TextFieldValue("")) }
    var totalVolume by remember { mutableStateOf(TextFieldValue("")) }
    var lastFill by remember { mutableStateOf(TextFieldValue("")) }
    var capUp by remember { mutableStateOf(false) }
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
            .padding(16.dp)
            .verticalScroll(scrollState),
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
                    .clickable { expanded = true },
                readOnly = true,
                singleLine = true,
                enabled = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant),
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
            value = bagVolume,
            onValueChange = { bagVolume = it },
            label = { Text("Bag Volume (L)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Button(
            onClick = {
                if (bagVolume.text.isNotEmpty()) {
                    solutions.add(Solution(type = solutionType, bagVolume = bagVolume.text))
                    solutionType = "1.5% Dextrose"
                    bagVolume = TextFieldValue("")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Text("Add Solution", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        solutions.forEachIndexed { index, solution ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${index + 1}. ${solution.type} - ${solution.bagVolume} L", fontSize = 16.sp)
                IconButton(onClick = {
                    solutions.removeAt(index)
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fillVolume,
            onValueChange = { fillVolume = it },
            label = { Text("Fill Volume (L)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
            value = totalCycles,
            onValueChange = { totalCycles = it },
            label = { Text("Total Cycles") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = totalVolume,
            onValueChange = { totalVolume = it },
            label = { Text("Total Volume (L)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = capUp,
                onCheckedChange = { capUp = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Cap Up")
        }

        if (!capUp) {
            OutlinedTextField(
                value = lastFill,
                onValueChange = { lastFill = it },
                label = { Text("Last Fill (L)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }

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
                    solutions = solutions.toList(),
                    fillVolume = fillVolume.text,
                    numberOfCycles = numberOfCycles.text,
                    totalCycles = totalCycles.text,
                    totalVolume = totalVolume.text,
                    lastFill = lastFill.text,
                    capUp = capUp,
                    additionalInstructions = additionalInstructions.text,
                    dateTime = formattedDateTime,
                    source = "Manual",
                    username = "" // This will be populated in the ViewModel
                )
                prescriptionViewModel.addPrescription(prescription)
                navigate(navController, "prescription", false)
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
