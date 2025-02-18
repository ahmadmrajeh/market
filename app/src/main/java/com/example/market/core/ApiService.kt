package com.example.market.core

import com.example.market.data.remote.Category
import com.example.market.data.remote.Product

interface ApiService {
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getProducts(): Result<List<Product>>
}
