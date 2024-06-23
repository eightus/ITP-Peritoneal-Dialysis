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
class CartSuppliesViewModel @Inject constructor(
    private val suppliesRepository: SuppliesRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<SupplyItem>>(emptyList())
    val cartItems: StateFlow<List<SupplyItem>> = _cartItems

    init {
        fetchCartItems()
    }

    private fun fetchCartItems() {
        viewModelScope.launch {
            val cartItemsFromRepo = suppliesRepository.fetchCartItems()
            _cartItems.value = cartItemsFromRepo
        }
    }

    fun removeFromCart(supplyItem: SupplyItem) {
        viewModelScope.launch {
            suppliesRepository.removeFromCart(supplyItem) {
                // Update local state by removing the item from _cartItems
                val updatedList = _cartItems.value.toMutableList()
                updatedList.remove(supplyItem)
                _cartItems.value = updatedList
            }
        }
    }


}

