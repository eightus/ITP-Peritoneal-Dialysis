package com.itp.pdbuddy.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.MenuBook
import androidx.compose.material.icons.twotone.Attachment
import androidx.compose.material.icons.twotone.Description
import androidx.compose.material.icons.twotone.Inventory2
import androidx.compose.material.icons.twotone.Restaurant
import androidx.compose.material.icons.twotone.School
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itp.pdbuddy.R
import com.itp.pdbuddy.data.repository.Resource

@Composable
fun ResourcesScreen(navController: NavHostController) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.TwoTone.MenuBook,
                contentDescription = "Resource Icon",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Resources",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            ResourceButton(
                text = "Diet",
                icon = Icons.TwoTone.Restaurant,
                onClick = { navController.navigate("diet") },

            )
            Spacer(modifier = Modifier.height(16.dp))
            ResourceButton(
                text = "Training",
                icon = Icons.TwoTone.School,
                onClick = { navController.navigate("training") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ResourceButton(
                text = "PD Essential Guides",
                icon = Icons.TwoTone.Description,
                onClick = { navController.navigate("additionalmaterial") }
            )

        }
    }
}


@Composable
fun ResourceButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$text Icon",
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
