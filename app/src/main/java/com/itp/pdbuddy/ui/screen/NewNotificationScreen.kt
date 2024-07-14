package com.itp.pdbuddy.ui.screen

import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.itp.pdbuddy.ui.viewmodel.NotificationViewModel
import com.itp.pdbuddy.ui.viewmodel.RecordViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun NewNotificationScreen(navController: NavController) {
    NewNotificationScreenContent(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNotificationScreenContent(navController: NavController){
    val notificationViewModel: NotificationViewModel = hiltViewModel()

    // States
    var showMedicationDropdown by remember { mutableStateOf(false) }

    // Time picker
    val calendarState = remember { mutableStateOf(Calendar.getInstance()) }
    val timeState = remember{ mutableStateOf(false)}
    val showTimePicker = remember { mutableStateOf(false) }
    val dateSelected = remember { mutableStateOf(false)}
    var timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    // Date picker
    val dateState = remember{ mutableStateOf(false)}
    val showDatePicker = remember { mutableStateOf(false) }
    var dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Dropdown menu
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val reminderType = arrayOf("Medication", "Appointment")// Can put in seperate file for better expansion
    var selectedText by remember { mutableStateOf(reminderType[0]) }

    // Input Variables
    val medicine = remember { mutableStateOf("")}
    val quantity = remember { mutableStateOf("")}
    val medicineType = arrayOf("" ,"Phosphate binders", "Iron", "Heparin", "Antibiotics")
    val unitType = arrayOf("", "mL", " Pills/Tablet")
    val unit = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            timePicker(showTimePicker, calendarState, timeState)
            Text(
                text = "New Notifications",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(text = "Time", fontSize = 25.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                if(timeState.value){
                    Text(text = timeFormat.format(calendarState.value.time), fontSize = 25.sp)
                }

                IconButton(
                    onClick = { showTimePicker.value = true }
                ) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                }
            }
            Text(text = "Notification Type", fontSize = 25.sp)
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    reminderType.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(text = item) },
                            onClick = {
                                selectedText = item
                                expanded = false
                                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                            },

                            )
                    }
                }

            }
            if(selectedText == reminderType[1]){ //Appointment
                datePicker(showDatePicker, calendarState, dateState)
                Text(text = "Date", fontSize = 25.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(dateFormat.format(calendarState.value.time), fontSize = 25.sp)

                    IconButton(
                        onClick = {
                            showDatePicker.value = true }
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calender")
                    }
                }
            } else if(selectedText == reminderType[0]){
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .clickable { showMedicationDropdown = true }
                            .fillMaxWidth(0.5F)
//                            .fillMaxWidth()
                        //            .clickable { showDropdown = !showDropdown },
                    ){
                        OutlinedTextField(
                            value = medicine.value,
                            onValueChange = { },
                            label = { Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text("Medication")
                            } },
                            modifier = Modifier
                                //.fillMaxWidth()
                                .onFocusEvent { showMedicationDropdown = it.isFocused },
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
                                if (showMedicationDropdown){
                                    Icon(Icons.Filled.ArrowDropUp,"contentDescription")
                                } else {
                                    Icon(Icons.Filled.ArrowDropDown,"contentDescription")
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = showMedicationDropdown,
                            onDismissRequest = { showMedicationDropdown = false },
                            //modifier = Modifier.fillMaxWidth()
                        ) {
                            medicineType.forEach { item ->
                                DropdownMenuItem(text = { Text(text = item) }, onClick = {
                                    medicine.value = item
                                    if (medicine.value == medicineType[3]){
                                        unit.value = 1
                                    } else {
                                       unit.value = 2
                                    }
                                    showMedicationDropdown = false
                                })
                            }
                        }
                    }

                    OutlinedTextField(
                        value = quantity.value,
                        onValueChange = {newText ->
                            val regex = Regex("^[0-9]*\\.?[0-9]*$")
                            if (newText.isEmpty() || newText.matches(regex)) {
                                quantity.value = newText
                            }
                        },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text("Qty")
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(0.5f),
                        singleLine = true
                    )
                    if (medicine.value == medicineType[2]){
                        Text(text = unitType[unit.value])
                    } else {
                        Text(text = unitType[unit.value])
                    }
                }
                Text("* You are not to change your medication regime. Should there be problems, immediately inform you doctor ")
            }

        }
        Button(
            onClick = {
                if (!timeState.value){
                    Toast.makeText(context, "Time not Selected", Toast.LENGTH_LONG).show()

                } else if (medicine.value.isEmpty() && selectedText == reminderType[0]) {
                    Toast.makeText(context, "Type of Medication not selected", Toast.LENGTH_LONG).show()
                }else if (quantity.value.isEmpty() && selectedText == reminderType[0]) {
                    Toast.makeText(context, "Quantity of Medication not filled", Toast.LENGTH_LONG).show()
                } else {
                    notificationViewModel.createNotification(calendarState, selectedText, medicine.value, quantity.value, unitType[unit.value])
                    navController.navigate("setNotification")
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text("Create Notification")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun timePicker(
    showTimePicker: MutableState<Boolean>,
    cal: MutableState<Calendar>,
    onTimeSelected: MutableState<Boolean>
){
    val timePickerState = rememberTimePickerState()
    var timePicker by remember { mutableStateOf(true) }
    if (showTimePicker.value) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val updatedCal = (cal.value.clone() as Calendar).apply {
                            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(Calendar.MINUTE, timePickerState.minute)
                            set(Calendar.SECOND, 0)
                        }
                        cal.value = updatedCal
                        //onTimeSelected.value = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.time)
                        //onTimeSelected.value = cal.value.time
                        onTimeSelected.value = true
                        timePicker = false
                        showTimePicker.value = false
                    },
                    ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker.value = false }) {
                    Text(text = "Dismiss")
                }
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datePicker(
    showDatePicker: MutableState<Boolean>,
    cal: MutableState<Calendar>,
    onTimeSelected: MutableState<Boolean>
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
                        onTimeSelected.value = true
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

// PastOrPresentSelectableDates.kt
@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates: SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year
    }
}