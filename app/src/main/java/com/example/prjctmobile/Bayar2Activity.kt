package com.example.prjctmobile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.prjctmobile.databinding.ActivityBayar2Binding
import com.example.prjctmobile.session.SessionManager
import com.example.prjctmobile.table.Purchase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Bayar2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityBayar2Binding
    private lateinit var dynamicTextView: TextView
    private lateinit var productCategory: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var productId: String
    private lateinit var namaLengkap: String
    private lateinit var nomorTelepon: String
    private lateinit var kota: String
    private lateinit var namaJalan: String
    private lateinit var detailLainnya: String
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBayar2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        productId = intent.getStringExtra("productId") ?: ""
        ref = FirebaseDatabase.getInstance().getReference("purchase")


        // Retrieve data from Intent extras
        // Retrieve data from Intent extras
        val intent = intent
        namaLengkap = intent.getStringExtra("NAMA_LENGKAP") ?: ""
        nomorTelepon = intent.getStringExtra("NOMOR_TELEPON") ?: ""
        kota = intent.getStringExtra("KOTA") ?: ""
        namaJalan = intent.getStringExtra("NAMA_JALAN") ?: ""
        detailLainnya = intent.getStringExtra("DETAIL_LAINNYA") ?: ""
        productCategory = intent.getStringExtra("productCategory") ?: ""


        // Now you can use these values in your ActivityBayar layout
        // For example, set them to TextViews

        // Now you can use these values in your ActivityBayar layout
        // For example, set them to TextViews
        val namaLengkapTextView = findViewById<TextView>(R.id.textViewNamaLengkap1)
        val nomorTeleponTextView = findViewById<TextView>(R.id.textViewNomorHp1)
        val kotaTextView = findViewById<TextView>(R.id.textViewKota1)
        val namaJalanTextView = findViewById<TextView>(R.id.textViewNamaJalan1)
        val detailLainnyaTextView = findViewById<TextView>(R.id.textViewDetailLainnya1)

        namaLengkapTextView.text = namaLengkap
        nomorTeleponTextView.text = nomorTelepon
        kotaTextView.text = kota
        namaJalanTextView.text = namaJalan
        detailLainnyaTextView.text = detailLainnya

        val productId = intent.getStringExtra("productId")
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getStringExtra("productPrice")
        val productDescription = intent.getStringExtra("productDescription")
        val imageUrl = intent.getStringExtra("imageUrl")

        val productNameTextView = findViewById<TextView>(R.id.textViewProductNameBayar)
        productNameTextView.text = productName

        val productPriceTextView = findViewById<TextView>(R.id.textViewTotalPembayaran)
        productPriceTextView.text = productPrice

        val productAddressTextView = findViewById<TextView>(R.id.textViewProductAddressBayar)
        productAddressTextView.text = namaJalan

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.kita) // You can set a placeholder image
            .into(binding.imageView)

        // Find the TextView by its ID
        dynamicTextView = findViewById(R.id.textViewDynamic)

        // Set the default text
        dynamicTextView.text = "Konfirmasi Pembelian"

        val backButton: ImageView = findViewById(R.id.img)
        backButton.setOnClickListener {
            onBackPressed()
        }

        binding.beliSekarangButton.setOnClickListener {
            // Perform purchase action
            performPurchase()
        }

    }

    private fun performPurchase() {
        // Get the current user's ID
        val sessionManager = SessionManager(applicationContext)
        val buyerUserId: String? = sessionManager.getUserId()

        val purchase = Purchase(
            buyerUserId,
            productId ?: "", // You need to get the productId from somewhere
            namaLengkap ?: "",
            nomorTelepon ?: "",
            kota ?: "",
            namaJalan ?: "",
            detailLainnya ?: "",
        )
        ref.child(buyerUserId ?: "").setValue(purchase).addOnCompleteListener {
            Toast.makeText(
                applicationContext,
                "Registrasi berhasil",
                Toast.LENGTH_SHORT
            )
                .show()
        }

        // Update the product in the database with the buyer's user ID
        if (buyerUserId != null) {
            // Update the product in the database with the buyer's user ID
            updateProductWithBuyer(buyerUserId)
            Toast.makeText(applicationContext, "Produk Berhasil Dibeli", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            // Handle the case where buyerUserId is null (e.g., user not logged in)
            // You might want to show an error message or redirect the user to the login screen
            Toast.makeText(applicationContext, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProductWithBuyer(buyerUserId: String) {
        // Update the 'buyerUserId' field in the 'Product' node
        val productUpdate = HashMap<String, Any>()
        productUpdate["buyerUserId"] = buyerUserId



        // Update the product in the database
        databaseReference.child("products").child(productCategory).child(productId)
            .updateChildren(productUpdate)
            .addOnSuccessListener {
                // Update successful
                // You can add additional actions here if needed
                // For example, navigate to a success page or show a toast
                markProductAsSold(productId)

            }
            .addOnFailureListener {
                // Update failed
                // Handle the failure, for example, show an error message
            }
    }

    private fun markProductAsSold(productId: String) {
        // Assuming you have a reference to the "products" node in your database
        val databaseReference = FirebaseDatabase.getInstance().reference.child("products").child(productId)

        // Update the productStatus to "sold"
        databaseReference.child("productStatus").setValue("sold")
            .addOnSuccessListener {
                // Handle success
                // For example, show a success message or navigate to a different screen
            }
            .addOnFailureListener {
                // Handle failure
                // For example, show an error message
            }
    }

}