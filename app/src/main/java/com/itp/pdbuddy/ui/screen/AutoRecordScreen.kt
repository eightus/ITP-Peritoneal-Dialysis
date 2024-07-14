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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
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
import java.util.Calendar
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

    val showRecordingDatePicker = remember { mutableStateOf(false) }
    val showTimeOnDatePicker = remember { mutableStateOf(false) }
    val showTimeOffDatePicker = remember { mutableStateOf(false) }
    val recordingDateState = remember { mutableStateOf(Calendar.getInstance()) }
    val timeOnState = remember { mutableStateOf(Calendar.getInstance()) }
    val timeOffState = remember { mutableStateOf(Calendar.getInstance()) }
    val timeOnSelected = remember{ mutableStateOf(false)}
    val timeOffSelected = remember{ mutableStateOf(false)}
    var showTherapyDropdown by remember { mutableStateOf(false) }
    var showColorDropdown by remember { mutableStateOf(false) }
    var showRedBagDropdown by remember { mutableStateOf(false) }
    var showWhiteBagDropdown by remember { mutableStateOf(false) }
    var showBlueBagDropdown by remember { mutableStateOf(false) }

    // Dropdown items
    val therapyItems = listOf("CAPD", "APD")
    val dischargeColor = listOf(
        "Clear Yellow", "Cloudy Yellow", "Red", "Orange", "Rust", "Bright Yellow",
        "Yellow Green", "Green", "Green with Particles", "Brown Green", "Blue/Purple",
        "Clear Brown", "Brown Black", "Milk White", "Peach/Pink")
    val bagDext = listOf("1.5% Dext", "2.5% Dext", "4.25% Dext")
    val blueDext = listOf("1.5% Dext", "2.5% Dext", "4.25% Dext", "Others")

    // Formatters
    var dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var datetimeFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())

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
    val timeOn = remember { mutableStateOf(if (data.isNotEmpty()) data[3].toString() else "") }
    val formattedTimeOn = remember {
        derivedStateOf {
            if (timeOn.value.isEmpty() || timeOnSelected.value) {
                datetimeFormat.format(timeOnState.value.time)
            } else {
                timeOn.value
            }
        }
    }
    var timeOff = remember { mutableStateOf(if (data.isNotEmpty()) data[4].toString() else "") }
    val formattedTimeOff = remember {
        derivedStateOf {
            if (timeOff.value.isEmpty() || timeOffSelected.value) {
                datetimeFormat.format(timeOffState.value.time)
            } else {
                timeOff.value
            }
        }
    }
    var redBagAmount = remember { mutableStateOf("") }
    var redBagDext = remember { mutableStateOf("") }
    var whiteBagDext = remember { mutableStateOf("") }
    var whiteBagAmount = remember { mutableStateOf("") }
    var blueBagDext = remember { mutableStateOf("") }
    var blueBagAmount = remember { mutableStateOf("") }
    var blueBagType = remember { mutableStateOf("") }
    val blueBagFormatter = remember {
        derivedStateOf {
            if (blueBagDext.value == "Others") blueBagType.value else ""
        }
    }
    var therapy = remember { mutableStateOf("") }
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
    var nettUF = remember {
        mutableStateOf(
            calculateNettUF(
                totalUF.value,
                lastFillVol.value,
                initDrain.value
            )
        )
    }
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
                    text = "Manual Entry",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            item {
                datePicker(showRecordingDatePicker, recordingDateState)
                Text(text = "Recording Date", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = dateFormat.format(recordingDateState.value.time), fontSize = 25.sp)
                    IconButton(
                        onClick = { showRecordingDatePicker.value = true }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
            }
            item {
                textNumberBox(
                    title = "Blood Pressure",
                    variable = bp,
                    tooltipMessage = "Blood Pressure, measured in Systolic/Diastolic mmHg"
                )

            }
            item {
                textNumberBox(
                    title = "Heart Rate",
                    variable = hr,
                    tooltipMessage = "Heart Rate, measured in BPM"
                )
            }
            item {
                textNumberBox(
                    title = "Weight",
                    variable = weight,
                    tooltipMessage = "Weight, measured in KG"
                )
            }
            item {
                textNumberBox(
                    title = "Urine Output",
                    variable = uo,
                    tooltipMessage = "Urine Output, measured in mL"
                )
            }
            item {
                dateTimePicker(
                    showDatePicker = showTimeOnDatePicker,
                    cal = timeOnState,
                    onDateSelected = timeOnSelected
                )
                Text(text = "Time on", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = formattedTimeOn.value, fontSize = 25.sp)

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
                    cal = timeOffState,
                    onDateSelected = timeOffSelected,
                )
                Text(text = "Time off", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = formattedTimeOff.value,
                        fontSize = 25.sp
                    )

                    IconButton(
                        onClick = { showTimeOffDatePicker.value = true }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
            }
            item {
                Divider(modifier = Modifier.height(5.dp))
            }
            item {
                Box(
                    modifier = Modifier
                        .clickable { showRedBagDropdown = true }
                        .fillMaxWidth(),

                    //            .clickable { showDropdown = !showDropdown },
                ) {
                    OutlinedTextField(
                        value = redBagDext.value,
                        onValueChange = { },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Heater Bag")
                                Tooltip(message = "Type Of Red Heater Bag used")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusEvent { showRedBagDropdown = it.isFocused },
                        readOnly = true,
                        singleLine = true
                    )
                    DropdownMenu(
                        expanded = showRedBagDropdown,
                        onDismissRequest = { showRedBagDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        bagDext.forEach { item ->
                            DropdownMenuItem(text = { Text(text = item) }, onClick = {
                                redBagDext.value = item
                                showRedBagDropdown = false
                            })
                        }
                    }
                }
            }
            item {
                textNumberBox(
                    title = "Amount",
                    variable = redBagAmount,
                    tooltipMessage = "Amount of Red Heater Bag used"
                )
            }
            item {
                Divider(modifier = Modifier.height(5.dp))
            }
            item {
                Box(
                    modifier = Modifier
                        .clickable { showWhiteBagDropdown = true }
                        .fillMaxWidth(),

                    //            .clickable { showDropdown = !showDropdown },
                ) {
                    OutlinedTextField(
                        value = whiteBagDext.value,
                        onValueChange = { },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Supply Bag")
                                Tooltip(message = "Type of White Supply Bag used")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusEvent { showWhiteBagDropdown = it.isFocused },
                        readOnly = true,
                        singleLine = true
                    )
                    DropdownMenu(
                        expanded = showWhiteBagDropdown,
                        onDismissRequest = { showWhiteBagDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        bagDext.forEach { item ->
                            DropdownMenuItem(text = { Text(text = item) }, onClick = {
                                whiteBagDext.value = item
                                showWhiteBagDropdown = false
                            })
                        }
                    }
                }
            }
            item {
                textNumberBox(
                    title = "Amount",
                    variable = whiteBagAmount,
                    tooltipMessage = "Amount of White Supply Bag used"
                )
            }
            item {
                Divider(modifier = Modifier.height(5.dp))
            }
            item {
                Box(
                    modifier = Modifier
                        .clickable { showBlueBagDropdown = true }
                        .fillMaxWidth(),

                    //            .clickable { showDropdown = !showDropdown },
                ) {
                    OutlinedTextField(
                        value = blueBagDext.value,
                        onValueChange = { },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Last Bag")
                                Tooltip(message = "Type of Blue Last Bag used")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusEvent { showBlueBagDropdown = it.isFocused },
                        readOnly = true,
                        singleLine = true
                    )
                    DropdownMenu(
                        expanded = showBlueBagDropdown,
                        onDismissRequest = { showBlueBagDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        blueDext.forEach { item ->
                            DropdownMenuItem(text = { Text(text = item) }, onClick = {
                                blueBagDext.value = item
                                showBlueBagDropdown = false
                            })
                        }
                    }
                }
            }
            if (blueBagDext.value == "Others") {
                item {
                    textNumberBox(
                        title = "Type (Others)",
                        variable = blueBagType,
                        numeric = false,
                        tooltipMessage = "If Others, state the type"
                    )
                }
            }
            item {
                textNumberBox(
                    title = "Amount",
                    variable = blueBagAmount,
                    tooltipMessage = "Amount of Blue Last Bag used"
                )
            }
            item {
                Divider(modifier = Modifier.height(2.dp))
            }
            item {
                //textNumberBox(title = "Type of Therapy", variable = therapy, numeric = false)
                Box(
                    modifier = Modifier
                        .clickable { showTherapyDropdown = true }
                        .fillMaxWidth(),

                    //            .clickable { showDropdown = !showDropdown },
                ) {
                    OutlinedTextField(
                        value = therapy.value,
                        onValueChange = { },
                        label = { Text("Therapy Type") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusEvent { showTherapyDropdown = it.isFocused },
                        readOnly = true,
                        singleLine = true
                    )
                    DropdownMenu(
                        expanded = showTherapyDropdown,
                        onDismissRequest = { showTherapyDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        therapyItems.forEach { item ->
                            DropdownMenuItem(text = { Text(text = item) }, onClick = {
                                therapy.value = item
                                showTherapyDropdown = false
                            })
                        }
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
                Box(
                    modifier = Modifier
                        .clickable { showColorDropdown = true }
                        .fillMaxWidth(),

                    //            .clickable { showDropdown = !showDropdown },
                ) {
                    OutlinedTextField(
                        value = colorDrain.value,
                        onValueChange = { },
                        label = { Text("Colour of Drainage") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusEvent { showColorDropdown = it.isFocused },
                        readOnly = true,
                        singleLine = true
                    )
                    DropdownMenu(
                        expanded = showColorDropdown,
                        onDismissRequest = { showColorDropdown = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        dischargeColor.forEach { item ->
                            DropdownMenuItem(text = { Text(text = item) }, onClick = {
                                colorDrain.value = item
                                showColorDropdown = false
                            })
                        }
                    }

                }
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
                                redBagDext.value,
                                redBagAmount.value,
                                whiteBagDext.value,
                                whiteBagAmount.value,
                                blueBagDext.value,
                                blueBagAmount.value,
                                blueBagFormatter.value,
                                therapy.value,
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
}



