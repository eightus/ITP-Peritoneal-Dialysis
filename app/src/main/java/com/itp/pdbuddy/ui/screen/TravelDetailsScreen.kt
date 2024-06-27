package com.itp.pdbuddy.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itp.pdbuddy.ui.viewmodel.TravelDetailsViewModel

@Composable
fun TravelDetailsScreen(navController: NavHostController, country: String) {
    val viewModel: TravelDetailsViewModel = hiltViewModel()
    val clinics by viewModel.clinics.collectAsState()

    // Ensure this is called inside the Composable function to avoid side-effects
    viewModel.loadClinics(country)

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Clinics in $country",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(clinics) { clinic ->
                    ClinicCard(clinic = clinic)
                }
            }
        }
    }
}

@Composable
fun ClinicCard(clinic: Clinic) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = clinic.clinicName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = clinic.clinicAddress, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = clinic.clinicContactNumber, fontSize = 16.sp)
        }
    }
}

data class Clinic(
    val clinicName: String = "",
    val clinicAddress: String = "",
    val clinicContactNumber: String = ""
)
