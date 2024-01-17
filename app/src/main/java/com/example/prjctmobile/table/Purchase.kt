package com.example.prjctmobile.table

class Purchase(
    val buyerUserId: String? = null,
    val productId: String? = null,
    val namaLengkap: String? = null,
    val nomorTelepon: String? = null,
    val kota: String? = null,
    val namaJalan: String? = null,
    val detailLainnya: String? = null
) {
    constructor() : this("", "", "", "", "", "", "")
}