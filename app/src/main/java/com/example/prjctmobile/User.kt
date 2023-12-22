package com.example.prjctmobile

data class User(
    val id: String,
    val email: String,
    val address: String,
    val username: String,
    val password: String,
) {
    constructor() : this("", "", "", "", "")
}