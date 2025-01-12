package com.excercise.growme.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
){
    fun asFormattedPrice(): String{
        return "$$price"
    }
}

data class Rating(
    val rate: Double,
    val count: Int
)

fun Product.toCartProduct(): CartProduct {
    val rating = CartRating(rate = this.rating.rate, count = this.rating.count)
    return CartProduct(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description,
        category = this.category,
        image = this.image,
        rating = rating,
        quantity = 1
    )
}