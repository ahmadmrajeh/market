package com.example.market.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.market.data.local.ProductEntity

@Composable
fun CartItemRow(product: ProductEntity, quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(product.name, fontWeight = FontWeight.Bold)
            Text("$${product.price}", color = MaterialTheme.colorScheme.secondary)
        }

        Row {
            Button(onClick = onDecrease, enabled = quantity > 1) {
                Text("-")
            }
            Text("$quantity", modifier = Modifier.padding(horizontal = 8.dp))
            Button(onClick = onIncrease) {
                Text("+")
            }
        }
    }
}
