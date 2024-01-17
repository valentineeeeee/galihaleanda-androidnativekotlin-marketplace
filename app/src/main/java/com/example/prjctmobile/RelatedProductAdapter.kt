package com.example.prjctmobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prjctmobile.table.Product

// RelatedProductAdapter.kt
class RelatedProductAdapter(private val relatedProducts: List<Product>) : RecyclerView.Adapter<RelatedProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.related_product_item, parent, false)
        return RelatedProductViewHolder(view)
    }


    override fun onBindViewHolder(holder: RelatedProductViewHolder, position: Int) {
        val product = relatedProducts[position]
        holder.textViewProductNameRelated.text = product.productName
        holder.textViewProductPriceRelated.text = product.productPrice
        product.imageUrl?.let{
            Glide.with(holder.itemView.context)
                .load(it)
                .placeholder(R.drawable.kita)
                .into(holder.imageViewProductRelated)
        }
        holder.textViewProductStatusRelated.text = product.productStatus
    }

    override fun getItemCount(): Int {
        return relatedProducts.size
    }
}
