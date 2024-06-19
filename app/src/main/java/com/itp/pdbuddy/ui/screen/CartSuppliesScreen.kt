package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.twotone.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.MainScreen
import com.itp.pdbuddy.R
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import com.itp.pdbuddy.ui.viewmodel.CartSuppliesViewModel
import com.itp.pdbuddy.ui.viewmodel.CurrentSuppliesViewModel

@Composable
fun CartSuppliesScreen(
    navController: NavHostController,
    csviewModel: CurrentSuppliesViewModel = hiltViewModel()) {

    val cartItems by csviewModel.cartItems.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cart",
                    fontSize = 24.sp,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (cartItems.isEmpty()) {
                Text(text = "No items in the cart")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(item = item,
                            onRemoveCart = { supplyItem ->
                                csviewModel.removeFromCart(supplyItem)
                            },
                            /* edit cart function */
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                val totalAmt = cartItems.sumOf { it.price }
                Text("Total Amount: $${totalAmt}")
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    navController.navigate("payment")
                    csviewModel.placeOrder(cartItems = csviewModel.cartItems.value)}) {
                    Text("Pay")
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: SupplyItem,
    onRemoveCart: (SupplyItem) -> Unit) {
    val imageRes = supplyImageMap[normalizeName(item.name)] ?: R.drawable.splash_heart
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
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
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                       onRemoveCart(item)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Quantity: ${item.quantity}", fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.weight(1f))
                Text(text = "Total Price: $${item.price}", fontSize = 16.sp)
            }



        }
    }
}

