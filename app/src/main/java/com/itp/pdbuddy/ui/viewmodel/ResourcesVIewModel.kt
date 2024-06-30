package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.Resource
import com.itp.pdbuddy.data.repository.ResourcesRepository
import com.itp.pdbuddy.data.repository.SuppliesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResourcesVIewModel @Inject constructor(
    private val resourcesRepository: ResourcesRepository
) : ViewModel() {

    private val _resources = MutableStateFlow<List<Resource>>(emptyList())
    val resources: StateFlow<List<Resource>> = _resources

    init {
        fetchResources()
    }

    private fun fetchResources() {
        viewModelScope.launch {
            val resourceList = resourcesRepository.getResources()
            _resources.value = resourceList
        }
    }

}

