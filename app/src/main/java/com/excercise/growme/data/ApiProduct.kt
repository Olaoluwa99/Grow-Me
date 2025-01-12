package com.excercise.growme.data

import com.google.gson.annotations.SerializedName

data class ApiProducts (
    @SerializedName("id"          ) var id          : Int?    = null,
    @SerializedName("title"       ) var title       : String? = null,
    @SerializedName("price"       ) var price       : Double? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("category"    ) var category    : String? = null,
    @SerializedName("image"       ) var image       : String? = null,
    @SerializedName("rating"      ) var rating      : ApiRating? = ApiRating()
)

data class ApiRating (
    @SerializedName("rate"  ) var rate  : Double? = null,
    @SerializedName("count" ) var count : Int?    = null
)

fun ApiProducts.toProduct(): Product {
    val rating = Rating(rate = this.rating?.rate ?: 0.0, count = this.rating?.count ?: 0)

    return Product(
        id = this.id ?: 99,
        title = this.title ?: "",
        price = this.price ?: 0.0,
        description = this.description ?: "",
        category = this.category ?: "",
        image = this.image ?: "",
        rating = rating
    )
}