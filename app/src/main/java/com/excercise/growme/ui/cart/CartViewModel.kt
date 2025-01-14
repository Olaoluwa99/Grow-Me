package com.excercise.growme.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.excercise.growme.constants.Constants
import com.excercise.growme.data.CartProduct
import com.excercise.growme.database.CartRepository
import com.excercise.growme.model.decreaseQuantity
import com.excercise.growme.model.increaseQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: CartRepository) : ViewModel() {

    private val _removeStatus = MutableStateFlow(Constants.INACTIVE)
    var removeStatus = _removeStatus.asStateFlow()

    private val _clearStatus = MutableStateFlow(Constants.INACTIVE)
    var clearStatus = _clearStatus.asStateFlow()

    private val _cartProducts = MutableStateFlow<List<CartProduct>>(emptyList())
    val cartProducts: StateFlow<List<CartProduct>> = _cartProducts

    fun fetchCartProducts() {
        viewModelScope.launch {
            try {
                val data = repository.getCartProducts()
                _cartProducts.value = data
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun removeFromCart(product: CartProduct){
        val mutableCartProduct = cartProducts.value.toMutableList()
        _removeStatus.value = Constants.LOADING
        viewModelScope.launch {
            try {
                repository.removeFromCart(product.id)
                mutableCartProduct.remove(product)
                _cartProducts.value = mutableCartProduct
                _removeStatus.value = Constants.SUCCESS
            } catch (e: Exception) {
                _removeStatus.value = Constants.FAILURE
            }
        }
    }

    fun checkout(){
        _clearStatus.value = Constants.LOADING
        viewModelScope.launch {
            try {
                repository.clearCart()
                _cartProducts.value = emptyList()
                _clearStatus.value = Constants.SUCCESS
            } catch (e: Exception) {
                println(e.stackTrace)
                println(e.message)
                _clearStatus.value = Constants.FAILURE
            }
        }
    }

    fun increaseItemQuantity(cartProduct: CartProduct){
        _cartProducts.value = increaseQuantity(cartProducts.value, cartProduct)
    }

    fun decreaseItemQuantity(cartProduct: CartProduct){
        _cartProducts.value = decreaseQuantity(cartProducts.value, cartProduct)
    }

    fun resetRemoveStatus() {
        _removeStatus.value = Constants.INACTIVE
    }

    fun resetClearStatus() {
        _clearStatus.value = Constants.INACTIVE
    }

}