package com.example.market.core

import com.example.market.data.remote.Category
import com.example.market.data.remote.Product
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClient : ApiService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("API_LOG: $message")
                }
            }
            level = LogLevel.ALL
        }
    }

    private val baseUrl = "https://mockapi.example.com" // Fake API for testing

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response: HttpResponse = client.get("$baseUrl/categories")
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                println("API_ERROR: Server returned ${response.status}")
                Result.failure(Exception("Server error: ${response.status}"))
            }
        } catch (e: Exception) {
            println("API_ERROR: Request failed - ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response: HttpResponse = client.get("$baseUrl/products")
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                println("API_ERROR: Server returned ${response.status}")
                Result.failure(Exception("Server error: ${response.status}"))
            }
        } catch (e: Exception) {
            println("API_ERROR: Request failed - ${e.message}")
            Result.failure(e)
        }
    }
}
