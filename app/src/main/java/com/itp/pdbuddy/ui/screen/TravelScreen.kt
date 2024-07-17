package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.TravelViewModel

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
        Icon(
            imageVector = Icons.Filled.ContactMail,
            contentDescription = "Contact Us",
            modifier = Modifier.size(64.dp).padding(bottom = 16.dp)
        )
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

        // Search Button
        Button(
            onClick = {
                val countryToSearch = searchText.trim()
                val matchingCountry = countries.find { it.equals(countryToSearch, ignoreCase = true) }
                if (matchingCountry != null) {
                    navController.navigate("travelDetails/$matchingCountry")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(top = 8.dp)
        ) {
            Text(text = "Search")
        }
    }
}

