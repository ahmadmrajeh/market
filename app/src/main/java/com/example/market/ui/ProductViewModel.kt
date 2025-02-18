package com.example.market.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.market.core.ProductRepository
import com.example.market.data.local.CartEntity
import com.example.market.data.local.CategoryEntity
import com.example.market.data.local.ProductEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProductWithCategory(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val price: Double,
    val categoryId: String,
    val categoryName: String
)

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories

    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    val products: StateFlow<List<ProductEntity>> = _products

    private val _productsWithCategory = MutableStateFlow<List<ProductWithCategory>>(emptyList())
    val productsWithCategory: StateFlow<List<ProductWithCategory>> = _productsWithCategory

    private val _cart = MutableStateFlow<List<CartEntity>>(emptyList())
    val cart: StateFlow<List<CartEntity>> = _cart

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * ✅ Load all categories and products when explicitly called.
     */
    fun loadCategoriesAndProducts() {
        viewModelScope.launch {
            try {
                repository.fetchCategories().collect { _categories.value = it }
                repository.fetchProducts().collect { _products.value = it }
                mapProductsToCategories() // ✅ Map products to categories
            } catch (e: Exception) {
                _errorMessage.value = "Error loading data: ${e.message}"
            }
        }
    }


    /**
     * ✅ Map products with their categories.
     */
    private fun mapProductsToCategories() {
        val mappedProducts = _products.value.map { product ->
            val category = _categories.value.find { it.id == product.categoryId }
            ProductWithCategory(
                id = product.id,
                name = product.name,
                description = product.description,
                image = product.image,
                price = product.price,
                categoryId = product.categoryId,
                categoryName = category?.name ?: "Unknown"
            )
        }
        _productsWithCategory.value = mappedProducts
    }

    /**
     * ✅ Search products by query.
     */
    fun search(query: String) {
        viewModelScope.launch {
            repository.searchProducts(query).collect { results ->
                _productsWithCategory.value = results.map { product ->
                    val category = _categories.value.find { it.id == product.categoryId }
                    ProductWithCategory(
                        id = product.id,
                        name = product.name,
                        description = product.description,
                        image = product.image,
                        price = product.price,
                        categoryId = product.categoryId,
                        categoryName = category?.name ?: "Unknown"
                    )
                }
            }
        }
    }

    /**
     * ✅ Filter products by category.
     */
    fun filterProductsByCategory(categoryId: String?) {
        val filteredProducts = if (categoryId.isNullOrBlank()) {
            _products.value
        } else {
            _products.value.filter { it.categoryId == categoryId }
        }

        _productsWithCategory.value = filteredProducts.map { product ->
            val category = _categories.value.find { it.id == product.categoryId }
            ProductWithCategory(
                id = product.id,
                name = product.name,
                description = product.description,
                image = product.image,
                price = product.price,
                categoryId = product.categoryId,
                categoryName = category?.name ?: "Unknown"
            )
        }
    }

    /**
     * ✅ Convert `ProductEntity` to `CartEntity`
     */
    private fun ProductEntity.toCartEntity(quantity: Int): CartEntity {
        return CartEntity(
            productId = this.id,
            name = this.name,
            description = this.description,
            image = this.image,
            price = this.price,
            categoryId = this.categoryId,
            quantity = quantity
        )
    }

    fun addToCart(product: ProductEntity, forceNewItem: Boolean = false) {
        viewModelScope.launch {
            val existingItem = _cart.value.find { it.productId == product.id }

            if (existingItem == null || forceNewItem) {
                repository.addToCart(product, quantity = 1)
            } else {
                repository.addToCart(product, quantity = existingItem.quantity + 1)
            }

            _cart.value = _cart.value.toList() // ✅ Ensure recomposition
            loadCart() // ✅ Refresh cart from Room
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
            _cart.value = _cart.value.toList() // ✅ Force recomposition
            loadCart() // ✅ Refresh cart
        }
    }

    fun loadCart() {
        viewModelScope.launch {
            repository.getCartItems().collect { items ->
                _cart.value = items.toList() // ✅ Ensure new reference to trigger recomposition
            }
        }
    }

    /**
     * ✅ Clears the cart.
     */
    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            loadCart() // ✅ Refresh cart
        }
    }

    /**
     * ✅ Load mock data for testing.
     */
    fun loadDumpData() {
        viewModelScope.launch {
            try {
                repository.insertMockData()
                loadCategoriesAndProducts()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load dump data: ${e.message}"
            }
        }
    }
}
