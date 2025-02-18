package com.example.market.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.market.R
import com.example.market.ui.ProductWithCategory

@Composable
fun ProductCard(product: ProductWithCategory, quantity: Int, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(8.dp)) {
        Card(
            modifier = Modifier
                .clickable { onClick() }
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Image(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null)
                Text(product.name, fontWeight = FontWeight.Bold)
                Text(product.categoryName, style = MaterialTheme.typography.bodySmall)
                Text("$${product.price}", color = MaterialTheme.colorScheme.secondary)
            }
        }

        // 🔹 Red Counter Badge for Items in Cart
        if (quantity > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color.Red, RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("$quantity", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

