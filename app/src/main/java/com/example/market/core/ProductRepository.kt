package com.example.market.core

import com.example.market.data.local.CartEntity
import com.example.market.data.local.CategoryEntity
import com.example.market.data.local.ProductEntity
import com.example.market.data.local.room.ProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ProductRepository(
    private val apiService: ApiService,
    private val productDao: ProductDao
) {

    fun fetchCategories(): Flow<List<CategoryEntity>> = flow {
        val localCategories = productDao.getCategories()

        if (localCategories.isNotEmpty()) {
            emit(localCategories) // ✅ Use local DB first
        } else {
            apiService.getCategories().onSuccess { categories ->
                val categoryEntities = categories.map { CategoryEntity(it.id, it.name) }
                productDao.insertCategories(categoryEntities)
                emit(productDao.getCategories()) // ✅ Refresh UI from DB
            }.onFailure {
                println("API_ERROR: Failed to fetch categories - ${it.message}")
                emit(productDao.getCategories()) // ✅ Fallback to DB
            }
        }
    }.catch { e ->
        println("DB_ERROR: ${e.message}")
        emit(emptyList()) // Prevents app crashes
    }

    fun fetchProducts(): Flow<List<ProductEntity>> = flow {
        val localProducts = productDao.getAllProducts()

        if (localProducts.isNotEmpty()) {
            emit(localProducts) // ✅ Use local DB first
        } else {
            apiService.getProducts().onSuccess { products ->
                val productEntities = products.map {
                    ProductEntity(it.id, it.name, it.description.toString(), it.image, it.price, it.categoryId)
                }
                productDao.insertProducts(productEntities)
                emit(productDao.getAllProducts()) // ✅ Refresh UI from DB
            }.onFailure {
                println("API_ERROR: Failed to fetch products - ${it.message}")
                emit(productDao.getAllProducts()) // ✅ Fallback to DB
            }
        }
    }.catch { e ->
        println("DB_ERROR: ${e.message}")
        emit(emptyList()) // Prevents app crashes
    }

    fun searchProducts(query: String): Flow<List<ProductEntity>> = flow {
        emit(productDao.searchProducts(query))
    }.catch { e ->
        println("DB_ERROR: Search failed: ${e.message}")
        emit(emptyList()) // Prevents crashes
    }

    suspend fun insertMockData() {
        val dummyCategories = listOf(
            CategoryEntity("1", "Electronics"),
            CategoryEntity("2", "Groceries"),
            CategoryEntity("3", "Clothing")
        )

        val dummyProducts = listOf(
            ProductEntity("101", "Laptop", "High-end gaming laptop", "https://example.com/laptop.png", 1200.0, "1"),
            ProductEntity("102", "Smartphone", "Latest model smartphone", "https://example.com/phone.png", 800.0, "1"),
            ProductEntity("103", "dell", "High-end gaming laptop", "https://example.com/laptop.png", 1200.0, "1"),
            ProductEntity("104", "LG", "Latest model smartphone", "https://example.com/phone.png", 800.0, "1"),
            ProductEntity("201", "Apples", "Fresh organic apples", "https://example.com/apples.png", 5.0, "2"),
            ProductEntity("202", "Milk", "Dairy milk 1L", "https://example.com/milk.png", 2.0, "2"),
            ProductEntity("301", "T-Shirt", "Cotton T-Shirt", "https://example.com/tshirt.png", 15.0, "3")
        )

        productDao.insertCategories(dummyCategories)
        productDao.insertProducts(dummyProducts)
    }

    // ✅ Added functions for fetching data from local DB
    suspend fun getAllCategories(): List<CategoryEntity> = productDao.getCategories()
    suspend fun getAllProducts(): List<ProductEntity> = productDao.getAllProducts()


    fun getCartItems(): Flow<List<CartEntity>> = productDao.getCartItems()

    suspend fun addToCart(product: ProductEntity, quantity: Int) {
        val cartItem = CartEntity(
            productId = product.id,
            name = product.name,
            description = product.description,
            image = product.image,
            price = product.price,
            categoryId = product.categoryId,
            quantity = quantity
        )
        productDao.addToCart(cartItem)
    }

    suspend fun removeFromCart(productId: String) {
        productDao.removeFromCart(productId)
    }

    suspend fun clearCart() {
        productDao.clearCart()
    }
}
