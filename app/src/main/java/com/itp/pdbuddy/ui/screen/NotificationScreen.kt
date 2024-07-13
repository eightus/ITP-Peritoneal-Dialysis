package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.itp.pdbuddy.service.NotificationService
import com.itp.pdbuddy.ui.viewmodel.NotificationViewModel

@Composable
fun NotificationScreen(navController: NavController) {
    NotificationScreenContent(navController = navController)
}

@Composable
fun NotificationScreenContent(navController: NavController){
    val context = LocalContext.current
    val notificationService = NotificationService()
    val notificationViewModel: NotificationViewModel = hiltViewModel()

    // States

    val showTimePicker = remember { mutableStateOf(false) }
    val notifications by notificationViewModel.allItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Notifications",
                fontSize = 50.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    //showTimePicker.value = true
                    navController.navigate("newNotification")}
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
        LazyColumn( verticalArrangement = Arrangement.spacedBy(10.dp)){
            items(notifications){notification ->
                val showConfirmDeleteState = remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier.fillMaxWidth()
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        if (!showConfirmDeleteState.value){
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ){
                                Text(notification.time)
                                Text(notification.date)
                                Text("${notification.type} ${notification.medication} ${notification.quantity}${notification.unit}" )
                            }
                            IconButton(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                onClick = {
                                    //notificationViewModel.deleteNotification(notification.id)
                                    showConfirmDeleteState.value = true
                                }
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete")
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ){
                                Text("")
                                Text("Confirm delete?")
                                Text("")
                            }
                            Row(
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ){
                                IconButton(
                                    onClick = {
                                        notificationViewModel.deleteNotification(notification.id)
                                        showConfirmDeleteState.value = false
                                    }

                                ) {
                                    Icon(Icons.Filled.Check, contentDescription = "Delete", tint = Color.Green)
                                }
                                IconButton(
                                    onClick = {showConfirmDeleteState.value = false }
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = "Delete", tint = Color.Red)
                                }

                            }

                        }

                    }

                }

            }
        }

    }
}

