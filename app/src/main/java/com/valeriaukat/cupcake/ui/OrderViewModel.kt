package com.valeriaukat.cupcake.ui 

import androidx.lifecycle.ViewModel 
import com.valeriaukat.cupcake.data.OrderUiState 
import kotlinx.coroutines.flow.MutableStateFlow 
import kotlinx.coroutines.flow.StateFlow 
import kotlinx.coroutines.flow.asStateFlow 
import kotlinx.coroutines.flow.update 
import java.text.NumberFormat 
import java.text.SimpleDateFormat 
import java.util.Calendar 
import java.util.Locale 

/** Price for a single cupcake */
private const val PRICE_PER_CUPCAKE = 2.00 // Menetapkan harga per cupcake

/** Additional cost for same day pickup of an order */
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00 // Menetapkan biaya tambahan untuk pengambilan di hari yang sama

/**
 * [OrderViewModel] holds information about a cupcake order in terms of quantity, flavor, and
 * pickup date. It also knows how to calculate the total price based on these order details.
 */
class OrderViewModel : ViewModel() { // Mendeklarasikan kelas OrderViewModel yang mewarisi ViewModel

    /**
     * Cupcake state for this order
     */
    private val _uiState = MutableStateFlow(OrderUiState(pickupOptions = pickupOptions())) // Menginisialisasi MutableStateFlow dengan state pesanan
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow() // Menyediakan akses ke StateFlow yang tidak dapat diubah

    /**
     * Set the quantity [numberCupcakes] of cupcakes for this order's state and update the price
     */
    fun setQuantity(numberCupcakes: Int) { // Fungsi untuk menetapkan jumlah cupcake
        _uiState.update { currentState -> // Memperbarui state saat ini
            currentState.copy( // Menggunakan copy untuk memperbarui state
                quantity = numberCupcakes, // Menetapkan jumlah cupcake
                price = calculatePrice(quantity = numberCupcakes) // Menghitung harga baru berdasarkan jumlah
            )
        }
    }

    /**
     * Set the [desiredFlavor] of cupcakes for this order's state.
     * Only 1 flavor can be selected for the whole order.
     */
    fun setFlavor(desiredFlavor: String) { // Fungsi untuk menetapkan rasa cupcake
        _uiState.update { currentState -> // Memperbarui state saat ini
            currentState.copy(flavor = desiredFlavor) // Menggunakan copy untuk memperbarui rasa
        }
    }

    /**
     * Set the [pickupDate] for this order's state and update the price
     */
    fun setDate(pickupDate: String) { // Fungsi untuk menetapkan tanggal pengambilan
        _uiState.update { currentState -> // Memperbarui state saat ini
            currentState.copy( // Menggunakan copy untuk memperbarui state
                date = pickupDate, // Menetapkan tanggal pengambilan
                price = calculatePrice(pickupDate = pickupDate) // Menghitung harga baru berdasarkan tanggal pengambilan
            )
        }
    }

    /**
     * Reset the order state
     */
    fun resetOrder() { // Fungsi untuk mereset state pesanan
        _uiState.value = OrderUiState(pickupOptions = pickupOptions()) // Mengembalikan state pesanan ke default
    }

    /**
     * Returns the calculated price based on the order details.
     */
    private fun calculatePrice( // Fungsi untuk menghitung harga berdasarkan detail pesanan
        quantity: Int = _uiState.value.quantity, // Menetapkan jumlah cupcake dari state saat ini
        pickupDate: String = _uiState.value.date // Menetapkan tanggal pengambilan dari state saat ini
    ): String {
        var calculatedPrice = quantity * PRICE_PER_CUPCAKE // Menghitung harga dasar berdasarkan jumlah
        // If the user selected the first option (today) for pickup, add the surcharge
        if (pickupOptions()[0] == pickupDate) { // Memeriksa apakah tanggal pengambilan adalah hari ini
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP // Menambahkan biaya tambahan untuk pengambilan hari yang sama
        }
        val formattedPrice = NumberFormat.getCurrencyInstance().format(calculatedPrice) // Memformat harga menjadi format mata uang
        return formattedPrice // Mengembalikan harga yang telah diformat
    }

    /**
     * Returns a list of date options starting with the current date and the following 3 dates.
     */
    private fun pickupOptions(): List<String> { // Fungsi untuk mendapatkan daftar opsi tanggal pengambilan
        val dateOptions = mutableListOf<String>() // Membuat daftar mutable untuk tanggal
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault()) // Menetapkan format tanggal
        val calendar = Calendar.getInstance() // Mendapatkan instance Calendar saat ini
        // add current date and the following 3 dates.
        repeat(4) { // Mengulang 4 kali untuk menambahkan tanggal
            dateOptions.add(formatter.format(calendar.time)) // Menambahkan tanggal ke daftar
            calendar.add(Calendar.DATE, 1) // Menambahkan satu hari ke kalender
        }
        return dateOptions // Mengembalikan daftar opsi tanggal
    }
}
