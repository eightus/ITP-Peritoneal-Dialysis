package com.itp.pdbuddy.ui.screen

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.ui.viewmodel.AuthViewModel
import com.itp.pdbuddy.ui.viewmodel.NetworkViewModel
import com.itp.pdbuddy.ui.viewmodel.ProfileViewModel
import com.itp.pdbuddy.utils.Result
import com.itp.pdbuddy.utils.navigate

@Composable
fun ProfileScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val networkViewModel: NetworkViewModel = hiltViewModel()
    var base64Image by remember { mutableStateOf<String?>(null) }


    Column {

        Button(onClick = { navigate(navController, "home", requireSaveState = false) }) {
            Text("Go to Home Screen")
        }

        Button(onClick = { authViewModel.updateDisplayName("UserTest") }) {
            Text("Set Username to UserTest")
        }

    }
}