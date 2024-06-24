package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ContentPaste
import androidx.compose.material.icons.twotone.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.TravelViewModel
import androidx.compose.material3.Icon

@Composable
fun TravelScreen(navController: NavHostController, viewModel: TravelViewModel = hiltViewModel()) {
    var searchText by remember { mutableStateOf("") }
    var countries by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(key1 = true) {
        countries = viewModel.getCountries()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Enable vertical scrolling
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            // Icon
            Icon(
                imageVector = Icons.TwoTone.ContentPaste,
                contentDescription = "Form Icon",
                modifier = Modifier.size(64.dp)
            )

            // Button to request supplies form
            Button(
                onClick = {
                    navController.navigate("travelRequest")
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Request Supplies Form", fontSize = 18.sp)
            }
        }

        // Text above search bar
        Text(
            text = "Find Contacts of overseas suppliers here",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Search Bar
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search countries") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(horizontal = 32.dp)
        )

        // Display filtered countries
        countries.filter {
            it.contains(searchText, ignoreCase = true)
        }.forEach { country ->
            Button(
                onClick = { navController.navigate("travelDetails/$country") },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text(text = country, fontSize = 18.sp)
            }
        }
    }
}
