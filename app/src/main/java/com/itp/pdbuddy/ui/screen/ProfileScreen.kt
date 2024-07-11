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

    LaunchedEffect(Unit) {
        networkViewModel.fetchGraph { result ->
            when (result) {
                is Result.Success -> {
                    Log.d("MainActivity", "Success: ${result.data.values}")
                    base64Image = result.data["image"] as? String
                }

                is Result.Failure -> Log.d("MainActivity", "Failure: ${result.exception}")
                is Result.Loading -> Log.d("MainActivity", "Loading")
                is Result.Idle -> Log.d("MainActivity", "Idle")
            }
        }
    }

    Column {

        Button(onClick = { navigate(navController, "home", requireSaveState = false) }) {
            Text("Go to Home Screen")
        }

        Button(onClick = { authViewModel.updateDisplayName("UserTest") }) {
            Text("Set Username to UserTest")
        }

        base64Image?.let { base64Str ->
            val imageBytes = Base64.decode(base64Str, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Test Image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
            )
        }

    }
}