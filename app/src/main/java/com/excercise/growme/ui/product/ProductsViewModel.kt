package com.excercise.growme.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.excercise.growme.constants.Constants
import com.excercise.growme.data.CartProduct
import com.excercise.growme.data.Product
import com.excercise.growme.data.toCartProduct
import com.excercise.growme.database.CartRepository
import com.excercise.growme.database.ProductRepository
import com.excercise.growme.model.setupProductsCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel@Inject constructor(private val repository: ProductRepository, private val cartRepository: CartRepository): ViewModel() {

    private val _addToCartStatus = MutableStateFlow(Constants.INACTIVE)
    var addToCartStatus = _addToCartStatus.asStateFlow()

    private val _categoryProducts = MutableStateFlow<List<Product>>(emptyList())
    val categoryProducts: StateFlow<List<Product>> = _categoryProducts

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _cartProducts = MutableStateFlow<List<CartProduct>>(emptyList())
    val cartProducts: StateFlow<List<CartProduct>> = _cartProducts

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val data = repository.getProducts()
                _products.value = data
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun fetchCartProducts() {
        viewModelScope.launch {
            try {
                val data = cartRepository.getCartProducts()
                _cartProducts.value = data
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun refreshProducts() {
        viewModelScope.launch {
            try {
                repository.refreshProductsFromApi()
                _products.value = repository.getProducts()
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun setupProductsForCategory(tag: String){
        _categoryProducts.value = setupProductsCategory(products.value, tag)
    }

    fun addToCart(product: Product){
        _addToCartStatus.value = Constants.LOADING
        if (cartProducts.value.contains(product.toCartProduct())){
            _addToCartStatus.value = Constants.DUPLICATE
        }else{
            val mutableCartProduct: MutableList<CartProduct> = cartProducts.value.toMutableList()
            viewModelScope.launch {
                try {
                    cartRepository.addToCart(product.toCartProduct())
                    mutableCartProduct.add(product.toCartProduct())
                    _cartProducts.value = mutableCartProduct
                    _addToCartStatus.value = Constants.SUCCESS
                } catch (e: Exception) {
                    _addToCartStatus.value = Constants.FAILURE
                }
            }
        }
    }

    fun resetCartStatus(){
        _addToCartStatus.value = Constants.INACTIVE
    }
}