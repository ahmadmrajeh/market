package com.example.market.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.market.data.local.CartEntity
import com.example.market.data.local.CategoryEntity
import com.example.market.data.local.ProductEntity

@Database(entities = [CategoryEntity::class, ProductEntity::class, CartEntity::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
