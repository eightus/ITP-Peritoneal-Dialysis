package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import com.itp.pdbuddy.ui.viewmodel.HistoryViewModel
import com.itp.pdbuddy.utils.Result

@Composable
fun HistoryScreen(navController: NavHostController, viewModel: HistoryViewModel = hiltViewModel()) {
    val recordData by viewModel.recordData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecords()
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "History",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when (recordData) {
            is Result.Success -> {
                val records = (recordData as Result.Success<List<Map<String, Any>>>).data
                records.forEach { record ->
                    HistoryItem(
                        title = record["Type of Therapy"] as? String ?: "Unknown Therapy",
                        date = record["RecordDate"] as? String ?: "Unknown Date",
                        time = "Time on: ${record["Time On"]}\nTime off: ${record["Time Off"]}",
                        data = record
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            is Result.Failure -> {
                Text(text = "Failed to load records", color = Color.Red)
            }
            else -> {
                Text(text = "Loading...")
            }
        }
    }
}

@Composable
fun HistoryItem(
    title: String,
    date: String,
    time: String,
    data: Map<String, Any>  // Pass data map for detailed information
) {
    var showDialog by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { showDialog = true },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text(text = date, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
            Text(text = time, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Details") },
            text = {
                Column {
                    Text(text = "Type of Therapy: ${data["Type of Therapy"]}")
                    Text(text = "Record Date: ${data["RecordDate"]}")
                    Text(text = "Time On: ${data["Time On"]}")
                    Text(text = "Time Off: ${data["Time Off"]}")
                    Text(text = "Avg Dwell Time: ${data["Avg Dwell Time"]}")
                    Text(text = "Blood Pressure: ${data["Blood Pressure"]}")
                    Text(text = "Blue Bag Amount: ${data["Blue Bag Amount"]}")
                    Text(text = "Blue Bag Type: ${data["Blue Bag Type"]}")
                    Text(text = "Blue Bag Type (Others): ${data["Blue Bag Type (Others)"]}")
                    Text(text = "Color of Drainage: ${data["Color of Drainage"]}")
                    Text(text = "Dextrose % Conc.: ${data["Dextrose % Conc."]}")
                    Text(text = "Fill Volume: ${data["Fill Volume"]}")
                    Text(text = "Heart Rate: ${data["Heart Rate"]}")
                    Text(text = "Heater Bag Amount: ${data["Heater Bag Amount"]}")
                    Text(text = "Heater Bag Type: ${data["Heater Bag Type"]}")
                    Text(text = "Initial Drain: ${data["Initial Drain"]}")
                    Text(text = "Last Fill volume: ${data["Last Fill volume"]}")
                    Text(text = "Name: ${data["Name"]}")
                    Text(text = "Nett UF: ${data["Nett UF"]}")
                    Text(text = "No. Of Cycles: ${data["No. Of Cycles"]}")
                    Text(text = "Remarks: ${data["Remarks"]}")
                    Text(text = "Target UF: ${data["Target UF"]}")
                    Text(text = "Therapy Time: ${data["Therapy Time"]}")
                    Text(text = "Time Off: ${data["Time Off"]}")
                    Text(text = "Time On: ${data["Time On"]}")
                    Text(text = "Total UF: ${data["Total UF"]}")
                    Text(text = "Total Volume: ${data["Total Volume"]}")
                    Text(text = "Urine Out: ${data["Urine Out"]}")
                    Text(text = "Weight: ${data["Weight"]}")
                    Text(text = "White Bag Amount: ${data["White Bag Amount"]}")
                    Text(text = "White Bag Type: ${data["White Bag Type"]}")
                }
            },
            confirmButton = {
                Text(
                    text = "Close",
                    modifier = Modifier.clickable { showDialog = false }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    PDBuddyTheme {
        val mockNavController = rememberNavController()
        HistoryScreen(mockNavController)
    }
}