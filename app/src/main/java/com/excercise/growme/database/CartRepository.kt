package com.excercise.growme.database

import com.excercise.growme.data.CartProduct
import com.excercise.growme.database.CartDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {

    suspend fun getCartProducts(): List<CartProduct> {
        return cartDao.getCartItems()
    }

    suspend fun addToCart(cartProduct: CartProduct) {
        cartDao.addToCart(cartProduct)
    }

    suspend fun removeFromCart(productId: Int) {
        cartDao.removeFromCart(productId)
    }
}
