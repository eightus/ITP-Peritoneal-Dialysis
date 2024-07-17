package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.R
import com.itp.pdbuddy.ui.viewmodel.CurrentSuppliesViewModel
import android.widget.Toast
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun CurrentSuppliesScreen(
    navController: NavHostController,
    csviewModel: CurrentSuppliesViewModel = hiltViewModel()
) {
    val suppliesList by csviewModel.suppliesList.collectAsState()
    val selectedSupplies by csviewModel.selectedSupplies.collectAsState()
    val cartItems by csviewModel.cartItems.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                    text = "Current Supplies",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Add New Supplies")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(selectedSupplies) { item ->
                    SupplyCard(
                        item = item,
                        onUpdateQuantity = { supplyItem, newQuantity ->
                            csviewModel.updateSupplyQuantity(supplyItem, newQuantity)
                        },
                        onDeleteSupply = { supplyItem ->
                            csviewModel.deleteSupplyFromFirestore(supplyItem)
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                        },
                        onRestock = { supplyItem, quantity ->
                            csviewModel.addToCart(supplyItem.copy(quantity = quantity))
                            Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("CartSupplies") }) {
                Text("Cart")
            }
        }
    }
    if (showDialog) {
        AddSuppliesDialog(
            suppliesList = suppliesList,
            selectedSupplies = selectedSupplies,
            onDismissRequest = { showDialog = false },
            onConfirm = { newSelectedSupplies ->
                csviewModel.addSuppliesToFirestore(newSelectedSupplies)
                showDialog = false
                Toast.makeText(context, "New supplies added", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

fun normalizeName(name: String): String {
    return name.replace(" ", "").lowercase()
}

val supplyImageMap = mapOf(
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

@Composable
fun SupplyCard(
    item: SupplyItem,
    onUpdateQuantity: (SupplyItem, Int) -> Unit,
    onDeleteSupply: (SupplyItem) -> Unit,
    onRestock: (SupplyItem, Int) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showRestockDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (showEditDialog) {
        UpdateQuantityDialog(
            item = item,
            initialQuantity = item.quantity,
            onDismissRequest = { showEditDialog = false },
            onConfirm = { newQuantity ->
                onUpdateQuantity(item.copy(quantity = newQuantity), newQuantity)
                showEditDialog = false
            }
        )
    }

    if (showRestockDialog) {
        RestockDialog(
            onDismissRequest = { showRestockDialog = false },
            onConfirm = { quantity ->
                onRestock(item, quantity)
                showRestockDialog = false
                Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show()
            }
        )
    }

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
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = item.name,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Quantity: ${item.quantity}", fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onDeleteSupply(item) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { showEditDialog = true }) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { showRestockDialog = true }) {
                    Text("Restock")
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun UpdateQuantityDialog(
    item: SupplyItem,
    initialQuantity: Int,
    onDismissRequest: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf(initialQuantity.toString()) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Edit Quantity") },
        text = {
            Column {
                Text(text = "Enter new quantity for ${item.name}:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newQuantity = quantity.toIntOrNull()
                    if (newQuantity != null) {
                        onConfirm(newQuantity)
                    }
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddSuppliesDialog(
    suppliesList: List<String>,
    selectedSupplies: List<SupplyItem>,
    onDismissRequest: () -> Unit,
    onConfirm: (List<SupplyItem>) -> Unit
) {
    val uncheckedSuppliesList = suppliesList.filter { supply ->
        selectedSupplies.none { it.name == supply && it.checked }
    }
    val tempSelectedSupplies = remember { mutableStateListOf<SupplyItem>() }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Add Supplies") },
        text = {
            Box(modifier = Modifier.heightIn(max = 400.dp)) {
                LazyColumn {
                    items(uncheckedSuppliesList) { supply ->
                        val item = tempSelectedSupplies.find { it.name == supply }
                            ?: SupplyItem(
                                name = supply,
                            )
                        val isChecked = remember { mutableStateOf(item.checked) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = isChecked.value,
                                onCheckedChange = {
                                    isChecked.value = it
                                    item.checked = it
                                    if (it) {
                                        if (!tempSelectedSupplies.contains(item)) {
                                            tempSelectedSupplies.add(item.copy(checked = true))
                                        }
                                    } else {
                                        tempSelectedSupplies.removeIf { it.name == supply }
                                    }
                                }
                            )
                            Text(text = supply, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newSelectedSupplies = tempSelectedSupplies.filter { it.checked }.toList()
                    onConfirm(newSelectedSupplies)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RestockDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var restockQuantity by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Restock Quantity") },
        text = {
            Column {
                Text(text = "Enter quantity to restock:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = restockQuantity,
                    onValueChange = { restockQuantity = it },
                    label = { Text("Quantity") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val quantity = restockQuantity.toIntOrNull() ?: 0
                    onConfirm(quantity)
                }
            ) {
                Text("Add to Cart")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    )
}

data class SupplyItem(
    val name: String,
    var quantity: Int = 0,
    var checked: Boolean = false,
    var price: Double = 0.0,
    val userId: String? = null
)


