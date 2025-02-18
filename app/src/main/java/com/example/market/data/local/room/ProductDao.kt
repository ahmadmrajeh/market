package com.example.market.data.local.room

import androidx.room.*
import com.example.market.data.local.CartEntity
import com.example.market.data.local.CategoryEntity
import com.example.market.data.local.ProductEntity
import kotlinx.coroutines.flow.Flow



@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<CategoryEntity>

    @Query("SELECT * FROM products") // Fetch all products (filtering happens in ViewModel)
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%'")
    suspend fun searchProducts(query: String): List<ProductEntity>

    // ✅ Cart Queries
    @Query("SELECT * FROM cart")
    fun getCartItems(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartItem: CartEntity)

    @Query("DELETE FROM cart WHERE productId = :productId")
    suspend fun removeFromCart(productId: String)

    @Query("DELETE FROM cart")
    suspend fun clearCart()
}
