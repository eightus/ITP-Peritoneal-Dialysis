package com.itp.pdbuddy.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Inventory2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentSuppliesScreen(navController: NavHostController) {

    var showDialog by remember { mutableStateOf(false) }
    val suppliesList = listOf(
        "Dialysis Solution Bags", "Transfer Sets", "Catheters", "Catheter Dressing Kits",
        "Sterile Gloves", "Antibacterial Solutions", "Peritoneal Dialysis Drainage Bags",
        "Clamps", "Tape and Dressings", "Face Masks", "Waste Disposal Bags",
        "Syringes", "Measuring Containers", "Emergency Kit"
    )
    val selectedSupplies = remember { mutableStateListOf<String>() }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
    ) { values ->
        Column (
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
            // Add your content here
        }
    }
    if (showDialog) {
        AddSuppliesDialog(
            suppliesList = suppliesList,
            selectedSupplies = selectedSupplies,
            onDismissRequest = { showDialog = false },
            onConfirm = {
                // Handle the selected items (Add button logic to be implemented)
                showDialog = false
            }
        )
    }
}

@Composable
fun AddSuppliesDialog(
    suppliesList: List<String>,
    selectedSupplies: MutableList<String>,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Add Supplies") },
        text = {
            Column {
                suppliesList.forEach { supply ->
                    val isSelected = selectedSupplies.contains(supply)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                if (isSelected) {
                                    selectedSupplies.remove(supply)
                                } else {
                                    selectedSupplies.add(supply)
                                }
                            }
                        )
                        Text(text = supply, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm
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