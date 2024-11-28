package com.valeriaukat.cupcake

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.valeriaukat.cupcake.data.DataSource
import com.valeriaukat.cupcake.data.OrderUiState
import com.valeriaukat.cupcake.ui.OrderSummaryScreen
import com.valeriaukat.cupcake.ui.OrderViewModel
import com.valeriaukat.cupcake.ui.SelectOptionScreen
import com.valeriaukat.cupcake.ui.StartOrderScreen

/**
 * enum values that represent the screens in the app
 */
enum class CupcakeScreen(@StringRes val title: Int) { // Mendeklarasikan enum untuk layar aplikasi
    Start(title = R.string.app_name), // Layar awal dengan judul dari string resource
    Flavor(title = R.string.choose_flavor), // Layar pilihan rasa
    Pickup(title = R.string.choose_pickup_date), // Layar pilihan tanggal pengambilan
    Summary(title = R.string.order_summary) // Layar ringkasan pesanan
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class) // Menandai penggunaan API eksperimen
@Composable
fun CupcakeAppBar( // Mendeklarasikan fungsi komposisi untuk AppBar
    currentScreen: CupcakeScreen, // Parameter untuk layar saat ini
    canNavigateBack: Boolean, // Parameter untuk menentukan apakah navigasi kembali memungkinkan
    navigateUp: () -> Unit, // Lambda untuk navigasi ke atas
    modifier: Modifier = Modifier // Parameter untuk Modifier, default ke Modifier kosong
) {
    TopAppBar( // Membuat komponen TopAppBar
        title = { Text(stringResource(currentScreen.title)) }, // Menampilkan judul layar saat ini
        colors = TopAppBarDefaults.mediumTopAppBarColors( // Mengatur warna untuk TopAppBar
            containerColor = MaterialTheme.colorScheme.primaryContainer // Mengatur warna latar belakang
        ),
        modifier = modifier, // Menerapkan Modifier
        navigationIcon = { // Menambahkan ikon navigasi jika memungkinkan
            if (canNavigateBack) { // Memeriksa apakah navigasi kembali diizinkan
                IconButton(onClick = navigateUp) { // Membuat tombol ikon untuk navigasi
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Menggunakan ikon panah kembali
                        contentDescription = stringResource(R.string.back_button) // Deskripsi konten untuk aksesibilitas
                    )
                }
            }
        }
    )
}

