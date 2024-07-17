package com.itp.pdbuddy.ui.screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.R
import com.itp.pdbuddy.ui.viewmodel.TravelRequestViewModel
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun TravelRequestScreen(navController: NavHostController, viewModel: TravelRequestViewModel = hiltViewModel()) {
    var country by remember { mutableStateOf("") }
    var hotelAddress by remember { mutableStateOf("") }
    var travelStartDate by remember { mutableStateOf("") }
    var travelEndDate by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val orderDate = remember {
        "${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)}/${Calendar.getInstance().get(Calendar.MONTH) + 1}/${Calendar.getInstance().get(Calendar.YEAR)}"
    }

    var quantityMap by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    val supplies by viewModel.supplies.collectAsState()
    val username by viewModel.username.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialogStart = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val formattedMonth = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
                val formattedDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                travelStartDate = "$formattedDayOfMonth/${formattedMonth}/${year}"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val datePickerDialogEnd = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val formattedMonth = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
                val formattedDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                travelEndDate = "$formattedDayOfMonth/${formattedMonth}/${year}"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val totalPrice by remember(supplies, quantityMap) {
        derivedStateOf {
            supplies.sumOf { supply ->
                val quantity = quantityMap[supply.name] ?: 0
                quantity * supply.price
            }
        }
    }

    val supplyImageMap = remember {
        mapOf(
            "antibacterialsolutions" to R.drawable.antibacterialsolutions,
            "catheterdressingkits" to R.drawable.catheterdressingkits,
            "catheters" to R.drawable.catheters,
            "clamps" to R.drawable.clamps,
            "dialysissolutionbags" to R.drawable.dialysissolutionbags,
            "emergencykit" to R.drawable.emergencykit,
            "facemasks" to R.drawable.facemasks,
            "measuringcontainers" to R.drawable.measuringcontainers,
            "peritonealdialysisdrainagebags" to R.drawable.peritonealdialysisdrainagebags,
            "sterilegloves" to R.drawable.sterilegloves,
            "syringes" to R.drawable.syringes,
            "tapeanddressings" to R.drawable.tapeanddressings,
            "transfersets" to R.drawable.transfersets,
            "wastedisposalbags" to R.drawable.wastedisposalbags,
            "apdmachine" to R.drawable.apdmachine
        )
    }

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
                    .padding(bottom = 8.dp),
            )

            OutlinedTextField(
                value = hotelAddress,
                onValueChange = { hotelAddress = it },
                label = { Text("Hotel Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable { datePickerDialogStart.show() }
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Select Travel Start Date",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Start Date", fontSize = 16.sp)
            }

            if (travelStartDate.isNotEmpty()) {
                Text(
                    text = "Selected Start Date: $travelStartDate",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable { datePickerDialogEnd.show() }
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Select Travel End Date",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("End Date", fontSize = 16.sp)
            }

            if (travelEndDate.isNotEmpty()) {
                Text(
                    text = "Selected End Date: $travelEndDate",
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

        items(supplies.size) { index ->
            val supply = supplies[index]
            val imageResource = supplyImageMap[supply.name.replace(" ", "").lowercase()] ?: R.drawable.splash_heart // Use placeholder if image is not found

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = supply.name,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = supply.name, fontSize = 16.sp)
                    Text(text = "$${supply.price}", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = quantityMap[supply.name]?.toString() ?: "",
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            val newQuantity = newValue.toIntOrNull() ?: 0
                            quantityMap = quantityMap.toMutableMap().apply {
                                put(supply.name, newQuantity)
                            }
                            errorMessage = ""
                        } else {
                            errorMessage = "Please enter a valid number for quantity."
                        }
                    },
                    label = { Text("Quantity") },
                    modifier = Modifier
                        .width(100.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
        }

        item {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        item {
            Text(
                text = "Total Price: $${totalPrice}",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    if (country.isNotEmpty() && hotelAddress.isNotEmpty() && travelStartDate.isNotEmpty() && travelEndDate.isNotEmpty()) {
                        coroutineScope.launch {
                            viewModel.submitTravelRequest(
                                country = country,
                                hotelAddress = hotelAddress,
                                travelStartDate = travelStartDate,
                                travelEndDate = travelEndDate,
                                supplyRequests = supplies.map { supply ->
                                    supply.copy(quantity = quantityMap[supply.name] ?: 0)
                                },
                                totalPrice = totalPrice,
                                orderDate = orderDate
                            )
                            navController.navigate("SuccessPaymentTravelScreen")
                        }
                    } else {
                        errorMessage = "Please fill in all fields."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Pay")
            }
        }
    }
}

data class SupplyRequest(
    val name: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
)


