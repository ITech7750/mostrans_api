package ru.itech.sno_api.core.util

data class AuthTokenResponse(
    val accessToken: String,
    val refreshToken: String
)