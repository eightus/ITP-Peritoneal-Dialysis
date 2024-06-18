package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.CurrentSuppliesViewModel

@Composable
fun CurrentSuppliesScreen(
    navController: NavHostController,
    csviewModel: CurrentSuppliesViewModel = hiltViewModel()
) {
    val suppliesList by csviewModel.suppliesList.collectAsState()
    val selectedSupplies by csviewModel.selectedSupplies.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { values ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current Supplies",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f) // This makes the text take up remaining space
                )
                IconButton(
                    onClick = {
                        showDialog = true
                        // Handle add button click
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
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
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /* Handle cart button click */ }) {
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
            }
        )
    }
}


@Composable
fun SupplyCard(
    item: SupplyItem,
    onUpdateQuantity: (SupplyItem, Int) -> Unit,
    onDeleteSupply: (SupplyItem) -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }

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
                Text(text = item.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onDeleteSupply(item)
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
                Button(onClick = { showEditDialog = true }) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /* Handle restock functionality here */ }) {
                    Text("Restock")
                }
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
                    singleLine = true
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
                            ?: SupplyItem(name = supply)
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


data class SupplyItem(
    val name: String,
    var quantity: Int = 0,
    var checked: Boolean = false,
    val userId: String? = null
)