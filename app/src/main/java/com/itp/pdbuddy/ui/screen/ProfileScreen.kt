import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
    var dryWeight by remember { mutableStateOf("") }

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
                    dryWeight = it["dryWeight"].toString()
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
        dryWeight = dryWeight,
        isEditing = isEditing,
        onEditButtonClick = { isEditing = !isEditing },
        onSaveButtonClick = {
            profileViewModel.updateUserInfo(
                name, address, phone, email, birthdate, gender, dryWeight.toFloatOrNull() ?: 0f
            )
            isEditing = false
        },
        onNameChange = { name = it },
        onAddressChange = { address = it },
        onPhoneChange = { phone = it },
        onEmailChange = { email = it },
        onBirthdateChange = { birthdate = it },
        onGenderChange = { gender = it },
        onDryWeightChange = { dryWeight = it }
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
        enabled = !readOnly,
        onValueChange = { newValue -> onValueChange(newValue) },
        label = label,
        readOnly = readOnly,
        colors = TextFieldDefaults.colors(
            disabledContainerColor = if (readOnly) Color.LightGray else MaterialTheme.colorScheme.surface,
            disabledIndicatorColor = Color.Transparent,
            disabledLabelColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(if (readOnly) Color.LightGray else MaterialTheme.colorScheme.surface)
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
    dryWeight: String,
    isEditing: Boolean,
    onEditButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onBirthdateChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onDryWeightChange: (String) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

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
            readOnly = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditableTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            readOnly = true
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
        EditableTextField(
            value = dryWeight,
            onValueChange = onDryWeightChange,
            label = { Text("Dry Weight") },
            readOnly = !isEditing
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (isEditing) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Button(onClick = { showDialog = true }) {
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

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Confirm Save") },
                text = { Text(text = "Are you sure you want to save the changes?") },
                confirmButton = {
                    TextButton(onClick = {
                        onSaveButtonClick()
                        Toast.makeText(context, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
                        showDialog = false
                    }) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(text = "No")
                    }
                }
            )
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
