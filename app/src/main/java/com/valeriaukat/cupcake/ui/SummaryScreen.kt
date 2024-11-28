package com.valeriaukat.cupcake.ui 

import androidx.compose.foundation.layout.* 
import androidx.compose.material3.* 
import androidx.compose.runtime.Composable 
import androidx.compose.ui.Alignment 
import androidx.compose.ui.Modifier 
import androidx.compose.ui.platform.LocalContext 
import androidx.compose.ui.res.dimensionResource 
import androidx.compose.ui.res.stringResource 
import androidx.compose.ui.text.font.FontWeight 
import androidx.compose.ui.tooling.preview.Preview 
import com.valeriaukat.cupcake.R 
import com.valeriaukat.cupcake.data.OrderUiState 
import com.valeriaukat.cupcake.ui.components.FormattedPriceLabel 
import com.valeriaukat.cupcake.ui.theme.CupcakeTheme 

/**
 * This composable expects [orderUiState] that represents the order state, [onCancelButtonClicked]
 * lambda that triggers canceling the order and passes the final order to [onSendButtonClicked]
 * lambda
 */
@Composable
fun OrderSummaryScreen( // Fungsi komposisi untuk layar ringkasan pesanan
    orderUiState: OrderUiState, // Parameter untuk state pesanan
    onCancelButtonClicked: () -> Unit, // Lambda untuk menangani klik tombol batal
    onSendButtonClicked: (String, String) -> Unit, // Lambda untuk menangani klik tombol kirim
    modifier: Modifier = Modifier // Parameter untuk Modifier, default ke Modifier kosong
) {
    val resources = LocalContext.current.resources // Mendapatkan resources dari konteks lokal

    // Mengambil dan memformat string jumlah cupcake berdasarkan opsi yang dipilih
    val numberOfCupcakes = resources.getQuantityString(
        R.plurals.cupcakes,
        orderUiState.quantity,
        orderUiState.quantity
    )

    // Memformat ringkasan pesanan dengan parameter yang relevan
    val orderSummary = stringResource(
        R.string.order_details,
        numberOfCupcakes,
        orderUiState.flavor,
        orderUiState.date,
        orderUiState.quantity
    )
    val newOrder = stringResource(R.string.new_cupcake_order) // String untuk pesanan baru

    // Membuat daftar ringkasan pesanan untuk ditampilkan
    val items = listOf(
        Pair(stringResource(R.string.quantity), numberOfCupcakes), // Menampilkan jumlah cupcake
        Pair(stringResource(R.string.flavor), orderUiState.flavor), // Menampilkan rasa cupcake
        Pair(stringResource(R.string.pickup_date), orderUiState.date) // Menampilkan tanggal pengambilan
    )

    Column( // Mengatur layout kolom untuk ringkasan pesanan
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween // Mengatur ruang vertikal antar elemen
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)), // Kolom untuk detail pesanan
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)) // Mengatur jarak antar elemen
        ) {
            items.forEach { item -> // Mengulangi setiap item dalam daftar ringkasan
                Text(item.first.uppercase()) // Menampilkan label dalam huruf kapital
                Text(text = item.second, fontWeight = FontWeight.Bold) // Menampilkan nilai dengan berat font tebal
                Divider(thickness = dimensionResource(R.dimen.thickness_divider)) // Menambahkan pemisah
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small))) // Menambahkan spasi
            FormattedPriceLabel( // Menampilkan label harga yang diformat
                subtotal = orderUiState.price,
                modifier = Modifier.align(Alignment.End) // Menyelaraskan ke akhir
            )
        }
        Row( // Mengatur layout baris untuk tombol
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Column( // Kolom untuk menampilkan tombol
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)) // Mengatur jarak antar tombol
            ) {
                Button( // Tombol untuk mengirim pesanan
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSendButtonClicked(newOrder, orderSummary) } // Menangani klik tombol kirim
                ) {
                    Text(stringResource(R.string.send)) // Menampilkan teks untuk tombol kirim
                }
                OutlinedButton( // Tombol batal dengan outline
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked // Menangani klik tombol batal
                ) {
                    Text(stringResource(R.string.cancel)) // Menampilkan teks untuk tombol batal
                }
            }
        }
    }
}

@Preview // Anotasi untuk menampilkan preview komposisi
@Composable
fun OrderSummaryPreview() { // Fungsi untuk preview layar ringkasan pesanan
    CupcakeTheme { // Mengaplikasikan tema cupcake
        OrderSummaryScreen( // Memanggil komposisi untuk menampilkan ringkasan pesanan
            orderUiState = OrderUiState(0, "Test", "Test", "$300.00"), // Menetapkan state pesanan untuk preview
            onSendButtonClicked = { subject: String, summary: String -> }, // Lambda kosong untuk preview
            onCancelButtonClicked = {}, // Lambda kosong untuk preview
            modifier = Modifier.fillMaxHeight() // Mengatur modifier untuk mengisi tinggi maksimum
        )
    }
}
