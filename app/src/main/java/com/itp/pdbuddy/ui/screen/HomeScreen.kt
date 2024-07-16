package com.itp.pdbuddy.ui.screen

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.twotone.MenuBook
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.twotone.Flight
import androidx.compose.material.icons.twotone.History
import androidx.compose.material.icons.twotone.Inventory2
import androidx.compose.material.icons.twotone.LocalPharmacy
import androidx.compose.material.icons.twotone.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import com.itp.pdbuddy.ui.viewmodel.AuthViewModel
import com.itp.pdbuddy.ui.viewmodel.NetworkViewModel
import com.itp.pdbuddy.utils.Result

@Composable
fun HomeScreen(navController: NavController) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val username by authViewModel.username.collectAsState()
    LaunchedEffect(Unit) {
        authViewModel.fetchUsername()

    }
    HomeScreenContent(navController = navController, username = username)
}

@Composable
fun HomeScreenContent(navController: NavController, username: Result<String?>) {
    val profileItems = listOf(
        ProfileItem(Icons.TwoTone.Inventory2, "Supplies") {
            navController.navigate("supplies")
        },
        ProfileItem(Icons.TwoTone.LocalPharmacy, "Prescription") {
            navController.navigate("prescription")
        },
        ProfileItem(Icons.TwoTone.Flight, "Travel") {
            navController.navigate("travelMain")
        },
        ProfileItem(Icons.AutoMirrored.TwoTone.MenuBook, "Resources") {
            navController.navigate("resources")
        },
        ProfileItem(Icons.TwoTone.History, "History") {
            navController.navigate("history")
        },
        ProfileItem(Icons.TwoTone.Notifications, "Reminders") {
            navController.navigate("setNotification")
        }
    )

    val usernameText = when (username) {
        is Result.Idle -> "Fetching username..."
        is Result.Loading -> "Loading..."
        is Result.Success -> "Welcome back,\n${(username).data ?: "User"}"
        is Result.Failure -> "Failed to fetch username"
    }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background) // Light background color
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Header
        Text(
            text = usernameText,
            color = MaterialTheme.colorScheme.primary,
            lineHeight = 35.sp,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Quote of the Day
        Text(
            text = "Stay hydrated and follow your dietary guidelines for optimal health.",
            color = PDBuddyTheme.customColors.strongTextColor,
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE3F2FD), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Profile Header
        Text(
            text = "My Profile",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

        // Profile Items Grid with Border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp), // Reduce the space between rows
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in profileItems.indices step 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (j in i until (i + 3).coerceAtMost(profileItems.size)) {
                            ProfileCard(profileItems[j])
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Upcoming Appointment
        SectionCard(
            title = "Upcoming Appointment",
            content = "Next appointment: June 20, 2024, 10:00 AM",
            icon = Icons.Default.CalendarToday // Replace with an appropriate icon
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Latest Announcement
        SectionCard(
            title = "Latest Announcement",
            content = "There will be a system maintenance on June 25, 2024.",
            icon = Icons.AutoMirrored.Filled.Announcement // Replace with an appropriate icon
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Graph
        HealthGraphScreen()
    }
}

@Composable
fun ProfileCard(item: ProfileItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                item.click()
            }
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            modifier = Modifier
                .size(64.dp)
                .border(
                    2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(32.dp)
                )
                .padding(8.dp),
            tint = MaterialTheme.colorScheme.secondary,
        )
        Text(
            text = item.label,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SectionCard(title: String, content: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HealthGraphScreen() {
    val graphTypes = listOf("Weight", "Heart Rate", "Blood Pressure", "Trend Analysis")
    val pagerState = rememberPagerState(pageCount = { graphTypes.size })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            HealthGraph(graphType = graphTypes[page])
        }
    }
}

@Composable
fun HealthGraph(graphType: String) {
    val networkViewModel: NetworkViewModel = hiltViewModel()
    var base64Image by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false)  }
    fun fetchGraph() {
        isLoading = true
        isError = false
        networkViewModel.fetchGraph(graphType) { result ->
            when (result) {
                is Result.Success -> {
                    base64Image = result.data["image"] as? String
                    isLoading = false
                }

                is Result.Failure -> {
                    isLoading = false
                    isError = true
                }

                is Result.Loading -> {
                    isLoading = true
                    isError = false
                }

                is Result.Idle -> {
                    isLoading = false
                    isError = false
                }
            }
        }
    }

    LaunchedEffect(graphType) {
        fetchGraph()
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            isError -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Failed to load $graphType graph",
                        color = Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(onClick = { fetchGraph() }) {
                        Text("Retry")
                    }
                }
            }

            base64Image != null -> {
                val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$graphType Graph",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "$graphType Graph",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
                            .clickable { showDialog = true }
                    )
                }
                if (showDialog) {
                    Dialog(onDismissRequest = { showDialog = false }) {
                        ZoomableImage(bitmap = bitmap)
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ZoomableImage(bitmap: android.graphics.Bitmap) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
            .clipToBounds(),
        contentAlignment = Alignment.Center,
    ) {
        val state = rememberTransformableState { zoomChange, panChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            val maxX = (constraints.maxWidth * scale - constraints.maxWidth) / 2
            val maxY = (constraints.maxHeight * scale - constraints.maxHeight) / 2
            offset = Offset(
                x = (offset.x + panChange.x * scale).coerceIn(-maxX, maxX),
                y = (offset.y + panChange.y * scale).coerceIn(-maxY, maxY)
            )
        }

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y,
                )
                .transformable(state)
                .fillMaxWidth()
        )
    }
}

data class ProfileItem(val icon: ImageVector, val label: String, val click: () -> Unit)

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    PDBuddyTheme {
        val mockNavController = rememberNavController()
        HomeScreenContent(navController = mockNavController, username = Result.Success("TestUser"))
    }
}
