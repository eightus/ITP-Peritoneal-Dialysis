package com.itp.pdbuddy.ui.screen

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Inventory2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itp.pdbuddy.ui.viewmodel.CurrentSuppliesViewModel
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentSuppliesScreen(navController: NavHostController,
                          csviewModel: CurrentSuppliesViewModel = hiltViewModel()) {


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
                .padding(values),

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
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(selectedSupplies) { item ->
                    SupplyCard(item = item, onUpdateQuantity = { supplyItem, newQuantity ->
                        csviewModel.updateSupplyQuantity(supplyItem, newQuantity)
                    })
                }
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
fun SupplyCard(item: SupplyItem,
               onUpdateQuantity: (SupplyItem, Int) -> Unit) {
    var showRestockDialog by remember { mutableStateOf(false) }

    if (showRestockDialog) {
        UpdateQuantityDialog(
            item = item,
            onDismissRequest = { showRestockDialog = false },
            onConfirm = { newQuantity ->
                onUpdateQuantity(item, newQuantity)
                showRestockDialog = false
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
            Text(text = item.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Quantity: ${item.quantity}", fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { }) {
                Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {showRestockDialog = true }) {
                    Text("Restock")
                }
            }
        }
    }
}


@Composable
fun UpdateQuantityDialog(
    item: SupplyItem,
    onDismissRequest: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf(item.quantity.toString()) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Restock Item") },
        text = {
            Column {
                Text(text = "Enter new quantity for ${item.name}:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
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