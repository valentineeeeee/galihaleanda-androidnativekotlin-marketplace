// SellProductFragment.kt
package com.example.prjctmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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


class SellProductFragment : Fragment(R.layout.fragment_sell_product) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productsList: MutableList<Product> = mutableListOf()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sell_product, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewSell)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        productAdapter = ProductAdapter(productsList, object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                // Handle item click jika diperlukan
            }
        })
        recyclerView.adapter = productAdapter

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("products")

        // Retrieve user's purchased products
        retrieveUserPurchasedProducts()

        return view
    }

    private fun retrieveUserPurchasedProducts() {
        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        // Query to get products with the current buyerUserId
        val query = databaseReference.orderByChild("buyerUserId").equalTo(userId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productsList.clear()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let {
                        productsList.add(it)
                    }
                }

                productAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DibeliFragment", "Error retrieving user purchased products: ${error.message}")
            }
        })
    }
}
