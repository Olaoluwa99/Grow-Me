package com.excercise.growme.database

import androidx.room.*
import com.excercise.growme.data.CartProduct

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    suspend fun getCartItems(): List<CartProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartItem: CartProduct)

    @Query("DELETE FROM cart WHERE id = :cartItemId")
    suspend fun removeFromCart(cartItemId: Int)

    @Query("DELETE FROM cart")
    suspend fun clearCart()
}