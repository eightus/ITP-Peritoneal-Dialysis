package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.itp.pdbuddy.ui.viewmodel.AuthViewModel
import com.itp.pdbuddy.ui.viewmodel.RecordViewModel
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ManualRecordScreen(navController: NavController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val username by authViewModel.username.collectAsState()
    authViewModel.fetchUsername()
    ManualRecordScreenContent(navController = navController, username = username)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualRecordScreenContent(navController: NavController, username: Result<String?>) {
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
    val bp = remember { mutableStateOf("") }
    val hr = remember { mutableStateOf("") }
    var weight = remember { mutableStateOf("") }
    var uo = remember { mutableStateOf("") }
    var timeOn = remember {
        derivedStateOf{(datetimeFormat.format(timeOnState.value.time))}
    }
    var timeOff = remember {
        derivedStateOf {(datetimeFormat.format(timeOffState.value.time))}
    }
    var redBagAmount = remember { mutableStateOf("") }
    var redBagDext = remember { mutableStateOf("") }
    var whiteBagDext = remember { mutableStateOf("") }
    var whiteBagAmount = remember { mutableStateOf("") }
    var blueBagDext = remember { mutableStateOf("") }
    var blueBagAmount = remember { mutableStateOf("") }
    var blueBagType = remember { mutableStateOf("") }
    // To ensure blueBagType is recorded as "" if "Others" is not selected
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
    var nettUF = remember { mutableStateOf(calculateNettUF(totalUF.value, lastFillVol.value, initDrain.value)) }
    var remarks = remember { mutableStateOf("") }

    // Auto calculate nett UF
    LaunchedEffect(totalUF.value, lastFillVol.value, initDrain.value) {
        nettUF.value = calculateNettUF(totalUF.value, lastFillVol.value, initDrain.value)
    }
    // Auto calculate therapy time
    LaunchedEffect(timeOffState.value, timeOnState.value) {
        val diffInMillis = timeOffState.value.timeInMillis - timeOnState.value.timeInMillis
        val diffInHours = diffInMillis / (1000 * 60 * 60).toFloat() // Difference in hours as a float
        therapyTime.value = diffInHours.toString()
    }
    // Auto calculate treatment hours
    val submitResult by recordViewModel.recordResult.collectAsState()

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
                Text(text = "Recording Date", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = dateFormat.format(recordingDateState.value.time), fontSize = 25.sp)
                    IconButton(
                        onClick = { showRecordingDatePicker.value = true }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
            }
            item{
                textNumberBox(title = "Blood Pressure", variable = bp, tooltipMessage = "Blood Pressure, measured in Systolic/Diastolic mmHg", numeric = false)

            }
            item{
                textNumberBox(title = "Heart Rate", variable = hr, tooltipMessage = "Heart Rate, measured in BPM")
            }
            item{
                textNumberBox(title = "Weight", variable = weight, tooltipMessage = "Weight, measured in KG")
            }
            item{
                textNumberBox(title = "Urine Output", variable = uo, tooltipMessage = "Urine Output, measured in mL")
            }
            item{
                dateTimePicker(
                    showDatePicker = showTimeOnDatePicker,
                    cal = timeOnState,
                    onDateSelected = timeOnSelected
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "Treatment Start Time", fontSize = 25.sp)
                    Tooltip(message = "Date and Time treatment starts")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if(timeOnSelected.value){
                        Text(text = timeOn.value, fontSize = 25.sp)
                    }
                    IconButton(
                        onClick = {
                            showTimeOnDatePicker.value = true }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
            }
            item{
                dateTimePicker(
                    showDatePicker = showTimeOffDatePicker,
                    cal = timeOffState,
                    onDateSelected = timeOffSelected,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "Treatment End Time", fontSize = 25.sp)
                    Tooltip(message = "Date and Time treatment ends")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if(timeOffSelected.value){
                        Text(text = timeOff.value, fontSize = 25.sp)
                    }
                    IconButton(
                        onClick = { showTimeOffDatePicker.value = true }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
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
                    textNumberBox(title = "Type (Others)", variable = blueBagType, tooltipMessage = "If Others, state the type")
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
                textNumberBox(title = "Total Volume", variable = totalVolume, tooltipMessage = "Volume of <>, measured in mL")
            }
            item{
                textNumberBox(title = "Target UF for Tidal (If applicable)", variable = targetUF, numeric = false, tooltipMessage = "If not applicable, leave this field empty")
            }
            item{
                textNumberBox(title = "Therapy Time", variable = therapyTime, tooltipMessage = "Total duration of treatment, measured in Hours")
            }
            item{
                textNumberBox(title = "Fill Volume", variable = fillVol, tooltipMessage = "Measured in mL")
            }
            item{
                textNumberBox(title = "Last Fill Volume", variable = lastFillVol, tooltipMessage = "Measured in mL")
            }
            item{
                textNumberBox(title = "Dextrose % Conc", variable = dextCon, numeric = false, tooltipMessage = "% concentration od Dextrose used")
            }
            item{
                textNumberBox(title = "No. of Cycles", variable = cycles)
            }
            item{
                textNumberBox(title = "Initial Drain", variable = initDrain, tooltipMessage = "Measured in mL")
            }
            item{
                textNumberBox(title = "Average Dwell Time", variable = avgDwellTime, tooltipMessage = "Measured in Hours")
            }
            item{
                dropdownBox(title = "Colour of Drainage", variable = colorDrain, showDropdown = showColorDropdown, dropdownItems = dischargeColor)
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                textNumberBox(title = "Total UF", variable = totalUF, tooltipMessage = "Measured in mL")
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                textNumberBox(title = "Nett UF", variable = nettUF, tooltipMessage = "Measured in mL, calculated using Nett UF = (Initial Drain - Last Fill Volume) + Total UF")
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

    // Date Picker
//    if (showRecordingDatePicker.value) {
//        DatePickerDialog(
//            onDismissRequest = { showRecordingDatePicker.value = false },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        val selectedDateMillis = datePickerState.selectedDateMillis
//                        if (selectedDateMillis != null) {
//                            recordingDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDateMillis))
//                        }
//                        showRecordingDatePicker.value = false
//                              },
//                    enabled = datePickerState.selectedDateMillis != null
//                ) {
//                    Text(text = "Confirm")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showRecordingDatePicker.value = false }) {
//                    Text(text = "Dismiss")
//                }
//            }) {
//            //Setting the selected date
//            DatePicker(state = datePickerState)
//        }
//    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dateTimePicker(
    showDatePicker: MutableState<Boolean>,
    cal: MutableState<Calendar>,
    onDateSelected: MutableState<Boolean>
){
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    var datePicker by remember { mutableStateOf(true) }
    var timePicker by remember { mutableStateOf(false) }

    if (showDatePicker.value) {
        if (datePicker){
            DatePickerDialog(
                onDismissRequest = { showDatePicker.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val selectedDateMillis = datePickerState.selectedDateMillis
                            if (selectedDateMillis != null) {
                                val selectedDate = Calendar.getInstance().apply {
                                    timeInMillis = selectedDateMillis
                                }
                                val updatedCal = (cal.value.clone() as Calendar).apply {
                                    set(Calendar.YEAR, selectedDate.get(Calendar.YEAR))
                                    set(Calendar.MONTH, selectedDate.get(Calendar.MONTH))
                                    set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH))
                                }
                                cal.value = updatedCal
                            }
                            //showDatePicker.value = false
                            datePicker = false
                            timePicker = true
                        },
                        enabled = datePickerState.selectedDateMillis != null
                    ) {
                        Text(text = "Next")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker.value = false }) {
                        Text(text = "Dismiss")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        if (timePicker){
            TimePickerDialog(
                onDismissRequest = { showDatePicker.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val updatedCal = (cal.value.clone() as Calendar).apply {
                                set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                set(Calendar.MINUTE, timePickerState.minute)
                                set(Calendar.SECOND, 0)
                            }
                            cal.value = updatedCal
                            onDateSelected.value = true
                            datePicker = true
                            timePicker = false
                            showDatePicker.value = false
                        },
                        enabled = datePickerState.selectedDateMillis != null
                    ) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker.value = false }) {
                        Text(text = "Dismiss")
                    }
                }
            ) {
                    TimePicker(state = timePickerState)
                }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePicker(
    showDatePicker: MutableState<Boolean>,
    cal: MutableState<Calendar>,
){
    val datePickerState = rememberDatePickerState(selectableDates = PastOrPresentSelectableDates)
    var datePicker by remember { mutableStateOf(true) }
    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val selectedDate = Calendar.getInstance().apply {
                                timeInMillis = selectedDateMillis
                            }
                            val updatedCal = (cal.value.clone() as Calendar).apply {
                                set(Calendar.YEAR, selectedDate.get(Calendar.YEAR))
                                set(Calendar.MONTH, selectedDate.get(Calendar.MONTH))
                                set(Calendar.DAY_OF_MONTH, selectedDate.get(Calendar.DAY_OF_MONTH))
                            }
                            cal.value = updatedCal
                        }
                        datePicker = false
                        showDatePicker.value = false
                    },
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text(text = "Dismiss")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}



