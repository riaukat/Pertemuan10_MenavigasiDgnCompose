package com.valeriaukat.cupcake.ui 

import androidx.compose.foundation.layout.* 
import androidx.compose.material3.* 
import androidx.compose.runtime.* 
import androidx.compose.ui.Alignment 
import androidx.compose.ui.Modifier 
import androidx.compose.ui.res.dimensionResource 
import androidx.compose.ui.res.stringResource 
import androidx.compose.ui.tooling.preview.Preview 
import com.valeriaukat.cupcake.R 
import com.valeriaukat.cupcake.ui.components.FormattedPriceLabel 
import com.valeriaukat.cupcake.ui.theme.CupcakeTheme 

/**
 * Composable that displays the list of items as [RadioButton] options,
 * [onSelectionChanged] lambda that notifies the parent composable when a new value is selected,
 * [onCancelButtonClicked] lambda that cancels the order when user clicks cancel and
 * [onNextButtonClicked] lambda that triggers the navigation to next screen
 */
@Composable
fun SelectOptionScreen( // Fungsi komposisi untuk layar pemilihan opsi
    subtotal: String, // Parameter untuk subtotal harga
    options: List<String>, // Parameter untuk daftar opsi yang ditampilkan
    onSelectionChanged: (String) -> Unit = {}, // Lambda untuk menangani perubahan pilihan
    onCancelButtonClicked: () -> Unit = {}, // Lambda untuk menangani klik tombol batal
    onNextButtonClicked: () -> Unit = {}, // Lambda untuk menangani klik tombol berikutnya
    modifier: Modifier = Modifier // Parameter untuk Modifier, default ke Modifier kosong
) {
    var selectedValue by rememberSaveable { mutableStateOf("") } // Menggunakan state untuk menyimpan nilai yang dipilih

    Column( // Mengatur layout kolom
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween // Mengatur ruang vertikal antar elemen
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) { // Kolom untuk menampilkan opsi
            options.forEach { item -> // Mengulangi setiap opsi untuk ditampilkan
                Row( // Mengatur layout baris untuk setiap opsi
                    modifier = Modifier.selectable( // Membuat opsi dapat dipilih
                        selected = selectedValue == item,
                        onClick = {
                            selectedValue = item // Mengupdate nilai yang dipilih
                            onSelectionChanged(item) // Memanggil lambda saat pilihan berubah
                        }
                    ),
                    verticalAlignment = Alignment.CenterVertically // Menyelaraskan elemen secara vertikal
                ) {
                    RadioButton( // Menampilkan tombol radio untuk pilihan
                        selected = selectedValue == item,
                        onClick = {
                            selectedValue = item // Mengupdate nilai yang dipilih
                            onSelectionChanged(item) // Memanggil lambda saat pilihan berubah
                        }
                    )
                    Text(item) // Menampilkan teks untuk setiap opsi
                }
            }
            Divider( // Menambahkan pemisah antar bagian
                thickness = dimensionResource(R.dimen.thickness_divider),
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
            )
            FormattedPriceLabel( // Menampilkan label harga yang diformat
                subtotal = subtotal,
                modifier = Modifier
                    .align(Alignment.End) // Menyelaraskan ke akhir
                    .padding( // Menambahkan padding
                        top = dimensionResource(R.dimen.padding_medium),
                        bottom = dimensionResource(R.dimen.padding_medium)
                    )
            )
        }
        Row( // Mengatur layout baris untuk tombol
            modifier = Modifier
                .fillMaxWidth() // Mengisi lebar maksimum
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)), // Mengatur jarak horizontal antar tombol
            verticalAlignment = Alignment.Bottom // Menyelaraskan elemen ke bawah
        ) {
            OutlinedButton( // Tombol batal dengan outline
                modifier = Modifier.weight(1f), // Mengatur bobot untuk proporsi
                onClick = onCancelButtonClicked // Memanggil lambda untuk aksi batal
            ) {
                Text(stringResource(R.string.cancel)) // Menampilkan teks untuk tombol batal
            }
            Button( // Tombol berikutnya
                modifier = Modifier.weight(1f), // Mengatur bobot untuk proporsi
                enabled = selectedValue.isNotEmpty(), // Mengaktifkan tombol jika ada pilihan
                onClick = onNextButtonClicked // Memanggil lambda untuk aksi berikutnya
            ) {
                Text(stringResource(R.string.next)) // Menampilkan teks untuk tombol berikutnya
            }
        }
    }
}

@Preview // Anotasi untuk menampilkan preview komposisi
@Composable
fun SelectOptionPreview() { // Fungsi untuk preview layar pemilihan opsi
    CupcakeTheme { // Mengaplikasikan tema cupcake
        SelectOptionScreen( // Memanggil komposisi untuk menampilkan opsi
            subtotal = "299.99", // Menetapkan subtotal harga
            options = listOf("Option 1", "Option 2", "Option 3", "Option 4"), // Daftar opsi yang ditampilkan
            modifier = Modifier.fillMaxHeight() // Mengatur modifier untuk mengisi tinggi maksimum
        )
    }
}
