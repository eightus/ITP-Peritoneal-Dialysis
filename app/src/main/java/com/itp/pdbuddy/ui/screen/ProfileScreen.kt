import android.annotation.SuppressLint
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
    val userData by profileViewModel.userData.collectAsState()

    // MutableState variables to hold editable data
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    // State to manage edit mode
    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(userData) {
        when (userData) {
            is Result.Success -> {
                val user = (userData as Result.Success<List<Map<String, Any>>>).data.firstOrNull()
                user?.let {
                    name = it["username"] as? String ?: ""
                    address = it["address"] as? String ?: ""
                    phone = it["phone"] as? String ?: ""
                    email = it["email"] as? String ?: ""
                    birthdate = it["birthdate"] as? String ?: ""
                    gender = it["gender"] as? String ?: ""
                }
            }
            else -> {
                // Handle loading, error, or idle states if needed
                // For simplicity, nothing is rendered here for other states
            }
        }
    }

    ProfileContent(
        name = name,
        address = address,
        phone = phone,
        email = email,
        birthdate = birthdate,
        gender = gender,
        isEditing = isEditing,
        onEditButtonClick = { isEditing = !isEditing },
        onSaveButtonClick = {
            profileViewModel.updateUserInfo(name, address, phone, email, birthdate, gender)
            isEditing = false
        },
        onNameChange = { name = it },
        onAddressChange = { address = it },
        onPhoneChange = { phone = it },
        onEmailChange = { email = it },
        onBirthdateChange = { birthdate = it },
        onGenderChange = { gender = it }
    )
}

@Composable
fun EditableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    readOnly: Boolean
) {
    TextField(
        value = value,
        onValueChange = { newValue -> onValueChange(newValue) },
        label = label,
        readOnly = readOnly
    )
}


@Composable
fun ProfileContent(
    name: String,
    address: String,
    phone: String,
    email: String,
    birthdate: String,
    gender: String,
    isEditing: Boolean,
    onEditButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onBirthdateChange: (String) -> Unit,
    onGenderChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfilePicture(imageRes = R.drawable.login_image)
        Spacer(modifier = Modifier.height(16.dp))
        EditableTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            readOnly = !isEditing
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditableTextField(
            value = address,
            onValueChange = onAddressChange,
            label = { Text("Address") },
            readOnly = !isEditing
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditableTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = { Text("Phone") },
            readOnly = !isEditing
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditableTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            readOnly = !isEditing
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditableTextField(
            value = birthdate,
            onValueChange = onBirthdateChange,
            label = { Text("Birthdate") },
            readOnly = !isEditing
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditableTextField(
            value = gender,
            onValueChange = onGenderChange,
            label = { Text("Gender") },
            readOnly = !isEditing
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (isEditing) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Button(onClick = onSaveButtonClick) {
                    Text(text = "Save")
                }
                Button(onClick = onEditButtonClick) {
                    Text(text = "Cancel")
                }
            }
        } else {
            Button(onClick = onEditButtonClick) {
                Text(text = "Edit")
            }
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
