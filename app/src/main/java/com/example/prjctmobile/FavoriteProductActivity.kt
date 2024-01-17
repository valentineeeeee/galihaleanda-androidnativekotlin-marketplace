package com.example.prjctmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prjctmobile.session.SessionManager
import com.example.prjctmobile.table.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoriteProductActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteProductsAdapter: FavoriteProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_product)

        recyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
        favoriteProductsAdapter = FavoriteProductsAdapter(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                // Handle item click for both product list and favorite products
                // You can navigate to the details or perform any other action
                val intent = Intent(applicationContext, DetailBarangActivity::class.java)
                intent.putExtra("productId", product.productId)
                intent.putExtra("productName", product.productName)
                intent.putExtra("productPrice", product.productPrice)
                intent.putExtra("productDescription", product.productDescription)
                intent.putExtra("imageUrl", product.imageUrl)
                showToast("Clicked on product: ${product.productName}")
                Log.d("FavoriteProductActivity", "Starting DetailBarangActivity")
                startActivity(intent)
            }
        })
        recyclerView.adapter = favoriteProductsAdapter

        // Retrieve favorite products from Firebase
        retrieveFavoriteProducts()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun retrieveFavoriteProducts() {
        val sessionManager = SessionManager(applicationContext)
        val userId = sessionManager.getUserId()
        if (userId != null) {
            val favoritesReference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference("favorites/$userId")

            favoritesReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favoriteProducts: MutableList<Product> = ArrayList()

                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.let { favoriteProducts.add(it) }
                    }

                    // Update the RecyclerView with favorite products
                    favoriteProductsAdapter.setProducts(favoriteProducts)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }
}
