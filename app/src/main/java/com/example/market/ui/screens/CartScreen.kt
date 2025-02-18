package com.example.market.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.market.data.local.CartEntity
import com.example.market.ui.ProductViewModel
import com.example.market.ui.components.CartItemRow
import com.example.market.data.local.ProductEntity
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: ProductViewModel = getViewModel()) {
    val cartItems by viewModel.cart.collectAsState()

    // ✅ Force recomposition when cart updates
    LaunchedEffect(cartItems) {
        viewModel.loadCart()
    }

    val totalPrice = cartItems.sumOf { it.price * it.quantity }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Your Cart") })

        if (cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your cart is empty", color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Continue Shopping")
                    }
                }
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { cartItem ->
                    CartItemRow(
                        product = cartItem.toProductEntity(),
                        quantity = cartItem.quantity,
                        onIncrease = { viewModel.addToCart(cartItem.toProductEntity()) },
                        onDecrease = { viewModel.removeFromCart(cartItem.productId) }
                    )
                }
            }

            // 🔹 Display Total Price and Clear Order Button
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total: $${"%.2f".format(totalPrice)}", style = MaterialTheme.typography.headlineMedium)

                Button(
                    onClick = { viewModel.clearCart() },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Clear Order", color = Color.White)
                }
            }
        }
    }
}

/**
 * ✅ Convert `CartEntity` to `ProductEntity` to match `CartItemRow` requirements.
 */
fun CartEntity.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = this.productId,
        name = this.name,
        description = this.description,
        image = this.image,
        price = this.price,
        categoryId = this.categoryId
    )
}
