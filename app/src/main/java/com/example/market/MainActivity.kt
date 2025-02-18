package com.example.market

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.market.ui.navigation.BottomNavBar
import com.example.market.ui.navigation.Destinations  // ✅ Import the sealed class Destinations
import com.example.market.ui.screens.CartScreen
import com.example.market.ui.screens.HomeScreen
import com.example.market.ui.screens.ProductDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    Scaffold(bottomBar = { BottomNavBar(navController = navController) }) {
        Box(Modifier.padding(it)) {
            NavHost(navController = navController, startDestination = Destinations.Home.route) {

                composable(Destinations.Home.route) {
                    HomeScreen(navController)
                }

                composable(Destinations.Cart.route) {
                    CartScreen(navController)
                }

                // ✅ Fix: Add Product Detail Screen Route
                composable("productDetail/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
                    ProductDetailScreen(productId = productId, navController = navController)
                }
            }
        }
    }
}

