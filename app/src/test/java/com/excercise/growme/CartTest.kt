package com.excercise.growme

import com.excercise.growme.data.CartProduct
import com.excercise.growme.data.CartRating
import com.excercise.growme.model.decreaseQuantity
import com.excercise.growme.model.increaseQuantity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CartTest {
    private lateinit var _cartProducts: MutableList<CartProduct>
    private val product1 = CartProduct(1, "Product A", 212.1, "This is description 1", "men's clothing", "www.google.com", CartRating(4.2, 200), 2)
    private val product2 = CartProduct(2, "Product B", 37.5, "This is description 2", "women's clothing", "www.google.com", CartRating(2.9, 42), 1)


    @Before
    fun setup() {
        _cartProducts = mutableListOf(product1, product2)
    }

    @Test
    fun `test increaseItemQuantity increases quantity by 1`() {
        _cartProducts = increaseQuantity(_cartProducts, product1).toMutableList()
        assertEquals(3, _cartProducts.find { it.id == 1 }?.quantity)
    }

    @Test
    fun `test increaseItemQuantity does not change other products`() {
        _cartProducts = increaseQuantity(_cartProducts, product1).toMutableList()
        assertEquals(1, _cartProducts.find { it.id == 2 }?.quantity)
    }

    @Test
    fun `test decreaseItemQuantity decreases quantity by 1 if greater than 1`() {
        _cartProducts = decreaseQuantity(_cartProducts, product1).toMutableList()
        assertEquals(1, _cartProducts.find { it.id == 1 }?.quantity)
    }

    @Test
    fun `test decreaseItemQuantity does not change quantity if it is 1`() {
        _cartProducts = decreaseQuantity(_cartProducts, product2).toMutableList()
        assertEquals(1, _cartProducts.find { it.id == 2 }?.quantity)
    }

    @Test
    fun `test non-matching products remain unchanged`() {
        _cartProducts = increaseQuantity(_cartProducts, product1).toMutableList()
        assertEquals(1, _cartProducts.find { it.id == 2 }?.quantity)
    }

    @Test
    fun `test empty cart remains empty`() {
        _cartProducts.clear()
        _cartProducts = increaseQuantity(_cartProducts, product1).toMutableList()
        assertEquals(0, _cartProducts.size)
    }
}