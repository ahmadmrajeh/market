package com.example.market.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val productId: String,
    val name: String,
    val description: String,
    val image: String,
    val price: Double,
    val categoryId: String,
    val quantity: Int
)