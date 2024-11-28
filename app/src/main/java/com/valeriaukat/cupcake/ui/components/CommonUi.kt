package com.valeriaukat.cupcake.ui.components 

import androidx.compose.material3.MaterialTheme 
import androidx.compose.material3.Text 
import androidx.compose.runtime.Composable 
import androidx.compose.ui.Modifier 
import androidx.compose.ui.res.stringResource 
import com.valeriaukat.cupcake.R 
/**
 * Composable that displays formatted [price] that will be formatted and displayed on screen
 */
@Composable
fun FormattedPriceLabel( // Fungsi komposisi untuk menampilkan label harga yang diformat
    subtotal: String, // Parameter untuk subtotal harga
    modifier: Modifier = Modifier // Parameter untuk Modifier, default ke Modifier kosong
) {
    Text( // Menampilkan teks
        text = stringResource(R.string.subtotal_price, subtotal), // Mengambil dan memformat string untuk subtotal
        modifier = modifier, // Menerapkan modifier untuk teks
        style = MaterialTheme.typography.headlineSmall // Mengatur gaya teks menggunakan tema
    )
}
