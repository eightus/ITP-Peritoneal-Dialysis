package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.itp.pdbuddy.data.repository.SuppliesRepository
import com.itp.pdbuddy.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.itp.pdbuddy.ui.screen.SupplyItem

@HiltViewModel
class CurrentSuppliesViewModel @Inject constructor
    (private val suppliesRepository: SuppliesRepository) : ViewModel() {

    private val _suppliesList = MutableStateFlow<List<String>>(emptyList())
    val suppliesList: StateFlow<List<String>> = _suppliesList

    private val _selectedSupplies = MutableStateFlow<List<SupplyItem>>(emptyList())
    val selectedSupplies: StateFlow<List<SupplyItem>> = _selectedSupplies

    init {
        fetchSupplies()
        fetchUserSupplies()
    }

    private fun fetchSupplies() {
        viewModelScope.launch {
            _suppliesList.value = suppliesRepository.fetchSuppliesList()
        }
    }

    private fun fetchUserSupplies() {
        viewModelScope.launch {
            val userSupplies = suppliesRepository.fetchUserSupplies()
            _selectedSupplies.value = userSupplies
        }
    }

    fun updateSupplyQuantity(item: SupplyItem, newQuantity: Int) {
        suppliesRepository.updateSupplyQuantityInFirestore(item, newQuantity) {
            val updatedList = _selectedSupplies.value.toMutableList()
            val index = updatedList.indexOfFirst { it.name == item.name }
            if (index != -1) {
                updatedList[index] = item.copy(quantity = newQuantity)
                _selectedSupplies.value = updatedList
            }
        }
    }

    fun addSuppliesToFirestore(supplies: List<SupplyItem>) {
        suppliesRepository.addSuppliesToFirestore(supplies)

        val updatedList = _selectedSupplies.value.toMutableList()
        updatedList.addAll(supplies)
        _selectedSupplies.value = updatedList
    }
}
