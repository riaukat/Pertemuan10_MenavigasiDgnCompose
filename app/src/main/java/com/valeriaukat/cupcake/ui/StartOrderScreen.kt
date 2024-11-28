package com.valeriaukat.cupcake.ui 

import androidx.annotation.StringRes 
import androidx.compose.foundation.* 
import androidx.compose.material3.* 
import androidx.compose.runtime.Composable 
import androidx.compose.ui.Alignment 
import androidx.compose.ui.Modifier 
import androidx.compose.ui.res.* 
import androidx.compose.ui.tooling.preview.Preview 
import androidx.compose.ui.unit.dp 
import com.valeriaukat.cupcake.R 
import com.valeriaukat.cupcake.data.DataSource 
import com.valeriaukat.cupcake.ui.theme.CupcakeTheme 

/**
 * Composable that allows the user to select the desired cupcake quantity and expects
 * [onNextButtonClicked] lambda that expects the selected quantity and triggers the navigation to
 * next screen
 */
@Composable
fun StartOrderScreen( // Fungsi komposisi untuk layar pemesanan awal
    quantityOptions: List<Pair<Int, Int>>, // Daftar opsi jumlah cupcake
    onNextButtonClicked: (Int) -> Unit, // Lambda untuk menangani klik tombol berikutnya
    modifier: Modifier = Modifier // Parameter untuk Modifier, default ke Modifier kosong
) {
    Column( // Mengatur layout kolom
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween // Mengatur ruang vertikal antar elemen
    ) {
        Column( // Kolom untuk menampilkan gambar dan teks
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, // Menyelaraskan elemen secara horizontal
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)) // Mengatur jarak antar elemen
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium))) // Menambahkan spasi
            Image( // Menampilkan gambar cupcake
                painter = painterResource(R.drawable.cupcake),
                contentDescription = null,
                modifier = Modifier.width(300.dp) // Mengatur lebar gambar
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium))) // Menambahkan spasi
            Text( // Menampilkan teks untuk pesanan cupcake
                text = stringResource(R.string.order_cupcakes),
                style = MaterialTheme.typography.headlineSmall // Mengatur gaya teks
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small))) // Menambahkan spasi
        }
        Column( // Kolom untuk menampilkan tombol pilihan jumlah
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally, // Menyelaraskan elemen secara horizontal
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)) // Mengatur jarak antar tombol
        ) {
            quantityOptions.forEach { item -> // Mengulangi setiap opsi jumlah
                SelectQuantityButton( // Memanggil komposisi tombol untuk setiap opsi
                    labelResourceId = item.first, // Resource ID untuk label tombol
                    onClick = { onNextButtonClicked(item.second) }, // Menangani klik tombol
                    modifier = Modifier.fillMaxWidth() // Mengatur tombol mengisi lebar maksimum
                )
            }
        }
    }
}

/**
 * Customizable button composable that displays the [labelResourceId]
 * and triggers [onClick] lambda when this composable is clicked
 */
@Composable
fun SelectQuantityButton( // Fungsi komposisi untuk tombol pilihan jumlah
    @StringRes labelResourceId: Int, // Resource ID untuk label tombol
    onClick: () -> Unit, // Lambda untuk menangani klik tombol
    modifier: Modifier = Modifier // Parameter untuk Modifier, default ke Modifier kosong
) {
    Button( // Membuat tombol
        onClick = onClick, // Menangani klik tombol
        modifier = modifier.widthIn(min = 250.dp) // Mengatur lebar minimum tombol
    ) {
        Text(stringResource(labelResourceId)) // Menampilkan teks untuk tombol
    }
}

@Preview // Anotasi untuk menampilkan preview komposisi
@Composable
fun StartOrderPreview() { // Fungsi untuk preview layar pemesanan awal
    CupcakeTheme { // Mengaplikasikan tema cupcake
        StartOrderScreen( // Memanggil komposisi untuk memulai pemesanan
            quantityOptions = DataSource.quantityOptions, // Mengambil opsi jumlah dari DataSource
            onNextButtonClicked = {}, // Lambda kosong untuk preview
            modifier = Modifier // Mengatur modifier untuk mengisi ukuran maksimum
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
