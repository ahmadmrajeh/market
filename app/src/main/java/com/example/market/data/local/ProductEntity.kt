package com.example.market.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val image: String,
    val price: Double,
    val categoryId: String // No longer a foreign key, just a normal field
)
