package com.itp.pdbuddy.ui.History

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.itp.pdbuddy.ui.theme.PDBuddyTheme

@Composable
fun HistoryScreen(navController: NavHostController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "History",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HistoryItem(
                title = "Peritoneal Dialysis 1",
                date = "Tuesday, 12 December 2023",
                time = "Time on: 15:20\nTime off: 16:00"
            )
            Spacer(modifier = Modifier.height(8.dp))
            HistoryItem(
                title = "Peritoneal Dialysis 2",
                date = "Tuesday, 23 December 2023",
                time = "Time on: 15:20\nTime off: 16:00"
            )
        }

    }

@Composable
fun HistoryItem(title: String, date: String, time: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /*TODO*/ }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text(text = date, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
            Text(text = time, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    PDBuddyTheme {
        val mockNavController = rememberNavController()
        HistoryScreen(mockNavController)
    }
}