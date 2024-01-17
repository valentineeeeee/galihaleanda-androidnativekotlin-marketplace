package com.example.prjctmobile

// ProductAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prjctmobile.table.Product
import com.bumptech.glide.Glide


class ProductAdapter(
    private val productList: MutableList<Product> = mutableListOf(),
    private val onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<ProductViewHolder>() {

    private var filteredList: List<Product> = mutableListOf()
    fun updateData(newData: List<Product>) {
        productList.clear()
        productList.addAll(newData)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            productList
        } else {
            productList.filter { it.productName?.contains(query, ignoreCase = true) == true }
        }.toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_card_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        // Isi elemen tampilan kartu dengan data produk
        // Contoh, sesuaikan dengan model produk Anda
        holder.textViewProductName.text = product.productName
        holder.textViewProductPrice.text = product.productPrice
        product.imageUrl?.let{
            Glide.with(holder.itemView.context)
                .load(it)
                .placeholder(R.drawable.kita)
                .into(holder.imageViewProduct)
        }
        holder.textViewProductStatus.text = product.productStatus

        // Use the new bind method
        holder.bind(product, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

}

