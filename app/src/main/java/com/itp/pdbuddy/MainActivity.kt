package com.itp.pdbuddy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.navigation.AppNavigation
import com.itp.pdbuddy.ui.screen.LoginScreen
import com.itp.pdbuddy.ui.screen.SplashScreen
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import com.itp.pdbuddy.utils.PermissionHelper
import com.itp.pdbuddy.utils.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissionHelper = PermissionHelper(this)
        permissionHelper.askNotificationPermission()

        createNotificationChannel()

        setContent {
            val navController = rememberNavController()
            PDBuddyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(navController = navController)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        val channelId = "fcm_default_channel"
        val channelName = "PDBuddy Channel"
        val channelDescription = "Default channel for app notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        val notificationManager = ContextCompat.getSystemService(this, NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

}




@Composable
fun MainScreen(navController: NavHostController) {
    val currentRoute by navController.currentBackStackEntryAsState()
    val appbarTitle = "PDBuddy"

    when (currentRoute?.destination?.route) {
        "register" -> {
            ScaffoldWithTopBar(
                appbarTitle = appbarTitle,
                navController = navController,
                showBottomBar = false
            )
        }

        "splash" -> {
            SplashScreen(navController = navController)
        }

        "login" -> {
            LoginScreen(navController = navController)
        }

        else -> {
            ScaffoldWithTopBar(
                appbarTitle = appbarTitle,
                navController = navController,
                showBottomBar = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTopBar(
    appbarTitle: String,
    navController: NavHostController,
    showBottomBar: Boolean
) {
    val canNavigateBack = navController.previousBackStackEntry != null
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(appbarTitle) },
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )

                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
            )
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = {
                navigate(navController, "home", true)
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.TextSnippet, contentDescription = "Record") },
            label = { Text("Record") },
            selected = false,
            onClick = {
                navigate(navController, "record", true)
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = {
                navigate(navController, "profile", true)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PDBuddyTheme {
        MainScreen(rememberNavController())
    }
}


