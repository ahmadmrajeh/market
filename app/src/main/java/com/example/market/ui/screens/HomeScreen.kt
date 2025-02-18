package com.example.market.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.market.ui.ProductViewModel
import com.example.market.ui.ProductWithCategory
import com.example.market.ui.components.ProductCard
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: ProductViewModel = getViewModel()) {
    val categories by viewModel.categories.collectAsState()
    val allProducts by viewModel.productsWithCategory.collectAsState() // ✅ Get product with categories
    val cartItems by viewModel.cart.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // Ensure categories load before selecting the first available category
    LaunchedEffect(categories) {
        if (categories.isNotEmpty() && selectedCategory == null) {
            selectedCategory = categories.first().id
        }
    }

    // ✅ Filtered products, using search or category selection
    val filteredProducts by remember(searchQuery, selectedCategory, allProducts) {
        derivedStateOf {
            when {
                searchQuery.isNotBlank() -> {
                    allProducts.filter { it.name.contains(searchQuery, ignoreCase = true) }
                }
                selectedCategory != null -> {
                    allProducts.filter { it.categoryId == selectedCategory }
                }
                else -> allProducts
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 🔹 App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Person, contentDescription = "User", tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ikram Merah", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        // 🔹 "Load Dump Data" button (Only appears if no categories or products exist)
        if (categories.isEmpty() || allProducts.isEmpty()) {
            Button(
                onClick = { viewModel.loadDumpData() }, // ✅ Calls ViewModel function
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Load Dump Data")
            }
        }

        // 🔹 Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.search(it) // ✅ Triggers search function
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = { Text("Search for products or categories") }
        )

        // 🔹 Categories Tab Row (only show if not searching)
        if (searchQuery.isBlank() && categories.isNotEmpty()) {
            val selectedIndex = categories.indexOfFirst { it.id == selectedCategory }
            val validSelectedIndex = if (selectedIndex >= 0) selectedIndex else 0

            ScrollableTabRow(selectedTabIndex = validSelectedIndex) {
                categories.forEach { category ->
                    Tab(
                        text = { Text(category.name) },
                        selected = selectedCategory == category.id,
                        onClick = { selectedCategory = category.id }
                    )
                }
            }
        }

        // 🔹 Product Grid - Displays Cart Quantity for Each Item
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.weight(1f)) {
            items(filteredProducts) { product ->
                val quantityInCart = cartItems.find { it.productId == product.id }?.quantity ?: 0
                ProductCard(product, quantityInCart) {
                    navController.navigate("productDetail/${product.id}")
                }
            }
        }

        // 🔹 View Order Button (Only Visible if Cart has Items)
        val totalItemsInCart = cartItems.sumOf { it.quantity } // ✅ Correct total quantity calculation
        if (totalItemsInCart > 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .padding(16.dp)
                    .clickable { navController.navigate("cart") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "View Order - $totalItemsInCart items", // ✅ Correct item count
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}
