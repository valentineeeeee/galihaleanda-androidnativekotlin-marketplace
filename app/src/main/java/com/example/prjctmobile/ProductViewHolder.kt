package com.example.prjctmobile

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.prjctmobile.table.Product

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardView: CardView = itemView.findViewById(R.id.cardViewProduct)
    val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
    val textViewProductName: TextView = itemView.findViewById(R.id.textViewProductName)
    val textViewProductPrice: TextView = itemView.findViewById(R.id.textViewProductPrice)
    val textViewProductStatus: TextView = itemView.findViewById(R.id.textViewProductStatus)
    fun bind(product: Product, onItemClickListener: ProductAdapter.OnItemClickListener) {
        itemView.setOnClickListener {
            onItemClickListener.onItemClick(product)
        }

        // Populate other views
        // ...
    }
}
