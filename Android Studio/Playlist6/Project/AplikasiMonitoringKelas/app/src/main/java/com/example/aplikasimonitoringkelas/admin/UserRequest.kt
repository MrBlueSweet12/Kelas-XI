package com.example.aplikasimonitoringkelas.admin

data class UserRequest(
    val nama: String,
    val email: String,
    val password: String? = null,
    val role: String
)
