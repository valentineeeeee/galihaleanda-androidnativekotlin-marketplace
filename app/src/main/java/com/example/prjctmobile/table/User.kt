package com.example.prjctmobile.table

data class User(
    val userId: String,
    val email: String,
    val address: String,
    val username: String,
    val password: String,
    val imageUrl: String?,
) {
    constructor() : this("","", "", "", "", "")
}