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
    var quantity: Int
){
    fun asFormattedPrice(): String{
        return "$$price"
    }

    fun asTotalPrice(): String{
        return "Total = $${price * quantity}"
    }

    fun asFormattedQuantity(): String{
        return "  $quantity  "
    }
}

data class CartRating(
    val rate: Double,
    val count: Int
)