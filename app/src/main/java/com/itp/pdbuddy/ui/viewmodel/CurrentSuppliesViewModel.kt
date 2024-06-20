package com.itp.pdbuddy.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
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
                val updatedList = _selectedSupplies.value.toMutableList()
                val index = updatedList.indexOfFirst { it.name == supplyItem.name }
                if (index != -1) {
                    updatedList[index] = supplyItem.copy(quantity = newQuantity)
                    _selectedSupplies.value = updatedList
                }
            }
        }
    }

    fun deleteSupplyFromFirestore(supplyItem: SupplyItem) {
        viewModelScope.launch {
            suppliesRepository.deleteSupplyFromFirestore(supplyItem) {
                val updatedList = _selectedSupplies.value.toMutableList()
                updatedList.remove(supplyItem)
                _selectedSupplies.value = updatedList
            }
        }
    }

    fun addToCart(supplyItem: SupplyItem) {
        viewModelScope.launch {
            val itemPrice = suppliesRepository.getItemPrice(supplyItem.name)

            Log.d(TAG, "Price for ${supplyItem.name} before adding to cart: $itemPrice")
            // Update local state with the new cart item
            val updatedCartList = _cartItems.value.toMutableList()
            updatedCartList.add(supplyItem.copy(price = itemPrice))
            _cartItems.value = updatedCartList

            suppliesRepository.addToCart(supplyItem.copy(price = itemPrice))
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

    fun placeOrder(cartItems: List<SupplyItem>) {
        viewModelScope.launch {
            suppliesRepository.placeOrder(cartItems)
            // Optionally clear cart items in local state after order is placed
            _cartItems.value = emptyList()
        }
    }

//    fun updateCartItemQuantity(supplyItem: SupplyItem, newQuantity: Int) {
//        viewModelScope.launch {
//            suppliesRepository.updateCartItemQuantity(supplyItem, newQuantity) {
//                // Update local state with the new quantity
//                val updatedList = _cartItems.value.toMutableList()
//                val index = updatedList.indexOfFirst { it.name == supplyItem.name }
//                if (index != -1) {
//                    val updatedItem = updatedList[index].copy(quantity = newQuantity)
//                    updatedList[index] = updatedItem
//                    _cartItems.value = updatedList
//                }
//            }
//        }
//    }

}