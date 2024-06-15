package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.itp.pdbuddy.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel()  {

    private val _loginResult = MutableStateFlow<Result<Boolean>>(Result.Idle)
    val loginResult: StateFlow<Result<Boolean>> = _loginResult.asStateFlow()

    private val _username = MutableStateFlow<Result<String?>>(Result.Idle)
    val username: StateFlow<Result<String?>> = _username.asStateFlow()

    fun login(email: String, password: String) {
        _loginResult.value = Result.Loading
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginResult.value = result
        }
    }

    fun updateDisplayName(displayName: String) {
        viewModelScope.launch {
            val result = repository.updateDisplayName(displayName)
            if (result is Result.Success) {
                _username.value = repository.getUsername()
            }
        }
    }

    fun fetchUsername() {
        viewModelScope.launch {
            _username.value = repository.getUsername()
        }
    }
}