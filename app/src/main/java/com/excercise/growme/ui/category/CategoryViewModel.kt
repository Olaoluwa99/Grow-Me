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

    private val _updateStatus = MutableStateFlow(Constants.INACTIVE)
    var updateStatus = _updateStatus.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        fetchProducts()
        refreshProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                val data = repository.getProducts()
                _products.value = data
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    private fun refreshProducts() {
        viewModelScope.launch {
            try {
                repository.refreshProductsFromApi()
                _products.value = repository.getProducts()
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    fun assignProducts(products: List<Product>){
        viewModelScope.launch {
            try {
                repository.assignProducts(products)
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    fun getCategoryTags(productsList: List<Product>): List<String> {
        val categoryTags = mutableListOf<String>()
        for (item in productsList) {
            if (item.category !in categoryTags) {
                categoryTags.add(item.category)
            }
        }
        return categoryTags
    }

    fun setDefinedTags(tagList: List<String>): List<String> {
        val definedTags = mutableListOf<String>()

        for (item in tagList){
            when (item) {
                "men's clothing" -> definedTags.add("Men's clothing")
                "women's clothing" -> definedTags.add("Women's clothing")
                "jewelery" -> definedTags.add("Jewelery")
                "electronics" -> definedTags.add("Electronics")
                else -> definedTags.add("Others")
            }
        }
        return definedTags
    }

}