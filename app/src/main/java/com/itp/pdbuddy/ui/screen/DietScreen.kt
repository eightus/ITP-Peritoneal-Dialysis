package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itp.pdbuddy.R
import com.itp.pdbuddy.data.repository.Resource
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import com.itp.pdbuddy.ui.viewmodel.ResourcesVIewModel

@Composable
fun DietScreen(
    navController: NavHostController,
    resourcesViewModel: ResourcesVIewModel = hiltViewModel()) {

    val resources by resourcesViewModel.resources.collectAsState()
    var selectedResource by remember { mutableStateOf<Resource?>(null) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Diet Resources",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text (
                text = "For individuals undergoing peritoneal dialysis, " +
                        "it's crucial to maintain a balanced diet to manage electrolyte levels and " +
                        "minimize waste buildup. Here are some dietary recommendations",
                fontSize = 20.sp,
                color = PDBuddyTheme.customColors.strongTextColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            resources.filter { it.type == "diet" }.forEach { resource ->
                //val imageRes2 = resourcesImageMap[normalizeTitle(resource.title)] ?: R.drawable.splash_heart
                Button(
                    onClick = { selectedResource = resource },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = resource.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (selectedResource != null) {
                val selectedImageRes = resourcesImageMap[normalizeTitle(selectedResource!!.title)] ?: R.drawable.splash_heart

                AlertDialog(
                    onDismissRequest = { selectedResource = null },
                    confirmButton = {
                        TextButton(onClick = { selectedResource = null }) {
                            Text("Close")
                        }
                    },
                    title = {
                        Text(text = selectedResource?.title ?: "")
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = selectedImageRes),
                                contentDescription = selectedResource?.title,
                                modifier = Modifier.size(180.dp) // Adjust size as needed
                            )
                            splitContent(selectedResource?.content ?: "").forEach { section ->
                                Text(
                                    text = section,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

fun splitContent(content: String): List<String> {
    return content.split(". ").map { it.trim() + ".\n" }
}

fun normalizeTitle(title: String): String {
    return title.replace(" ", "").lowercase()
}

val resourcesImageMap = mapOf (
    "fluidrestriction" to R.drawable.waterintake,
    "sodiumrestriction" to R.drawable.eatlesssalt,
    "potassiumbalance" to R.drawable.potassium,
    "limitphosphorusintake" to R.drawable.phosphorus
)



