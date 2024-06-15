package com.itp.pdbuddy.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.ui.theme.PDBuddyTheme

@Composable
fun HomeScreen(navController: NavController) {
    HomeScreenContent(navController = navController)
}

@Composable
fun HomeScreenContent(navController: NavController) {
    val profileItems = listOf(
        ProfileItem(Icons.Default.Inventory, "Supplies"),
        ProfileItem(Icons.Default.LocalPharmacy, "Prescription"),
        ProfileItem(Icons.Default.Flight, "Travel"),
        ProfileItem(Icons.AutoMirrored.Filled.MenuBook, "Resources"),
        ProfileItem(Icons.Default.History, "History"),
        ProfileItem(Icons.Default.Notifications, "Reminders")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8)) // Light background color
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Welcome back,\nAlaine Page",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Carousel Header
        Text(
            text = "My Profile",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Profile Items Grid with Border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
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
            content = "Next appointment: June 20, 2024, 10:00 AM"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Latest Announcement
        SectionCard(
            title = "Latest Announcement",
            content = "There will be a system maintenance on June 25, 2024."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Health Tip
        SectionCard(
            title = "Daily Health Tip",
            content = "Stay hydrated and follow your dietary guidelines for optimal health."
        )
    }
}

@Composable
fun ProfileCard(item: ProfileItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
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
fun SectionCard(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
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

data class ProfileItem(val icon: ImageVector, val label: String)

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    PDBuddyTheme {
        val mockNavController = rememberNavController()
        HomeScreenContent(navController = mockNavController)
    }
}
