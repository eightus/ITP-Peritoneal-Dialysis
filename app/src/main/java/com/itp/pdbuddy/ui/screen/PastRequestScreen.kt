package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.TravelRequestViewModel

@Composable
fun PastRequestScreen(navController: NavHostController, viewModel: TravelRequestViewModel = hiltViewModel()) {
    val pastRequests by viewModel.pastRequests.collectAsState()

    // State to manage the selected tab
    var selectedTab by remember { mutableStateOf("Not Delivered") }
    var selectedRequest by remember { mutableStateOf<TravelRequestViewModel.PastRequest?>(null) }

    // Filter requests based on the selected tab
    val filteredRequests = pastRequests.filter { request ->
        request.status == selectedTab
    }

    LaunchedEffect(key1 = true) {
        viewModel.fetchPastRequests() // Fetch past requests for the logged-in user on initial load
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Tab Row
        TabRow(selectedTabIndex = if (selectedTab == "Delivered") 1 else 0) {
            Tab(
                selected = selectedTab == "Not Delivered",
                onClick = { selectedTab = "Not Delivered" },
                text = { Text("Not Delivered") }
            )
            Tab(
                selected = selectedTab == "Delivered",
                onClick = { selectedTab = "Delivered" },
                text = { Text("Delivered") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Past Requests",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(filteredRequests) { request ->
                PastRequestCard(request = request) {
                    selectedRequest = request
                }
            }
        }

        // Dialog to show detailed information of the selected request
        selectedRequest?.let { request ->
            PastRequestDetailsDialog(request = request) {
                selectedRequest = null // Reset selected request after dialog is dismissed
            }
        }
    }
}

@Composable
fun PastRequestCard(request: TravelRequestViewModel.PastRequest, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Order Date: ${request.orderDate}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Country: ${request.country}",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status: ${request.status}",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun PastRequestDetailsDialog(request: TravelRequestViewModel.PastRequest, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Past Request Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Order Date: ${request.orderDate}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Country: ${request.country}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Hotel Address: ${request.hotelAddress}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Travel Dates: ${request.travelDates}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Total Price: $${request.totalPrice}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Status: ${request.status}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Delivery Time: ${request.deliveryTime}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Supplies:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                request.supplyRequests.forEach { supplyRequest ->
                    Text(
                        text = "${supplyRequest.name}: ${supplyRequest.quantity}",
                        fontSize = 16.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                content = { Text(text = "Close") }
            )
        },
        modifier = Modifier.padding(16.dp)
    )
}