@Composable
fun CupcakeApp( // Mendeklarasikan fungsi komposisi untuk aplikasi Cupcake
    viewModel: OrderViewModel = viewModel(), // Mendapatkan ViewModel untuk pesanan
    navController: NavHostController = rememberNavController() // Mengingat controller navigasi
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState() // Mendapatkan entri back stack saat ini
    // Get the name of the current screen
    val currentScreen = CupcakeScreen.valueOf( // Mendapatkan nama layar saat ini dari back stack
        backStackEntry?.destination?.route ?: CupcakeScreen.Start.name // Menetapkan layar awal jika tidak ada
    )

    Scaffold( // Membuat komponen Scaffold untuk layout
        topBar = { // Menentukan konten untuk bagian atas
            CupcakeAppBar( // Memanggil AppBar
                currentScreen = currentScreen, // Menetapkan layar saat ini
                canNavigateBack = navController.previousBackStackEntry != null, // Memeriksa apakah ada entri sebelumnya
                navigateUp = { navController.navigateUp() } // Mengatur lambda untuk navigasi ke atas
            )
        }
    ) { innerPadding -> // Parameter innerPadding untuk konten di dalam Scaffold
        val uiState by viewModel.uiState.collectAsState() // Mengumpulkan state UI dari ViewModel

        NavHost( // Membuat komponen NavHost untuk navigasi
            navController = navController, // Menetapkan controller navigasi
            startDestination = CupcakeScreen.Start.name, // Menetapkan layar awal
            modifier = Modifier // Menerapkan modifier
                .fillMaxSize() // Mengisi ukuran maksimum
                .verticalScroll(rememberScrollState()) // Mengaktifkan scroll vertikal
                .padding(innerPadding) // Menerapkan padding
        ) {
            composable(route = CupcakeScreen.Start.name) { // Menentukan layar untuk memulai pesanan
                StartOrderScreen( // Memanggil layar untuk memulai pesanan
                    quantityOptions = DataSource.quantityOptions, // Menetapkan opsi jumlah dari DataSource
                    onNextButtonClicked = { // Lambda untuk tindakan ketika tombol berikutnya diklik
                        viewModel.setQuantity(it) // Menetapkan jumlah di ViewModel
                        navController.navigate(CupcakeScreen.Flavor.name) // Navigasi ke layar pilihan rasa
                    },
                    modifier = Modifier // Menerapkan modifier
                        .fillMaxSize() // Mengisi ukuran maksimum
                        .padding(dimensionResource(R.dimen.padding_medium)) // Menerapkan padding medium
                )
            }
            composable(route = CupcakeScreen.Flavor.name) { // Menentukan layar untuk memilih rasa
                val context = LocalContext.current // Mendapatkan konteks lokal
                SelectOptionScreen( // Memanggil layar untuk memilih opsi
                    subtotal = uiState.price, // Menetapkan subtotal dari state UI
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) }, // Navigasi ke layar pilihan pengambilan
                    onCancelButtonClicked = { // Lambda untuk tindakan ketika tombol batal diklik
                        cancelOrderAndNavigateToStart(viewModel, navController) // Membatalkan pesanan dan kembali ke layar awal
                    },
                    options = DataSource.flavors.map { id -> context.resources.getString(id) }, // Mendapatkan daftar rasa dari DataSource
                    onSelectionChanged = { viewModel.setFlavor(it) }, // Menetapkan rasa di ViewModel saat pilihan diubah
                    modifier = Modifier.fillMaxHeight() // Mengisi tinggi maksimum
                )
            }
            composable(route = CupcakeScreen.Pickup.name) { // Menentukan layar untuk memilih pengambilan
                SelectOptionScreen( // Memanggil layar untuk memilih opsi
                    subtotal = uiState.price, // Menetapkan subtotal dari state UI
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) }, // Navigasi ke layar ringkasan
                    onCancelButtonClicked = { // Lambda untuk tindakan ketika tombol batal diklik
                        cancelOrderAndNavigateToStart(viewModel, navController) // Membatalkan pesanan dan kembali ke layar awal
                    },
                    options = uiState.pickupOptions, // Mendapatkan opsi pengambilan dari state UI
                    onSelectionChanged = { viewModel.setDate(it) }, // Menetapkan tanggal di ViewModel saat pilihan diubah
                    modifier = Modifier.fillMaxHeight() // Mengisi tinggi maksimum
                )
            }
            composable(route = CupcakeScreen.Summary.name) { // Menentukan layar untuk ringkasan pesanan
                val context = LocalContext.current // Mendapatkan konteks lokal
                OrderSummaryScreen( // Memanggil layar ringkasan pesanan
                    orderUiState = uiState, // Menetapkan state UI pesanan
                    onCancelButtonClicked = { // Lambda untuk tindakan ketika tombol batal diklik
                        cancelOrderAndNavigateToStart(viewModel, navController) // Membatalkan pesanan dan kembali ke layar awal
                    },
                    onSendButtonClicked = { subject: String, summary: String -> // Lambda untuk tindakan ketika tombol kirim diklik
                        shareOrder(context, subject = subject, summary = summary) // Membagikan rincian pesanan
                    },
                    modifier = Modifier.fillMaxHeight() // Mengisi tinggi maksimum
                )
            }
        }
    }
}

/**
 * Resets the [OrderUiState] and pops up to [CupcakeScreen.Start]
 */
private fun cancelOrderAndNavigateToStart( // Fungsi untuk membatalkan pesanan dan kembali ke layar awal
    viewModel: OrderViewModel, // Parameter ViewModel untuk pesanan
    navController: NavHostController // Parameter controller navigasi
) {
    viewModel.resetOrder() // Mereset state pesanan di ViewModel
    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false) // Kembali ke layar awal tanpa menyertakan layar awal
}

/**
 * Creates an intent to share order details
 */
private fun shareOrder(context: Context, subject: String, summary: String) { // Fungsi untuk membagikan rincian pesanan
    // Create an ACTION_SEND implicit intent with order details in the intent extras 
    val intent = Intent(Intent.ACTION_SEND).apply { // Membuat intent ACTION_SEND
        type = "text/plain" // Menetapkan tipe data untuk intent
        putExtra(Intent.EXTRA_SUBJECT, subject) // Menambahkan subjek ke intent
        putExtra(Intent.EXTRA_TEXT, summary) // Menambahkan teks ke intent
    }
    context.startActivity( // Memulai aktivitas dengan intent
        Intent.createChooser( // Membuat chooser untuk memilih aplikasi berbagi
            intent,
            context.getString(R.string.new_cupcake_order) // Judul chooser dari resource string
        )
    )
}
