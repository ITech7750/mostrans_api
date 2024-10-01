package ru.itech.sno_api.core.domain.request.user

data class SignInRequest(
    val login: String,
    val password: String,
)