package com.excercise.growme

import com.excercise.growme.constants.Constants
import com.excercise.growme.data.Product
import com.excercise.growme.data.Rating
import com.excercise.growme.model.setupProductsCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ProductCategoryTest {

    private val product1 = Product(1, "Product A", 212.1, "This is description 1", Constants.MENS_CLOTHING, "www.google.com", Rating(4.2, 200))
    private val product2 = Product(2, "Product B", 37.5, "This is description 2", Constants.WOMENS_CLOTHING, "www.google.com", Rating(2.9, 42))
    private val product3 = Product(3, "Product C", 407.5, "This is description 3", Constants.JEWELERY, "www.google.com", Rating(5.0, 35))
    private val product4 = Product(4, "Product O", 42.5, "This is description 4", "None", "www.google.com", Rating(4.0, 11))


    @Test
    fun `test setupProductsCategory filters products by a specific category`() {
        val products = listOf(product1, product2, product3)
        val filteredProducts = setupProductsCategory(products, Constants.MENS_CLOTHING)
        assertEquals(1, filteredProducts.size)
        assertEquals(Constants.MENS_CLOTHING, filteredProducts[0].category)
    }

    @Test
    fun `test setupProductsCategory returns empty list for unmatched tag`() {
        val products = listOf(product1, product2, product3)
        val filteredProducts = setupProductsCategory(products, "NON_EXISTENT_CATEGORY")
        assertEquals(0, filteredProducts.size)
    }

    @Test
    fun `test setupProductsCategory filters products as OTHERS`() {
        val products = listOf(product1, product2, product3, product4)

        val filteredProducts = setupProductsCategory(products, Constants.OTHERS)
        assertEquals(1, filteredProducts.size)
        assertNotEquals(Constants.MENS_CLOTHING, filteredProducts[0].category)
        assertNotEquals(Constants.WOMENS_CLOTHING, filteredProducts[0].category)
        assertNotEquals(Constants.JEWELERY, filteredProducts[0].category)
        assertNotEquals(Constants.ELECTRONICS, filteredProducts[0].category)
    }

    @Test
    fun `test setupProductsCategory returns empty list when no OTHERS products`() {
        val products = listOf(product1, product2, product3)
        val filteredProducts = setupProductsCategory(products, Constants.OTHERS)
        assertEquals(0, filteredProducts.size)
    }

    @Test
    fun `test setupProductsCategory handles empty product list`() {
        val products = emptyList<Product>()

        val filteredProducts = setupProductsCategory(products, Constants.MENS_CLOTHING)
        assertEquals(0, filteredProducts.size)
    }
}
