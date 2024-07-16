package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.remote.api.DataPayload
import com.itp.pdbuddy.data.repository.AuthRepository
import com.itp.pdbuddy.data.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.itp.pdbuddy.utils.Result
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    fun sendData(token: String, onResult: (Result<Map<String, Any>>) -> Unit) {
        val data = DataPayload(token)
        viewModelScope.launch {
            val result = networkRepository.sendData(data)
            onResult(result)
        }
    }


    fun fetchGraph(graphType: String, onResult: (Result<Map<String, Any>>) -> Unit) {
        viewModelScope.launch {
            val tokenResult = authRepository.getIdToken()
            if (tokenResult is Result.Success) {
                val token = tokenResult.data
                if (token != null) {
                    val data = DataPayload(token)
                    val result = networkRepository.fetchGraph(data, graphType)
                    onResult(result)
                } else {
                    onResult(Result.Failure(Exception("Token is null")))
                }
            } else {
                onResult(Result.Failure(Exception("Failed to fetch token")))
            }
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