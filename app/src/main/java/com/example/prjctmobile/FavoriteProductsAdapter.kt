// FavoriteProductsAdapter.kt
package com.example.prjctmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prjctmobile.table.Product

class FavoriteProductsAdapter(private val onItemClickListener: ProductAdapter.OnItemClickListener) :
    RecyclerView.Adapter<FavoriteProductsAdapter.ViewHolder>() {

    private var favoriteProducts: List<Product> = ArrayList()

    fun setProducts(products: List<Product>) {
        favoriteProducts = products
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = favoriteProducts[position]
        holder.bind(product, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return favoriteProducts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardViewProduct)
        private val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
        private val textViewProductName: TextView = itemView.findViewById(R.id.textViewProductName)
        private val textViewProductPrice: TextView = itemView.findViewById(R.id.textViewProductPrice)
        private val textViewProductStatus: TextView = itemView.findViewById(R.id.textViewProductStatus)


        fun bind(product: Product, onItemClickListener: ProductAdapter.OnItemClickListener) {
            textViewProductName.text = product.productName
            textViewProductPrice.text = product.productPrice
            textViewProductStatus.text = product.productStatus

            // You can also load the image using Glide
            product.imageUrl?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .placeholder(R.drawable.kita)
                    .into(imageViewProduct)
            }
        }
    }
}
