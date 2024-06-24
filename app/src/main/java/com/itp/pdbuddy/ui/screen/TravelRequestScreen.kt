package com.itp.pdbuddy.ui.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.TravelRequestViewModel
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun TravelRequestScreen(navController: NavHostController, viewModel: TravelRequestViewModel = hiltViewModel()) {
    var country by remember { mutableStateOf("") }
    var hotelAddress by remember { mutableStateOf("") }
    var travelDates by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Mutable state to track quantity input for each selected supply
    var quantityMap by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    // Fetch supplies from ViewModel
    val supplies by viewModel.supplies.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Date picker dialog for selecting travel dates
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            travelDates = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Submit Travel Request",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("Country") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = hotelAddress,
                onValueChange = { hotelAddress = it },
                label = { Text("Hotel Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Row for calendar icon and text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable { datePickerDialog.show() }
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Select Travel Dates",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Travel Date", fontSize = 16.sp)
            }

            // Display selected travel dates
            if (travelDates.isNotEmpty()) {
                Text(
                    text = "Selected Travel Dates: $travelDates",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Text(
                text = "Supplies Needed (Please input the quantity):",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Display supplies with quantity input fields
        items(supplies.size) { index ->
            val supply = supplies[index]
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(text = supply, fontSize = 16.sp)

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = quantityMap[supply]?.toString() ?: "",
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            val newQuantity = newValue.toIntOrNull() ?: 0
                            quantityMap = quantityMap.toMutableMap().apply {
                                put(supply, newQuantity)
                            }
                            errorMessage = ""
                        } else {
                            errorMessage = "Please enter a valid number for quantity."
                        }
                    },
                    label = { Text("Quantity") },
                    modifier = Modifier
                        .width(100.dp)
                )
            }
        }

        // Display error message if any
        item {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = androidx.compose.ui.graphics.Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        // Button to submit the travel request
        item {
            Button(
                onClick = {
                    if (country.isBlank() || hotelAddress.isBlank() || travelDates.isBlank()) {
                        errorMessage = "Please fill in all fields (Country, Hotel Address, Travel Date)."
                    } else if (quantityMap.values.any { it <= 0 }) {
                        errorMessage = "All quantities must be greater than zero."
                    } else {
                        // Create a list of SupplyRequest objects from selectedSupplies
                        val supplyRequests = quantityMap.map { (name, quantity) ->
                            SupplyRequest(name = name, quantity = quantity)
                        }

                        coroutineScope.launch {
                            viewModel.submitTravelRequest(country, hotelAddress, travelDates, supplyRequests)
                            // Optionally add logic to notify user and navigate to confirmation screen
                            navController.navigateUp()
                        }
                    }
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Submit Request", fontSize = 18.sp)
            }
        }
    }
}

data class SupplyRequest(
    val name: String = "",
    val quantity: Int = 0
)

