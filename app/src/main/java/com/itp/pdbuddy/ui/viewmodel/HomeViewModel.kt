package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.itp.pdbuddy.utils.Result
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _announcement = MutableStateFlow<Result<String>>(Result.Idle)
    val announcement: StateFlow<Result<String>> = _announcement.asStateFlow()

    private val _qotd = MutableStateFlow<Result<String>>(Result.Idle)
    val qotd: StateFlow<Result<String>> = _qotd.asStateFlow()

    fun getQOTD() {
        _qotd.value = Result.Loading
        viewModelScope.launch {
            _qotd.value = userRepository.getQOTD()
        }
    }

    fun getAnnouncement() {
        _announcement.value = Result.Loading
        viewModelScope.launch {
            _announcement.value = userRepository.getAnnouncement()
        }
    }

}