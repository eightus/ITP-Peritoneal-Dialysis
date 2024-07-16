package com.itp.pdbuddy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.UserRepository
import com.itp.pdbuddy.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _userData = MutableStateFlow<Result<List<Map<String, Any>>>>(Result.Idle)
    val userData: StateFlow<Result<List<Map<String, Any>>>> = _userData.asStateFlow()

    init {
        doTest("UserTest")
    }

    fun doTest(name: String) {
        viewModelScope.launch {
            try {
                _userData.value = Result.Loading
                val result = userRepository.getUserInfo(name)
                _userData.value = result
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching user info", e)
                _userData.value = Result.Failure(e)
            }
        }
    }

    fun updateUserInfo(
        name: String,
        address: String,
        phone: String,
        email: String,
        birthdate: String,
        gender: String
    ) {
        viewModelScope.launch {
            try {
                val result = userRepository.updateUserInfo(name, address, phone, email, birthdate, gender)
                // Optionally, you can refresh the user data after updating
                if (result is Result.Success) {
                    doTest(name)
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating user info", e)
                // Handle error state if needed
            }
        }
    }
}
