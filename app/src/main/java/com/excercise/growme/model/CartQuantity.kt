package com.excercise.growme.model

import com.excercise.growme.data.CartProduct

fun increaseQuantity(cartProducts: List<CartProduct>, cartProduct: CartProduct): List<CartProduct> {
    val newCartProductList = mutableListOf<CartProduct>()
    for(item in cartProducts){
        if (item.id == cartProduct.id){
            newCartProductList.add(cartProduct.copy(quantity = cartProduct.quantity+1))
        }else newCartProductList.add(item)
    }
    return newCartProductList
}

fun decreaseQuantity(cartProducts: List<CartProduct>, cartProduct: CartProduct): List<CartProduct>{
    val newCartProductList = mutableListOf<CartProduct>()
    for(item in cartProducts){
        if (item.id == cartProduct.id){
            if (cartProduct.quantity > 1){
                newCartProductList.add(cartProduct.copy(quantity = cartProduct.quantity-1))
            }else{
                newCartProductList.add(item)
            }
        }else newCartProductList.add(item)
    }
    return newCartProductList
}