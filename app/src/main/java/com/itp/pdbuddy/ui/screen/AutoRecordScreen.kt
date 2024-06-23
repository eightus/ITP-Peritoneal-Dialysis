package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.AuthViewModel
import com.itp.pdbuddy.ui.viewmodel.ProfileViewModel
import com.itp.pdbuddy.ui.viewmodel.RecordViewModel
import com.itp.pdbuddy.utils.Result
import com.itp.pdbuddy.utils.navigate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AutoRecordScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val recordViewModel: RecordViewModel = hiltViewModel()
    
    val username by authViewModel.username.collectAsState()
    val autoRecordStatus by recordViewModel.autoRecordResult.collectAsState()
    
    authViewModel.fetchUsername()


    val autoRecordData by recordViewModel.autoRecord.collectAsState()
    
    var loading by remember { mutableStateOf(false) }
//    var infotext by remember {
//        mutableStateOf("Connecting to machine")
//    }

    val infotext = when (autoRecordStatus) {
        is Result.Idle -> "Fetching data..."
        is Result.Loading -> "Loading..."
        is Result.Success -> ""
        is Result.Failure -> "Failed to fetch data"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        when (autoRecordStatus) {
            is Result.Idle -> {
                recordViewModel.getAutoRecord()
                Text(
                    text = infotext,
                    Modifier.padding(20.dp)
                )
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            is Result.Loading -> {
                Text(
                    text = infotext,
                    Modifier.padding(20.dp)
                )
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            is Result.Success -> AutoRecordScreenContent(navController, username, autoRecordData)
            is Result.Failure -> {
                Text(
                    text = infotext,
                    Modifier.padding(20.dp)
                )
                Button(
                    onClick = { navigate(navController, "autorecord") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ){
                    Text("Retry")
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoRecordScreenContent(navController: NavHostController, username: Result<String?>, data: List<Any>) {
    val recordViewModel: RecordViewModel = hiltViewModel()

    // State Variables
    val datePickerState =
        rememberDatePickerState(
            initialDisplayedMonthMillis = System.currentTimeMillis(),
            yearRange = 2000..2024
        )
    val showRecordingDatePicker = remember { mutableStateOf(false) }
    val showTimeOnDatePicker = remember { mutableStateOf(false) }
    val showTimeOffDatePicker = remember { mutableStateOf(false) }
    var showTherapyDropdown by remember { mutableStateOf(false) }
    val therapyItems = listOf("A", "B", "C", "D", "E", "F")

    // Username
    val usernameText = when (username) {
        is Result.Idle -> "Fetching username..."
        is Result.Loading -> "Loading..."
        is Result.Success -> (username).data ?: "User"
        is Result.Failure -> "Failed to fetch username"
    }

    // Input values
    var recordingDate by remember { mutableStateOf(SimpleDateFormat("dd/M/yyyy").format(Date())) }
    val bp = remember { mutableStateOf(if (data.isNotEmpty()) data[0].toString() else "") }
    val hr = remember { mutableStateOf(if (data.isNotEmpty()) data[1].toString() else "") }
    var weight = remember { mutableStateOf(if (data.isNotEmpty()) data[2].toString() else "") }
    var uo = remember { mutableStateOf("") }
    var timeOn = remember { mutableStateOf(if (data.isNotEmpty()) data[3].toString() else "") }
    var timeOff = remember { mutableStateOf(if (data.isNotEmpty()) data[4].toString() else "") }
    var hbr15 = remember { mutableStateOf("") }
    var hbr25 = remember { mutableStateOf("") }
    var hbr425 = remember { mutableStateOf("") }
    var sbw15 = remember { mutableStateOf("") }
    var sbw25 = remember { mutableStateOf("") }
    var sbw425 = remember { mutableStateOf("") }
    var lbb15 = remember { mutableStateOf("") }
    var lbb25 = remember { mutableStateOf("") }
    var lbb425 = remember { mutableStateOf("") }
    var lbbo = remember { mutableStateOf("") }
    var therapy = remember { mutableStateOf(0) }
    var totalVolume = remember { mutableStateOf("") }
    var targetUF = remember { mutableStateOf("") }
    var therapyTime = remember { mutableStateOf("") }
    var fillVol = remember { mutableStateOf("") }
    var lastFillVol = remember { mutableStateOf("") }
    var dextCon = remember { mutableStateOf("") }
    var cycles = remember { mutableStateOf("") }
    var initDrain = remember { mutableStateOf("") }
    var avgDwellTime = remember { mutableStateOf("") }
    var colorDrain = remember { mutableStateOf("") }
    var totalUF = remember { mutableStateOf("") }
    var nettUF = remember { mutableStateOf(calculateNettUF(totalUF.value, lastFillVol.value, initDrain.value)) }
    var remarks = remember { mutableStateOf("") }

    // Auto calculate nett UF
    LaunchedEffect(totalUF.value, lastFillVol.value, initDrain.value) {
        nettUF.value = calculateNettUF(totalUF.value, lastFillVol.value, initDrain.value)
    }

    //
    val submitResult by recordViewModel.recordResult.collectAsState()

    Column {

        // UI Element
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Auto Entry",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            item {
                Text(text = "Recording Date", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = recordingDate, fontSize = 25.sp)
                    IconButton(
                        onClick = { showRecordingDatePicker.value = true }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
            }
            item {
                textNumberBox(title = "Blood Pressure", variable = bp)
            }
            item {
                textNumberBox(title = "Heart Rate", variable = hr)
            }
            item {
                textNumberBox(title = "Weight", variable = weight)
            }
            item {
                textNumberBox(title = "Urine Output", variable = uo)
            }
            item {
                dateTimePicker(
                    showDatePicker = showTimeOnDatePicker,
                    onDateSelected = timeOn
                )
                Text(text = "Time on", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = timeOn.value, fontSize = 25.sp)
                    IconButton(
                        onClick = {
                            showTimeOnDatePicker.value = true
                        }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
            }
            item {
                dateTimePicker(
                    showDatePicker = showTimeOffDatePicker,
                    onDateSelected = timeOff
                )
                Text(text = "Time off", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = timeOff.value, fontSize = 25.sp)
                    IconButton(
                        onClick = { showTimeOffDatePicker.value = true }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
            }
            item {
                Divider(modifier = Modifier.height(2.dp))
            }
            item {
                Text(text = "Heater Bag (Red)", fontSize = 30.sp)
            }
            item {
                textNumberBox(title = "1.5% Dext.(Amount)", variable = hbr15)
            }
            item {
                textNumberBox(title = "2.5% Dext.(Amount)", variable = hbr25)
            }
            item {
                textNumberBox(title = "4.25% Dext.(Amount)", variable = hbr425)
            }
            item {
                Divider(modifier = Modifier.height(2.dp))
            }
            item {
                Text(text = "Supply Bag (White)", fontSize = 30.sp)
            }
            item {
                textNumberBox(title = "1.5% Dext.(Amount)", variable = sbw15)
            }
            item {
                textNumberBox(title = "2.5% Dext.(Amount)", variable = sbw25)
            }
            item {
                textNumberBox(title = "4.25% Dext.(Amount)", variable = sbw425)
            }
            item {
                Divider(modifier = Modifier.height(2.dp))
            }
            item {
                Text(text = "Last Bag (Blue)", fontSize = 30.sp)
            }
            item {
                textNumberBox(title = "1.5% Dext.(Amount)", variable = lbb15)
            }
            item {
                textNumberBox(title = "2.5% Dext.(Amount)", variable = lbb25)
            }
            item {
                textNumberBox(title = "4.25% Dext.(Amount)", variable = lbb425)
            }
            item {
                textNumberBox(title = "Others", variable = lbbo)
            }
            item {
                Divider(modifier = Modifier.height(2.dp))
            }
            item {
                //textNumberBox(title = "Type of Therapy", variable = therapy, numeric = false)
                Text(text = "Therapy Type", fontSize = 25.sp)
                Box(
                    modifier = Modifier
                        .background(Color(0xFFEFB8C8))
                        .clickable { showTherapyDropdown = true }
                        .fillMaxWidth(),

                    //            .clickable { showDropdown = !showDropdown },
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)

                    ) {
                        Text(text = therapyItems[therapy.value], modifier = Modifier.padding(3.dp))
                        Icon(Icons.Filled.ArrowDownward, contentDescription = "Dropdown")
                    }

                }
                DropdownMenu(
                    expanded = showTherapyDropdown,
                    onDismissRequest = { showTherapyDropdown = false },
                ) {
                    therapyItems.forEachIndexed { index, s ->
                        DropdownMenuItem(
                            text = { Text(text = s) },
                            onClick = {
                                therapy.value = index
                                showTherapyDropdown = false
                            }
                        )
                    }
                }
            }
            item {
                textNumberBox(title = "Total Volume", variable = totalVolume)
            }
            item {
                textNumberBox(
                    title = "Target UF for Tidal (If applicable)",
                    variable = targetUF,
                    numeric = false
                )
            }
            item {
                textNumberBox(title = "Therapy Time (In Hours)", variable = therapyTime)
            }
            item {
                textNumberBox(title = "Fill Volume (In mL)", variable = fillVol)
            }
            item {
                textNumberBox(title = "Last Fill Volume (In mL)", variable = lastFillVol)
            }
            item {
                textNumberBox(title = "Dextrose % Conc", variable = dextCon, numeric = false)
            }
            item {
                textNumberBox(title = "No. of Cycles", variable = cycles)
            }
            item {
                textNumberBox(title = "Initial Drain", variable = initDrain)
            }
            item {
                textNumberBox(title = "Average Dwell Time", variable = avgDwellTime)
            }
            item {
                textNumberBox(title = "Color of Drainage", variable = colorDrain)
            }
            item {
                Divider(modifier = Modifier.height(2.dp))
            }
            item {
                textNumberBox(title = "Total UF (In mL)", variable = totalUF)
            }
            item {
                Divider(modifier = Modifier.height(2.dp))
            }
            item {
                textNumberBox(title = "Nett UF (In mL)", variable = nettUF)
            }
            item {
                Divider(modifier = Modifier.height(2.dp))
            }
            item {
                textNumberBox(title = "Remarks", variable = remarks, numeric = false)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        recordViewModel.submitRecord(
                            usernameText, listOf(
                                recordingDate,
                                bp.value,
                                hr.value,
                                weight.value,
                                uo.value,
                                timeOn.value,
                                timeOff.value,
                                hbr15.value,
                                hbr25.value,
                                hbr425.value,
                                sbw15.value,
                                sbw25.value,
                                sbw425.value,
                                lbb15.value,
                                lbb25.value,
                                lbb425.value,
                                lbbo.value,
                                therapyItems[therapy.value],
                                totalVolume.value,
                                targetUF.value,
                                therapyTime.value,
                                fillVol.value,
                                lastFillVol.value,
                                dextCon.value,
                                cycles.value,
                                initDrain.value,
                                avgDwellTime.value,
                                colorDrain.value,
                                totalUF.value,
                                nettUF.value,
                                remarks.value
                            )
                        )
                    }) {
                        Text(text = "Submit")
                    }
                    when (submitResult) {
                        is Result.Loading -> {
                            CircularProgressIndicator()
                        }

                        is Result.Success -> {
                            // Navigate to home screen on success
                            LaunchedEffect(Unit) {
                                navController.navigate("recordsuccess") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }

                        is Result.Failure -> {
                            Text(
                                text = "Failed: ${(submitResult as Result.Failure).exception.message}",
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        is Result.Idle -> {
                            Text("")
                        }
                    }

                }

            }

        }
    }
    // Date Picker
    if (showRecordingDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showRecordingDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            recordingDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDateMillis))
                        }
                        showRecordingDatePicker.value = false
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRecordingDatePicker.value = false }) {
                    Text(text = "Dismiss")
                }
            }) {
            //Setting the selected date
            DatePicker(state = datePickerState)
        }
    }
}