@Composable
fun textNumberBox(
    title : String,
    variable : MutableState<String>,
    numeric: Boolean = true,
    tooltipMessage: String = ""
){
    if(numeric){
        OutlinedTextField(
            value = variable.value,
            onValueChange = {newText ->
                val regex = Regex("^[0-9]*\\.?[0-9]*$")
                if (newText.isEmpty() || newText.matches(regex)) {
                    variable.value = newText
                }
            },
            label = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(title)
                    if (tooltipMessage.isNotEmpty()) {
                        Tooltip(message = tooltipMessage)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }else{
        OutlinedTextField(
            value = variable.value,
            onValueChange = { variable.value = it },
            label = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(title)
                    if (tooltipMessage.isNotEmpty()) {
                        Tooltip(message = tooltipMessage)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dropdownBox(
    title: String,
    variable: MutableState<String>,
    showDropdown: MutableState<Boolean>,
    dropdownItems: List<String>,
    tooltipMessage: String = ""

    ){
    Box(
        modifier = Modifier
            .clickable { showDropdown.value = true }
            .fillMaxWidth(),

        //            .clickable { showDropdown = !showDropdown },
    ) {
        OutlinedTextField(
            value = variable.value,
            onValueChange = { },
            label = { Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(title)
                if (tooltipMessage.isNotEmpty()) {
                    Tooltip(message = tooltipMessage)
                }
            } },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDropdown.value = !showDropdown.value },
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
                if (showDropdown.value){
                    Icon(Icons.Filled.ArrowDropUp,"contentDescription")
                } else {
                    Icon(Icons.Filled.ArrowDropDown,"contentDescription")
                }
            }
        )
        DropdownMenu(
            expanded = showDropdown.value,
            onDismissRequest = { showDropdown.value = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            dropdownItems.forEach { item ->
                DropdownMenuItem(text = { Text(text = item) }, onClick = {
                    variable.value = item
                    showDropdown.value = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tooltip(
    message: String,
    modifier: Modifier = Modifier
) {

    val state = rememberTooltipState()
    val scope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        tooltip = {
            RichTooltip(
//                title = { Text("Smart Cycle") },
                text = { Text(message) })
        },
        state = state)
    {
        IconButton(onClick = {
            scope.launch {
                state.show(MutatePriority.Default)
            }
        }) {
            Icon(Icons.Default.Info, contentDescription = "info")
        }

    }

}

fun calculateNettUF(totalUF: String, fillVol: String, initDrain: String): String {
    return if (totalUF.isNotEmpty() && fillVol.isNotEmpty() && initDrain.isNotEmpty()) {
        try {
            (initDrain.toInt() - fillVol.toInt() + totalUF.toInt()).toString()
        } catch (e: NumberFormatException) {
            ""
        }
    } else {
        ""
    }
}

fun formatDateTime(calendar: Calendar): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(calendar.time)
}