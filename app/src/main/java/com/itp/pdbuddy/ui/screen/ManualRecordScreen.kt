package com.itp.pdbuddy.ui.screen

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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.AuthViewModel
import com.itp.pdbuddy.ui.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualRecordScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

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

    // Input values
    var recordingDate by remember { mutableStateOf(SimpleDateFormat("dd/M/yyyy").format(Date())) }
    val bp = remember { mutableStateOf("") }
    val hr = remember { mutableStateOf("") }
    var weight = remember { mutableStateOf("") }
    var uo = remember { mutableStateOf("") }
    var timeOn = remember { mutableStateOf("") }
    var timeOff = remember { mutableStateOf("") }
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
                Text(text = "Recording Date", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = recordingDate, fontSize = 25.sp)
                    IconButton(
                        onClick = { showRecordingDatePicker.value = true }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
            }
            item{
                textNumberBox(title = "Blood Pressure", variable = bp)
            }
            item{
                textNumberBox(title = "Heart Rate", variable = hr)
            }
            item{
                textNumberBox(title = "Weight", variable = weight)
            }
            item{
                textNumberBox(title = "Urine Output", variable = uo)
            }
            item{
                dateTimePicker(
                    showDatePicker = showTimeOnDatePicker,
                    onDateSelected = timeOn
                )
                Text(text = "Time on", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = timeOn.value, fontSize = 25.sp)
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
                    onDateSelected = timeOff
                )
                Text(text = "Time off", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = timeOff.value, fontSize = 25.sp)
                    IconButton(
                        onClick = { showTimeOffDatePicker.value = true }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                Text(text = "Heater Bag (Red)", fontSize = 30.sp)
            }
            item{
                textNumberBox(title = "1.5% Dext.(Amount)", variable = hbr15)
            }
            item{
                textNumberBox(title = "2.5% Dext.(Amount)", variable = hbr25)
            }
            item{
                textNumberBox(title = "4.25% Dext.(Amount)", variable = hbr425)
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                Text(text = "Supply Bag (White)", fontSize = 30.sp)
            }
            item{
                textNumberBox(title = "1.5% Dext.(Amount)", variable = sbw15)
            }
            item{
                textNumberBox(title = "2.5% Dext.(Amount)", variable = sbw25)
            }
            item{
                textNumberBox(title = "4.25% Dext.(Amount)", variable = sbw425)
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                Text(text = "Last Bag (Blue)", fontSize = 30.sp)
            }
            item{
                textNumberBox(title = "1.5% Dext.(Amount)", variable = lbb15)
            }
            item{
                textNumberBox(title = "2.5% Dext.(Amount)", variable = lbb25)
            }
            item{
                textNumberBox(title = "4.25% Dext.(Amount)", variable = lbb425)
            }
            item{
                textNumberBox(title = "Others", variable = lbbo)
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
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

                    ){
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
                          text = {Text(text = s)}  ,
                            onClick = {
                                therapy.value = index
                                showTherapyDropdown = false
                            }
                        )
                    }
                }
            }
            item{
                textNumberBox(title = "Total Volume", variable = totalVolume)
            }
            item{
                textNumberBox(title = "Target UF for Tidal (If applicable)", variable = targetUF, numeric = false)
            }
            item{
                textNumberBox(title = "Therapy Time (In Hours)", variable = therapyTime)
            }
            item{
                textNumberBox(title = "Fill Volume (In mL)", variable = fillVol)
            }
            item{
                textNumberBox(title = "Last Fill Volume (In mL)", variable = lastFillVol)
            }
            item{
                textNumberBox(title = "Dextrose % Conc", variable = dextCon, numeric = false)
            }
            item{
                textNumberBox(title = "No. of Cycles", variable = cycles)
            }
            item{
                textNumberBox(title = "Initial Drain", variable = initDrain)
            }
            item{
                textNumberBox(title = "Average Dwell Time", variable = avgDwellTime)
            }
            item{
                textNumberBox(title = "Color of Drainage", variable = colorDrain)
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                textNumberBox(title = "Total UF (In mL)", variable = totalUF)
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                textNumberBox(title = "Nett UF (In mL)", variable = nettUF)
            }
            item{
                Divider(modifier = Modifier.height(2.dp))
            }
            item{
                textNumberBox(title = "Remarks", variable = remarks, numeric = false)
            }
            item{
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Submit")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun dateTimePicker(
    showDatePicker: MutableState<Boolean>,
    onDateSelected: MutableState<String>
){
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    var datePicker by remember { mutableStateOf(true) }
    var timePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    if (showDatePicker.value) {
        if (datePicker){
            DatePickerDialog(
                onDismissRequest = { showDatePicker.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val selectedDateMillis = datePickerState.selectedDateMillis
                            if (selectedDateMillis != null) {
                                //onDateSelected.value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDateMillis))
                                selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDateMillis))
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
                            val selectedTimeHour = timePickerState.hour
                            val selectedTimeMin = timePickerState.minute

                            if (selectedTimeHour != null && selectedTimeMin != null) {
                                onDateSelected.value = selectedDate + " " + selectedTimeHour.toString() + ":" + selectedTimeMin

                                //selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDateMillis))
                            }
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
    numeric: Boolean = true
){
    Text(text = title, fontSize = 25.sp)
    if(numeric){
        TextField(
            value = variable.value,
            onValueChange = {newText ->
                val regex = Regex("^[0-9]*\\.?[0-9]*$")
                if (newText.isEmpty() || newText.matches(regex)) {
                    variable.value = newText
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
    }else{
        TextField(
            value = variable.value,
            onValueChange = { variable.value = it },
            modifier = Modifier.fillMaxWidth()
        )
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