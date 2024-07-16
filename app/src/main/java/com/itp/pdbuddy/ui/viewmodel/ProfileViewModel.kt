package com.itp.pdbuddy.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.AuthRepository
import com.itp.pdbuddy.data.repository.UserRepository
import com.itp.pdbuddy.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _userData = MutableStateFlow<Result<List<Map<String, Any>>>>(Result.Idle)
    val userData: StateFlow<Result<List<Map<String, Any>>>> = _userData.asStateFlow()

    init {
        loadInfo()
    }

    fun loadInfo() {
        _userData.value = Result.Loading
        viewModelScope.launch {
            try {
                when (val result = authRepository.getUsername()) {
                    is Result.Success -> {
                        if (result.data != null) {
                            _userData.value = userRepository.getUserInfo(result.data)
                        } else {
                            _userData.value = Result.Failure(Exception("Not logged in"))
                        }
                    }

                    is Result.Failure -> {
                        _userData.value = result
                    }

                    else -> {

                    }
                }
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
                authRepository.updateDisplayName(name)
                // Optionally, you can refresh the user data after updating
                if (result is Result.Success) {
                    loadInfo()
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating user info", e)
                // Handle error state if needed
            }
        }
    }
}
