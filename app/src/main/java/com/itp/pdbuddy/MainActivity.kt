package com.itp.pdbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.navigation.AppNavigation
import com.itp.pdbuddy.navigation.NavItem
import com.itp.pdbuddy.navigation.NavigationConfig.navItems
import com.itp.pdbuddy.ui.screen.HomeScreen
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import com.itp.pdbuddy.ui.theme.getScreenWidth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
}

@Composable
fun MainScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val items = navItems
    val appbarTitle = "PDBuddy"

    LaunchedEffect(currentRoute) {
        drawerState.close()
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(getScreenWidth() * 2 / 3)
            ) {
                Text(text = "SIT Mobile", color = Color.Blue, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                DrawerBody(items, navController)
            }
        },
        gesturesEnabled = true // swipe to open
    ) {
        ScaffoldWithTopBar(
            appbarTitle = appbarTitle,
            navController = navController,
            drawerState = drawerState,
            scope = scope
        )
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTopBar(appbarTitle: String, navController: NavHostController, drawerState: DrawerState, scope: CoroutineScope, showBurger: Boolean = true) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(appbarTitle) },
                navigationIcon =  {
                    if (showBurger){
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply { if (isClosed) open() else close() }
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
            )
        },
    ) { innerPadding ->
        // Screen content
        AppNavigation(navController = navController, modifier = Modifier.padding(innerPadding) )
    }
}

@Composable
fun DrawerBody(items: List<NavItem>, navController: NavController) {
    items.forEach { item ->
        NavigationDrawerItem(
            label = { Text(text = item.title) },
            icon = { item.icon?.let { Icon(imageVector = it, contentDescription = item.title) } },
            selected = false,
            onClick = {
                navController.navigate(item.route)
            }
        )
        HorizontalDivider()
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PDBuddyTheme {
        MainScreen(rememberNavController())
    }
}