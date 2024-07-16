package com.itp.pdbuddy.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.UserRepository
import com.itp.pdbuddy.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class ProfileViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    private val _userData = MutableStateFlow<Result<List<Map<String, Any>>>>(Result.Idle)
    val userData: StateFlow<Result<List<Map<String, Any>>>> = _userData.asStateFlow()


    fun doTest(sample: String) {
        viewModelScope.launch {
            val test = userRepository.getUserInfo("UserTest")
            Log.d("test", test.toString())
            _userData.value = Result.Loading
            //delay(3000) // delay 3 second
            _userData.value = test
        }
    }
}
