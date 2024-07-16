import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itp.pdbuddy.R
import com.itp.pdbuddy.ui.viewmodel.ProfileViewModel
import com.itp.pdbuddy.utils.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("RememberReturnType")
fun ProfileScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val userdata by profileViewModel.userData.collectAsState()
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        profileViewModel.doTest("UserTest")
    }

    when (userdata) {
        is Result.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Result.Success -> {
            val user = (userdata as Result.Success<List<Map<String, Any>>>).data.firstOrNull()
            user?.let {
                name = it["username"] as? String ?: ""
                address = it["address"] as? String ?: ""
                phone = it["phone"] as? String ?: ""
                email = it["email"] as? String ?: ""
                birthdate = it["birthdate"] as? String ?: ""
                gender = it["gender"] as? String ?: ""
            }
        }
        is Result.Failure -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Failed to load user data")
            }
        }
        Result.Idle -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Idle state")
            }
        }
    }

    val userProfile = UserProfile(
        name = name,
        bio = "Android Developer. Tech Enthusiast. Coffee Lover.",
        profileImage = R.drawable.login_image
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") }
            )
        }
    ) { paddingValues ->
        ProfileContent(
            name = userProfile.name,
            address = address,
            phone = phone,
            email = email,
            birthdate = birthdate,
            gender = gender,
            paddingValues = paddingValues
        )
    }
}

@Composable
fun ProfileContent(
    name: String,
    address: String,
    phone: String,
    email: String,
    birthdate: String,
    gender: String,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfilePicture(imageRes = R.drawable.login_image)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = address,
            onValueChange = {},
            label = { Text("Address") },
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = phone,
            onValueChange = {},
            label = { Text("Phone") },
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = {},
            label = { Text("Email") },
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = birthdate,
            onValueChange = {},
            label = { Text("Birthdate") },
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = gender,
            onValueChange = {},
            label = { Text("Gender") },
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* Handle message action */ }) {
            Text(text = "Edit")
        }
    }
}

@Composable
fun ProfilePicture(imageRes: Int) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.LightGray, CircleShape)
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

data class UserProfile(
    val name: String,
    val bio: String,
    val profileImage: Int
)