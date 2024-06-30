package com.itp.pdbuddy.ui.screen

import android.webkit.WebView
import android.webkit.WebViewClient
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
fun TrainingScreen(navController: NavHostController, resourcesViewModel: ResourcesVIewModel = hiltViewModel()) {
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
                text = "Training Resources",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text (
                text = "This training resource provides clear guidance on peritoneal dialysis (PD), " +
                        "explaining its procedure" + " for patients. It aims to improve understanding through simple explanations and visuals," +
                        " supporting better patient outcomes and quality of life.",
                fontSize = 20.sp,
                color = PDBuddyTheme.customColors.strongTextColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            resources.filter { it.type == "training" }.forEach { resource ->
                Button(
                    onClick = { selectedResource = resource },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = resource.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
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
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun YouTubePlayer(
    youtubeVideoId: String,
    lifecycleOwner: LifecycleOwner
){
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        factory = { context->
            YouTubePlayerView(context = context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(object: AbstractYouTubePlayerListener(){
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(youtubeVideoId, 0f)
                    }
                })
            }
        }
    )
}