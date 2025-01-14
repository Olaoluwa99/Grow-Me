package com.excercise.growme.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.excercise.growme.constants.Constants
import com.excercise.growme.data.Product
import com.excercise.growme.database.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel@Inject constructor(private val repository: ProductRepository): ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

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

    fun refreshProducts() {
        viewModelScope.launch {
            try {
                repository.refreshProductsFromApi()
                _products.value = repository.getProducts()
            } catch (e: Exception) {
                println(e.message)
                refreshProducts()
            }
        }
    }

    fun assignProducts(products: List<Product>){
        viewModelScope.launch {
            try {
                repository.assignProducts(products)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}