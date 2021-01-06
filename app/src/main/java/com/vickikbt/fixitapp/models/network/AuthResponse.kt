package com.vickikbt.fixitapp.models.network

import com.vickikbt.fixitapp.models.entity.User

data class AuthResponse(
    val message: String,
    val user: User
)