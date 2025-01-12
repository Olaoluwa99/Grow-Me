package com.excercise.growme.di

import com.excercise.growme.data.ApiProducts
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<ApiProducts>
}