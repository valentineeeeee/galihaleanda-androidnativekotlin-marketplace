package com.example.prjctmobile

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RelatedProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Add views for the related product item
    val imageViewProductRelated: ImageView = itemView.findViewById(R.id.imageViewProductRelated)
    val textViewProductNameRelated: TextView = itemView.findViewById(R.id.textViewProductNameRelated)
    val textViewProductPriceRelated: TextView = itemView.findViewById(R.id.textViewProductPriceRelated)
    val textViewProductStatusRelated: TextView = itemView.findViewById(R.id.textViewProductStatusRelated)
    // Add other views as needed
}