package com.excercise.growme.model

import com.excercise.growme.constants.Constants
import com.excercise.growme.data.Product

fun setupProductsCategory(products: List<Product>, tag: String): List<Product>{
    val list = mutableListOf<Product>()
    if (tag != Constants.OTHERS){
        for (item in products){
            if (item.category == tag){
                list.add(item)
            }
        }
    }else{
        for (item in products){
            if (item.category != Constants.MENS_CLOTHING && item.category != Constants.WOMENS_CLOTHING && item.category != Constants.JEWELERY && item.category != Constants.ELECTRONICS){
                list.add(item)
            }
        }
    }
    return list
}