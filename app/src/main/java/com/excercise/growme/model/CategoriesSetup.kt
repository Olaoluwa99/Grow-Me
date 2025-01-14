package com.excercise.growme.model

import com.excercise.growme.constants.Constants
import com.excercise.growme.data.Product

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
            Constants.MENS_CLOTHING -> definedTags.add(Constants.MENS_CLOTHING_CAP)
            Constants.WOMENS_CLOTHING -> definedTags.add(Constants.WOMENS_CLOTHING_CAP)
            Constants.JEWELERY -> definedTags.add(Constants.JEWELERY_CAP)
            Constants.ELECTRONICS -> definedTags.add(Constants.ELECTRONICS)
            else -> definedTags.add(Constants.OTHERS_CAP)
        }
    }
    return definedTags
}