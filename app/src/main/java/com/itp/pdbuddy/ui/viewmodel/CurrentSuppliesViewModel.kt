package com.itp.pdbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itp.pdbuddy.data.repository.SuppliesRepository
import com.itp.pdbuddy.ui.screen.SupplyItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentSuppliesViewModel @Inject constructor(
    private val suppliesRepository: SuppliesRepository
) : ViewModel() {

    private val _suppliesList = MutableStateFlow<List<String>>(emptyList())
    val suppliesList: StateFlow<List<String>> = _suppliesList

    private val _selectedSupplies = MutableStateFlow<List<SupplyItem>>(emptyList())
    val selectedSupplies: StateFlow<List<SupplyItem>> = _selectedSupplies

    private val _cartItems = MutableStateFlow<List<SupplyItem>>(emptyList())
    val cartItems: StateFlow<List<SupplyItem>> = _cartItems

    init {
        fetchSupplies()
        fetchUserSupplies()
        fetchCartItems() // Fetch cart items initially
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

    private fun fetchCartItems() {
        viewModelScope.launch {
            val cartItems = suppliesRepository.fetchCartItems()
            _cartItems.value = cartItems
        }
    }

    fun addSuppliesToFirestore(supplies: List<SupplyItem>) {
        viewModelScope.launch {
            suppliesRepository.addSuppliesToFirestore(supplies)
            // Update the local state with the new supplies
            val updatedList = _selectedSupplies.value.toMutableList()
            updatedList.addAll(supplies)
            _selectedSupplies.value = updatedList
        }
    }

    fun updateSupplyQuantity(supplyItem: SupplyItem, newQuantity: Int) {
        viewModelScope.launch {
            suppliesRepository.updateSupplyQuantityInFirestore(supplyItem, newQuantity) {
                // Update local state after successful update if needed
            }
        }
    }

    fun deleteSupplyFromFirestore(supplyItem: SupplyItem) {
        viewModelScope.launch {
            suppliesRepository.deleteSupplyFromFirestore(supplyItem) {
                // Update local state after successful deletion if needed
            }
        }
    }

    fun addToCart(supplyItem: SupplyItem) {
        viewModelScope.launch {
            suppliesRepository.addToCart(supplyItem)
            // Update local state with the new cart item
            val updatedCartList = _cartItems.value.toMutableList()
            updatedCartList.add(supplyItem)
            _cartItems.value = updatedCartList
        }
    }

    fun removeFromCart(supplyItem: SupplyItem) {
        viewModelScope.launch {
            suppliesRepository.removeFromCart(supplyItem) {
                // Update local state after successful removal if needed
                val updatedCartList = _cartItems.value.toMutableList()
                updatedCartList.remove(supplyItem)
                _cartItems.value = updatedCartList
            }
        }
    }
}