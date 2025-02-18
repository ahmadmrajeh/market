package com.example.market.data.remote

data class Product(
  val id: String,
    val name: String,
    val description: String?,
    val image: String,
    val price: Double,
    val categoryId: String
)

