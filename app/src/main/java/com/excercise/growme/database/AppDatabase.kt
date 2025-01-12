package com.excercise.growme.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.excercise.growme.data.CartProduct
import com.excercise.growme.data.Product

@Database(entities = [Product::class, CartProduct::class], version = 1, exportSchema = false)
@TypeConverters(ProductConverter::class, CartConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
}
