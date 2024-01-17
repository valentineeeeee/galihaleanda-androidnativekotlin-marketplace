package com.example.prjctmobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prjctmobile.databinding.ActivityAlamatBinding


class AddAlamatActivity : AppCompatActivity() {

    private lateinit var namaLengkapEditText: EditText
    private lateinit var nomorTeleponEditText: EditText
    private lateinit var kotaEditText: EditText
    private lateinit var namaJalanEditText: EditText
    private lateinit var detailLainnyaEditText: EditText
    private lateinit var simpanButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alamat)

        // Initialize your EditText and Button views
        namaLengkapEditText = findViewById(R.id.namaLengkapEditText)
        nomorTeleponEditText = findViewById(R.id.nomorTeleponEditText)
        kotaEditText = findViewById(R.id.kotaEditText)
        namaJalanEditText = findViewById(R.id.namaJalanEditText)
        detailLainnyaEditText = findViewById(R.id.detailLainnyaEditText)
        simpanButton = findViewById(R.id.simpanButton)

        // Set onClickListener for the Simpan button
        simpanButton.setOnClickListener {
            // Get the values from EditText fields
            val namaLengkap = namaLengkapEditText.text.toString()
            val nomorTelepon = nomorTeleponEditText.text.toString()
            val kota = kotaEditText.text.toString()
            val namaJalan = namaJalanEditText.text.toString()
            val detailLainnya = detailLainnyaEditText.text.toString()

            if (validateInput(namaLengkap, nomorTelepon, kota, namaJalan, detailLainnya)) {

                val productId = intent.getStringExtra("productId")
                val productName = intent.getStringExtra("productName")
                val productPrice = intent.getStringExtra("productPrice")
                val productDescription = intent.getStringExtra("productDescription")
                val imageUrl = intent.getStringExtra("imageUrl")

                // Create an Intent to start the ActivityBayar
                val bayarIntent = Intent(applicationContext, Bayar2Activity::class.java)

                // Put the data as extras in the Intent

                bayarIntent.putExtra("productId", productId)
                bayarIntent.putExtra("productName", productName)
                bayarIntent.putExtra("productPrice", productPrice)
                bayarIntent.putExtra("productDescription", productDescription)
                bayarIntent.putExtra("imageUrl", imageUrl)

                bayarIntent.putExtra("NAMA_LENGKAP", namaLengkap)
                bayarIntent.putExtra("NOMOR_TELEPON", nomorTelepon)
                bayarIntent.putExtra("KOTA", kota)
                bayarIntent.putExtra("NAMA_JALAN", namaJalan)
                bayarIntent.putExtra("DETAIL_LAINNYA", detailLainnya)


                // Start the ActivityBayar with the created Intent
                startActivity(bayarIntent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    private fun validateInput(
        namaLengkap: String,
        nomorTelepon: String,
        kota: String,
        namaJalan: String,
        detailLainnya: String
    ): Boolean {
        // Perform input validation here
        if (namaLengkap.isEmpty() || nomorTelepon.isEmpty() || kota.isEmpty() || namaJalan.isEmpty() || detailLainnya.isEmpty()) {
            // Display an error message or handle the validation failure
            // For example, you can show a Toast message
            Toast.makeText(this, "Isi semua data", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}