package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.remote.network.DataPayload
import com.itp.pdbuddy.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.itp.pdbuddy.utils.Result
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    fun sendData(title: String, body: String, userId: Int, onResult: (Result<Map<String, Any>>) -> Unit) {
        val data = DataPayload(title, body, userId)
        viewModelScope.launch {
            val result = networkRepository.sendData(data)
            onResult(result)
        }
    }
}


// USAGE: (Inside xxxScreen.kt)
//val networkViewModel: NetworkViewModel = hiltViewModel()
//networkViewModel.sendData("title", "body", 1) { result ->
//    when (result) {
//        is Result.Success -> Log.d("MainActivity", "Success: ${result.data}")
//        is Result.Failure -> Log.d("MainActivity", "Failure: ${result.exception}")
//        is Result.Loading -> Log.d("MainActivity", "Loading")
//        is Result.Idle -> Log.d("MainActivity", "Idle")
//    }
//}