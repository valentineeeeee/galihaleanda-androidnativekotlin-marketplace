package com.example.prjctmobile

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.prjctmobile.table.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prjctmobile.session.SessionManager

class DetailBarangActivity : AppCompatActivity() {

    private lateinit var dynamicTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_barang)

        val productId = intent.getStringExtra(  "productId")
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getStringExtra("productPrice")
        val productDescription = intent.getStringExtra("productDescription")
        val imageUrl = intent.getStringExtra("imageUrl")
        val productCategory = intent.getStringExtra("productCategory")

        // Menetapkan informasi pengguna pada TextView
        findViewById<TextView>(R.id.TextViewProductCategory).text = productCategory
        findViewById<TextView>(R.id.TextViewProductName).text = productName
        findViewById<TextView>(R.id.TextViewProductPrice).text = productPrice
        findViewById<TextView>(R.id.TextViewProductDescription).text = productDescription

        // Load the image using a library like Glide
        val imageView = findViewById<ImageView>(R.id.image)
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.kita) // You can set a placeholder image
            .into(imageView)

        val buyNowButton = findViewById<RelativeLayout>(R.id.addBuyProduct)
        buyNowButton.setOnClickListener {

            val intent = Intent(applicationContext, AddAlamatActivity::class.java)
            intent.putExtra("productId", productId)
            intent.putExtra("productName", productName)
            intent.putExtra("productPrice", productPrice)
            intent.putExtra("productDescription", productDescription)
            intent.putExtra("imageUrl", imageUrl)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val addToFavoritesButton = findViewById<RelativeLayout>(R.id.btn_favorit)
        addToFavoritesButton.setOnClickListener {
            // Add the current product to favorites
            addProductToFavorites(productId)
        }

        fetchDataFromFirebase(productId)

        // Find the TextView by its ID
        dynamicTextView = findViewById(R.id.textViewDynamic)

        // Set the default text
        dynamicTextView.text = "Detail Product"

        val backButton: ImageView = findViewById(R.id.img)
        backButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun displayRelatedProducts(relatedProducts: List<Product>) {
        val recyclerView: RecyclerView = findViewById(R.id.rv_related_products)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        val relatedProductAdapter = RelatedProductAdapter(relatedProducts)
        recyclerView.adapter = relatedProductAdapter
    }

    private fun  fetchDataFromFirebase(productId: String?) {
        // Check if productId is not null before using it
        productId?.let { nonNullProductId ->
            val databaseReference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference("products/$nonNullProductId")

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Get the product category from the snapshot
                    val productCategory = snapshot.child("productCategory").getValue(String::class.java)
                    val productStatus = snapshot.child("productStatus").getValue(String::class.java)



                    // Fetch related products based on the category
                    productCategory?.let {
                        fetchRelatedProducts(it)
                    }

                    handleProductStatus(productStatus)

                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    private fun fetchRelatedProducts(productCategory: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")

        databaseReference.orderByChild("productCategory").equalTo(productCategory).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val relatedProducts: MutableList<Product> = ArrayList()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let { relatedProducts.add(it) }
                }

                // Display related products in the RecyclerView
                displayRelatedProducts(relatedProducts)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun handleProductStatus(productStatus: String?) {
        val buyNowButton = findViewById<RelativeLayout>(R.id.addBuyProduct)

        // Check if the product status is "Sold Out" and disable the button accordingly
        if (productStatus == "sold") {
            buyNowButton.isEnabled = false
            buyNowButton.isClickable = false
            buyNowButton.alpha = 0.5f // You can also adjust the alpha to visually indicate that it's disabled
        } else {
            buyNowButton.isEnabled = true
            buyNowButton.isClickable = true
            buyNowButton.alpha = 1.0f
        }
    }

    private fun addProductToFavorites(productId: String?) {
        productId?.let { nonNullProductId ->
            // Get the user's ID (you need to implement user authentication)
            val sessionManager = SessionManager(applicationContext)
            val userId = sessionManager.getUserId()

            // Reference to the user's favorites node in Firebase
            val favoritesReference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference("favorites/$userId")

            // Check if the product is already in the favorites
            favoritesReference.child(nonNullProductId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Product is already in favorites, show toast
                        showToast("You already liked this product")
                    } else {
                        // Product is not in favorites, add it
                        addProductToFirebaseFavorites(nonNullProductId, favoritesReference)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    private fun addProductToFirebaseFavorites(productId: String, favoritesReference: DatabaseReference) {
        // Reference to the specific product in Firebase
        val productReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("products/$productId")

        productReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get the product details
                val product = snapshot.getValue(Product::class.java)

                // Check if the product is not null
                product?.let {
                    // Add the product to the user's favorites
                    favoritesReference.child(productId).setValue(it)
                    showToast("Product added to favorites")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}