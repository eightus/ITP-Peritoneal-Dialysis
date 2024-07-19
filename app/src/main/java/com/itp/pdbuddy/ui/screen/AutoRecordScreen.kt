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
import androidx.compose.material3.TextFieldDefaults
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
import com.itp.pdbuddy.data.model.Prescription
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
    val prescriptionData by recordViewModel.latestPrescription.collectAsState()
    
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
            is Result.Success -> AutoRecordScreenContent(navController, username, autoRecordData, prescriptionData)
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
fun AutoRecordScreenContent(navController: NavHostController, username: Result<String?>, data: List<Any>, prescriptionData:Result<Prescription>) {
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
    val showTherapyDropdown = remember { mutableStateOf(false) }
    val showColorDropdown = remember { mutableStateOf(false) }
    val showRedBagDropdown = remember { mutableStateOf(false) }
    val showWhiteBagDropdown = remember { mutableStateOf(false) }
    val showBlueBagDropdown = remember { mutableStateOf(false) }

    // Dropdown items
    val therapyItems = listOf("CAPD", "APD")
    val dischargeColor = listOf(
        "Clear Yellow", "Cloudy Yellow", "Red", "Orange", "Rust", "Bright Yellow",
        "Yellow Green", "Green", "Green with Particles", "Brown Green", "Blue/Purple",
        "Clear Brown", "Brown Black", "Milk White", "Peach/Pink")
    val bagDext = listOf("1.5% Dextrose", "2.5% Dextrose", "4.25% Dextrose")
    val blueDext = listOf("1.5% Dextrose", "2.5% Dextrose", "4.25% Dextrose", "Others")

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

    when (prescriptionData) {
        is Result.Loading -> {

        }
        is Result.Success -> {
            val prescription = (prescriptionData as Result.Success).data

            redBagAmount.value = prescription.solutions[0].bagVolume
            whiteBagAmount.value = prescription.solutions[1].bagVolume
            blueBagAmount.value = prescription.solutions[2].bagVolume
            redBagDext.value = prescription.solutions[0].type
            whiteBagDext.value = prescription.solutions[1].type
            blueBagDext.value = prescription.solutions[2].type
            cycles.value = prescription.numberOfCycles

        }
        is Result.Failure -> {

        }
        else -> {
            // No data
        }
    }

    Column {

        // UI Element
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            item{
                Text(
                    text = "Manual Entry",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            item{
                datePicker(showRecordingDatePicker, recordingDateState)
//                Text(text = "Recording Date", fontSize = 25.sp)
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ){
//                    Text(text = dateFormat.format(recordingDateState.value.time), fontSize = 25.sp)
//                    IconButton(
//                        onClick = { showRecordingDatePicker.value = true }
//                    ) {
//                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
//                    }
//                }
                OutlinedTextField(
                    value = dateFormat.format(recordingDateState.value.time),
                    onValueChange = { },
                    label = { Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("Recording Date")
                    } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showRecordingDatePicker.value = !showRecordingDatePicker.value
                        },
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
                    trailingIcon = {
                        Icon(Icons.Filled.CalendarMonth,"contentDescription")
                    }
                )
            }
            item{
                textNumberBox(title = "Blood Pressure (mm/Hg)", variable = bp, tooltipMessage = "Blood Pressure, measured in Systolic/Diastolic mmHg", numeric = false)

            }
            item{
                textNumberBox(title = "Heart Rate (bpm)", variable = hr, tooltipMessage = "Heart Rate, measured in BPM")
            }
            item{
                textNumberBox(title = "Weight (Kg)", variable = weight, tooltipMessage = "Weight, measured in KG")
            }
            item{
                textNumberBox(title = "Urine Output (mL)", variable = uo, tooltipMessage = "Urine Output, measured in mL")
            }
            item{
                dateTimePicker(
                    showDatePicker = showTimeOnDatePicker,
                    cal = timeOnState,
                    onDateSelected = timeOnSelected
                )
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ){
//                    Text(text = "Treatment Start Time", fontSize = 25.sp)
//                    Tooltip(message = "Date and Time treatment starts")
//                }
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ){
//                    if(timeOnSelected.value){
//                        Text(text = timeOn.value, fontSize = 25.sp)
//                    }
//                    IconButton(
//                        onClick = {
//                            showTimeOnDatePicker.value = true }
//                    ) {
//                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
//                    }
//                }
                OutlinedTextField(
                    value = if (timeOnSelected.value) timeOn.value else "",
                    onValueChange = { },
                    label = { Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("Treatment Start Time")
                    } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showTimeOnDatePicker.value = !showTimeOnDatePicker.value
                        },
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
                    trailingIcon = {
                        Icon(Icons.Filled.CalendarMonth,"contentDescription")
                    }
                )
            }
            item{
                dateTimePicker(
                    showDatePicker = showTimeOffDatePicker,
                    cal = timeOffState,
                    onDateSelected = timeOffSelected,
                )
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ){
//                    Text(text = "Treatment End Time", fontSize = 25.sp)
//                    Tooltip(message = "Date and Time treatment ends")
//                }
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ){
//                    if(timeOffSelected.value){
//                        Text(text = timeOff.value, fontSize = 25.sp)
//                    }
//                    IconButton(
//                        onClick = { showTimeOffDatePicker.value = true }
//                    ) {
//                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
//                    }
//                }
                OutlinedTextField(
                    value = if (timeOffSelected.value) timeOff.value else "",
                    onValueChange = { },
                    label = { Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text("Treatment Start Time")
                    } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showTimeOffDatePicker.value = !showTimeOffDatePicker.value
                        },
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
                    trailingIcon = {
                        Icon(Icons.Filled.CalendarMonth,"contentDescription")
                    }
                )
            }
            item{
                Divider(modifier = Modifier.height(5.dp))
            }
            item{
                dropdownBox(title = "Heater Bag", variable = redBagDext, showDropdown = showRedBagDropdown, dropdownItems = bagDext, tooltipMessage = "Type Of Red Heater Bag used")
            }
            item{
                textNumberBox(title = "Amount", variable = redBagAmount, tooltipMessage = "Amount of Red Heater Bag used")
            }
            item{
                Divider(modifier = Modifier.height(5.dp))
            }
            item{
                dropdownBox(title = "Supply Bag", variable = whiteBagDext, showDropdown = showWhiteBagDropdown, dropdownItems = bagDext, tooltipMessage = "Type of White Supply Bag used")
            }
            item{
                textNumberBox(title = "Amount", variable = whiteBagAmount, tooltipMessage = "Amount of White Supply Bag used")
            }
            item{
                Divider(modifier = Modifier.height(5.dp))
            }
            item{
                dropdownBox(title = "Last Bag", variable = blueBagDext, showDropdown = showBlueBagDropdown, dropdownItems = blueDext, tooltipMessage = "Type of Blue Last Bag used")
            }
            if (blueBagDext.value == "Others"){
                item{
                    textNumberBox(title = "Type (Others)", variable = blueBagType, tooltipMessage = "If Others, state the type", numeric = false)
                }
            }
            item{
                textNumberBox(title = "Amount", variable = blueBagAmount, tooltipMessage = "Amount of Blue Last Bag used")
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                //textNumberBox(title = "Type of Therapy", variable = therapy, numeric = false)
                dropdownBox(
                    title = "Therapy Type",
                    variable = therapy,
                    showDropdown = showTherapyDropdown,
                    dropdownItems = therapyItems,
                )
            }
            item{
                textNumberBox(title = "Total Volume (mL)", variable = totalVolume, tooltipMessage = "Volume of <>, measured in mL")
            }
            item{
                textNumberBox(title = "Target UF for Tidal (If applicable)", variable = targetUF, numeric = false, tooltipMessage = "If not applicable, leave this field empty")
            }
            item{
                textNumberBox(title = "Therapy Time", variable = therapyTime, tooltipMessage = "Total duration of treatment, measured in Hours")
            }
            item{
                textNumberBox(title = "Fill Volume (mL)", variable = fillVol, tooltipMessage = "Measured in mL")
            }
            item{
                textNumberBox(title = "Last Fill Volume (mL)", variable = lastFillVol, tooltipMessage = "Measured in mL")
            }
            item{
                textNumberBox(title = "Dextrose % Conc", variable = dextCon, numeric = false, tooltipMessage = "% concentration od Dextrose used")
            }
            item{
                textNumberBox(title = "No. of Cycles", variable = cycles, numeric = false)
            }
            item{
                textNumberBox(title = "Initial Drain (mL)", variable = initDrain, tooltipMessage = "Measured in mL")
            }
            item{
                textNumberBox(title = "Average Dwell Time (H)", variable = avgDwellTime, tooltipMessage = "Measured in Hours")
            }
            item{
                dropdownBox(title = "Colour of Drainage", variable = colorDrain, showDropdown = showColorDropdown, dropdownItems = dischargeColor)
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                textNumberBox(title = "Total UF (mL)", variable = totalUF, tooltipMessage = "Measured in mL")
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                textNumberBox(title = "Nett UF (mL)", variable = nettUF, tooltipMessage = "Measured in mL, calculated using Nett UF = (Initial Drain - Last Fill Volume) + Total UF")
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                textNumberBox(title = "Remarks", variable = remarks, numeric = false)
            }
            item{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Button(onClick = { recordViewModel.submitRecord(
                        usernameText,listOf(
                            recordingDate, bp.value, hr.value, weight.value, uo.value,
                            timeOn.value, timeOff.value, redBagDext.value, redBagAmount.value, whiteBagDext.value,
                            whiteBagAmount.value, blueBagDext.value, blueBagAmount.value, blueBagFormatter.value, therapy.value,
                            totalVolume.value, targetUF.value, therapyTime.value, fillVol.value, lastFillVol.value,
                            dextCon.value, cycles.value, initDrain.value, avgDwellTime.value, colorDrain.value,
                            totalUF.value, nettUF.value, remarks.value)
                    ) }) {
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
                        is Result.Idle -> { Text("") }
                    }
                }
            }
        }
    }
}



