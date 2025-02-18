package com.example.market.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(val route: String, val icon: ImageVector, val title: String) {
    object Home : Destinations("home", Icons.Default.Home, "Home")
    object Cart : Destinations("cart", Icons.Default.ShoppingCart, "Cart")

    companion object {
        val items = listOf(Home, Cart)
    }
}
