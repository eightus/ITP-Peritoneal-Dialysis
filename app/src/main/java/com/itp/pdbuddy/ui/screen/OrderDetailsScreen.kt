package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itp.pdbuddy.R
import com.itp.pdbuddy.ui.viewmodel.Item
import com.itp.pdbuddy.ui.viewmodel.PastSuppliesViewModel

@Composable
fun OrderDetailsScreen(
    orderId: String,
    psviewModel: PastSuppliesViewModel = hiltViewModel()
) {
    val order by psviewModel.orderDetails.collectAsState()

    LaunchedEffect(orderId) {
        psviewModel.fetchOrderDetails(orderId)
    }
    // Fetch and display the details of the order using the orderId
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Details for Order ID: $orderId",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        order?.let { order ->
            LazyColumn {
                items(order.items) { item ->
                    OrderItemCard(item)
                }
            }
        } ?: run {
            Text("Loading order details...")
        }
    }
}

@Composable
fun OrderItemCard(item: Item) {
    val imageRes = supplyImageMap[normalizeName(item.name)] ?: R.drawable.splash_heart
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = item.name,
                modifier = Modifier.size(100.dp)
            )
            Column {
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Item Name: ${item.name}")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Quantity: ${item.quantity}")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Price: ${item.price}")
            }


        }
    }
}