package com.codersee.dto

data class AppUserRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val street: String,
    val city: String,
    val code: Int
)
