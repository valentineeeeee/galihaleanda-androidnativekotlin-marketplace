package com.example.prjctmobile.table

class Product(

    val productId: String? = null,
    val productName: String? = null,
    val productDescription: String? = null,
    val productPrice: String? = null,
    val productImage: String? = null, // URL gambar produk
    val productStatus: String? = null, // Contoh: "available", "sold"
    val productCategory: String? = null,
    val userId: String? = null,
    val buyerUserId: String? = null,
    val imageUrl: String?
)
{
    constructor(): this("","","","","","","","","","")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (productId != other.productId) return false
        if (productName != other.productName) return false
        // Add more comparisons for other fields

        return true
    }

    override fun hashCode(): Int {
        var result = productId?.hashCode() ?: 0
        result = 31 * result + (productName?.hashCode() ?: 0)
        // Add more hash code calculations for other fields
        return result
    }

}

