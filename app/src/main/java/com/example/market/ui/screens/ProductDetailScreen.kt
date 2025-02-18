package com.example.market.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.market.R
import com.example.market.ui.ProductViewModel
import com.example.market.data.local.ProductEntity
import org.koin.androidx.compose.getViewModel

@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductViewModel = getViewModel(),
    navController: NavController
) {
    // 🔹 Ensure data is loaded before trying to access the product
    LaunchedEffect(Unit) {
        if (viewModel.products.value.isEmpty()) {
            viewModel.loadCategoriesAndProducts()
        }
    }

    val allProducts by viewModel.products.collectAsState()
    val cartItems by viewModel.cart.collectAsState()

    // 🔹 Retrieve product from available products
    val product = allProducts.find { it.id == productId }

    // 🔹 If product is null, show "Product Not Found" screen
    if (product == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Product not found", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        return
    }

    // 🔹 Get quantity of the product in cart
    val quantity = cartItems.find { it.productId == product.id }?.quantity ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Product Image Placeholder
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = product.name
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Product Name & Description
        Text(product.name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(product.description, fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(8.dp))
        Text(
            "$${product.price}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50) // Green price color
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Quantity Control Buttons
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { viewModel.removeFromCart(product.id) },
                enabled = quantity > 0,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("-")
            }

            Text(
                text = "$quantity",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { viewModel.addToCart(product) }, // ✅ Increase quantity when "+" button is clicked
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Purple color
            ) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Add to Cart Button (Only Adds the Product, Doesn't Increase Quantity)
        Button(
            onClick = { viewModel.addToCart(product) }, // ✅ Adds new item instead of increasing quantity
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Add to Cart", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Back to Home Button
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home", fontSize = 16.sp)
        }
    }
}
