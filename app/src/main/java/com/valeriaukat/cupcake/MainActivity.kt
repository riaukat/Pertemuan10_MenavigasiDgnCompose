package com.valeriaukat.cupcake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.valeriaukat.cupcake.ui.theme.CupcakeTheme

class MainActivity : ComponentActivity() { // Mendeklarasikan kelas MainActivity yang mewarisi ComponentActivity
    override fun onCreate(savedInstanceState: Bundle?) { // Mengoverride metode onCreate, dipanggil saat aktivitas dibuat
        enableEdgeToEdge() // Mengaktifkan mode tampilan edge-to-edge untuk aktivitas ini
        super.onCreate(savedInstanceState) // Memanggil onCreate dari kelas induk untuk inisialisasi
        setContent { // Mengatur konten tampilan menggunakan fungsi lambda
            CupcakeTheme { // Menerapkan tema Cupcake pada konten
                CupcakeApp() // Memanggil fungsi CupcakeApp untuk menampilkan antarmuka aplikasi
            }
        }
    }
}
