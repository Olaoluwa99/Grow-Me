package com.excercise.growme.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: CartRating,
    val quantity: Int
)

data class CartRating(
    val rate: Double,
    val count: Int
)