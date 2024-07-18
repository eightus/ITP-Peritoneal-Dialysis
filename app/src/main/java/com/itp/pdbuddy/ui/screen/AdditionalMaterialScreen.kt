package com.itp.pdbuddy.ui.screen

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.itp.pdbuddy.data.repository.Resource
import com.itp.pdbuddy.ui.theme.PDBuddyTheme
import com.itp.pdbuddy.ui.viewmodel.ResourcesVIewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun AdditionalMaterialScreen(navController: NavHostController, resourcesViewModel: ResourcesVIewModel = hiltViewModel()) {
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
                text = "PD Essential Guides",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text (
                text = "PD Essential Guides covers various topics such as how to shower while on peritoneal " +
                        "dialysis, choosing the right bag to use, and an education video to learn more.",
                fontSize = 18.sp,
                color = PDBuddyTheme.customColors.strongTextColor
            )
            Spacer(modifier = Modifier.height(25.dp))
            resources.filter { it.type == "additionalmaterial" }.forEach { resource ->
                Button(
                    onClick = { selectedResource = resource },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth()
                        .height(80.dp)
                ) {
                    Text(
                        text = resource.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 21.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (selectedResource != null) {
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
                        Column {
                            Text(text = selectedResource?.content ?: "")
                            selectedResource?.videoUrl?.let { youtubeVideoId ->
                                Spacer(modifier = Modifier.height(16.dp))
                                YouTubePlayer(youtubeVideoId = youtubeVideoId,
                                    lifecycleOwner = LocalLifecycleOwner.current
                                )
                            }
                            selectedResource?.websiteUrl?.let { websiteUrl ->
                                Spacer(modifier = Modifier.height(16.dp))
                                ClickableLink(text = "Click to view the link.", url = websiteUrl)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ClickableLink(text: String, url: String) {
    val context = LocalContext.current
    Text(
        text = text,
        modifier = Modifier.clickable {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    )
}

